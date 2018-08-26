package com.thirdwayv.server.utils;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thirdwayv.persistence.dao.ControllerDao;
import com.thirdwayv.persistence.dao.LogDao;
import com.thirdwayv.persistence.dao.NodeDao;
import com.thirdwayv.persistence.dao.impl.ControllerDaoImpl;
import com.thirdwayv.persistence.dao.impl.LogDaoImpl;
import com.thirdwayv.persistence.dao.impl.NodeDaoImpl;
import com.thirdwayv.persistence.entity.BackfillStream;
import com.thirdwayv.persistence.entity.Controller;
import com.thirdwayv.persistence.entity.LogData_CGM;
import com.thirdwayv.persistence.entity.LogMessage_CGM;
import com.thirdwayv.persistence.entity.Node;

public class LogSaver {
	private final static Logger logger = LoggerFactory.getLogger(LogSaver.class);
	
	public boolean save(int gatewayNumber, LogMessage_CGM logMessage) {
		ControllerDao controllerDao = new ControllerDaoImpl();
		NodeDao nodeDao = new NodeDaoImpl();
		LogDao logMessageDao = new LogDaoImpl();
		
		Controller controller = controllerDao.getControllerByDeviceNumber(gatewayNumber);
		if(controller == null){
			throw new NoSuchElementException("controller not found");
		}
		for (LogData_CGM logData : logMessage.getCgmLogs()) {
			String serialId = logData.getCgmTxId();
			Node checkedDevice = nodeDao.getNodeBySerialId(serialId);
			if (checkedDevice == null) {
				boolean saved = nodeDao.saveNode(serialId);
				if (saved) {
					checkedDevice = nodeDao.getNodeBySerialId(serialId);
				} else {
					throw new RuntimeException("can't save the device");
				}

			}
			if (checkedDevice != null) {
				if(checkedDevice.getState().equals(NodeState.REGISTERED)){
					nodeDao.updateNodeState(checkedDevice.getId(), NodeState.INPROGRESS);
					checkedDevice.setState(NodeState.INPROGRESS);
					int estimatedEgv = estimateEGVNo(logData);
					if(estimatedEgv != -1){
						nodeDao.updateNodeEstimatedEGV(checkedDevice.getId(), estimatedEgv);
					}
				} 
				if (checkedDevice.getState().equals(NodeState.INPROGRESS)) {
					logData.setCtrId(controller.getId());
					logData.setNodeId(checkedDevice.getId());
					
					boolean expectedDone = false;
					
					if(logData.getEstimatedGlucoseValue() == 1)
					{
						//set cgm as done
						System.out.println("### Expected done "+checkedDevice.getSerialId());
						expectedDone = true;
						
//						nodeDao.updateNodeState(checkedDevice.getId(), NodeState.DONE);
					}
					
					long logId = logMessageDao.saveLogData(logData);
					logMessageDao.saveStream(logId, logData.getLogTime(), logData.getEstimatedGlucoseValue());
					
					for (BackfillStream stream : logData.getStreams()) {
						
						long savedEgvs = logMessageDao.saveStreamHeader(checkedDevice.getId(),logId, stream, expectedDone);
						logger.info("number of saved EGVs = " +savedEgvs);
						boolean secondBit = (stream.getPeriod()  & 2) > 0 ? true : false;
						
						if(secondBit){
						//	nodeDao.updateNodeState(checkedDevice.getId(), NodeState.DONE);
						}
						nodeDao.updateNodeProgress(checkedDevice.getId(),savedEgvs);
						
					}
						
					
					logger.info("Dexcom Log Message saved successfuly");
					nodeDao.updateNodeLastActivity(checkedDevice.getId());
				}
			}
		}
		controllerDao.updateControllerLastActivity(controller.getId());
		return true;
	}
	private int estimateEGVNo(LogData_CGM logData){
		if(logData != null &&  logData.getStreams()!= null && logData.getStreams().get(0)!= null){
			long currentLogTime = logData.getLogTime();
			long firstEgvTime = logData.getStreams().get(0).getStartingTimestamp();
			boolean firstBit = (logData.getStreams().get(0).getPeriod() & 1) > 0 ? true : false;
			int timeInterval = (firstBit) ? 5 * 60 : 30;
			int estimatedEGVNo = (int) (currentLogTime - firstEgvTime) / timeInterval;
			return estimatedEGVNo;
		}
		return -1;
		
	}
	
}
