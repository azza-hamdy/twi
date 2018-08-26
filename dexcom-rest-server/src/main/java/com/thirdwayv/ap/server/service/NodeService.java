package com.thirdwayv.ap.server.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.thirdwayv.ap.server.model.Controller;
import com.thirdwayv.ap.server.model.Log;
import com.thirdwayv.ap.server.model.Node;
import com.thirdwayv.ap.server.model.Stream;
public interface NodeService {
	public Page<Node> findDevicesByPage(Pageable pageable,String searchText);
	public Node  findDevice(long deviceId);
//	public CtrDevice  findDeviceByDeviceNumber(int deviceNumber);
//	public boolean saveDevice(Controller ctrDevice);
	public List<Stream> getAllLogs(long deviceId);
	
//	public void clearDeviceBySerialNumber(String serialNumber);
	public boolean createNode(String serialId);
	public boolean removeNode(String serialId);
}
