package com.thirdwayv.ap.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thirdwayv.ap.server.model.Controller;
import com.thirdwayv.ap.server.model.Node;
import com.thirdwayv.ap.server.repository.ControllerRepository;
import com.thirdwayv.ap.server.service.ControllerService;

@Service
public class ControllerServiceImpl implements ControllerService{

	@Autowired
	private ControllerRepository gatewayRepo;
	@Override
	public Controller findGatewayByDeviceNumber(int deviceNumber) {
		return gatewayRepo.findByDeviceNumber(deviceNumber);
	}

	@Override
	public boolean saveGateway(Controller gateway) {
		Controller savedCtrDevice =gatewayRepo.save(gateway);
		return (savedCtrDevice != null)?true:false;
	}

}
