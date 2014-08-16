package com.n3p7un3.computerurllauncher;

import com.n3p7un3.computerurllauncher.NetworkCom.NetworkCommunicator;
import com.n3p7un3.computerurllauncher.NetworkCom.NetworkEvent;
import com.n3p7un3.computerurllauncher.NetworkCom.NetworkEventType;
import com.n3p7un3.computerurllauncher.NetworkCom.NetworkCommunicator.NetworkComListener;

public class UrlLauncherComProtocol {

	private String mServerAddr;
	private int mServerPort;
	private String mUrl;
	
	private NetworkCommunicator mCom;
	
	private boolean mConnectedSuccess;
	private boolean mSuccessSuccess;
	private Object mActionTimeout;
	
	public UrlLauncherComProtocol(String serverAddress, int serverPort, String url)
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
		Wait(2000);
		
		if (!mConnectedSuccess)
			return false;
		
		//If got here, connection success.
		mCom.SendPacket(mUrl);
		Wait(1000);
		
		if (!mSuccessSuccess)
			return false;
		
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
			case ComFailureDisconnecting:
				DoneWaiting();
				break;
			
			case PacketReceived:
				ParsePacket(ne.Data);
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
		mActionTimeout.notifyAll();
	}
}
