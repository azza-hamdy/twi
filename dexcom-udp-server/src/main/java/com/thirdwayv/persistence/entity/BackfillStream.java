package com.thirdwayv.persistence.entity;

import java.util.List;

public class BackfillStream {
	private long startingTimestamp;
	private int numberOfEGV;
	private short period;
	private List<Float> egvList;
	public long getStartingTimestamp() {
		return startingTimestamp;
	}
	public void setStartingTimestamp(long startingTimestamp) {
		this.startingTimestamp = startingTimestamp;
	}
	public int getNumberOfEGV() {
		return numberOfEGV;
	}
	public void setNumberOfEGV(int numberOfEGV) {
		this.numberOfEGV = numberOfEGV;
	}
	public short getPeriod() {
		return period;
	}
	public void setPeriod(short period) {
		this.period = period;
	}
	
	public List<Float> getEgvList() {
		return egvList;
	}
	public void setEgvList(List<Float> egvList) {
		this.egvList = egvList;
	}
	@Override
	public String toString() {
		return new StringBuilder().append("startingTimestamp:").append(startingTimestamp).append("\t")
				.append("numberOfEGV:").append(numberOfEGV).append("\t")
				.append("period:").append(period).toString();

	}
	
	
}
