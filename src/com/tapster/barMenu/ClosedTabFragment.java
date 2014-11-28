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

public class ClosedTabFragment extends Fragment
{

	private static ArrayList<OrderList> items = new ArrayList<OrderList>();
	private static OrderListAdapter adapter;

	private Context ctx;
	private View rootView;

	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{

		ctx = getActivity().getApplicationContext();
		rootView = inflater.inflate(R.layout.fragment_pending_order, container, false);

		ListView POText = (ListView) rootView.findViewById(R.id.order_list);
		adapter = new OrderListAdapter(ctx, R.layout.listentry_textview, items);
		adapter.setNotifyOnChange(true);

		POText.setAdapter(adapter);

		return rootView;
	}

	public static void addNewItem(OrderList newOrder)
	{
		items.add(new OrderList(newOrder));
	}
}
