package com.thirdwayv.persistence.dao;

import com.thirdwayv.persistence.entity.BackfillStream;
import com.thirdwayv.persistence.entity.LogData_CGM;

public interface LogDao {
	
//	public boolean saveLogMessage(long ctrId,LogMessage_CGM logMessage);
	public long saveLogData(LogData_CGM logData);
	public long saveStreamHeader(long nodeId, long logId, BackfillStream backfillStream,boolean expectedDone);
	public boolean saveStream(long logId, long timestamp, float egv);
	
}
