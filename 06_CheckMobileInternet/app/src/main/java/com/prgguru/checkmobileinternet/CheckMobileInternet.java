package com.prgguru.checkmobileinternet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CheckMobileInternet extends Activity {

	//Internet status flag
	Boolean isConnectionExist = false;
	
	// Connection detector class
	MobileInternetConnectionDetector cd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button btnStatus = (Button) findViewById(R.id.btn_check);

		// creating connection detector class instance
		cd = new MobileInternetConnectionDetector(getApplicationContext());

		//Button click listener to check internet status
		btnStatus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				// get Internet status
				isConnectionExist = cd.checkMobileInternetConn();

				// check for Internet status
				if (isConnectionExist) {
					// Internet Connection exists
					showAlertDialog(CheckMobileInternet.this, "Internet Connection",
							"Your device has mobile internet", true);
				} else {
					// Internet connection doesn't exist
					showAlertDialog(CheckMobileInternet.this, "No Internet Connection",
							"Your device doesn't have mobile internet", false);
				}
			}

		});

	}

	/**
	 * Function to display simple Alert Dialog
	 * @param context - application context
	 * @param title - alert dialog title
	 * @param message - alert message
	 * @param status - success/failure (used to set icon)
	 * */
	public void showAlertDialog(Context context, String title, String message, Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);
		
		// Setting alert dialog icon
		alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}
}