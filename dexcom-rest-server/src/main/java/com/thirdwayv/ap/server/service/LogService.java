package com.thirdwayv.ap.server.service;

import java.util.List;

import com.thirdwayv.ap.server.model.Log;;

public interface LogService {
	
	public List<Log> getLogsByNodeId(long nodeId);

}
