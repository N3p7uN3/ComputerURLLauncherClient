package com.n3p7un3.computerurllauncher;

import android.os.Debug;
import android.util.Log;

import com.n3p7un3.computerurllauncher.NetworkCommunicator.NetworkComListener;

public class UrlLauncherComProtocol {

	private String mServerAddr;
	private int mServerPort;
	private String mUrl;
	
	private NetworkCommunicator mCom;
	
	private boolean mConnectedSuccess;
	private boolean mSuccessSuccess;
	private Object mActionTimeout;
	
	public UrlLauncherComProtocol(String url, String serverAddress, int serverPort)
	{
		mServerAddr = serverAddress;
		mServerPort = serverPort;
		
		mCom = new NetworkCommunicator();
		mCom.AddNetworkEventListener(new NetworkComListener() {

			@Override
			public void fireEvent(NetworkEvent ne) {
				// TODO Auto-generated method stub
				NetworkComEvent(ne);
			}
		}
		);
		
		mConnectedSuccess = false;
		mSuccessSuccess = false;
		mActionTimeout = new Object();
		
		
		
	
	}
	
	public boolean Go()
	{
		//Attempt to start the connection
		mCom.AttemptConnect(mServerAddr, mServerPort);
		Wait(1000);
		
		if (!mConnectedSuccess)
		{
			Log.w("com.n3p7un3.computerurllauncher", "Could not connect");
			return false;
		}
		
		//If got here, connection success.
		mCom.SendPacket(mUrl);
		Wait(1000);
		
		if (!mSuccessSuccess)
		{
			Log.w("com.n3p7un3.computerurllauncher", "Did not succeed");
			return false;
		}
		
		//If got here, received a success packet
		mCom.Disconnect("We're done here.");
		return true;
		
	}
	
	private void NetworkComEvent(NetworkEvent ne)
	{
		switch (ne.EventType)
		{
			case Connected:
				mConnectedSuccess = true;
				Log.w("com.n3p7un3.computerurllauncher", "connected");
				DoneWaiting();
				break;
			case ComFailureDisconnecting:
				Log.w("com.n3p7un3.computerurllauncher", "could not connect");
				DoneWaiting();
				break;
			
			case PacketReceived:
				ParsePacket(ne.Data);
				Log.w("com.n3p7un3.computerurllauncher", "Received okay");
				DoneWaiting();
				break;
		}
	}
	
	private void ParsePacket(String packet)
	{
		if (packet.equals("success"))
		{
			mSuccessSuccess = true;
			DoneWaiting();
		}
	}
	
	private void Wait(int milliseconds)
	{
		synchronized(mActionTimeout) {
			try {
				mActionTimeout.wait(milliseconds);
			} catch (InterruptedException e)
			{
				//nothing
			}
		}
	}
	
	private void DoneWaiting()
	{
		try {
			mActionTimeout.notifyAll();
		} catch (IllegalMonitorStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
