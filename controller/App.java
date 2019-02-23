package controller;

import java.io.File;
import java.net.ConnectException;
import java.sql.SQLException;

import view.View;

public class App {
	
	// Default Program Configuration
	private static String default_path				= "C:\\DEV_WORKSPACE\\MY_WORKSPACE\\SRDBMS\\";
	private static String default_commandFileName	= "command.dat";
	private static String default_logFileName		= "log.dat";
	
	// Database Manager
	private static DatabaseManager dbm 	= null;
	private static File commandFile 	= null;
	private static File logFile   		= null;
	
	
	public static void main(String[] args) {
		handleMainArguments(args);
	}
	
	public static DatabaseManager handleDatabaseManager(String db_name) {
		DatabaseManager dbm	= null;
		
		if(db_name != null) {
			dbm = new DatabaseManager(db_name);
		} else {
			// use default DB_NAME
			dbm = new DatabaseManager();
		}
		
		try {
			// create database
			dbm.createDatabase();
			
			// get database connection
			dbm.getConnection();
			
			if(!dbm.tableExists()) {
				// create a table				
				dbm.createTable();
			}
			
			View.printDatabaseConfig(dbm.getDB_NAME(), dbm.getDB_TABLE_NAME(), dbm.getDB_URL());
			
			
		} catch (ClassNotFoundException e) {
			View.driverError();
			// e.printStackTrace();
			
		} catch (ConnectException e) {
			View.databaseConnectionError();
			//e.printStackTrace();

		} catch (SQLException e) {
			View.println("Error! " + e.getMessage() + View.NL + View.hr(2));
			//e.printStackTrace();
			
		} catch (Exception e) {
			//e.printStackTrace();
			View.println("Error! " + e.getMessage() + View.NL + View.hr(2));
		}
		
		return dbm;
	}
	
	public static void handleCommandFileReader(DatabaseManager dbm, File commandFile) {
		if(dbm.validateConnection()) {
			new CommandFileReader(dbm, commandFile);
		} else {
			View.commandFileReaderError();
		}
	}
	
	public static void handleMainArguments(String[] arguments) {		
		switch(arguments.length) {
			case 3:
				// get argument values
				commandFile 		= new File(arguments[0]);
				logFile 			= new File(arguments[1]);
				String user_DB_NAME	= arguments[2];
				
				// display user configuration
				View.printAppConfig(
					commandFile.getAbsolutePath(), 
					commandFile.getName(), 
					logFile.getAbsolutePath(), 
					logFile.getName()
				);
				
				dbm = handleDatabaseManager(user_DB_NAME);
				handleCommandFileReader(dbm, commandFile);
				View.log(logFile.getAbsolutePath());
				break;
				
			case 2:
				// get argument values
				commandFile = new File(arguments[0]);
				logFile 	= new File(arguments[1]);
				
				// display user configuration
				View.printAppConfig(
					commandFile.getAbsolutePath(), 
					commandFile.getName(), 
					logFile.getAbsolutePath(),
					logFile.getName()
				);
				
				dbm = handleDatabaseManager(null);
				handleCommandFileReader(dbm, commandFile);
				View.log(logFile.getAbsolutePath());
				break;
				
			default:
				// use default program configuration
				commandFile 	= new File(default_path.concat(default_commandFileName));
				logFile			= new File(default_path.concat(default_logFileName));
				
				// display default configuration
				View.printDefaultAppConfig(
					commandFile.getAbsolutePath(), 
					logFile.getAbsolutePath(), 
					default_commandFileName, 
					default_logFileName
				);
				
				dbm = handleDatabaseManager(null);
				handleCommandFileReader(dbm, commandFile);
				View.log(logFile.getAbsolutePath());
				break;
		}
		
		
		
	}
}
