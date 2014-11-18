package com.tapster.barMenu;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tapster.R;
import com.tapster.data.OrderList;
import com.tapster.ui.OrderListAdapter;

public class PendingOrderFragment extends Fragment
{

	public static ArrayList<OrderList> items = new ArrayList<OrderList>();
	public static OrderListAdapter adapter;



	private Context ctx;
	private View rootView;

	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{

		ctx = getActivity().getApplicationContext();
		rootView = inflater.inflate(R.layout.fragment_pending_order, container, false);

		ListView POText = (ListView) rootView.findViewById(R.id.pending_order_list_xml);
		adapter = new OrderListAdapter(ctx, R.layout.listentry_textview, items);
		adapter.setNotifyOnChange(true);
		POText.setAdapter(adapter);
		return rootView;
	}

	public static void getNewOrders(OrderList newOrder)
	{
		items.add(newOrder);	
		SendOrder(newOrder);
	}
	
	private static void SendOrder(OrderList newOrder)
	{
		//Send to server here
		OrderReady(newOrder);	
	}	
	
	public static void OrderReady(OrderList newOrder)
	{
		items.remove(newOrder);
		
		OpenTabFragment.getNewOrders(newOrder);
		
		// Start Color Screen.
		
	}
	
}
