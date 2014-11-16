package com.tapster.barMenu;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tapster.R;

public class Tab extends Activity{
	
	public static ArrayList<String> items=new ArrayList<String>();
	public static ArrayAdapter<String> adapter;
	
	private static float total;
	private static float subTotal;
	private float tipAmount;
	private Button pay;
	private Button later;
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tab);
		setTipSpinner();

		pay = (Button) findViewById(R.id.button_Pay);
		later = (Button) findViewById(R.id.button_Later);

		ListView tabText =(ListView) findViewById(R.id.tabList);
		adapter= new ArrayAdapter<String>(this, R.layout.tabtext, items);
		adapter.setNotifyOnChange(true);
		tabText.setAdapter(adapter);
		setCCSpinner();
		setTotal();
		
		ClickBars(pay);
		ClickBars(later);
	}


public static void  getNewOrder(String order)
{
	items.add(order);
	String priceString=order.substring(order.indexOf('$')+1);
	float temp=Float.parseFloat(priceString);
	subTotal+=temp;
	
}
	private void setTipSpinner() {
		Spinner dropdown = (Spinner)findViewById(R.id.spinner_tip);
		String[] tipPercentages = new String[]{"10%","15%", "20%","25%", "30%", "35%","40%", "45%", "50%", "60%", "70%", "80%", "90%", "100%", "120%", "140%", "150%", "180%"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(Tab.this, R.layout.myspineritem, tipPercentages);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dropdown.setAdapter(adapter);
		dropdown.setSelection(3);
		dropdown.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				TextView textView = (TextView) view;
				String tipString =  textView.getText().toString().substring(0, textView.getText().toString().indexOf('%'));
		    	setTipAmount(Float.parseFloat(tipString));

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		
		});

	}
	
	private void setCCSpinner() {
		Spinner dropdown = (Spinner)findViewById(R.id.spinner_CC);
		String[] CClist = new String[]{"Visa ending in 666", "othercard%"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(Tab.this, R.layout.myspineritem, CClist);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dropdown.setAdapter(adapter);
		dropdown.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}});
	}
	
private void ClickBars(Button b) {
		b.setOnClickListener(new OnClickListener() {
			@Override
		public void onClick(View v) {
				switch (v.getId()) {
			    case R.id.button_Pay:
			    	String message=total+" charged to: ";
			    	Toast.makeText(Tab.this, message, Toast.LENGTH_LONG).show();
			    	items.clear();
			    	adapter.clear();
			    	clearTotal();
			      break;
			    case R.id.button_Later:
			    	finish();
			      break;
			    default:
			    	finish();
			      break;
			    }
			
		}
	});
	}

	public void setTipAmount(float x)
	{
		float percentage=x/100;
		tipAmount=subTotal*percentage;
		setTotal();
	}


	public void setTotal()
	{
		total=subTotal+tipAmount;
		TextView textView2 =(TextView) findViewById(R.id.string_total);
		textView2.setText("Total: $"+total);
	}
	
	public void clearTotal()
	{
		subTotal=0;
		total=0;
		TextView textView2 =(TextView) findViewById(R.id.string_total);
		textView2.setText("Total: $"+total);
	}
	

}
