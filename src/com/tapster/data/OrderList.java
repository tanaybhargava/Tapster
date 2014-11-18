package com.tapster.data;

import java.util.ArrayList;

public class OrderList
{
	ArrayList<OrderItem> items;

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

	public void AddOrder(OrderItem order)
	{
		items.add(order);
	}

	public ArrayList<OrderItem> getItems()
	{
		return items;
	}

	public String printOrder()
	{
		String result = "";

		for (OrderItem order : items)
		{
			result += order.itemName + "\n\n";
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
}
