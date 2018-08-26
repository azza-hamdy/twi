package com.thirdwayv.ap.server.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.thirdwayv.ap.server.model.Controller;
import com.thirdwayv.ap.server.model.Log;
import com.thirdwayv.ap.server.model.Node;
import com.thirdwayv.ap.server.model.Stream;
import com.thirdwayv.ap.server.repository.NodeRepository;
import com.thirdwayv.ap.server.repository.LogRepository;
import com.thirdwayv.ap.server.repository.StreamRepository;
import com.thirdwayv.ap.server.service.NodeService;

@Service("ctrDeviceServiceImpl")
@Transactional
public class NodeServiceImpl implements NodeService {

	@Autowired
	NodeRepository nodeRepo;
	@Autowired 
	StreamRepository streamRepo;
	
	@Override
	public Page<Node> findDevicesByPage(Pageable pageable,String searchText) {
		return nodeRepo.findNodesSortedByLastActivity(pageable, searchText);
	}

	@Override
	public Node findDevice(long deviceId) {
		return nodeRepo.getOne(deviceId);
	}
	
	public boolean saveDevice(Node ctrDevice){
		Node savedCtrDevice =nodeRepo.save(ctrDevice);
		return (savedCtrDevice != null)?true:false;
	}

	@Override
	public List<Stream> getAllLogs(long deviceId) {

		List<Stream> res = streamRepo.findAllByNodeId(deviceId);
		return res;
	}
	
	
//	public void clearDeviceBySerialNumber(String serialId){
//		
//		Node device = nodeRepo.findBySerialId(serialId);
//		streamRepo.deleteAllLogsByDeviceId(device.getId());
//		logMsgRepo.deleteAllMessagesByDeviceId(device.getId());
//		nodeRepo.setCtrDeviceBySerialId(serialNumber);
//
//	}

	@Override
	public boolean createNode(String serialId) {
		Node device = createNewDevice(serialId);
		if(isExist(serialId))
			return true;
		try{
			Node savedDevice = nodeRepo.saveAndFlush(device);
			if(savedDevice == null)
				return false;
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean removeNode(String serialId) {
		try{
			nodeRepo.deleteBySerialId(serialId);
			return true;
		}catch (Exception e) {
			return false;
		}
	}
	private Node createNewDevice(String cgmTxId){
		Node device = new Node();
		device.setSerialId(cgmTxId);
		device.setLastActivity(null);
		device.setState("2");
		device.setDateCreated(LocalDateTime.now());
		return device;
	}
	private boolean isExist(String serialId){
		Node savedDevice = nodeRepo.findBySerialId(serialId);
		if (savedDevice == null)
			return false;
		savedDevice.setDeleted(false);
		savedDevice.setState("2");
		nodeRepo.saveAndFlush(savedDevice);
		return  true;
	}

	
	
}
