package com.thirdwayv.persistence.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import com.thirdwayv.persistence.connection.PostgreSQL_DatabaseConnector;
import com.thirdwayv.persistence.dao.LogDao;
import com.thirdwayv.persistence.dao.NodeDao;
import com.thirdwayv.persistence.entity.BackfillStream;
import com.thirdwayv.persistence.entity.LogData_CGM;
import com.thirdwayv.server.utils.NodeState;

public class LogDaoImpl implements LogDao {

	@Override
	public long saveLogData(LogData_CGM logData) {
		long generatedId = 0;

		PostgreSQL_DatabaseConnector connector = new PostgreSQL_DatabaseConnector();
		Connection dbConnection = connector.getDatabaseConnection();

		if (dbConnection != null) {
			try {
				String query = "INSERT INTO public.log (controller_id, node_id, log_time, flag, egv, bg, bg_time, seq_no, stream_count)"
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

				PreparedStatement ps = dbConnection.prepareStatement(query);

				ps.setLong(1, logData.getCtrId());
				ps.setLong(2, logData.getNodeId());
				ps.setLong(3, logData.getLogTime());
				ps.setInt(4, logData.getFlags());
				ps.setFloat(5, logData.getEstimatedGlucoseValue());
				ps.setFloat(6, logData.getBloodGlucose());
				ps.setLong(7, logData.getBloodGlucoseTime());
				ps.setLong(8, logData.getSeqNo());
				ps.setInt(9, logData.getBackfillStreamCount());

//				try(ResultSet rs = ps.executeQuery()) {
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					generatedId = rs.getInt(1);
				}
				rs.close();
//			    }				
				ps.close();
			} catch (Exception e) {
				e.printStackTrace();
				return 0L;
			}
			connector.closeConnection();
		}

		return generatedId;
	}

	public long saveStreamHeader(long nodeId, long logId, BackfillStream stream, boolean expectedDone) {
		long result = 0;

		PostgreSQL_DatabaseConnector connector = new PostgreSQL_DatabaseConnector();
		Connection dbConnection = connector.getDatabaseConnection();

		if (dbConnection != null) {
			try {
				int successfulySaved = 0;
				int affectedRows = 0;
				long streamStartingTime;
				int timeInterval;
				String query = "INSERT INTO public.stream_header(log_id, starting_log_time, egv_count, period)VALUES (?, ?, ?, ?);";
				PreparedStatement ps_streamHeader = dbConnection.prepareStatement(query);
				ps_streamHeader.setLong(1, logId);
				ps_streamHeader.setLong(2, stream.getStartingTimestamp());
				ps_streamHeader.setInt(3, stream.getNumberOfEGV());
				ps_streamHeader.setInt(4, stream.getPeriod());
				affectedRows = ps_streamHeader.executeUpdate();

				ps_streamHeader.close();

				boolean firstBit = (stream.getPeriod() & 1) > 0 ? true : false;
				timeInterval = (firstBit) ? 5 * 60 : 30; // (first bit is true) ? period will be 300 seconds: 30 second
				streamStartingTime = stream.getStartingTimestamp();
				long lastLogTime = 0;
				int i = 0;
//				String egvQuery = "INSERT INTO stream (log_id, log_time, egv) SELECT ?, ?, ? WHERE NOT EXISTS (SELECT log_id, log_time, egv FROM STREAM WHERE log_time = ? AND egv = ?);";
				String egvQuery = "INSERT INTO stream (log_id, log_time, egv) SELECT ?, ?, ? WHERE NOT EXISTS (SELECT log_id, log_time, egv FROM STREAM WHERE log_time = ? AND egv = ? AND log_id in "
						+ "(select id from log where node_id = ?  ) )";
				PreparedStatement ps = dbConnection.prepareStatement(egvQuery);
				for (float egv : stream.getEgvList()) {

					if (expectedDone == true && egv == 1) {
						System.out.println("### Setting Node as done " + nodeId);
						NodeDao nodeDao = new NodeDaoImpl();
						nodeDao.updateNodeState(nodeId, NodeState.DONE);
					}

//					String egvQuery = "INSERT INTO public.stream(log_id, log_time, egv) VALUES (?, ?, ?)";	
					lastLogTime = streamStartingTime + timeInterval * i++;
					ps.setLong(1, logId);
					ps.setLong(2, lastLogTime);
					ps.setFloat(3, egv);
					ps.setLong(4, lastLogTime);
					ps.setFloat(5, egv);
					ps.setLong(6, nodeId);
					affectedRows = ps.executeUpdate();
					successfulySaved += affectedRows;
				}
				ps.close();
				result = successfulySaved;
			} catch (Exception e) {
				e.printStackTrace();
				result = 0;
			}
			connector.closeConnection();
		}

		return result;
	}

	public boolean saveStream(long logId, long timestamp, float egv) {
		boolean result = false;

		PostgreSQL_DatabaseConnector connector = new PostgreSQL_DatabaseConnector();
		Connection dbConnection = connector.getDatabaseConnection();

		if (dbConnection != null) {
			try {
				String query = "INSERT INTO public.stream(log_id, log_time, egv) VALUES (?, ?, ?)";

				PreparedStatement ps = dbConnection.prepareStatement(query);

				ps.setLong(1, logId);
				ps.setLong(2, timestamp);
				ps.setFloat(3, egv);

				int affectedRows = ps.executeUpdate();

				ps.close();

				if (affectedRows > 0)
					result = true;
				else
					result = false;
			} catch (Exception e) {
				e.printStackTrace();
				result = false;
			}
			connector.closeConnection();
		}

		return result;
	}
}
