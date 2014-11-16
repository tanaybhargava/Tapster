package com.tapster.customer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.tapster.R;
import com.tapster.azureConnectivity.AzureServiceConnection;
import com.tapster.data.User;

public class ProfileFragment extends Fragment
{
	private TextView firstName;
	private TextView lastName;
	private TextView email;
	private TextView mobile;
	private TextView creditcard;
	private RelativeLayout overlay;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

		// Get UI elements.
		firstName = (TextView) rootView.findViewById(R.id.firstName);
		lastName = (TextView) rootView.findViewById(R.id.lastName);
		email = (TextView) rootView.findViewById(R.id.email);
		mobile = (TextView) rootView.findViewById(R.id.mobile);
		creditcard = (TextView) rootView.findViewById(R.id.creditcardnumber);

		// Set overlay progress bar.
		overlay = (RelativeLayout) rootView.findViewById(R.id.overlay);
		overlay.setVisibility(RelativeLayout.VISIBLE);

		MobileServiceClient mClient = AzureServiceConnection.instance.getClient();

		mClient.invokeApi("customer/profile", "get", null, User.class, new ApiOperationCallback<User>()
		{

			@Override
			public void onCompleted(User arg0, Exception arg1,
					ServiceFilterResponse arg2)
			{
				if (arg0 != null)
				{
					firstName.setText("First Name : " + arg0.getFirstName());
					lastName.setText("Last Name : " + arg0.getLastName());
					email.setText("Email :" + arg0.getEmail());
					mobile.setText("Mobile : " + arg0.getMobile());
				}
			}
		});

		mClient.invokeApi("customer/creditcard", "get", null, String.class, new ApiOperationCallback<String>()
		{
			@Override
			public void onCompleted(String arg0, Exception arg1,
					ServiceFilterResponse arg2)
			{
				if (arg0 != null)
					creditcard.setText("Credit Card Ending With : " + arg0);
				overlay.setVisibility(RelativeLayout.INVISIBLE);
			}
		});

		return rootView;
	}
}
