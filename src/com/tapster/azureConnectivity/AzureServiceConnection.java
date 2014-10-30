package com.tapster.azureConnectivity;

import java.net.MalformedURLException;
import java.util.concurrent.atomic.AtomicBoolean;

import tapster.utilities.ProgressFilter;
import tapster.utilities.TapsterNotificationHandler;
import tapster.utilities.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceAuthenticationProvider;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.Registration;
import com.microsoft.windowsazure.mobileservices.RegistrationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponseCallback;
import com.microsoft.windowsazure.mobileservices.UserAuthenticationCallback;
import com.microsoft.windowsazure.notifications.NotificationsManager;

public class AzureServiceConnection {
	
	public static AzureServiceConnection instance;
	
	public boolean bAuthenticating = false;
	public final Object mAuthenticationLock = new Object();

	private MobileServiceClient mClient;
	
	private Activity mainActivity;
	private AzureCallback callback;
	
	private Class<TapsterNotificationHandler> notificationHandlerClass;
		
	@SuppressWarnings("unchecked")
	public static <T extends TapsterNotificationHandler> void Initialize(Activity mainActivity,Class<T> notificationHandlerClass)
	{
		if(mainActivity!=null)
			instance = new AzureServiceConnection(mainActivity,(Class<TapsterNotificationHandler>) notificationHandlerClass);
		else
			 Log.e(AzureServiceConnection.class.getName(),"Error in initalizing azure service connection.");
	}
	
	public MobileServiceClient getClient()
	{
		return mClient;
	}
	
	private AzureServiceConnection(Activity mainActivity,Class<TapsterNotificationHandler> notificationHandlerClass) 
	{
		this.mainActivity = mainActivity;
		this.callback = (AzureCallback)(mainActivity);
		this.notificationHandlerClass = notificationHandlerClass;
	    try {
	        // Create the Mobile Service Client instance, using the provided
	        // Mobile Service URL and key
	        mClient = new MobileServiceClient(
	        		Config.APPURL,
	                Config.APPKEY, mainActivity)
	                   .withFilter(new ProgressFilter(mainActivity))
	                   .withFilter(new RefreshTokenCacheFilter());

	    } catch (MalformedURLException e) {
	        Utility.createAndShowDialog(new Exception("Error creating the Mobile Service. " +
	            "Verify the URL"), "Error",mainActivity);
	        callback.ServiceRequestComplete(false);
	    }
	}

	/**
	 * Registers mobile services client to receive GCM push notifications
	 * @param gcmRegistrationId The Google Cloud Messaging session Id returned 
	 * by the call to GoogleCloudMessaging.register in NotificationsManager.handleNotifications
	 */
	public void registerForPush(String gcmRegistrationId)
	{
	  mClient.getPush().register(gcmRegistrationId,null,new RegistrationCallback()
	  {
	    @Override
	    public void onRegister(Registration registration, Exception exception)
	      {
	        if (exception != null)
	        {
				 Log.e(AzureServiceConnection.class.getName(),"Error in registering for push.");
	        }
	      }
	  });
	}   
		
	private void registerNotificationHandler() 
	{
		NotificationsManager.handleNotifications(mainActivity, Config.SENDER_ID, notificationHandlerClass);
		callback.ServiceRequestComplete(true);
	}
	
	private void cacheUserToken(MobileServiceUser user)
	{
	    SharedPreferences prefs = mainActivity.getSharedPreferences(Config.SHAREDPREFFILE, Context.MODE_PRIVATE);
	    Editor editor = prefs.edit();
	    editor.putString(Config.USERIDPREF, user.getUserId());
	    editor.putString(Config.TOKENPREF, user.getAuthenticationToken());
	    editor.commit();
	}   
	
	private boolean loadUserTokenCache(MobileServiceClient client)
	{
	    SharedPreferences prefs = mainActivity.getSharedPreferences(Config.SHAREDPREFFILE, Context.MODE_PRIVATE);
	    String userId = prefs.getString(Config.USERIDPREF, "undefined"); 
	    if (userId == "undefined")
	        return false;
	    String token = prefs.getString(Config.TOKENPREF, "undefined"); 
	    if (token == "undefined")
	        return false;

	    MobileServiceUser user = new MobileServiceUser(userId);
	    user.setAuthenticationToken(token);
	    client.setCurrentUser(user);

	    return true;
	}
	
	/**
	 * Detects if authentication is in progress and waits for it to complete. 
	 * Returns true if authentication was detected as in progress. False otherwise.
	 */
	private boolean detectAndWaitForAuthentication()
	{
	    boolean detected = false;
	    synchronized(mAuthenticationLock)
	    {
	        do
	        {
	            if (bAuthenticating == true)
	                detected = true;
	            try
	            {
	                mAuthenticationLock.wait(1000);
	            }
	            catch(InterruptedException e)
	            {}
	        }
	        while(bAuthenticating == true);
	    }
	    if (bAuthenticating == true)
	        return true;

	    return detected;
	}
	
	/**
	 * Waits for authentication to complete then adds or updates the token 
	 * in the X-ZUMO-AUTH request header.
	 * 
	 * @param request
	 *            The request that receives the updated token.
	 */
	private void waitAndUpdateRequestToken(ServiceFilterRequest request)
	{
	    MobileServiceUser user = null;
	    if (detectAndWaitForAuthentication())
	    {
	        user = mClient.getCurrentUser();
	        if (user != null)
	        {
	            request.removeHeader("X-ZUMO-AUTH");
	            request.addHeader("X-ZUMO-AUTH", user.getAuthenticationToken());
	        }
	    }
	}
	
	/**
	 * Authenticates with the desired login provider. Also caches the token. 
	 * 
	 * If a local token cache is detected, the token cache is used instead of an actual 
	 * login unless bRefresh is set to true forcing a refresh.
	 * 
	 * @param bRefreshCache
	 *            Indicates whether to force a token refresh. 
	 */
	public void authenticate(boolean bRefreshCache) {

	    bAuthenticating = true;

	    if (bRefreshCache || !loadUserTokenCache(mClient))
	    {
	        // New login using the provider and update the token cache.
	        mClient.login(MobileServiceAuthenticationProvider.Google,
	                new UserAuthenticationCallback() {
	                    @Override
	                    public void onCompleted(MobileServiceUser user,
	                            Exception exception, ServiceFilterResponse response) {

	                        synchronized(mAuthenticationLock)
	                        {
	                            if (exception == null) {
	                                cacheUserToken(mClient.getCurrentUser());
	                                registerNotificationHandler();
	                            } else {
	                                Utility.createAndShowDialog(exception.getMessage(), "Login Error",mainActivity);
	                                callback.ServiceRequestComplete(false);
	                            }
	                            bAuthenticating = false;
	                            mAuthenticationLock.notifyAll();
	                        }
	                    }
	                });
	    }
	    else
	    {
	        // Other threads may be blocked waiting to be notified when 
	        // authentication is complete.
	        synchronized(mAuthenticationLock)
	        {
	            bAuthenticating = false;
	            mAuthenticationLock.notifyAll();
	        }
	        registerNotificationHandler();
	    }
	}   
	
	/**
	 * The RefreshTokenCacheFilter class filters responses for HTTP status code 401. 
	 * When 401 is encountered, the filter calls the authenticate method on the 
	 * UI thread. Out going requests and retries are blocked during authentication. 
	 * Once authentication is complete, the token cache is updated and 
	 * any blocked request will receive the X-ZUMO-AUTH header added or updated to 
	 * that request.   
	 */
	private class RefreshTokenCacheFilter implements ServiceFilter {

	    AtomicBoolean mAtomicAuthenticatingFlag = new AtomicBoolean();

	    /**
	     * The AuthenticationRetryFilterCallback class is a wrapper around the response 
	     * callback that encapsulates the request and other information needed to enable 
	     * a retry of the request when HTTP status code 401 is encountered. 
	     */
	    private class AuthenticationRetryFilterCallback implements ServiceFilterResponseCallback
	    {
	        // Data members used to retry the request during the response.
	        ServiceFilterRequest mRequest;
	        NextServiceFilterCallback mNextServiceFilterCallback;
	        ServiceFilterResponseCallback mResponseCallback;

	        public AuthenticationRetryFilterCallback(ServiceFilterRequest request, 
	                NextServiceFilterCallback nextServiceFilterCallback, 
	                ServiceFilterResponseCallback responseCallback)
	        {
	            mRequest = request;
	            mNextServiceFilterCallback = nextServiceFilterCallback;
	            mResponseCallback = responseCallback;
	        }

	        @Override
	        public void onResponse(ServiceFilterResponse response, Exception exception) {

	            // Filter out the 401 responses to update the token cache and 
	            // retry the request
	            if ((response != null) && (response.getStatus().getStatusCode() == 401))
	            { 
	                // Two simultaneous requests from independent threads could get HTTP 
	                // status 401. Protecting against that right here so multiple 
	                // authentication requests are not setup to run on the UI thread.
	                // We only want to authenticate once. Other requests should just wait 
	                // and retry with the new token.
	                if (mAtomicAuthenticatingFlag.compareAndSet(false, true))                           
	                {
	                    // Authenticate on UI thread
	                	mainActivity.runOnUiThread(new Runnable() {
	                        @Override
	                        public void run() {
	                            // Force a token refresh during authentication.
	                            authenticate(true);
	                        }
	                    });
	                }

	                // Wait for authentication to complete then 
	                // update the token in the request.
	                waitAndUpdateRequestToken(this.mRequest);
	                mAtomicAuthenticatingFlag.set(false);                       

	                // Retry recursively with a new token as long as we get a 401.
	                mNextServiceFilterCallback.onNext(this.mRequest, this);
	            }

	            // Responses that do not have 401 status codes just pass through.
	            else if (this.mResponseCallback != null)  
	              mResponseCallback.onResponse(response, exception);
	        }
	    }

	    @Override
	    public void handleRequest(final ServiceFilterRequest request, 
	        final NextServiceFilterCallback nextServiceFilterCallback,
	        ServiceFilterResponseCallback responseCallback) {

	        // In this example, if authentication is already in progress we block the request
	        // until authentication is complete to avoid unnecessary authentications as 
	        // a result of HTTP status code 401. 
	        // If authentication was detected, add the token to the request.
	        waitAndUpdateRequestToken(request);

	        // Wrap the request in a callback object that will facilitate retries.
	        AuthenticationRetryFilterCallback retryCallbackObject = 
	            new AuthenticationRetryFilterCallback(request, nextServiceFilterCallback,
	                  responseCallback); 

	        // Send the request down the filter chain.
	        nextServiceFilterCallback.onNext(request, retryCallbackObject);         
	    }
	}

}
