package com.tapster.customer;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;
import com.tapster.R;
import com.tapster.azureConnectivity.AzureCallback;
import com.tapster.azureConnectivity.AzureServiceConnection;
import com.tapster.data.User;

public class LoginActivity extends Activity implements AzureCallback
{
	private Button loginButton;
	private ProgressBar progressbar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		progressbar = (ProgressBar) findViewById(R.id.progressBarMain);
		loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Login(true);
			}
		});

		// Connect to azure.
		AzureServiceConnection.Initialize(this, CustomerNotificationHandler.class);

		// Auto Login:
		Login(false);
	}

	@Override
	public void ServiceRequestComplete(Boolean result)
	{
		if (result)
		{
			UserExists();
		} else
		{
			loginButton.setVisibility(Button.VISIBLE);
			progressbar.setVisibility(ProgressBar.GONE);
		}
	}

	private void UserExists()
	{
		progressbar.setVisibility(ProgressBar.VISIBLE);
		MobileServiceClient mClient = AzureServiceConnection.instance.getClient();

		// Get the Mobile Service Table instance to use
		MobileServiceTable<User> mToDoTable = mClient.getTable(User.class);

		final String userId = mClient.getCurrentUser().getUserId();

		mToDoTable.where().field("userid").eq(userId).execute(new TableQueryCallback<User>()
		{

			public void onCompleted(List<User> result, int count,
					Exception exception, ServiceFilterResponse response)
			{
				if (exception == null)
				{
					if (result.size() == 0)
						setupNewUser(userId);
					else
						StartMainActivity();
				} else
				{
					Log.e(LoginActivity.class.getName(), "Error in fetching user.");
				}
			}
		});
	}

	private void setupNewUser(String userId)
	{
		Intent myIntent = new Intent(this, NewUser.class);
		myIntent.putExtra("userId", userId);
		startActivity(myIntent);
		loginButton.setVisibility(Button.VISIBLE);
		progressbar.setVisibility(ProgressBar.GONE);
	}

	private void StartMainActivity()
	{
		Intent myIntent = new Intent(this, MainActivity.class);
		startActivity(myIntent);
		finish();
	}

	private void Login(boolean refreshToken)
	{
		// Hide Button and progress bar.
		loginButton.setVisibility(Button.INVISIBLE);
		progressbar.setVisibility(ProgressBar.VISIBLE);
		AzureServiceConnection.instance.authenticate(refreshToken);
	}
	
	
}
