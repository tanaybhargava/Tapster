package com.tapster.customer;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.tapster.R;
import com.tapster.azureConnectivity.AzureServiceConnection;
import com.tapster.data.Payment;
import com.tapster.data.User;

public class NewUser extends Activity
{

	private static final int MY_SCAN_REQUEST_CODE = 0;
	private RelativeLayout overlay;

	private User newUser = new User();

	private TextView firstName;
	private TextView lastName;
	private TextView mobile;
	private TextView email;
	private TextView creditcard;
	private TextView expiry;
	private Payment creditCard;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_user);

		Intent intent = getIntent();
		newUser.setUserid(intent.getStringExtra("userId"));

		overlay = (RelativeLayout) findViewById(R.id.overlay);
		// Disable touch events.
		overlay.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				// Override Touch to background.
				return true;
			}
		});

		// Get UI elements.
		firstName = (TextView) findViewById(R.id.firstName);
		lastName = (TextView) findViewById(R.id.lastName);
		email = (TextView) findViewById(R.id.email);
		mobile = (TextView) findViewById(R.id.mobile);
		creditcard = (TextView) findViewById(R.id.creditcardnumber);
		expiry = (TextView) findViewById(R.id.creditcardexpiry);
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

				creditcard.setText(cardNumber);
				expiry.setText(expDate);
				creditCard = new Payment(newUser.getUserid(), cardNumber, scanResult.cvv, scanResult.expiryMonth, scanResult.expiryYear);
			}
		}
	}

	public void scanCreditCard(View v)
	{
		Intent scanIntent = new Intent(this, CardIOActivity.class);
		scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true);
		scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true);
		scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false);
		startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
	}

	public void signUp(View v)
	{
		// Set overlay progressbar.
		overlay.setVisibility(RelativeLayout.VISIBLE);

		// Get information from form.
		newUser.setEmail(email.getText().toString());
		newUser.setFirstName(firstName.getText().toString());
		newUser.setLastName(lastName.getText().toString());
		newUser.setMobile(mobile.getText().toString());

		// Add user to table.
		// Insert the new item
		AddUser(this, newUser);

	}

	private void AddUser(final Context ctx, User user)
	{
		MobileServiceClient mClient = AzureServiceConnection.instance.getClient();
		MobileServiceTable<User> mToDoTable = mClient.getTable(User.class);

		mToDoTable.insert(user, new TableOperationCallback<User>()
		{
			public void onCompleted(User entity, Exception exception,
					ServiceFilterResponse response)
			{
				if (exception != null)
					Toast.makeText(ctx, "Failed", Toast.LENGTH_LONG).show();
				else
				{
					if (creditCard != null)
						AddPaymentInfo(ctx, creditCard);
					else
						finish();
				}
			}
		});
	}

	private void AddPaymentInfo(final Context ctx, Payment user)
	{
		MobileServiceClient mClient = AzureServiceConnection.instance.getClient();
		MobileServiceTable<Payment> mToDoTable = mClient.getTable(Payment.class);

		mToDoTable.insert(user, new TableOperationCallback<Payment>()
		{
			public void onCompleted(Payment entity, Exception exception,
					ServiceFilterResponse response)
			{
				if (exception != null)
					Toast.makeText(ctx, "Failed", Toast.LENGTH_LONG).show();
				else
				{
					Toast.makeText(ctx, "Success", Toast.LENGTH_LONG).show();
					finish();
				}
			}
		});
	}

}
