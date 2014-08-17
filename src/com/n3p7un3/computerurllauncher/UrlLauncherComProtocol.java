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
	private Object mSuccessTimeout;
	
	public UrlLauncherComProtocol(String url, String serverAddress, int serverPort)
	{
		mServerAddr = serverAddress;
		mServerPort = serverPort;
		mUrl = url;
		
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
		mSuccessTimeout = new Object();
		
		
	
	}
	
	public boolean Go()
	{
		//Attempt to start the connection
		mCom.AttemptConnect(mServerAddr, mServerPort);
		Log.w("com.n3p7un3.computerurllauncher", "attempt connection " + mServerAddr + ":" + Integer.toString(mServerPort));
		Wait(mActionTimeout, 1000);
		
		if (!mConnectedSuccess)
		{
			Log.w("com.n3p7un3.computerurllauncher", "Could not connect");
			Done();
			return false;
		}
		
		//If got here, connection success.
		Log.w("com.n3p7un3.computerurllauncher", "got before sending packet: " + mUrl);
		mCom.SendPacket(mUrl);
		Wait(mSuccessTimeout, 3000);
		
		if (!mSuccessSuccess)
		{
			Log.w("com.n3p7un3.computerurllauncher", "Did not succeed");
			Done();
			return false;
		}
		
		//If got here, received a success packet
		//mCom.Disconnect("We're done here.");
		Done();
		return true;
		
	}
	
	private void Done()
	{
		mCom.Disconnect("");
		Wait(mActionTimeout, 1000);
	}
	
	private void NetworkComEvent(NetworkEvent ne)
	{
		switch (ne.EventType)
		{
			case Connected:
				mConnectedSuccess = true;
				Log.w("com.n3p7un3.computerurllauncher", "connected");
				DoneWaiting(mActionTimeout);
				break;
			case ComFailureDisconnecting:
				Log.w("com.n3p7un3.computerurllauncher", "disconnected: " + ne.Data);
				DoneWaiting(mActionTimeout);
				break;
			
			case PacketReceived:
				ParsePacket(ne.Data);
				//Log.w("com.n3p7un3.computerurllauncher", "Received okay");
				//DoneWaiting();
				break;
		}
	}
	
	private void ParsePacket(String packet)
	{
		if (packet.equals("success"))
		{
			mSuccessSuccess = true;
			Log.w("com.n3p7un3.computerurllauncher", "received success");
			DoneWaiting(mSuccessTimeout);
		}
	}
	
	private void Wait(Object waitObject, int milliseconds)
	{
		synchronized(waitObject) {
			try {
				waitObject.wait(milliseconds);
			} catch (InterruptedException e)
			{
				//nothing
			}
		}
	}
	
	private void DoneWaiting(Object waitObject)
	{
		try {
			waitObject.notifyAll();
		} catch (IllegalMonitorStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
