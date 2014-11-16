package com.tapster.utilities;

import android.app.Activity;
import android.widget.ProgressBar;

import com.microsoft.windowsazure.mobileservices.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponseCallback;
import com.tapster.R;

public class ProgressFilter implements ServiceFilter
{
	//Todo:: Set Unique ID of progress bar.
	public static int mProgressBarID;
	
	private Activity mainActivity;
	private ProgressBar mProgressBar;

	public ProgressFilter(Activity mainActivity)
	{
		super();
		this.mainActivity = mainActivity;
		mProgressBar = (ProgressBar) mainActivity.findViewById(R.id.progressBarMain);
	}

	@Override
	public void handleRequest(ServiceFilterRequest request,
			NextServiceFilterCallback nextServiceFilterCallback,
			final ServiceFilterResponseCallback responseCallback)
	{
		mainActivity.runOnUiThread(new Runnable()
		{

			@Override
			public void run()
			{
				if (mProgressBar != null)
					mProgressBar.setVisibility(ProgressBar.VISIBLE);
			}
		});

		nextServiceFilterCallback.onNext(request,
				new ServiceFilterResponseCallback()
				{

					@Override
					public void onResponse(ServiceFilterResponse response,
							Exception exception)
					{
						mainActivity.runOnUiThread(new Runnable()
						{

							@Override
							public void run()
							{
								if (mProgressBar != null)
									mProgressBar
											.setVisibility(ProgressBar.GONE);
							}
						});

						if (responseCallback != null)
							responseCallback.onResponse(response, exception);
					}
				});
	}
}