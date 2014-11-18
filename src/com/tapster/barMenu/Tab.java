package com.tapster.barMenu;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.tapster.R;
import com.tapster.data.Order;
import com.tapster.data.OrderList;
import com.tapster.ui.OrderAdapter;

public class Tab extends Fragment
{

	public static OrderList items = new OrderList();
	public static OrderAdapter adapter;

	private static float total;
	private static float subTotal;
	private float tipAmount;
	private Button pay;
	//private Button later;
	private ToggleButton tip15, tip20, tip30, tipCustom;

	private Context ctx;
	private View rootView;

	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{

		ctx = getActivity().getApplicationContext();
		rootView = inflater.inflate(R.layout.tab, container, false);

		setTipAmount(20);
		tip15 = (ToggleButton) rootView.findViewById(R.id.button_15);
		tip20 = (ToggleButton) rootView.findViewById(R.id.button_20);
		tip30 = (ToggleButton) rootView.findViewById(R.id.button_30);
		tipCustom = (ToggleButton) rootView.findViewById(R.id.button_customAmount);
		setTipButtons(tip15);
		setTipButtons(tip20);
		setTipButtons(tip30);
		setTipButtons(tipCustom);
		tip20.setChecked(true);

		pay = (Button) rootView.findViewById(R.id.button_Pay);
//		later = (Button) rootView.findViewById(R.id.button_Later);
		ListView tabText = (ListView) rootView.findViewById(R.id.tabList);
		adapter = new OrderAdapter(ctx,R.layout.tabtext, items.getItems());
		adapter.setNotifyOnChange(true);
		tabText.setAdapter(adapter);
		setCCSpinner();
		setTotal();
		ClickBars(pay);
//		ClickBars(later);

		return rootView;
	}

	

	private void setTipButtons(ToggleButton b)
	{
		b.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				switch (v.getId())
				{
				case R.id.button_15:
					tip15.setChecked(true);
					tip20.setChecked(false);
					tip30.setChecked(false);
					setTipAmount(15);
					break;
				case R.id.button_20:
					tip15.setChecked(false);
					tip20.setChecked(true);
					tip30.setChecked(false);
					setTipAmount(20);
					break;
				case R.id.button_30:
					tip15.setChecked(false);
					tip20.setChecked(false);
					tip30.setChecked(true);
					setTipAmount(30);
					break;
				case R.id.button_customAmount:
					ToggleButton x = (ToggleButton) rootView.findViewById(R.id.button_customAmount);
					if (x.isChecked() == false)
					{
						EditText textField = (EditText) rootView.findViewById(R.id.Text_customTip);
						Button done = (Button) rootView.findViewById(R.id.button_tip_done);
						done.setVisibility(View.GONE);
						textField.setVisibility(View.GONE);
						tip15.setVisibility(View.VISIBLE);
						tip20.setVisibility(View.VISIBLE);
						tip30.setVisibility(View.VISIBLE);

					} else
					{
						tip15.setVisibility(View.GONE);
						tip20.setVisibility(View.GONE);
						tip30.setVisibility(View.GONE);
						EditText textField = (EditText) rootView.findViewById(R.id.Text_customTip);
						Button done = (Button) rootView.findViewById(R.id.button_tip_done);
						done.setVisibility(View.VISIBLE);
						textField.setVisibility(View.VISIBLE);
						setCustomTip(done);
					}
					break;
				default:
					break;
				}
			}

			private void setCustomTip(Button done)
			{
				done.setOnClickListener(new OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						EditText textField = (EditText) rootView.findViewById(R.id.Text_customTip);
						String temp = textField.getText().toString();
						if (temp.isEmpty())
						{
							tipAmount = 20;
							setTotal();
						} else
						{
							float x = Float.parseFloat(temp);
							tipAmount = x;
							Toast.makeText(ctx, temp, Toast.LENGTH_LONG).show();
							setTotal();
						}
					}

				});

			}
		});
	}

	private void setCCSpinner()
	{
		Spinner dropdown = (Spinner) rootView.findViewById(R.id.spinner_CC);
		String[] CClist = new String[] { "Visa ending in 666", "othercard%" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, R.layout.myspineritem, CClist);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dropdown.setAdapter(adapter);
		dropdown.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
				// TODO Auto-generated method stub

			}
		});
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
				case R.id.button_Pay:
					String message = total + " charged to: ";
					Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
					items = new OrderList();
					adapter.clear();
					clearTotal();
					break;
				}

			}
		});
	}

	public void setTipAmount(float x)
	{
		float percentage = x / 100;
		tipAmount = subTotal * percentage;
		setTotal();
	}

	public void setTotal()
	{
		total = subTotal + tipAmount;
		TextView textView2 = (TextView) rootView.findViewById(R.id.string_total);
		textView2.setText("Total: $" + total);
	}

	public void clearTotal()
	{
		subTotal = 0;
		total = 0;
		TextView textView2 = (TextView) rootView.findViewById(R.id.string_total);
		textView2.setText("Total: $" + total);
	}

	public static void getNewOrders(OrderList newOrder)
	{
		for (Order order: newOrder.getItems())
		{
			items.AddOrder(order);
		}
		subTotal+=items.getTotal();
	}

}
