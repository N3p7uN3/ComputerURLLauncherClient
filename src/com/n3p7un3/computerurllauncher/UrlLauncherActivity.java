package com.n3p7un3.computerurllauncher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class UrlLauncherActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_computer_receiver_settings);
		//Toast.makeText(getApplicationContext(), "fdafs", Toast.LENGTH_LONG).show();
		
		Intent intent = getIntent();
		
		if (intent.hasExtra(Intent.EXTRA_TEXT))
		{
			String url = intent.getStringExtra(Intent.EXTRA_TEXT);
			
			Log.w("com.n3p7un3.computerurllauncher", "started the url launcher: " + url);
			
			SharedPreferences prefs = getApplicationContext().getSharedPreferences("com.n3p7un3.computerurllauncher.prefs", Context.MODE_MULTI_PROCESS);
			
			
			
			String serverAddr = prefs.getString("serverAddress", "");
			int serverPort = Integer.parseInt(prefs.getString("serverPort", "-1"));
			
			if ((serverAddr != "") && (serverPort != -1))
			{
				new UrlLauncher(url, serverAddr, serverPort);
			}
		}
		
		
		
		
		finish();

	}
	
	
}
