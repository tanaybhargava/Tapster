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
import android.widget.Toast;

import com.tapster.R;

public class BarMenuFragment extends Fragment
{

	public static BarMenuDB menuDB;

	public static String SetCategory;

	private Context ctx;
	private View rootView;

	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{

		ctx = getActivity().getApplicationContext();
		rootView = inflater.inflate(R.layout.fragment_bar_menu, container, false);

		accesDB();
		pupulateDB();
		getCategories();
		ClickCategory();

		return rootView;
	}

	private void getCategories()
	{ // fill list with the categories
		ArrayList<String> Categories = new ArrayList<String>();
		Cursor cursor = menuDB.getAllRows();

		if (cursor.moveToFirst())
		{
			do
			{
				String name = cursor.getString(BarMenuDB.COL_CATEGORY);
				if (Categories.contains(name) == false)
				{
					Categories.add(name);
				}
			} while (cursor.moveToNext());
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, R.layout.listentry_textview, Categories);
		ListView Categorylist = (ListView) rootView.findViewById(R.id.categoriesList);
		Categorylist.setAdapter(adapter);

	}

	private void accesDB()
	{
		menuDB = new BarMenuDB(ctx);
		menuDB.open();
	}

	private void pupulateDB()
	{
		menuDB.insertRow(1, "Whiskey", "TheWhiskey on the rocks", (float) 1.99, " ", 0);
		menuDB.insertRow(1, "Whiskey", "AScotch on the rocks", (float) 2.99, " ", 0);
		menuDB.insertRow(1, "Whiskey", "TheBourbon on the rocks", (float) 4.99, " ", 0);
		menuDB.insertRow(1, "Whiskey", "TheBourbon on the rocks", (float) 3.99, " ", 0);
		menuDB.insertRow(1, "Whiskey", "TheRye on the rocks", (float) 3.99, " ", 0);
		menuDB.insertRow(1, "Whiskey", "AWhiskey on the rocks", (float) 2.99, " ", 0);
		menuDB.insertRow(1, "Vodka", "TheVodka on the rocks", (float) 2.99, " ", 0);
		menuDB.insertRow(1, "Vodka", "Double TheVodka on the rocks", (float) 4, " ", 0);
		menuDB.insertRow(1, "Vodka", "OtherVodka on the rocks", (float) 8.99, " ", 0);
		menuDB.insertRow(1, "Vodka", "AnotherVodka on the rocks", (float) 10.99, " ", 0);
		menuDB.insertRow(1, "Vodka", "4Vodka on the rocks", (float) 4.99, " ", 0);
		menuDB.insertRow(1, "Vodka", "Canthinkofaname on the rocks", (float) 16.99, " ", 0);
		menuDB.insertRow(1, "Vodka", "SmoothVodka on the rocks", (float) 4.50, " ", 0);
		menuDB.insertRow(1, "Vodka", "5Vodka on the rocks", (float) 6, " ", 0);
		menuDB.insertRow(1, "Vodka", "RussianVodka on the rocks", (float) 3.25, " ", 0);
		menuDB.insertRow(1, "Vodka", "3Vodka on the rocks", (float) 4.40, " ", 0);
		menuDB.insertRow(1, "Cocktail", "Margarita", (float) 3.99, " ", 0);
		menuDB.insertRow(1, "Tequila", "Thetequila shot", (float) 1, " ", 0);
		menuDB.insertRow(1, "Rum", "GoodRum on the rocks", (float) 2.99, " ", 0);
		menuDB.insertRow(1, "Snacks", "Nachos", (float) 1, " ", 0);
		menuDB.insertRow(1, "Rum", "SomeRum", (float) 2.99, " ", 0);
		menuDB.insertRow(1, "QuickSelection", "SomeRum and Coke", (float) 2.99, " ", 0);
		menuDB.insertRow(1, "QuickSelection", "SomeRum and Sprite", (float) 2.99, " ", 0);

	}

	@Override
	public void onDestroyView()
	{
		super.onDestroy();
	}

	private void ClickCategory()
	{
		ListView CategoryList = (ListView) rootView.findViewById(R.id.categoriesList);
		CategoryList.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			//Fragment fragment = null;
			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked,
					int position, long id)
			{
				TextView textView = (TextView) viewClicked;
				SetCategory = textView.getText().toString();
				Toast.makeText(ctx, SetCategory, Toast.LENGTH_LONG).show();
				getFragmentManager().beginTransaction().replace(R.id.container, new ItemMenuFragment()).addToBackStack("").commit();
			}
		});

	}

}
