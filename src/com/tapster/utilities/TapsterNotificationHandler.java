package com.tapster.utilities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.microsoft.windowsazure.notifications.NotificationsHandler;
import com.tapster.R;
import com.tapster.azureConnectivity.AzureServiceConnection;
import com.tapster.customer.MainActivity;



public class TapsterNotificationHandler extends NotificationsHandler {

	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	Context ctx;
	
	@Override
	public void onRegistered(Context context, String gcmRegistrationId) 
	{
	  super.onRegistered(context, gcmRegistrationId);
	  AzureServiceConnection.instance.registerForPush(gcmRegistrationId);
	}
	
	@Override
	public void onReceive(Context context, Bundle bundle) {
	    ctx = context;
	    String nhMessage = bundle.getString("message");

	    sendNotification(nhMessage);
	}

	private void sendNotification(String msg) 
	{
	    mNotificationManager = (NotificationManager)
	              ctx.getSystemService(Context.NOTIFICATION_SERVICE);

	    PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
	          new Intent(ctx, MainActivity.class), 0);

	    NotificationCompat.Builder mBuilder =
	          new NotificationCompat.Builder(ctx)
	          .setSmallIcon(R.drawable.ic_launcher)
	          .setContentTitle("Notification Hub Demo")
	          .setStyle(new NotificationCompat.BigTextStyle()
	                     .bigText(msg))
	          .setContentText(msg);

	     mBuilder.setContentIntent(contentIntent);
	     mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}
	
}