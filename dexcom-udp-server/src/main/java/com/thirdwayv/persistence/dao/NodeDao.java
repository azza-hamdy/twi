package com.thirdwayv.persistence.dao;

import java.util.List;

import com.thirdwayv.persistence.entity.Node;
import com.thirdwayv.server.utils.NodeState;

public interface NodeDao {
	public Node getNodeBySerialId(String serialId);
	public List<Node> getNodesByPartialSerialId(String serialId);
	public boolean saveNode(String serialId);
	public boolean updateNodeState(long nodeId, NodeState nodeState);
	public boolean updateNodeProgress(long nodeId, float progress);
	public boolean updateNodeEstimatedEGV(long nodeId, int egvNo);
	public boolean updateNodeLastActivity(long nodeId);
	public boolean updateNodeBLEAddress(long nodeId,long BleAddress);
	public long getLastAddedStreamHeaderTime(long nodeId);
	public int getNodeSequenceNo();
}
