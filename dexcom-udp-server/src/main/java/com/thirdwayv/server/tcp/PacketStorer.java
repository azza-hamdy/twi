package com.thirdwayv.server.tcp;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.thirdwayv.persistence.entity.LogMessage_CGM;
import com.thirdwayv.persistence.entity.QueryMessage;
import com.thirdwayv.server.messages.log.parser.BackfillParser;
import com.thirdwayv.server.messages.query.parser.QueryMessageParser;
import com.thirdwayv.server.messages.query.response.QueryMessageResponseComposer;
import com.thirdwayv.server.packet.CTwiPacket;
import com.thirdwayv.server.utils.ByteOperationsHandlers;
import com.thirdwayv.server.utils.ByteReader;
import com.thirdwayv.server.utils.LogSaver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

public class PacketStorer {
	private final static Logger logger = LoggerFactory.getLogger(PacketStorer.class);

//	private LinkedBlockingQueue<CTwiPacket> incomingPacketsQueue = null;

	private ChannelHandlerContext ctx;

	static public PacketStorer newInstance(LinkedBlockingQueue<CTwiPacket> incomingPacketsQueue) {

		PacketStorer packetStorer = new PacketStorer();
//		packetStorer.incomingPacketsQueue = incomingPacketsQueue;
		return packetStorer;
	}

	// @Override
	public Void call(CTwiPacket twiPacket) throws Exception {
		try {
			if (twiPacket == null) {
				logger.info("Un-parsable packet format");
			} else {
				boolean isPingMessage = isPingMessage(twiPacket);
				logger.info("isPingMessage : " + isPingMessage);
				if (isPingMessage == true) {
					sendACKMessage(twiPacket, null);
				} else {
					handleMessage(twiPacket);
				}
			}
		} catch (Exception e) {
		}

		return null;
	}

	private CTwiPacket parsePacket(DatagramPacket datagramPacket) {
		byte[] recievedBytes = new byte[datagramPacket.content().readableBytes()];
		datagramPacket.content().readBytes(recievedBytes);
//		logger.info("Packet bytes to parse : " + ByteOperationsHandlers.bytesToHexString(recievedBytes));
		CTwiPacket receivedPacket = new CTwiPacket();
		receivedPacket = CTwiPacket.fromBytes(recievedBytes, false);
		return receivedPacket;

	}

	private boolean isPingMessage(CTwiPacket packet) {
		if (packet.bytesCount == 4)
			if (packet.payload[0] == 0x57 && packet.payload[1] == 0x03 && packet.payload[2] == 0x00
					&& packet.payload[3] == 0x00)
				return true;
		return false;
	}

	private void handleMessage(CTwiPacket receivedPacket) {
		try {
			char messageId = new String(receivedPacket.getPayload(), "UTF-8").charAt(0);
			short messageType = ByteReader.getUnsignedByte8(receivedPacket.getPayload(), 1);
			logger.info("messageId = " + messageId + " messageType = " + messageType);

			if (messageId == 'W' && messageType == 4) {
				handleQueryMessage(receivedPacket);
			} else if (messageId == 'W' && messageType == 5) {
				int controllerId = new BigInteger(receivedPacket.sourceTwiSN).intValue();
				logger.info("controller id = " + controllerId);
				handleBackfillMessage(receivedPacket);
			}

		} catch (Exception e) {
			logger.info("error --> " + e.getMessage());
		}
	}

	private void handleQueryMessage(CTwiPacket receivedPacket) {
		
		QueryMessageParser parser = new QueryMessageParser(receivedPacket.getPayload());
		QueryMessage parsedMessage = parser.GetParsedQueryMessage();
		QueryMessageResponseComposer composer = new QueryMessageResponseComposer(parsedMessage);
		Byte[] response = composer.getQueryResponse();
		byte[] responseBytes = new byte[response.length];

		int j = 0;

		for (Byte b : response)
			responseBytes[j++] = b.byteValue();

		sendACKMessage(receivedPacket, responseBytes);
	}

	private void handleBackfillMessage(CTwiPacket receivedPacket) {
		try {
			boolean isSaved = false;
			LogMessage_CGM logMessage_CGM = null;
			int controllerId = new BigInteger(receivedPacket.sourceTwiSN).intValue();

			logMessage_CGM = parseLogs(controllerId, receivedPacket.getPayload());

			if (logMessage_CGM != null && receivedPacket.eqos == 1) {
				sendACKMessage(receivedPacket, null);
			}

			isSaved = saveLogs(controllerId, logMessage_CGM);
			if (isSaved) {
				logger.debug("message saved successfully");
			} else {
				logger.debug("message not saved");
			}
		} catch (Exception e) {
			logger.info("error --> " + e.getMessage());
		}
	}

	private LogMessage_CGM parseLogs(int gatewayNumber, byte[] logs) throws Exception {
		BackfillParser parser = new BackfillParser(logs);
		LogMessage_CGM logMessage = (LogMessage_CGM) parser.parseLogs();
		return logMessage;
	}

	private boolean saveLogs(int gatewayNumber, LogMessage_CGM logMessage) {
		LogSaver logSaver = new LogSaver();
		return logSaver.save(gatewayNumber, logMessage);
	}

	private void sendACKMessage(CTwiPacket receivedPacket, byte[] payload) {
		CTwiPacket ackResponse = receivedPacket.createACK(receivedPacket, payload);
		byte[] byteAckResponse = CTwiPacket.toBytes(ackResponse);
		
		logger.info("TOTAL Response length is  : "+byteAckResponse.length);
		logger.info("sending response to client in hex: " + ByteOperationsHandlers.bytesToHexString(byteAckResponse));
		ByteBuf buf = Unpooled.copiedBuffer(byteAckResponse);
		ctx.writeAndFlush(buf);
	}

	public ChannelHandlerContext getCtx() {
		return ctx;
	}

	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	public static void main(String[] args) {

		String data = "57050001001ad0eb3830353434590000000100000000000000000000a41670492ac2090014d3590046000078006a0060005a00580059005f006800750084009700ab00c100d800ef01050119012c013d014a0154015a015d015b0156014c01400130011d010900f300dc00c600b0009b00880078006a0060005a00580059005f006800750084009700ab00c100d800ef01050119012c013d014a0154015a015d015b0156014c01400130011d010900f300dc00c600b00014dbab000300009b008800780014dd13002c00006a0060005a00580059005f006800750084009700ab00c100d800ef01050119012c013d014a0154015a015d015b0156014c01400130011d010900f300dc00c600b0009b00880078006a0060005a00580059005f006800750014e2580006000084009700ab00c100d800ef0014e32a00190001050119012c013d014a0154015a015d015b0156014c01400130011d010900f300dc00c600b0009b00880078006a0060005a0014e65400140000580059005f006800750084009700ab00c100d800ef01050119012c013d014a0154015a015d015b0014e8ca000a000156014c01400130011d010900f300dc00c600b00014ea14001800009b00880078006a0060005a00580059005f006800750084009700ab00c100d800ef01050119012c013d014a0154015a0014edf200ba00015d015b0156014c01400130011d010900f300dc00c600b0009b00880078006a0060005a00580059005f006800750084009700ab00c100d800ef01050119012c013d014a0154015a015d015b0156014c01400130011d010900f300dc00c600b0009b00880078006a0060005a00580059005f006800750084009700ab00c100d800ef01050119012c013d014a0154015a015d015b0156014c01400130011d010900f300dc00c600b0009b00880078006a0060005a00580059005f006800750084009700ab00c100d800ef01050119012c013d014a0154015a015d015b0156014c01400130011d010900f300dc00c600b0009b00880078006a0060005a00580059005f006800750084009700ab00c100d800ef01050119012c013d014a0154015a015d015b0156014c01400130011d010900f300dc00c600b0009b00880078006a0060005a00580059005f006800750084009700ab00c100d800ef01050119012c013d014a0154015a015d015b0156014c01400130";

		PacketStorer packetStorer = new PacketStorer();
		try {
			LogMessage_CGM logMessage_CGM = packetStorer.parseLogs(1,ByteOperationsHandlers.hexStringToByteArray(data));
			boolean isSaved = packetStorer.saveLogs(222, logMessage_CGM);
			System.out.println("done" + isSaved);
		} catch (Exception e) {
			System.out.println("Error while handling");
			e.printStackTrace();
		}
	}
	

}
