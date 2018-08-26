package com.thirdwayv.ap.server.dto;

public class DeviceRange {
	
	private String startRange;
	private int size;
	private String listOfDevices;
	
	public DeviceRange() {
		super();
	}
	public String getStartRange() {
		return startRange;
	}
	public void setStartRange(String startRange) {
		this.startRange = startRange;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getListOfDevices() {
		return listOfDevices;
	}
	public void setListOfDevices(String listOfDevices) {
		this.listOfDevices = listOfDevices;
	}
	@Override
	public String toString() {
		
		return new StringBuilder().append("Start Range: ").append(startRange).append("\tSize:").append(size)
				.append("\tList of Devices:").append(listOfDevices).toString();
	}
	
}
