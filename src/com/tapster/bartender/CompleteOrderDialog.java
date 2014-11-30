package com.tapster.bartender;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.tapster.R;
import com.tapster.data.OrderList;

public class CompleteOrderDialog extends Dialog implements
		android.view.View.OnClickListener
{
	private OrderList order;

	BarTenderActivity parentActivity;

	public CompleteOrderDialog(BarTenderActivity a, OrderList order)
	{
		super(a);
		parentActivity = a;
		this.order = order;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_order_complete);

		Button comfirm = (Button) findViewById(R.id.button_Confirm);
		comfirm.setOnClickListener(this);

		findViewById(R.id.backGroundLayout).setBackgroundColor(order.getmColor());
	}

	@Override
	public void onClick(View v)
	{
		if (v.getId() == R.id.button_Confirm)
		{
			dismiss();
			parentActivity.completeOrder(order);
		}
	}
}
