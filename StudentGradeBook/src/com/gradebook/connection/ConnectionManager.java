package com.gradebook.connection;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager
{
	private static Connection connection = null;
	
	public static void makeConnection()
	{
		// Properties will be used to access our properties file and read its values
		Properties props = new Properties();
		
		try
		{
			props.load(new FileInputStream("resources/config.properties"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		String url = props.getProperty("url");
		String username = props.getProperty("username");
		String password = props.getProperty("password");
		
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(url, username, password);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection()
	{
		if (connection == null)
			makeConnection();
		return connection;
	}
	
	
	public static void main(String[] args)
	{
		Connection conn = ConnectionManager.getConnection();
		System.out.println("Made connection");
		
		try
		{
			conn.close();
			System.out.println("Connection closed");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}


