package com.thirdwayv.server.udp;

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

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

public class PacketStorer implements Callable<Void> {
	private final static Logger logger = LoggerFactory.getLogger(PacketStorer.class);

	private boolean shutdownNow = false;
	private LinkedBlockingQueue<DatagramPacket> incomingPacketsQueue = null;

	public static ChannelHandlerContext ctx;

	static public PacketStorer newInstance(LinkedBlockingQueue<DatagramPacket> incomingPacketsQueue) {
		// Chose to use static factory method to avoid baking queue into
		// constructor args this early
		PacketStorer packetStorer = new PacketStorer();
		packetStorer.incomingPacketsQueue = incomingPacketsQueue;
		return packetStorer;
	}

	@Override
	public Void call() throws Exception {
//		final int MAX_PACKETS_PER_CHUNK = 100;
//		ArrayList<DatagramPacket> packets = new ArrayList<DatagramPacket>(MAX_PACKETS_PER_CHUNK);
//		packets.clear();

		while (!shutdownNow) {
			logger.info("waiting....");

//			DatagramPacket dpInitial = incomingPacketsQueue.poll(1, TimeUnit.SECONDS); // Hold until pack or timeout
//			if (dpInitial != null) {
//				packets.add(dpInitial);
//				int packetsDrained = incomingPacketsQueue.drainTo(packets, MAX_PACKETS_PER_CHUNK - 1);
			DatagramPacket datagramPacket = incomingPacketsQueue.take();
			if(datagramPacket != null)
//				for (DatagramPacket datagramPacket : packets) {
					try {
						CTwiPacket receivedPacket = parsePacket(datagramPacket);
						if (receivedPacket == null) {
							logger.info("Un-parsable packet format");
						} else {
							boolean isPingMessage = isPingMessage(receivedPacket);
							logger.info("isPingMessage : " + isPingMessage);
							if (isPingMessage == true) {
								sendACKMessage(receivedPacket, datagramPacket, null);
							} else {
								handleMessage(receivedPacket, datagramPacket);
							}
						}
					} catch (Exception e) {
					}

//				}
//				for (DatagramPacket packet : packets) {
			datagramPacket.release();
//				}
//				packets.clear(); // Wipe references so GC can recycle
//			}
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

	private void handleMessage(CTwiPacket receivedPacket, DatagramPacket datagramPacket) {
		try {
			char messageId = new String(receivedPacket.getPayload(), "UTF-8").charAt(0);
			short messageType = ByteReader.getUnsignedByte8(receivedPacket.getPayload(), 1);
			logger.info("messageId = " + messageId + " messageType = " + messageType);

			if (messageId == 'W' && messageType == 4) {
				handleQueryMessage(receivedPacket, datagramPacket);
			} else if (messageId == 'W' && messageType == 5) {
				int controllerId = new BigInteger(receivedPacket.sourceTwiSN).intValue();
				logger.info("controller id = " + controllerId);
				handleBackfillMessage(receivedPacket, datagramPacket);
			}

		} catch (Exception e) {
			logger.info("error --> " + e.getMessage());
		}
	}

	private void handleQueryMessage(CTwiPacket receivedPacket, DatagramPacket datagramPacket) {
		
		QueryMessageParser parser = new QueryMessageParser(receivedPacket.getPayload());
		QueryMessage parsedMessage = parser.GetParsedQueryMessage();
		QueryMessageResponseComposer composer = new QueryMessageResponseComposer(parsedMessage);
		Byte[] response = composer.getQueryResponse();
		byte[] responseBytes = new byte[response.length];

		int j = 0;

		for (Byte b : response)
			responseBytes[j++] = b.byteValue();

		sendACKMessage(receivedPacket, datagramPacket, responseBytes);
	}

	private void handleBackfillMessage(CTwiPacket receivedPacket, DatagramPacket datagramPacket) {
		try {
			boolean isSaved = false;
			LogMessage_CGM logMessage_CGM = null;
			int controllerId = new BigInteger(receivedPacket.sourceTwiSN).intValue();

			logMessage_CGM = parseLogs(controllerId, receivedPacket.getPayload());

			if (logMessage_CGM != null && receivedPacket.eqos == 1) {
				sendACKMessage(receivedPacket, datagramPacket, null);
			}

			isSaved = saveLogs(controllerId, logMessage_CGM);
			if (isSaved) {
				logger.debug("message saved successfully");
				logger.info("message saved successfully");
			} else {
				logger.debug("message not saved");
				logger.info("message not saved");
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

	private void sendACKMessage(CTwiPacket receivedPacket, DatagramPacket datagramPacket, byte[] payload) {
		CTwiPacket ackResponse = receivedPacket.createACK(receivedPacket, payload);
		byte[] byteAckResponse = CTwiPacket.toBytes(ackResponse);
		
		logger.info("TOTAL Response length is  : "+byteAckResponse.length);
		logger.info("sending response to client in hex: " + ByteOperationsHandlers.bytesToHexString(byteAckResponse));
		ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(byteAckResponse), datagramPacket.sender()));
	}

	void shutdownGracefully() {
		shutdownNow = true;
	}

	public static void main(String[] args) {

//		String data = "57050001001ad0eb3830353434590000000100000000000000000000a41670492ac2090014d3590046000078006a0060005a00580059005f006800750084009700ab00c100d800ef01050119012c013d014a0154015a015d015b0156014c01400130011d010900f300dc00c600b0009b00880078006a0060005a00580059005f006800750084009700ab00c100d800ef01050119012c013d014a0154015a015d015b0156014c01400130011d010900f300dc00c600b00014dbab000300009b008800780014dd13002c00006a0060005a00580059005f006800750084009700ab00c100d800ef01050119012c013d014a0154015a015d015b0156014c01400130011d010900f300dc00c600b0009b00880078006a0060005a00580059005f006800750014e2580006000084009700ab00c100d800ef0014e32a00190001050119012c013d014a0154015a015d015b0156014c01400130011d010900f300dc00c600b0009b00880078006a0060005a0014e65400140000580059005f006800750084009700ab00c100d800ef01050119012c013d014a0154015a015d015b0014e8ca000a000156014c01400130011d010900f300dc00c600b00014ea14001800009b00880078006a0060005a00580059005f006800750084009700ab00c100d800ef01050119012c013d014a0154015a0014edf200ba00015d015b0156014c01400130011d010900f300dc00c600b0009b00880078006a0060005a00580059005f006800750084009700ab00c100d800ef01050119012c013d014a0154015a015d015b0156014c01400130011d010900f300dc00c600b0009b00880078006a0060005a00580059005f006800750084009700ab00c100d800ef01050119012c013d014a0154015a015d015b0156014c01400130011d010900f300dc00c600b0009b00880078006a0060005a00580059005f006800750084009700ab00c100d800ef01050119012c013d014a0154015a015d015b0156014c01400130011d010900f300dc00c600b0009b00880078006a0060005a00580059005f006800750084009700ab00c100d800ef01050119012c013d014a0154015a015d015b0156014c01400130011d010900f300dc00c600b0009b00880078006a0060005a00580059005f006800750084009700ab00c100d800ef01050119012c013d014a0154015a015d015b0156014c01400130";
		String data = "57050001000e574f3830344a5150000000010000000000000000000097cef4ba70d1010001e2d1019b01003c004100470050005a00660072007e008b009700a300ae00b700bf00c400c800ca00c900c600c100ba00b200a7009c009000830077006a005f0054004b0043003d003a00380039003c004000470050005a00650071007e008b009700a300ad00b600be00c400c700c900c800c500c000ba00b100a7009c008f00830076006a005e0054004b0043003d003a00380039003b004000470050005a00650071007e008a009600a200ad00b600bd00c300c700c800c800c500c000b900b000a6009b008f00830076006a005e0054004a0043003d003a00380039003b004000470050005a00650071007d008a009600a200ac00b500bd00c200c600c800c700c400bf00b800b000a6009b008f00820076006a005e0054004a0043003d003900380039003b004000470050005a00650071007d0089009600a100ac00b500bc00c200c600c700c600c400bf00b800af00a5009a008e008200760069005e0053004a0043003d003900380039003b00400047004f005900640070007d0089009500a100ab00b400bc00c100c500c700c600c300be00b700af00a5009a008e008200750069005e0053004a0043003d003900380038003b00400047004f005900640070007c0089009500a000ab00b400bb00c100c500c600c500c300be00b700af00a5009a008e008100750069005e0053004a0043003d003900380038003b00400047004f005900640070007c0089009500a000aa00b300bb00c000c400c600c500c200bd00b700ae00a40099008d008100750069005d0053004a0043003d003900380038003b00400047004f005900640070007c0088009400a000aa00b300ba00c000c400c500c400c200bd00b600ae00a40099008d008100750069005d0053004a0042003d003900380038003b00400047004f005900640070007c00880094009f00aa00b300ba00c000c300c500c400c100bc00b600ad00a40099008d008100740068005d0053004a0042003d003900380038003b00400047004f00590064006f007c00880094009f00a900b200ba00bf00c300c400c400c100bc00b500ad00a30098008d008000740068005d0053004a0042003d003900380038003b00400046004f00590063006f007b00880093009f00a900b200b900bf";
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
