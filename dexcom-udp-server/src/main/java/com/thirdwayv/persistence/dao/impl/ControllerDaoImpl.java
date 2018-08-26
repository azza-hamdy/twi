package com.thirdwayv.persistence.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thirdwayv.persistence.connection.PostgreSQL_DatabaseConnector;
import com.thirdwayv.persistence.dao.ControllerDao;
import com.thirdwayv.persistence.entity.Controller;

public class ControllerDaoImpl implements ControllerDao{
	final static Logger logger = LoggerFactory.getLogger(ControllerDaoImpl.class);

	@Override
	public Controller getControllerByDeviceNumber(int deviceNumber) {
		
		Controller device = null;
		
		
		PostgreSQL_DatabaseConnector connector = new PostgreSQL_DatabaseConnector();
		Connection dbConnection = connector.getDatabaseConnection();
		
		if(dbConnection!=null)
		{
			try 
			{
				PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT * FROM controller WHERE device_number = ? " );
				preparedStatement.setInt(1, deviceNumber);
				
				ResultSet resultSet = preparedStatement.executeQuery();
				
				
				if (resultSet.next())
				{
					device = new Controller();
					device.setId(resultSet.getInt("id"));
					device.setDeviceNumber(resultSet.getInt("device_number"));
					device.setLtk(resultSet.getString("cloud_long_term_key"));
					device.setSequenceNumber(resultSet.getInt("session_sequence_number"));
				}
				else
				{
					logger.debug("No Controller devices found for passed parameters");
				}
				
				resultSet.close();
				preparedStatement.close();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			
			connector.closeConnection();
		}
		
		return device;
	}

	@Override
	public boolean updateControllerLastActivity(long controllerId) {

		PostgreSQL_DatabaseConnector connector = new PostgreSQL_DatabaseConnector();
		Connection dbConnection = connector.getDatabaseConnection();
		
		boolean result = false;
		
		if(dbConnection!=null)
		{
			try 
			{
				String query = " UPDATE controller  SET last_activity = ? WHERE id = ? ";

				PreparedStatement ps = dbConnection.prepareStatement(query); 
				
		        Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			         
				ps.setTimestamp(1,new Timestamp(System.currentTimeMillis()),utc );
				ps.setLong(2, controllerId); //id
				
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

	public static void main(String[] args){
		    ZonedDateTime nowUTC = ZonedDateTime.now(ZoneOffset.UTC);

		    LocalDateTime localDateTime = nowUTC.toLocalDateTime();
		    System.out.println(localDateTime);
		
	}
}
