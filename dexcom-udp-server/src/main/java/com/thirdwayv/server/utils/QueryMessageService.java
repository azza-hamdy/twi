package com.thirdwayv.server.utils;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thirdwayv.persistence.dao.NodeDao;
import com.thirdwayv.persistence.dao.impl.NodeDaoImpl;
import com.thirdwayv.persistence.entity.Node;

public class QueryMessageService {

	private final static Logger logger = LoggerFactory.getLogger(LogSaver.class);

	NodeDao nodeDao;

	public QueryMessageService() {
		
		nodeDao = new NodeDaoImpl();
	}
	
	public Node getNodeBySerial(String NodeSerial)
	{
		return nodeDao.getNodeBySerialId(NodeSerial);
	}
	
	public List<Node> getNodesByPartialSerial(String NodeSerial)
	{
		return nodeDao.getNodesByPartialSerialId(NodeSerial);
	}
	
	
	public int getNodesCount()
	{
		return nodeDao.getNodeSequenceNo();
	}

}
