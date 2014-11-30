package com.tapster.customer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.tapster.R;
import com.tapster.azureConnectivity.AzureCallback;
import com.tapster.azureConnectivity.AzureServiceConnection;
import com.tapster.azureConnectivity.Config;
import com.tapster.bartender.BarTenderActivity;
import com.tapster.data.User;

public class LoginActivity extends Activity implements AzureCallback
{
	private Button loginButton;
	private ProgressBar progressbar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		progressbar = (ProgressBar) findViewById(R.id.progressBarMain);
		loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Login(true);
			}
		});

		// Connect to azure.
		AzureServiceConnection.Initialize(this, CustomerNotificationHandler.class);

		// Auto Login:
		Login(false);
	}

	@Override
	public void ServiceRequestComplete(Boolean result)
	{
		if (result)
		{
			UserExists();
		} else
		{
			loginButton.setVisibility(Button.VISIBLE);
			progressbar.setVisibility(ProgressBar.GONE);
		}
	}

	private void UserExists()
	{
		progressbar.setVisibility(ProgressBar.VISIBLE);
		final MobileServiceClient mClient = AzureServiceConnection.instance.getClient();
		final String userId = mClient.getCurrentUser().getUserId();

		mClient.invokeApi("customer/profile", "get", null, User.class, new ApiOperationCallback<User>()
		{

			@Override
			public void onCompleted(User arg0, Exception arg1,
					ServiceFilterResponse arg2)
			{
				if (arg0 != null)
				{
					ProfileFragment.setUserData(arg0);
					mClient.invokeApi("customer/creditcard", "get", null, String.class, new ApiOperationCallback<String>()
					{
						@Override
						public void onCompleted(String arg0, Exception arg1,
								ServiceFilterResponse arg2)
						{
							if (arg0 != null)
								ProfileFragment.setCreditCard(arg0);
							StartMainActivity();
						}
					});
				} else
					setupNewUser(userId);
			}
		});
	}

	private void setupNewUser(String userId)
	{
		Intent myIntent = new Intent(this, NewUser.class);
		myIntent.putExtra("userId", userId);
		startActivity(myIntent);
		loginButton.setVisibility(Button.VISIBLE);
		progressbar.setVisibility(ProgressBar.GONE);
	}

	private void StartMainActivity()
	{
		Intent myIntent;

		if (Config.barTenderMode)
			myIntent = new Intent(this, BarTenderActivity.class);
		else
			myIntent = new Intent(this, MainActivity.class);

		startActivity(myIntent);
		finish();
	}

	private void Login(boolean refreshToken)
	{
		// Hide Button and progress bar.
		loginButton.setVisibility(Button.INVISIBLE);
		progressbar.setVisibility(ProgressBar.VISIBLE);
		AzureServiceConnection.instance.authenticate(refreshToken);
	}

}
