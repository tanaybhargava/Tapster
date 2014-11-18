package com.tapster.barMenu;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tapster.R;
import com.tapster.data.OrderItem;

public class OrderConfirmDialog extends Dialog implements
android.view.View.OnClickListener {

public Activity a;
public Dialog d;
private String order;
private String priceString;
private float price;
public String addToOrderString;
public float addToOrderPrice;
public Button comfirm;
public Button addToOrder;
public AddonDB menuDB;
public String category;



public OrderConfirmDialog(Activity a, String name, String Category) {
	
super(a);
this.a = a;
order =name.substring(0, name.indexOf('$')-1);
priceString=name.substring(name.indexOf('$')+1);
price=Float.parseFloat(priceString);
category=Category;

}

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
requestWindowFeature(Window.FEATURE_NO_TITLE);
setContentView(R.layout.dialog_order_confirm);
accesDB();
populateDB();
comfirm = (Button) findViewById(R.id.button_Confirm);
addToOrder = (Button) findViewById(R.id.button_add);
updateOrderString(order);
setTotal();
setAddSpinner();
comfirm.setOnClickListener(this);
addToOrder.setOnClickListener(this);

}
private void updateOrderString(String order2) {
	TextView textView =(TextView) findViewById(R.id.string_order);
	textView.setText(order2);
}

public void setAddSpinner() {
	Spinner dropdown = (Spinner)this.findViewById(R.id.spinner_add);
	ArrayList<String> additionOptions = getAdditions();
	ArrayAdapter<String> adapter=new ArrayAdapter<String>(a, R.layout.myspineritem, additionOptions);
	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	dropdown.setAdapter(adapter);
	dropdown.setSelection(1);
	dropdown.setOnItemSelectedListener(new OnItemSelectedListener(){

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			TextView textView = (TextView) view;
			addToOrderString =  textView.getText().toString().substring(0, textView.getText( ).toString().indexOf('+'));
	    	String temp=textView.getText().toString().substring(textView.getText( ).toString().indexOf('$')+1);
			addToOrderPrice=Float.parseFloat(temp);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
		
	});

}

@Override
public void onClick(View v) {
	switch (v.getId()) {
    case R.id.button_Confirm:
    
    	OrderItem newOrder = new OrderItem(order,price);
    	
    	CurrentOrderFragment.getNewItem(newOrder);
    	exitDB();
    	dismiss();
      break;
    case R.id.button_add:
    	order+=" + "+addToOrderString;
		updateOrderString(order);
		if (addToOrderPrice!=0){price+=addToOrderPrice;}
		setTotal();
    	Toast.makeText(a, addToOrderString+" was added", Toast.LENGTH_LONG).show();
    	break;
    default:
    	exitDB();
    	dismiss();
      break;
    }
  }
public void setTotal()
{
	TextView textView =(TextView) findViewById(R.id.string_total1);
	textView.setText("Total: "+" $"+price);
}

public void placeOrder(String order){
	//send the order to the bartender app
}

private void accesDB(){
	menuDB=new AddonDB(this);
	menuDB.open();
}

private ArrayList<String> getAdditions(){ //fill list with the categories
	ArrayList<String> additionOptions = new ArrayList<String>();
//	switch (category)
//	{}
	Cursor cursor= menuDB.getAllRows();
	
	if (cursor.moveToFirst()) {
		do {
			String name = cursor.getString(BarMenuDB.COL_NAME);
			String price = cursor.getString(BarMenuDB.COL_PRICE);
			if (additionOptions.contains(name)==false)
			{
				additionOptions.add(name+" +$"+price);
			}
		} while(cursor.moveToNext());
	}
	
	return additionOptions;
	
}

private void populateDB(){
	menuDB.insertRow(1, "liquor","soda" , (float)0, " ", 0);
	menuDB.insertRow(1, "liquor","Cola" , (float)0, " ", 0);
	menuDB.insertRow(1, "all drinks","water" , (float)0, " ", 0);
	menuDB.insertRow(1, "all drinks","lime" , (float)0, " ", 0);
	menuDB.insertRow(1, "liquor","Ginger Beer" , (float).59, " ", 0);
	menuDB.insertRow(1, "liquor","Juice" , (float)0, " ", 0);
	menuDB.insertRow(1, "all drinks","no ice" , (float)300, " ", 0);

}

private void exitDB(){
	menuDB.deleteAll();
	menuDB.close();
}

}

