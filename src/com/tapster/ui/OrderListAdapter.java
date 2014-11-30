package com.tapster.ui;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

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

	@SuppressLint({ "ViewHolder", "CutPasteId" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		convertView = LayoutInflater.from(this.getContext()).inflate(layout, parent, false);

		OrderList item = getItem(position);

		if (layout == R.layout.listentry_pendingorder)
		{

			CheckBox name = (CheckBox) convertView.findViewById(R.id.list_item);
			name.setText(item.printOrder(false));
			name.setChecked(item.getmComplete());

			if (item.getmComplete())
				convertView.setBackgroundColor(item.getmColor());
			else
				convertView.setBackgroundColor(Color.WHITE);
		}

		if (layout == R.layout.listentry_textview)
		{
			TextView tv = (TextView) convertView.findViewById(R.id.list_item);
			tv.setText(item.printOrder(true) + "\n" + "Total = $" + item.getTotal());
		}

		return convertView;
	}
}
