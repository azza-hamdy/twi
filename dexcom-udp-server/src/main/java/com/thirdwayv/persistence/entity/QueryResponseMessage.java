package com.thirdwayv.persistence.entity;

import java.util.List;


public class QueryResponseMessage {
	
	private char GatewayID;
	private short MessageType;
	private int Flags;
	private int payloadSize;
	private int MessageSequence;
	private List <QueryResponseRecord>QueryResponseRecords;
	
	
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
	public int getPayloadSize() {
		return payloadSize;
	}
	public void setPayloadSize(int size) {
		payloadSize = size;
	}
	public int getMessageSequence() {
		return MessageSequence;
	}
	public void setMessageSequence(int messageSequence) {
		MessageSequence = messageSequence;
	}
	public List<QueryResponseRecord> getQueryResponseRecords() {
		return QueryResponseRecords;
	}
	public void setQueryResponseRecords(List<QueryResponseRecord> queryResponseRecords) {
		QueryResponseRecords = queryResponseRecords;
	}

	
	
	
	
	
	
	
	
	
	public class QueryResponseRecord{
		
		private String cgmID;
		private long BLEAddress;
		private short countOfPossibleIDs;
		private List <String>possibleCGMIDs;
		private long specificRecordTxTime = -1;		
		
		
		public long getSpecificRecordTxTime() {
			return specificRecordTxTime;
		}
		public void setSpecificRecordTxTime(long specificRecordTxTime) {
			this.specificRecordTxTime = specificRecordTxTime;
		}
		
		public String getCgmID() {
			return cgmID;
		}
		public void setCgmID(String cgmID) {
			this.cgmID = cgmID;
		}
		public long getBLEAddress() {
			return BLEAddress;
		}
		public void setBLEAddress(long bLEAddress) {
			BLEAddress = bLEAddress;
		}
		
		public short getCountOfPossibleIDs() {
			
			if(possibleCGMIDs!=null)
				return (short) possibleCGMIDs.size();
			else
				return 0;
		}
		
		public List<String> getPossibleCGMIDs() {
			return possibleCGMIDs;
		}
		
		public void setPossibleCGMIDs(List<String> possibleCGMIDs) {
			this.possibleCGMIDs = possibleCGMIDs;
		}
		
		
		
		
	}

	
}


