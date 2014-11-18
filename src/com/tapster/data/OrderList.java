package com.tapster.data;

import java.util.ArrayList;

public class OrderList
{
	ArrayList<Order> items;

	public OrderList(OrderList copy)
	{
		items = new ArrayList<Order>();
		for (Order order : copy.items)
			items.add(order);
	}

	public OrderList()
	{
		items = new ArrayList<Order>();
	}

	public void AddOrder(Order order)
	{
		items.add(order);
	}

	public ArrayList<Order> getItems()
	{
		return items;
	}

	public String printOrder()
	{
		String result = "";

		for (Order order : items)
		{
			result += order.itemName + "\n\n";
		}

		return result;
	}

	public float getTotal()
	{
		float total = 0;

		for (Order order : items)
		{
			total += order.price;
		}

		return total;
	}
}
