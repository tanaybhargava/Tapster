package com.tapster.ui;

import java.util.List;

import com.tapster.R;
import com.tapster.data.OrderList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class OrderListAdapter extends ArrayAdapter<OrderList>
{
	public OrderListAdapter(Context context, int resource,
			List<OrderList> objects)
	{
		super(context, resource, objects);
	}

	@SuppressLint("ViewHolder") @Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.listentry_textview, parent, false);

		TextView name = (TextView) convertView.findViewById(R.id.list_item);

		name.setText(getItem(position).printOrder());

		return convertView;
	}

}
