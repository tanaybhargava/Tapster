package com.tapster.customer;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tapster.R;

public class NewUser extends Activity
{

	private static final int MY_SCAN_REQUEST_CODE = 0;
	private RelativeLayout overlay;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_user);

		Intent intent = getIntent();
		String userId = intent.getStringExtra("userId");

		overlay = (RelativeLayout) findViewById(R.id.overlay);

		overlay.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				// Override Touch to background.
				return true;
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == MY_SCAN_REQUEST_CODE)
		{
			if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT))
			{
				CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

				String cardNumber = scanResult.getRedactedCardNumber();
				String expDate = scanResult.expiryMonth + "/" + scanResult.expiryYear;

				TextView cardTV = (TextView) findViewById(R.id.creditcardnumber);
				cardTV.setText(cardNumber);

				TextView expTV = (TextView) findViewById(R.id.creditcardexpiry);
				expTV.setText(expDate);
			}
		}
	}

	public void scanCreditCard(View v)
	{
		Intent scanIntent = new Intent(this, CardIOActivity.class);
		scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // true
		scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false);
		scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false);
		startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
	}

	public void signUp(View v)
	{
		overlay.setVisibility(RelativeLayout.VISIBLE);
	}

}
