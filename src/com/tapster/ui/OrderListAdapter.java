package com.tapster.ui;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import com.tapster.R;
import com.tapster.data.OrderList;

public class OrderListAdapter extends ArrayAdapter<OrderList>
{
	int layout;

	public OrderListAdapter(Context context, int resource,
			List<OrderList> objects)
	{
		super(context, resource, objects);
		layout = resource;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		convertView = LayoutInflater.from(this.getContext()).inflate(layout, parent, false);

		if (layout == R.layout.listentry_pendingorder)
		{

			CheckBox name = (CheckBox) convertView.findViewById(R.id.list_item);
			OrderList item = getItem(position);
			name.setText(item.printOrder());
			name.setChecked(item.getmComplete());

			if (item.getmComplete())
				name.setTextColor(item.getmColor());
		}

		return convertView;
	}

}
