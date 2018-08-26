package com.thirdwayv.persistence.entity;

import java.time.LocalDateTime;

import com.thirdwayv.server.utils.NodeState;

public class Node {
	
	private long id;
	private String serialId;
	private long bleAddress;
	private NodeState state;
	private LocalDateTime dateCreated;
	private LocalDateTime lastActivity;


	public Node(){
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSerialId() {
		return serialId;
	}

	public void setSerialId(String serialId) {
		this.serialId = serialId;
	}
	
	public long getBleAddress() {
		return bleAddress;
	}

	public void setBleAddress(long bleAddress) {
		this.bleAddress = bleAddress;
	}

	public NodeState getState() {
		return state;
	}

	public void setState(NodeState state) {
		this.state = state;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public LocalDateTime getLastActivity() {
		return lastActivity;
	}

	public void setLastActivity(LocalDateTime lastActivity) {
		this.lastActivity = lastActivity;
	}

}
