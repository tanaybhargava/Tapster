package com.tapster.barMenu;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.tapster.R;
import com.tapster.azureConnectivity.AzureServiceConnection;
import com.tapster.azureConnectivity.Config;
import com.tapster.data.OrderItem;
import com.tapster.data.OrderList;
import com.tapster.ui.OrderItemAdapter;
import com.tapster.utilities.ProgressActivity;

public class CurrentOrderFragment extends Fragment
{

	private static OrderList currentOrderList = new OrderList();
	private static ArrayAdapter<OrderItem> adapter;

	private static float total;

	private Button placeOrder;

	private Context ctx;
	private View rootView;

	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{

		ctx = getActivity().getApplicationContext();
		rootView = inflater.inflate(R.layout.fragment_current_order, container, false);

		placeOrder = (Button) rootView.findViewById(R.id.button_placeOrder);
		ListView tabText = (ListView) rootView.findViewById(R.id.orderList);
		adapter = new OrderItemAdapter(ctx, R.layout.listentry_menu_item, currentOrderList.getItems());
		adapter.setNotifyOnChange(true);
		tabText.setAdapter(adapter);

		if (currentOrderList.getTotal() <= 0)
		{
			placeOrder.setBackgroundColor(Color.DKGRAY);
			placeOrder.setClickable(false);
		} else
			ClickBars(placeOrder);

		setTotal();
		return rootView;
	}

	public static void getNewItem(OrderItem newOrder)
	{
		currentOrderList.AddOrder(newOrder);

	}

	private void setTotal()
	{
		total = currentOrderList.getTotal();
		TextView textView2 = (TextView) rootView.findViewById(R.id.string_total2);
		textView2.setText("Total: $" + total);

	}

	private void ClickBars(Button b)
	{
		b.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				switch (v.getId())
				{
				case R.id.button_placeOrder:

					MobileServiceTable<OrderList> mpendingOrderTable = AzureServiceConnection.instance.getClient().getTable(Config.PendingOrderTable, OrderList.class);

					// Insert the new item
					mpendingOrderTable.insert(currentOrderList, new TableOperationCallback<OrderList>()
					{

						public void onCompleted(OrderList entity,
								Exception exception,
								ServiceFilterResponse response)
						{
							ProgressActivity.End();

							if (exception == null)
							{
								TextView textView2 = (TextView) rootView.findViewById(R.id.string_total2);
								textView2.setText("Total: $" + 0);
								currentOrderList = new OrderList();
								adapter.clear();
								Toast.makeText(getActivity().getApplicationContext(), "Order Placed", Toast.LENGTH_SHORT).show();
							} else
								Toast.makeText(getActivity().getApplicationContext(), "Error in placing order", Toast.LENGTH_SHORT).show();
						}
					});

					ProgressActivity.Start(getActivity(), "Placing order, please wait...");

					break;
				}

			}
		});
	}
}
