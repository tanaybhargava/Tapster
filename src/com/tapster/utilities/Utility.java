package com.tapster.utilities;

import android.app.AlertDialog;
import android.content.Context;

public class Utility 
{
	public static void createAndShowDialog(Exception exception, String title,Context ctx) 
	{
		Throwable ex = exception;
		if (exception.getCause() != null) {
			ex = exception.getCause();
		}
		createAndShowDialog(ex.getMessage(), title,ctx);
	}

	public static void createAndShowDialog(String message, String title, Context ctx) 
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

		builder.setMessage(message);
		builder.setTitle(title);
		builder.create().show();
	}
}
