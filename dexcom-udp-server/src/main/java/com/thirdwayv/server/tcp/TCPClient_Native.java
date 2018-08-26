package com.thirdwayv.server.tcp;


import java.io.*;
import java.net.*;

import com.thirdwayv.server.utils.ByteOperationsHandlers;

public class TCPClient_Native {
	public static void main(String[] args)  {
		
		try {
			int serverPort = 8322;
			String ip = "127.0.0.1";

			//open new socket connection
			Socket socket = new Socket(ip, serverPort);
			DataInputStream input = new DataInputStream(socket.getInputStream());
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());

			String message = "545711303c000080333333660000000057030000";
			System.out.println("Writing.......");
			byte[] msg = ByteOperationsHandlers.hexStringToByteArray(message);
			output.write(msg);
			output.flush();
	
			
			
			byte[] recievingData = new byte[100]; // the well known size
			input.read(recievingData);
			
//			int messageLength = input.readInt();
			System.out.println(ByteOperationsHandlers.bytesToHexString(recievingData));
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
