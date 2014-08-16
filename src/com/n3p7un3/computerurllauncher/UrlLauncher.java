package com.n3p7un3.computerurllauncher;

import android.content.Context;
import android.content.SharedPreferences;

public class UrlLauncher {
	
	
	private Context mContext;

	public UrlLauncher(Context context)
	{
		mContext = context;
	}
	
	public String SendUrl(String url)
	{
		SharedPreferences prefs = mContext.getSharedPreferences("com.n3p7un3.computerurllauncher.PREFERENCES_KEY", Context.MODE_PRIVATE);
		
		
		String serverAddr = prefs.getString("serverAddress", "");
		if (serverAddr == "")
			return "Server address not set.";
		
		int serverPort = prefs.getInt("serverPort", -1);
		if (serverPort == -1)
			return "Server port is invalid or not specified";
		
		UrlLauncherComProtocol theLauncher = new UrlLauncherComProtocol(serverAddr, serverPort, url);
		boolean result = theLauncher.Go();
		
		if (result)
			return "";
		else
			return "Communication failed.";
		
		
	
	}

	
	
	
	
}
