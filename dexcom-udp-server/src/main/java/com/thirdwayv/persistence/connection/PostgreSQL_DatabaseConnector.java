package com.thirdwayv.persistence.connection;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

import org.postgresql.jdbc3.Jdbc3PoolingDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PostgreSQL_DatabaseConnector 
{
	private static Logger logger = LoggerFactory.getLogger(PostgreSQL_DatabaseConnector.class);
	private static Properties DB_PROPERTIES = new Properties();
	static Jdbc3PoolingDataSource source;
	private Connection currentDbConnection;
	
	
	static 
	{
		String propFile = "db.properties";
		
		InputStream input = PostgreSQL_DatabaseConnector.class.getClassLoader().getResourceAsStream(propFile);
		
		if (input == null) 
		{
			logger.error("Sorry, unable to find " + propFile);
		}
		else
		{
			try
			{
				DB_PROPERTIES.load(input);
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
	 	source = new Jdbc3PoolingDataSource();
	
	 	
	 	try
		{
			source.setDataSourceName("ADataSource");
			source.setServerName("localhost");
			
			source.setDatabaseName(DB_PROPERTIES.getProperty("database"));
			source.setUser(DB_PROPERTIES.getProperty("user"));
			source.setPassword(DB_PROPERTIES.getProperty("password"));
			source.setMaxConnections(Integer.parseInt(DB_PROPERTIES.getProperty("maxConnectionPool")));
		}
	 	catch (Exception e)
	 	{
			logger.info("Unable to get database connection parameters", "");
	 	}
				
	}
	
	
	/**
     * This method is used to get database connection with the specified url and parameters
     * @return Connection object if connection went successfully, or null otherwise
     */
	public Connection getDatabaseConnection()
	{
		//logger.info("Getting db connection..."); 
		
		if(currentDbConnection==null)
		{
			try 
			{
				currentDbConnection = source.getConnection();
			} 
			catch (Exception e) 
			{
				logger.error("Failed to get db connection", e.toString()); 
			}
		}
		
		return currentDbConnection;
	}
	
	
	
	/**
     * This method is used to close the database connection
     */
	public void closeConnection()
	{

		try
		{
			if (currentDbConnection != null && !currentDbConnection.isClosed()) 
			{
				try 
				{ 
					currentDbConnection.close(); 
				} 
				catch (Exception e) 
				{
					logger.error("Failed to close db connection", e.toString());
				}
			}
		}
		catch (Exception e)
		{
			currentDbConnection = null;
		}
		
	}
	

}
