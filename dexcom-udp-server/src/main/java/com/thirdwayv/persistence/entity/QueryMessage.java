package com.thirdwayv.persistence.entity;

import java.util.ArrayList;
import java.util.List;

public class QueryMessage {

	private char GatewayID;
	private short MessageType;
	private int Flags;
	private int Size;
	private List <Node>CGM_Records;
	
	
	public QueryMessage()
	{
		CGM_Records = new  ArrayList<Node>();
	}
	
	public char getGatewayID() {
		return GatewayID;
	}

	public void setGatewayID(char gatewayID) {
		GatewayID = gatewayID;
	}

	public short getMessageType() {
		return MessageType;
	}

	public void setMessageType(short messageType) {
		MessageType = messageType;
	}

	public int getFlags() {
		return Flags;
	}

	public void setFlags(int flags) {
		Flags = flags;
	}

	public int getSize() {
		return Size;
	}

	public void setSize(int size) {
		Size = size;
	}

	public List<Node> getCGM_Records() {
		return CGM_Records;
	}

	public void setCGM_Records(List<Node> cGM_Records) {
		CGM_Records = cGM_Records;
	}

	
	
	
}
