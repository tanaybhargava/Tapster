package com.tapster.utilities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.tapster.R;

public class ProgressActivity extends Activity
{
	private static Activity instance;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		instance = this;
		
		setContentView(R.layout.activity_progress);

		TextView messageBox = (TextView) findViewById(R.id.messageBox);

		String message = getIntent().getExtras().getString("message");

		if (!message.isEmpty())
			messageBox.setText(message);
	}

	@Override
	public void onBackPressed()
	{
	}

	public static void End()
	{
		if (instance != null)
			instance.finish();
	}
	
	public static void Start(Activity activity, String message)
	{
		Intent myIntent = new Intent(activity.getApplicationContext(), ProgressActivity.class);
		myIntent.putExtra("message", message);
		activity.startActivity(myIntent);
	}
}
