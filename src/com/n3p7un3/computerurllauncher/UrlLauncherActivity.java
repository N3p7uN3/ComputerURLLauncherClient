package com.n3p7un3.computerurllauncher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class UrlLauncherActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_computer_receiver_settings);
		//Toast.makeText(getApplicationContext(), "fdafs", Toast.LENGTH_LONG).show();
		Intent intent = getIntent();
		
		UrlLauncher theLauncher = new UrlLauncher(getApplicationContext());
		String result = theLauncher.SendUrl(intent.getAction());
		
		if (result == "")
			Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(getApplicationContext(), "Failed: " + result, Toast.LENGTH_LONG).show();
		
		//finished();
	}
	
	
}
