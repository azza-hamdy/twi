package com.thirdwayv.ap.server.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thirdwayv.ap.server.model.Log;
import com.thirdwayv.ap.server.repository.LogRepository;
import com.thirdwayv.ap.server.service.LogService;

@Service
public class LogServiceImpl implements LogService {

	@Autowired
	LogRepository logRepo;
	
	@Override
	public List<Log> getLogsByNodeId(long nodeId) {
		return logRepo.findAllByDeviceId(nodeId);
	}

}
