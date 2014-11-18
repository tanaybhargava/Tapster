package com.tapster.barMenu;

import android.content.Context;
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

import com.tapster.R;
import com.tapster.data.Order;
import com.tapster.data.OrderList;
import com.tapster.ui.OrderAdapter;

public class OrderFragment extends Fragment
{

	public static OrderList items = new OrderList();
	public static ArrayAdapter<Order> adapter;

	private static float total;

	private Button placeOrder;

	private Context ctx;
	private View rootView;

	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{

		ctx = getActivity().getApplicationContext();
		rootView = inflater.inflate(R.layout.order_fragment, container, false);

		placeOrder = (Button) rootView.findViewById(R.id.button_placeOrder);
		ListView tabText = (ListView) rootView.findViewById(R.id.orderList);
		adapter = new OrderAdapter(ctx,R.layout.tabtext, items.getItems());
		adapter.setNotifyOnChange(true);
		tabText.setAdapter(adapter);
		ClickBars(placeOrder);
		setTotal();
		return rootView;
	}

	public static void getNewItem(Order newOrder)
	{
		items.AddOrder(newOrder);	

	}

	private void setTotal()
	{
		total=items.getTotal();
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
					String message = "order placed ";
					Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
					PendingOrderFragment.getNewOrders(new OrderList(items));
					TextView textView2 = (TextView) rootView.findViewById(R.id.string_total2);
					textView2.setText("Total: $" + 0);
					items = new OrderList();
					adapter.clear();
					break;
				}

			}
		});
	}
}
