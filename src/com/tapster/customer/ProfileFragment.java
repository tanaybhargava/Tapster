package com.tapster.customer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tapster.R;
import com.tapster.data.User;

public class ProfileFragment extends Fragment
{
	private static User userData;
	private static String creditCard;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

		// Get UI elements.
		TextView firstNameTv = (TextView) rootView.findViewById(R.id.firstName);
		TextView lastNameTv = (TextView) rootView.findViewById(R.id.lastName);
		TextView emailTv = (TextView) rootView.findViewById(R.id.email);
		TextView mobileTv = (TextView) rootView.findViewById(R.id.mobile);
		TextView creditcardTv = (TextView) rootView.findViewById(R.id.creditcardnumber);

		firstNameTv.setText("First Name : " + userData.getFirstName());
		lastNameTv.setText("Last Name : " + userData.getLastName());
		emailTv.setText("Email :" + userData.getEmail());
		mobileTv.setText("Mobile : " + userData.getMobile());
		creditcardTv.setText("Credit Card Ending with:" + creditCard);

		return rootView;
	}

	public static User getUserData()
	{
		return userData;
	}

	public static void setUserData(User data)
	{
		userData = data;
	}

	public static String getCreditCard()
	{
		return creditCard;
	}

	public static void setCreditCard(String crCard)
	{
		creditCard = crCard;
	}
}
