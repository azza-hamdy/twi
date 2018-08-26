package com.thirdwayv.server.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteReader {
//	
//	public static byte[] getBytes(byte[] source, int start, int end){
//		new 
//	}

	public static short getUnsignedByte8(byte[] payload,int offset){
		return new BigInteger(new byte[]{0,0,0,0,0,0,0,payload[offset]}).shortValue();
    }
	
	public static  short getShort(byte[] payload,int offset, boolean reverse)
    {
		if (reverse)
	        return new BigInteger(new byte[]{0,0,0,0,0,0, payload[offset + 1], payload[offset]}).shortValue();
        return new BigInteger(new byte[]{0,0,0,0,0,0, payload[offset], payload[offset + 1]}).shortValue();
    }
    
	public static  int getUnsignedShort(byte[] payload,int offset, boolean reverse)
    {
		if (reverse)
			return new BigInteger(new byte[]{0,0,0,0,0,0, payload[offset + 1], payload[offset]}).intValue();
        return new BigInteger(new byte[]{0,0,0,0,0,0, payload[offset], payload[offset + 1]}).intValue();
    }
    
	public static long getLong48 (byte[] payload,int offset, boolean reverse)
	{
		if (reverse)
			return new BigInteger(new byte[]{0,0,payload[offset + 5],payload[offset + 4],payload[offset + 3], payload[offset + 2], payload[offset + 1], payload[offset]}).longValue();
		
        return new BigInteger(new byte[]{0,0,payload[offset],payload[offset+1],payload[offset+2], payload[offset + 3], payload[offset + 4], payload[offset + 5]}).longValue();
	}
	
	public static  int getInteger32(byte[] payload,int offset, boolean reverse)
    {		
		if (reverse)
			return new BigInteger(new byte[]{0,0,0,0,payload[offset + 3], payload[offset + 2], payload[offset + 1], payload[offset]}).intValue();
        return new BigInteger(new byte[]{0,0,0,0,payload[offset], payload[offset + 1], payload[offset + 2], payload[offset + 3]}).intValue();
    }

	public static  long getUnsignedInteger32(byte[] payload,int offset, boolean reverse)
    {		
		if (reverse)
			return new BigInteger(new byte[]{0,0,0,0,payload[offset + 3], payload[offset + 2], payload[offset + 1], payload[offset]}).longValue();
        return new BigInteger(new byte[]{0,0,0,0,payload[offset], payload[offset + 1], payload[offset + 2], payload[offset + 3]}).longValue();
    }


	public static  short GetShort(byte[] payload,int offset, boolean reverse)
    {
		if (reverse)
			return new BigInteger(new byte[]{0,0,0,0,0,0, payload[offset + 1], payload[offset]}).shortValue();
        return new BigInteger(new byte[]{0,0,0,0,0,0, payload[offset], payload[offset + 1]}).shortValue();
    }
	
	public static  float getFloat32(byte[] payload,int offset, boolean reverse)
    {		
		if (reverse)
			return ByteBuffer.wrap(new byte[]{payload[offset], payload[offset + 1], payload[offset + 2], payload[offset+3]}).order(ByteOrder.LITTLE_ENDIAN).getFloat();
		return ByteBuffer.wrap(new byte[]{payload[offset], payload[offset + 1], payload[offset + 2], payload[offset+3]}).order(ByteOrder.BIG_ENDIAN).getFloat();

    }
	
	public static String getString (byte [] payload, int offset, int count)
	{
		try {
			return new String(payload,"UTF-8").substring(offset, offset+count);
		} 
		catch (Exception e) {
			System.out.println("Coversion to string exception "+e.toString());
			return "";
		}
	}
	
	public static String getString (byte [] payload)
	{
		try {
			return new String(payload,"UTF-8");
		} 
		catch (Exception e) {
			System.out.println("Coversion to string exception "+e.toString());
			return "";
		}
	}
	

}
