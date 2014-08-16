package com.n3p7un3.computerurllauncher.NetworkCom;

public class NetworkEvent
{
	public volatile NetworkEventType EventType;
	public volatile String Data;
	
	
	
	public NetworkEvent(NetworkEventType eventType, String data) {
		EventType = eventType;
		Data = data;	
	}
	
}
