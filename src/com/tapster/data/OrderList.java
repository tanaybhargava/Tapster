package com.tapster.data;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class OrderList
{
	public void setmColor(int mColor)
	{
		this.mColor = mColor;
	}

	@com.google.gson.annotations.SerializedName("id")
	private String mId;

	@com.google.gson.annotations.SerializedName("content")
	private String mSerializedString = "";

	@com.google.gson.annotations.SerializedName("color")
	private int mColor;

	@com.google.gson.annotations.SerializedName("complete")
	private Boolean mComplete = false;

	transient List<OrderItem> items;

	public OrderList(OrderList copy)
	{
		items = new ArrayList<OrderItem>();
		for (OrderItem order : copy.items)
			items.add(order);
	}

	public OrderList()
	{
		items = new ArrayList<OrderItem>();
	}

	public OrderList(String mId, String mSerializedString, int mColor,
			Boolean mComplete)
	{
		this.mId = mId;
		this.mSerializedString = mSerializedString;
		this.mColor = mColor;
		this.mComplete = mComplete;

		items = new ArrayList<OrderItem>();

		deSerialize();
	}

	public int getmColor()
	{
		return mColor;
	}

	public Boolean getmComplete()
	{
		return mComplete;
	}

	@Override
	public boolean equals(Object o)
	{
		return o instanceof OrderList && ((OrderList) o).mId == mId;
	}

	public void AddOrder(OrderItem order)
	{
		items.add(order);
		mSerializedString = serialize();
	}

	public List<OrderItem> getItems()
	{
		return items;
	}

	public String printOrder(boolean printPrice)
	{
		String result = "";

		for (OrderItem order : items)
		{
			String price = "";
			if (printPrice)
				price = ": " + order.price;
			result += order.itemName + price + "\n\n";
		}

		return result;
	}

	public float getTotal()
	{
		float total = 0;

		for (OrderItem order : items)
		{
			total += order.price;
		}

		return total;
	}

	public void setmComplete(Boolean mComplete)
	{
		this.mComplete = mComplete;
	}

	public String serialize()
	{
		String serializedString = "";

		for (OrderItem order : items)
			serializedString += order.itemName + "," + order.price + "&";

		return serializedString;
	}

	public void deSerialize()
	{
		items = new ArrayList<OrderItem>();
		try
		{
			for (String pair : mSerializedString.split("&"))
			{
				String[] pairSplit = pair.split(",");

				String n = pairSplit[0];
				float p = Float.parseFloat(pairSplit[1]);
				items.add(new OrderItem(n, p));
			}
		} catch (Exception ex)
		{
			Log.e(OrderList.class.getName(), "Error is parsing: " + ex);
		}
	}
}
