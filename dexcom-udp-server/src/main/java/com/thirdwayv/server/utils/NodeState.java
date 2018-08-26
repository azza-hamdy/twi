package com.thirdwayv.server.utils;

import java.util.HashMap;
import java.util.Map;

public enum NodeState {
	DONE('0'),
	INPROGRESS('1'),
	REGISTERED('2')	;
	private static final Map<Character, NodeState> lookup = new HashMap<Character, NodeState>();

	private final char stateValue;
	
	static {
	    for (NodeState nodeState : NodeState.values()) {
	        lookup.put(nodeState.getStateValue(), nodeState);
	    }
	}
	private NodeState(char stateValue){
		this.stateValue = stateValue;
	}
	public char getStateValue(){
		return this.stateValue;
	}
	public static NodeState get(char stateValue) {
	    return lookup.get(stateValue);
	}
}
