package com.thirdwayv.ap.server.dto;

import java.util.List;

import com.thirdwayv.ap.server.model.Log;
import com.thirdwayv.ap.server.model.Stream;

public class NodeLogs {
	long deviceId;
	List<Log> deviceBGLogs;
	List<Stream> deviceEGVLogs;
	public NodeLogs() {
		super();
	}
	
	public long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(long deviceId) {
		this.deviceId = deviceId;
	}

	public List<Log> getDeviceBGLogs() {
		return deviceBGLogs;
	}
	public void setDeviceBGLogs(List<Log> deviceBGLogs) {
		this.deviceBGLogs = deviceBGLogs;
	}
	public List<Stream> getDeviceEGVLogs() {
		return deviceEGVLogs;
	}
	public void setDeviceEGVLogs(List<Stream> deviceEGVLogs) {
		this.deviceEGVLogs = deviceEGVLogs;
	}
	
}
