package com.thirdwayv.ap.server.service;

import com.thirdwayv.ap.server.model.Controller;
import com.thirdwayv.ap.server.model.Node;

public interface ControllerService {
	public Controller  findGatewayByDeviceNumber(int deviceNumber);
	public boolean saveGateway(Controller gateway);
}
