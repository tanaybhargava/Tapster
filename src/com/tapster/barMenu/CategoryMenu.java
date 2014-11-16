package com.tapster.barMenu;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.tapster.R;
import com.tapster.customer.ConfirmDialog;

public class CategoryMenu extends Activity {
	
	private Button showTab;
	private String category;

	public BarMenuDB menu=BarMenu.menuDB;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subcat);
		showTab = (Button) findViewById(R.id.tabButton3);
		showTab.setText("see my tab");
		showTab.setOnClickListener(new OnClickListener() {

				@Override
			public void onClick(View v) {
					Intent myIntent = new Intent(CategoryMenu.this, Tab.class);
					startActivity(myIntent);
				
			}
		});
		getItems();
		ClickcItem();
		
	}
	
	private void getItems(){ //get the Items from the menu
		category=BarMenu.SetCategory;
		ArrayList<String> Items = new ArrayList<String>();
		Cursor c= menu.getAllRowsOfCat(category);
		
		if (c.moveToFirst()) {
			do {
				String newItem="";
				String name = c.getString(BarMenuDB.COL_NAME);
				float price = c.getFloat(BarMenuDB.COL_PRICE);
				newItem +=name+"    $"+price;
				Items.add(newItem);
			} while(c.moveToNext());
		}
		
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.categories2,Items);
		ListView itemsList =(ListView) findViewById(R.id.itemsView1);
		itemsList.setAdapter(adapter2);
		
	}
	
	private void ClickcItem()
	{
		ListView itemsList = (ListView) findViewById(R.id.itemsView1);
		itemsList.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked,
					int position, long id) 
			{
				TextView textView = (TextView) viewClicked;
				String message =  textView.getText().toString(); 
				ConfirmDialog comfirm=new ConfirmDialog(CategoryMenu.this, message, category);
				comfirm.show();  
			}
		
		});
	}
}
