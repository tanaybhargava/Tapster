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
import com.tapster.azureConnectivity.Config;
import com.tapster.customer.CollectOrderActivity;
import com.tapster.data.OrderList;

public class TapsterNotificationHandler extends NotificationsHandler
{

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
	public void onReceive(Context context, Bundle bundle)
	{
		ctx = context;

		if (Config.barTenderMode)
		{

		} else
		{

			boolean complete = Boolean.parseBoolean(bundle.getString("complete", "false"));

			if (complete)
			{
				String id = bundle.getString("id");
				String serializedString = bundle.getString("content");
				int color = Integer.parseInt(bundle.getString("color"));

				OrderList order = new OrderList(id, serializedString, color, complete);

				sendNotificationToCustomer("Your order is ready", order);

			}
		}

	}

	private void sendNotificationToCustomer(String msg, OrderList order)
	{
		mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

		CollectOrderActivity.readyOrder = order;

		PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, new Intent(ctx, CollectOrderActivity.class), 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx).setSmallIcon(R.drawable.ic_launcher).setContentTitle("exTab").setStyle(new NotificationCompat.BigTextStyle().bigText(msg)).setContentText(msg);

		mBuilder.setContentIntent(contentIntent);

		mBuilder.setAutoCancel(true);

		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}
}
