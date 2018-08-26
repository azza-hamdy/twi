package com.thirdwayv.persistence.dao;

import com.thirdwayv.persistence.entity.Controller;

public interface ControllerDao {
	
	public Controller getControllerByDeviceNumber(int deviceNumber);
	public boolean updateControllerLastActivity(long controllerId);

}
