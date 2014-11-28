package com.tapster.customer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableDeleteCallback;
import com.tapster.R;
import com.tapster.azureConnectivity.AzureServiceConnection;
import com.tapster.azureConnectivity.Config;
import com.tapster.barMenu.OpenTabFragment;
import com.tapster.data.OrderList;
import com.tapster.utilities.ProgressActivity;

public class CollectOrderActivity extends Activity
{
	public static OrderList readyOrder;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collectorder);

		RelativeLayout rl = (RelativeLayout) findViewById(R.id.backGroundLayout);
		rl.setBackgroundColor(readyOrder.getmColor());
	}

	public static void Start(Activity activity, OrderList order)
	{
		readyOrder = order;
		Intent myIntent = new Intent(activity.getApplicationContext(), CollectOrderActivity.class);
		activity.startActivity(myIntent);
	}

	public void collectOrder(View v)
	{
		OpenTabFragment.getNewOrders(readyOrder);

		MobileServiceTable<OrderList> mpendingOrderTable = AzureServiceConnection.instance.getClient().getTable(Config.PendingOrderTable, OrderList.class);

		mpendingOrderTable.delete(readyOrder, new TableDeleteCallback()
		{

			@Override
			public void onCompleted(Exception arg0, ServiceFilterResponse arg1)
			{
				ProgressActivity.End();
				finish();
			}
		});
		ProgressActivity.Start((Activity) v.getContext(), "Please Wait");
	}
}
