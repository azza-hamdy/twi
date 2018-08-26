package com.thirdwayv.server.utils;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ahmed Mohsen
 */

public class ByteOperationsHandlers 
{
	/**
     * This method is used to convert hex string values into byte array
     * @param str hex value string
     * @return byte array with hex values
     */
    public static byte[] hexStringToByteArray(String str) {
        int len = str.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4)
                                 + Character.digit(str.charAt(i+1), 16));
        }
        return data;
    }
    
    /**
     * This method is used to convert byte array values into hex string
     * @param input byte array to be converted
     * @return string in hex decimal format
     */
   static public String bytesToHexString(byte[] input) {
        final StringBuilder builder = new StringBuilder();
        for (byte b : input) {
            builder.append(String.format("%02x", b) + ":");
        }
        return builder.toString();
    }
    
   
   
   /**
    * This method is used to convert long value into 8 byte long array
    * @param value long value 
    * @return 8 byte-length byte array
    */
    static public byte[] longToByteArray(long value) {
        return ByteBuffer.allocate(8).putLong(value).array();
    }
    
    static public byte[] longToByteArray(long value,int bytesToAllocate) {
    	
    	int from = Long.BYTES-bytesToAllocate;
    	int to = Long.BYTES;
    	
        return Arrays.copyOfRange(ByteBuffer.allocate(Long.BYTES).putLong(value).array(),from,to);
        
    }
    
    
    static public byte[] intToByteArray(int value, int bytesToAllocate) {
        
    	int from = Integer.BYTES-bytesToAllocate;
    	int to = Integer.BYTES;
    	
    	return Arrays.copyOfRange(ByteBuffer.allocate(Integer.BYTES).putInt(value).array(),from,to);
    	
    }
    
    
    static public List<Byte> addByteRangeToList(List<Byte> listToPopulate,byte [] data)
	{
		for (byte b : data) {
			listToPopulate.add(b);
		}

		return listToPopulate;
	}
    
    
    
    
    
}
