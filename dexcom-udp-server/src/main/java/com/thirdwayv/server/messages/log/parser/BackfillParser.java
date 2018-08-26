package com.thirdwayv.server.messages.log.parser;

import java.util.ArrayList;
import java.util.List;

import com.thirdwayv.persistence.entity.BackfillStream;
import com.thirdwayv.persistence.entity.LogData_CGM;
import com.thirdwayv.persistence.entity.LogMessage_CGM;
import com.thirdwayv.server.utils.ByteReader;

public class BackfillParser{
	
	private LogMessage_CGM  CGM_logMessage = new LogMessage_CGM();
	
//	final int LOG_LENGTH=31;
	
	final int OFFSET_SIGNATURE=0;
	final int OFFSET_LOG_COUNT=2;
	
	final int OFFSET_LOG_TIME=0;
	final int OFFSET_CGM_TX_ID=4;
	final int OFFSET_FLAG=10;
	final int OFFSET_EGV=12;
	final int OFFSET_BG=14;
	final int OFFSET_BG_TIME=16;
	final int OFFSET_SEQ_NO=20;
	final int OFFSET_BLE_ADDRESS=24;
	final int OFFSET_BACKFILL_STREAM	 = 31;
	
	private int logIndex=4; // to skip the signature(G5 2 bytes) and log count(2 bytes)
	
	private byte[] payload;

	public BackfillParser(byte[] payload) {
		this.payload=payload;
	}

	
	public LogMessage_CGM parseLogs() {
		LogData_CGM cgmLog;
		BackfillStream backfillStream;
		byte[] dataForOneLog = new byte[OFFSET_BACKFILL_STREAM];
		byte[] dataForOneStream = new byte[STREAM_LENGTH];
		List<LogData_CGM> cgmLogs = new ArrayList<LogData_CGM>();
		int logCount = ByteReader.getUnsignedShort(payload, OFFSET_LOG_COUNT, false);
		
		for (int i = 0; i < logCount; i++) {
			System.arraycopy(payload, logIndex, dataForOneLog, 0, OFFSET_BACKFILL_STREAM);
			cgmLog = parselogData(dataForOneLog);
			cgmLog.setStreams(new ArrayList<BackfillStream>());
			logIndex+= (OFFSET_BACKFILL_STREAM) ;
			int streamIndex = 0;
			for (int j = 0; j < cgmLog.getBackfillStreamCount(); j++) {
				
				System.arraycopy(payload, logIndex + streamIndex , dataForOneStream, 0, STREAM_LENGTH);
				streamIndex += STREAM_LENGTH;
				backfillStream = parseLogBackfillStream(dataForOneStream);
				List<Float> egvList = null;
				byte[] egvDataInStream = getALLEGVBytesInTheStream(backfillStream.getNumberOfEGV() , streamIndex);
			    egvList = parseStreamEGV(egvDataInStream);
				backfillStream.setEgvList(egvList);
				streamIndex += egvDataInStream.length;				
				cgmLog.getStreams().add(backfillStream);
			}
			logIndex += streamIndex;
			cgmLogs.add(cgmLog);	
		}
		
		CGM_logMessage.setCgmLogs(cgmLogs);
		return CGM_logMessage;
	}
	
	private LogData_CGM parselogData(byte[] logPayLoad) {	
		LogData_CGM cgmLogHeader = new LogData_CGM();

		try{
			cgmLogHeader.setLogTime(ByteReader.getUnsignedInteger32(logPayLoad, OFFSET_LOG_TIME, false));
			byte[] CgmTxId = new byte[6];
			System.arraycopy(logPayLoad, OFFSET_CGM_TX_ID, CgmTxId, 0, 6);
			
			cgmLogHeader.setCgmTxId(new String(CgmTxId, "UTF-8"));
			cgmLogHeader.setFlags(ByteReader.getUnsignedShort(logPayLoad, OFFSET_FLAG, false));
			cgmLogHeader.setEstimatedGlucoseValue(ByteReader.getShort(logPayLoad, OFFSET_EGV, false));
			cgmLogHeader.setBloodGlucose(ByteReader.getShort(logPayLoad, OFFSET_BG, false));
			cgmLogHeader.setBloodGlucoseTime(ByteReader.getUnsignedInteger32(logPayLoad, OFFSET_BG_TIME, false));
			cgmLogHeader.setSeqNo(ByteReader.getUnsignedInteger32(logPayLoad, OFFSET_SEQ_NO, false));
			cgmLogHeader.setBackfillStreamCount(ByteReader.getUnsignedByte8(logPayLoad, OFFSET_BACKFILL_STREAM-1)); //last byte in the log header
			
		}catch (Exception e) {
			throw new IllegalArgumentException("Exception in parsing cgm header : "+e.getMessage());
		}
		return cgmLogHeader;
	}

	final int STREAM_LENGTH=7;
	final int OFFSET_BACKFILL_STREAM_LOG_TIME=0;
	final int OFFSET_BACKFILL_STRAM_EGV_NO=4;
	final int OFFSET_BACKFILL_STRAM_PERIOD=6;
	
	private BackfillStream parseLogBackfillStream(byte[] backfillStreamHeader) {
		BackfillStream backfillStream = new BackfillStream();
		try {
			backfillStream.setStartingTimestamp(
					ByteReader.getUnsignedInteger32(backfillStreamHeader, OFFSET_BACKFILL_STREAM_LOG_TIME, false));
			backfillStream.setNumberOfEGV(
					ByteReader.getUnsignedShort(backfillStreamHeader, OFFSET_BACKFILL_STRAM_EGV_NO, false));
			backfillStream.setPeriod(ByteReader.getUnsignedByte8(backfillStreamHeader, OFFSET_BACKFILL_STRAM_PERIOD));
		} catch (Exception e) {
			throw new IllegalArgumentException("Exception in parsing cgm header : " + e.getMessage());
		}
		return backfillStream;
	}
	
	private byte[] getALLEGVBytesInTheStream(int egvCount, int streamIndex){
		int totalLengthOfEGVinStream = egvCount * (OFFSET_BACKFILL_STRAM_PERIOD - OFFSET_BACKFILL_STRAM_EGV_NO);
		byte[] totalEGVDataInStream = new byte[totalLengthOfEGVinStream];
		System.arraycopy(payload, logIndex + streamIndex , totalEGVDataInStream, 0, totalLengthOfEGVinStream);
		return totalEGVDataInStream;
	}
	
	private List<Float> parseStreamEGV(byte[] egvData) {
		int egvSize  = 2;
		List<Float> egvList = new ArrayList<Float>();
		float egvValue;
		for (int i = 0; i < egvData.length; i+=egvSize) {
			egvValue = ByteReader.getShort(egvData, i, false);
			egvList.add(egvValue);
		}
		return egvList;
	}
}