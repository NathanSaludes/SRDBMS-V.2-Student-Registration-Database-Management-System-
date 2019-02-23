package view;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class View {
	
	public static StringBuilder sb 	= new StringBuilder();
	
	// LOG FILE GENERATOR
	public static void log(String logFilePath) {
		try {
			FileWriter fw = new FileWriter(logFilePath);
			fw.write(sb.toString());
			fw.flush();
			fw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	// PRINT NEXT LINE
	public static String NL = System.lineSeparator();
	
	public static void printAppConfig(String user_commandFilePath, String user_commandFileName, String user_logFilePath, String user_logFileName) {		
		println( 
			"APPLICATION CONFIGURATION"					 	+ NL + 
			"Command File Path: " 	+ user_commandFilePath 	+ NL +
			"Command File Name: "	+ user_commandFileName	+ NL +
			"Log File Path: " 		+ user_logFilePath		+ NL +
			"Log File Name: "		+ user_logFileName		+ NL +
			hr(2) //===============================================
		);
	}
	
	public static void printDefaultAppConfig(String commandFilePath, String logFilePath, String commandFileName, String logFileName) {
		println(
			"APPLICATION CONFIGURATION" 						+ NL + 
			"(Default) Command File Path: " + commandFilePath 	+ NL +
			"(Default) Command File Name: " + commandFileName	+ NL +
			"(Default) Log File Path: " 	+ logFilePath		+ NL +
			"(Default) Log File Name: "		+ logFileName		+ NL +
			hr(2) //===============================================
		);
	}
	
	public static void printDatabaseConfig(String DB_NAME, String DB_TABLE_NAME, String DB_URL) {
		println(
			"DATABASE CONFIGURATION: " 			+ NL +
			"Database: "	+ DB_NAME 			+ NL +
			"Table: " 		+ DB_TABLE_NAME 	+ NL +
			"URL: " 		+ DB_URL			+ NL +
			hr(2) //===============================================
		);
	}
	
	public static void printStudent(String id, String lastName, String firstName, String course, int unitsTaken, int yearLevel, boolean graduate) {
		println(
			"ID: " + id 							+ NL +
			"Name: " + lastName + ", " + firstName 	+ NL +
			"Course: " + course 					+ NL +
			"Units Earned: " + unitsTaken 			+ NL +
			"Year level: " + yearLevel 				+ NL
		);
		
		if(graduate) {
			println("GRADUATED STUDENT");
		}
	}
	
	public static void printCommand(String input) {
		println("Command: " + input + NL);
	}
	
	public static void commandFileReaderError() {
		println(
			"# Error! Command file reader aborted." + NL + hr(2)
		);
	}
	
	public static void databaseConnectionError() {
		println("Error! database connection failed." + NL + hr(2));
	}
	
	public static void driverError() {
		println("Error! Failed to load JDBC driver." + NL + hr(2));
	}
	
	public static void addStudentError() {
		println("# Error! Unable to add student.");
	}
	
	
	
	
	
	
	public static void printSearchResults(ResultSet rs) throws SQLException {
		rs.beforeFirst();
		for(int counter=1; rs.next(); ++counter) {
			View.print(NL);
			View.println("[" + counter + "]");
			View.println("Name: " + rs.getString("lastName") + ", " + rs.getString("firstName"));
			View.println("ID: " + rs.getString("studentId"));
			View.println("Course: " + rs.getString("course"));
			View.println("Units Earned: " + rs.getInt("unitsTaken"));
		}
	}
	
	
	
	
	
	
	
	
	
	// HORIZONTAL LINE
		public static String hr(int format) {
			String line = null;
			
			switch (format) {
				case 1:
					line = "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------";
					break;
					
				case 2:
					line = "========================================================================================================================================================================================================";
					break;
			}
			
			return line;
		}
		
		public static void println(String message) {
			System.out.print(message + NL);
			sb.append(message + NL);
		}
		
		public static void print(String message) {
			System.out.print(message);
			sb.append(message);
		}

		
	
	

	

	

}
