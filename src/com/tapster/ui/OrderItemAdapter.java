package com.tapster.ui;

import java.util.List;

import com.tapster.R;
import com.tapster.data.OrderItem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class OrderItemAdapter extends ArrayAdapter<OrderItem>
{

	public OrderItemAdapter(Context context, int resource, List<OrderItem> objects)
	{
		super(context, resource, objects);
	}

	@SuppressLint("ViewHolder") @Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		
		convertView = LayoutInflater.from(this.getContext())
	            .inflate(R.layout.listentry_menu_item, parent, false);
		
		TextView name = (TextView) convertView.findViewById(R.id.orderItem);
		TextView price = (TextView) convertView.findViewById(R.id.orderPrice);
		
		name.setText(getItem(position).itemName);
		price.setText("$"+getItem(position).price);
		
		return convertView;
	}

	


}
