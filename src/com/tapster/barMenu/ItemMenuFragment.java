package com.tapster.barMenu;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tapster.R;

public class ItemMenuFragment extends Fragment
{

	private Context ctx;
	private View rootView;
	private String category;

	public BarMenuDB menu = BarMenuFragment.menuDB;

	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		ctx = getActivity().getApplicationContext();
		rootView = inflater.inflate(R.layout.fragment_item_menu, container, false);
		getItems();
		ClickcItem();
		return rootView;

	}

	private void getItems()
	{ // get the Items from the menu
		category = BarMenuFragment.SetCategory;
		ArrayList<String> Items = new ArrayList<String>();
		Cursor c = menu.getAllRowsOfCat(category);

		if (c.moveToFirst())
		{
			do
			{
				String newItem = "";
				String name = c.getString(BarMenuDB.COL_NAME);
				float price = c.getFloat(BarMenuDB.COL_PRICE);
				newItem += name + "    $" + price;
				if (Items.contains(newItem) == false)
				{
					Items.add(newItem);
					;
				}

			} while (c.moveToNext());
		}

		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(ctx, R.layout.listentry_textview, Items);
		ListView itemsList = (ListView) rootView.findViewById(R.id.itemsView1);
		itemsList.setAdapter(adapter2);

	}

	private void ClickcItem()
	{
		ListView itemsList = (ListView) rootView.findViewById(R.id.itemsView1);
		itemsList.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked,
					int position, long id)
			{
				TextView textView = (TextView) viewClicked;
				String message = textView.getText().toString();
				OrderConfirmDialog comfirm = new OrderConfirmDialog(getActivity(), message, category);
				comfirm.show();
			}

		});
	}

	public void onDestroyView()
	{
		super.onDestroy();
	}
}
