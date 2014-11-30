package com.tapster.bartender;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;
import com.tapster.R;
import com.tapster.azureConnectivity.AzureServiceConnection;
import com.tapster.azureConnectivity.Config;
import com.tapster.data.OrderList;
import com.tapster.ui.OrderListAdapter;

public class BarTenderActivity extends Activity
{

	private ArrayList<OrderList> pendingItems = new ArrayList<OrderList>();
	private OrderListAdapter pendingAdapter;

	private ArrayList<OrderList> completedItems = new ArrayList<OrderList>();
	private OrderListAdapter completedAdapter;

	private Activity ctx;
	private RelativeLayout overlay;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bartender);

		ctx = this;

		// Set overlay progress bar.
		overlay = (RelativeLayout) findViewById(R.id.overlay);

		ListView pList = (ListView) findViewById(R.id.pending_order_list);
		pendingAdapter = new OrderListAdapter(ctx, R.layout.listentry_pendingorder, pendingItems);
		pendingAdapter.setNotifyOnChange(true);
		pList.setAdapter(pendingAdapter);

		ListView cList = (ListView) findViewById(R.id.completed_order_list);
		completedAdapter = new OrderListAdapter(ctx, R.layout.listentry_pendingorder, completedItems);
		completedAdapter.setNotifyOnChange(true);
		cList.setAdapter(completedAdapter);

		ClickPendingOrder();
		refreshOrders();
	}

	private void refreshOrders()
	{
		overlay.setVisibility(RelativeLayout.VISIBLE);

		pendingAdapter.clear();
		completedAdapter.clear();

		MobileServiceClient mClient = AzureServiceConnection.instance.getClient();
		MobileServiceTable<OrderList> mpendingOrderTable = mClient.getTable(Config.PendingOrderTable, OrderList.class);

		mpendingOrderTable.execute(new TableQueryCallback<OrderList>()
		{

			public void onCompleted(List<OrderList> result, int count,
					Exception exception, ServiceFilterResponse response)
			{
				overlay.setVisibility(RelativeLayout.INVISIBLE);
				if (exception == null)
				{
					for (OrderList item : result)
					{
						item.deSerialize();
						if (item != null)
						{
							if (!item.getmComplete())
								pendingAdapter.add(item);
							else
								completedAdapter.add(item);
						}
					}
				} else
					Toast.makeText(ctx.getApplicationContext(), "Error in fetching orders", Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void refreshOrders(View v)
	{
		refreshOrders();
	}

	private void ClickPendingOrder()
	{
		ListView CategoryList = (ListView) findViewById(R.id.pending_order_list);
		CategoryList.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked,
					int position, long id)
			{
				int color = getColor();
				if (color == Color.WHITE)
				{
					Toast.makeText(ctx, "No Color available !", Toast.LENGTH_SHORT).show();
					return;
				}

				OrderList temp = pendingItems.get(position);
				temp.setmColor(color);

				CompleteOrderDialog comfirm = new CompleteOrderDialog((BarTenderActivity) ctx, temp);
				comfirm.show();
			}
		});
	}

	public void completeOrder(OrderList order)
	{
		overlay.setVisibility(RelativeLayout.VISIBLE);

		order.setmComplete(true);

		MobileServiceClient mClient = AzureServiceConnection.instance.getClient();
		MobileServiceTable<OrderList> mpendingOrderTable = mClient.getTable(Config.PendingOrderTable, OrderList.class);

		mpendingOrderTable.update(order, new TableOperationCallback<OrderList>()
		{

			public void onCompleted(OrderList entity, Exception exception,
					ServiceFilterResponse response)
			{
				if (exception != null)
					Toast.makeText(ctx, "Error in notifying user", Toast.LENGTH_SHORT).show();

				refreshOrders();
			}
		});
	}

	private int getColor()
	{
		ArrayList<Integer> colorList = new ArrayList<Integer>();

		colorList.add(Color.RED);
		colorList.add(Color.GREEN);
		colorList.add(Color.BLUE);
		colorList.add(Color.CYAN);
		colorList.add(Color.MAGENTA);
		colorList.add(Color.LTGRAY);
		colorList.add(Color.YELLOW);

		int color = Color.WHITE;

		for (Integer col : colorList)
		{
			if (!checkIfColorExists(col))
			{
				color = col;
				break;
			}
		}

		return color;
	}

	private boolean checkIfColorExists(int color)
	{
		for (OrderList order : completedItems)
		{
			if (order.getmColor() == color)
				return true;
		}
		return false;
	}

}
