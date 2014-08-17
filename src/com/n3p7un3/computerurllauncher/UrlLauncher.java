package com.n3p7un3.computerurllauncher;

import android.util.Log;

public class UrlLauncher {
	
	private Thread mLauncherThread;
	
	public UrlLauncher(String url, String serverAddress, int serverPort)
	{
		new Thread(new UrlLauncherThread(url, serverAddress, serverPort)).start();
	}
	
	private class UrlLauncherThread implements Runnable
	{
		private String mUrl;
		private volatile String mServerAddr;
		private volatile int mServerPort;
		
		public UrlLauncherThread(String url, String serverAddress, int serverPort)
		{
			mUrl = url;
			mServerAddr = serverAddress;
			mServerPort = serverPort;
			
		}
		
		public void run()
		{
			UrlLauncherComProtocol com = new UrlLauncherComProtocol(mUrl, mServerAddr, mServerPort);
			com.Go();
		}
	}
	
}
