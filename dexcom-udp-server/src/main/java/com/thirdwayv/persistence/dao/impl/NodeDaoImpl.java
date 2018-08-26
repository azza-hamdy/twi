package com.thirdwayv.persistence.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thirdwayv.persistence.connection.PostgreSQL_DatabaseConnector;
import com.thirdwayv.persistence.dao.NodeDao;
import com.thirdwayv.persistence.entity.Node;
import com.thirdwayv.server.utils.NodeState;

public class NodeDaoImpl implements NodeDao {
	final static Logger logger = LoggerFactory.getLogger(NodeDaoImpl.class);

	@Override
	public Node getNodeBySerialId(String serialId) {

		Node device = null;

		PostgreSQL_DatabaseConnector connector = new PostgreSQL_DatabaseConnector();
		Connection dbConnection = connector.getDatabaseConnection();

		if (dbConnection != null) {
			try {
				PreparedStatement preparedStatement = dbConnection
						.prepareStatement("SELECT * FROM node WHERE serial_id = ? ");
				preparedStatement.setString(1, serialId);

				ResultSet resultSet = preparedStatement.executeQuery();

				if (resultSet.next()) {
					device = new Node();
					device.setId(resultSet.getLong("id"));
					device.setSerialId(resultSet.getString("serial_id"));
					NodeState state = NodeState.get(resultSet.getString("state").charAt(0));
					device.setState(state);
					device.setBleAddress(resultSet.getLong("ble_address"));
				} else {
					logger.debug("No pdm devices found for passed parameters");
				}

				resultSet.close();
				preparedStatement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			connector.closeConnection();
		}

		return device;
	}

	@Override
	public List<Node> getNodesByPartialSerialId(String serialId) {

		PostgreSQL_DatabaseConnector connector = new PostgreSQL_DatabaseConnector();
		Connection dbConnection = connector.getDatabaseConnection();
		List<Node> nodes = new ArrayList<Node>();
		
		
		if (dbConnection != null) {
			
			try {
				
				PreparedStatement preparedStatement = dbConnection
						.prepareStatement("SELECT * FROM node WHERE state <> '0' and serial_id  like ? LIMIT 253");
				preparedStatement.setString(1, "%"+serialId);

				ResultSet resultSet = preparedStatement.executeQuery();

				
				while (resultSet.next()) {

					Node device = new Node();

					device.setId(resultSet.getLong("id"));
					device.setSerialId(resultSet.getString("serial_id"));
					NodeState state = NodeState.get(resultSet.getString("state").charAt(0));
					device.setState(state);
					device.setBleAddress(resultSet.getLong("ble_address"));
					nodes.add(device);
					
				}
				
				
				if(nodes.size()==0)
				{
					logger.debug("No Nodes found for passed parameters "+serialId);
				}


				resultSet.close();
				preparedStatement.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}

			connector.closeConnection();
		}

		return nodes;

	}

	@Override
	public boolean saveNode(String serialId) {
		boolean result = false;

		PostgreSQL_DatabaseConnector connector = new PostgreSQL_DatabaseConnector();
		Connection dbConnection = connector.getDatabaseConnection();

		if (dbConnection != null) {
			try {
				String query = "INSERT INTO node (serial_id) VALUES (?) ";

				PreparedStatement ps = dbConnection.prepareStatement(query);

				ps.setString(1, serialId);

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

	@Override
	public boolean updateNodeState(long nodeId, NodeState nodeState) {

		PostgreSQL_DatabaseConnector connector = new PostgreSQL_DatabaseConnector();
		Connection dbConnection = connector.getDatabaseConnection();

		boolean result = false;

		if (dbConnection != null) {
			try {
				String query = " UPDATE node  SET state = ? WHERE id = ? ";
				PreparedStatement ps = dbConnection.prepareStatement(query);
				ps.setString(1, String.valueOf(nodeState.getStateValue()));
				ps.setLong(2, nodeId);
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
	@Override
	public boolean updateNodeProgress(long nodeId, float progress) {

		PostgreSQL_DatabaseConnector connector = new PostgreSQL_DatabaseConnector();
		Connection dbConnection = connector.getDatabaseConnection();

		boolean result = false;

		if (dbConnection != null) {
			try {
//				String query = " UPDATE node  SET progress = ? WHERE id = ? ";
				String query = " update node set progress = (select progress from node where id = ? ) + ? where id = ?;";
				PreparedStatement ps = dbConnection.prepareStatement(query);
				ps.setLong(1, nodeId);
				ps.setFloat(2, progress);
				ps.setLong(3, nodeId);
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
	public boolean updateNodeLastActivity(long nodeId){
		PostgreSQL_DatabaseConnector connector = new PostgreSQL_DatabaseConnector();
		Connection dbConnection = connector.getDatabaseConnection();
		
		boolean result = false;
		
		if(dbConnection!=null)
		{
			try 
			{
				String query = " UPDATE node  SET last_activity = ? WHERE id = ? ";

				PreparedStatement ps = dbConnection.prepareStatement(query); 
				
		        Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			         
				ps.setTimestamp(1,new Timestamp(System.currentTimeMillis()),utc );
				ps.setLong(2, nodeId); //id
				
				int affectedRows = ps.executeUpdate();
				
				ps.close();
				
				if(affectedRows>0)
					result= true;
				else
					result= false;
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				result= false;
			}
			
			connector.closeConnection();
		}
		return result;
	}

	@Override
	public int getNodeSequenceNo() {

		PostgreSQL_DatabaseConnector connector = new PostgreSQL_DatabaseConnector();
		Connection dbConnection = connector.getDatabaseConnection();
		List<Node> nodes = new ArrayList<Node>();
		int count =0;
		
		if (dbConnection != null) {
			
			try {
				
				PreparedStatement preparedStatement = dbConnection
						.prepareStatement("SELECT seq_no FROM node_sequence;");

				ResultSet resultSet = preparedStatement.executeQuery();

				resultSet.next();
				 count = resultSet.getInt(1);

				resultSet.close();
				preparedStatement.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}

			connector.closeConnection();
		}

		return count;
	}

	@Override
	public boolean updateNodeEstimatedEGV(long nodeId, int egvNo) {
		PostgreSQL_DatabaseConnector connector = new PostgreSQL_DatabaseConnector();
		Connection dbConnection = connector.getDatabaseConnection();

		boolean result = false;

		if (dbConnection != null) {
			try {
				String query = " UPDATE node  SET estimated_egv = ? WHERE id = ? ";
				PreparedStatement ps = dbConnection.prepareStatement(query);
				ps.setInt(1, egvNo);
				ps.setLong(2, nodeId);
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

	@Override
	public boolean updateNodeBLEAddress(long nodeId, long BleAddress) {
		
		PostgreSQL_DatabaseConnector connector = new PostgreSQL_DatabaseConnector();
		Connection dbConnection = connector.getDatabaseConnection();

		boolean result = false;

		if (dbConnection != null) {
			try {
				String query = " UPDATE node  SET ble_address = ? WHERE id = ? ";
				PreparedStatement ps = dbConnection.prepareStatement(query);
				ps.setLong(1, BleAddress);
				ps.setLong(2, nodeId);
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

	@Override
	public long getLastAddedStreamHeaderTime(long nodeId) {
		
		PostgreSQL_DatabaseConnector connector = new PostgreSQL_DatabaseConnector();
		Connection dbConnection = connector.getDatabaseConnection();

		long lastTimeSynced =0;
		
		if (dbConnection != null) {
			
			try {
				
				PreparedStatement preparedStatement = dbConnection
						.prepareStatement("SELECT stream_header.starting_log_time,"
								+ "egv_count, "
								+ "period, "
								+ "stream_header.date_created "
								+ "FROM public.stream_header "
								+ "inner join log "
								+ "on log.id = stream_header.log_id "
								+ "where log.node_id = ? "
								+ "order by stream_header.starting_log_time desc "
								+ "Limit 1;");
//202526
				preparedStatement.setLong(1, nodeId);
				ResultSet resultSet = preparedStatement.executeQuery();

				
				if (resultSet.next()) {
					
					long starting_log_time = resultSet.getLong("starting_log_time");
					long egv_count = resultSet.getLong("egv_count");
					long period = resultSet.getLong("period");
					
					boolean firstBit = (period & 1) > 0 ? true : false;
					period = (firstBit) ? 5 * 60 : 30; // (first bit is true) ? period will be 300 seconds: 30 second
					
					lastTimeSynced = starting_log_time+(egv_count*period);
					System.out.println("### stream header time fetched "+starting_log_time+" period "+ period+ " egv_count "+egv_count);
					System.out.println("### last sync time calculated "+lastTimeSynced);
					
				} else {
					System.out.println("### Didn't find last stream header");
					logger.debug("last sent stream is not found");
				}

				resultSet.close();
				preparedStatement.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}

			connector.closeConnection();
		}

		return lastTimeSynced;
		
	}
	
	
}
