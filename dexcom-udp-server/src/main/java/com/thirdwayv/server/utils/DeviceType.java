package com.thirdwayv.server.utils;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author mostafa.elkhateeb
 * 
 */
public enum DeviceType
{
	NODE((byte)0),
	CONTROLLER((byte)1);
	
	private final byte code;
	private static final Map<Byte, DeviceType> lookup = new HashMap<Byte, DeviceType>();
	
	static
	{
		for (DeviceType deviceType : EnumSet.allOf(DeviceType.class))
			lookup.put(deviceType.getCode(), deviceType);
	}

	DeviceType(byte code) {
        this.code = code;
    }

	private byte getCode()
	{
		return code;
	}

	public static DeviceType valueOf(byte code) {
        return lookup.get(code);
    }	
}
