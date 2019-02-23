package controller;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import exceptions.StudentNotFoundException;
import model.Student;
import view.View;

public class DatabaseManager {
	
	// Database Configuration
	private static String JDBC_DRIVER	= "com.mysql.jdbc.Driver";
	private static String DB_NAME		= "SRDBMS";
	private static String DB_URL		= "jdbc:mysql://localhost:3306/";
	private static String DB_TABLE_NAME	= "students";
	private static Connection conn		= null;
	
	public String getDB_NAME() {
		return DB_NAME;
	}
	public String getDB_URL() {
		return DB_URL;
	}
	public String getDB_TABLE_NAME() {
		return DB_TABLE_NAME;
	}

	public DatabaseManager() {}
	
	public DatabaseManager(String db_name) {
		// change default name of database to user defined database name
		DB_NAME = db_name;
	}
	
	
	
	
	
	// CREATE DATABASE
	public boolean createDatabase() throws Exception {
		
		Class.forName(JDBC_DRIVER);
		
		// [STEP 1]: initialize connection to create a database
		View.println("# Initializing connection...");
		Connection init_conn = DriverManager.getConnection(DB_URL, "root", "");		
		
		// [STEP 2]: check if a database exists
		if(init_conn.isValid(5)) {
			Statement stmnt			= init_conn.createStatement();
			DatabaseMetaData dbmd 	= init_conn.getMetaData();
			ResultSet rs			= dbmd.getCatalogs();
			String SQL				= null;
			
			//  [STEP 3]: update the database URL
			DB_URL = DB_URL.concat(DB_NAME);
			
			if(!databaseExists(rs)) {
				View.println("# Creating a database...");
				
				// [STEP 4]: if the database does not exist, create a database
				SQL = "CREATE DATABASE `" + DB_NAME + "`";
				stmnt.executeUpdate(SQL);
				
				return true;
				
			} else {
				View.println("# Database already exists. Using `" + DB_NAME + "` database!");
				// [STEP 5]: Else, use the existing database
				return true;
			}
			
		} else {
			throw new ConnectException();
		}
		
	}
	
	// CHECK DATABASE DUPLICATES
	private boolean databaseExists(ResultSet rs) throws SQLException {
		
		while(rs.next()) {
			if(rs.getString("TABLE_CAT").equalsIgnoreCase(DB_NAME)) {
				return true;
			}
		}
		
		return false;
	}

	// GET DATABASE CONNECTION
	public void getConnection() throws Exception {
		Class.forName(JDBC_DRIVER);
		conn = DriverManager.getConnection(DB_URL, "root", "");
		View.println("# Successfully connected to the database! " + View.NL + "#");
	}

	// CREATE A STUDENT TABLE
	public void createTable() throws Exception {
		Statement stmnt = conn.createStatement();
		String SQL		= null;
		
		SQL  =	"CREATE TABLE " + DB_TABLE_NAME + " ("  +
				" id INTEGER NOT NULL AUTO_INCREMENT, " 	+
				" studentId VARCHAR(15), " 		+
				" firstName VARCHAR(255), " +
				" lastName VARCHAR(255), " 	+
				" course VARCHAR(5), " 		+
				" units INTEGER, "			+
				" unitsTaken INTEGER, " 	+
				" yearLevel INTEGER, " 		+
				" graduated BOOLEAN, "			+
				" PRIMARY KEY ( id ))";
		
		stmnt.executeUpdate(SQL);
		
		View.println("# Successfully created a new `" + DB_TABLE_NAME + "` table!" + View.NL + View.hr(2));
	}

	// DELETE EXISTING DUPLICATE TABLE
	public void deleteExistingTable() throws Exception {
		try {
			Statement stmnt			= conn.createStatement();
			String SQL			= null;
			
			SQL = "DROP TABLE `" + DB_TABLE_NAME + "`";
			
			stmnt.executeUpdate(SQL);
			
			View.println("# `" + DB_TABLE_NAME + "` table deleted!" + View.NL + "# ");
			
		} catch (SQLException e) {
			View.println("# Error. Failed to delete `" + DB_TABLE_NAME + "` table.");
		}
	}
	
	// CHECK EXISTING TABLES
	public boolean tableExists() {
		try {
			DatabaseMetaData dbmd = null;
			ResultSet rs = null;
			
			dbmd = conn.getMetaData();
			rs = dbmd.getTables(DB_NAME, null, DB_TABLE_NAME, null);
			
			if(rs.next()) {
				View.println("# Table `" + DB_TABLE_NAME + "` already exists. ");
				View.println(View.hr(2));
				return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			View.println("# Error! Unable to check existing database tables.");
			return false;
		}
		
		return false;
	}
	
	// CLEAR TABLE RECORDS
	public void clearTable() {
		Statement stmnt 	= null;
		ResultSet rs		= null;
		String SQL			= null;
		
		try {
			SQL = "DELETE FROM `" + DB_TABLE_NAME + "`";
			
			stmnt = conn.createStatement();
			
			View.println("# ERASING ALL STUDENT RECORDS FROM `" + DB_TABLE_NAME + "` TABLE...");
			
			rs = getAllRecords();
			if(rs.next()) {
				stmnt.executeUpdate(SQL);				
				View.println(View.NL + "# Successfully deleted all records in the `" + DB_TABLE_NAME + "` table.");
			} else {
				View.println(View.NL + "# Unable to delete records.");
				View.println("# There are currently no existing student record(s) in the database.");
			}
			
			
			
		} catch (SQLException e) {
			// e.printStackTrace();
			View.println("# Error! Unable to delete all records from table `" + DB_TABLE_NAME + "`.");
		}
	}
	
	// VALIDATE DATABASE CONNECTION
	public boolean validateConnection() {

		try {
			if(!conn.isClosed()) {
				if(conn.isValid(5)) {
					return true;
				}
			}			
			return false;
		} catch (SQLException e) {
			// e.printStackTrace();
			return false;
		} catch (NullPointerException e) {
			return false;
		}
	}

	// CLOSE/TERMINATE CONNECTION FROM DATABASE
	public void terminateConnection() {
		try {
			if(validateConnection() || !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			View.println(View.NL + "# Error! Unable to close/terminate database connection");
			View.println("ERROR: " + e.getMessage());
			//e.printStackTrace();
		}
	}
	
	
	/*==========================================================================================================================================================================================*/
	// ADD RECORD 
	public void insert(Student s) {
		PreparedStatement ps = null;
		String SQL = "INSERT INTO " + DB_TABLE_NAME + " " + 
					 "(studentId, firstName, lastName, course, units, unitsTaken, yearLevel, graduated) " +
					 "values (?,?,?,?,?,?,?,?)";
		try {
			if(validateConnection()) {
				ps = conn.prepareStatement(SQL);
				
				ps.setString(1, s.getID());
				ps.setString(2, s.getFirstName());
				ps.setString(3, s.getLastName());
				ps.setString(4, s.getCourse());
				ps.setInt(5, s.getUnits());
				ps.setInt(6, s.getUnits_taken());
				ps.setInt(7, s.getYearLevel());
				ps.setBoolean(8, s.isGraduated());
				
				ps.executeUpdate();
			}
			
			ps.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			 View.println("# Error! Unable to add student to the database.");
		}
	}
	
	// SEARCH RECORD
	public ResultSet search(String input) {
		PreparedStatement ps 	= null;
		ResultSet rs			= null;
		
		String SQL = 	"SELECT * FROM `" + DB_TABLE_NAME + "` WHERE " +
						"studentId=? OR " +
						"lastName=? OR " +
						"firstName=? OR " +
						"course=? OR " +
						"unitsTaken=? OR " +
						"yearLevel=? ORDER BY lastName, firstName";
		
		try {
			if(validateConnection()) {
				ps = conn.prepareStatement(SQL);
				
				ps.setString(1, input);
				ps.setString(2, input);
				ps.setString(3, input);
				ps.setString(4, input);
				ps.setString(5, input);
				ps.setString(6, input);
				
				rs = ps.executeQuery();
				
				int rowCount = 0;
				
				while(rs.next()) {
					++rowCount;
				}
				
				View.println("--- [ Found Results: " + rowCount + " ] ---");
				
				rs.beforeFirst();
				
				return rs;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return rs;
		
	}

	// DELETE RECORD
	public void delete(String id) {
		PreparedStatement ps 	= null;
		ResultSet rs 			= null;
		String SQL				= "DELETE FROM `" + DB_TABLE_NAME + "` WHERE studentId=?";
		
		try {
			if(validateConnection()) {
				ps = conn.prepareStatement(SQL);
				ps.setString(1, id);
				
				rs = search(id);
				rs.beforeFirst();
				
				if(rs.next()) {
					ps.executeUpdate();
					
					View.print(View.NL);
					View.printStudent(
						rs.getString("studentId"), 
						rs.getString("lastName"), 
						rs.getString("firstName"), 
						rs.getString("course"), 
						rs.getInt("unitsTaken"), 
						rs.getInt("yearLevel"), 
						rs.getBoolean("graduated")
					);
					
					View.println("# Successfully deleted a record in `" + DB_TABLE_NAME + "`");
				} else {
					throw new StudentNotFoundException();
				}
				
			}
		} catch (StudentNotFoundException e) {
			View.println(View.NL + "# Error! Unable to delete record from `" + DB_TABLE_NAME + "` table.");
			View.println("# " + e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
			View.println("# Error! Unable to delete a record from `" + DB_TABLE_NAME + "` table.");
		}
		
	}
	
	// RETRIEVE ALL RECORDS
	public ResultSet getAllRecords() {
		Statement s = null;
		ResultSet rs = null;
		String SQL = "SELECT * FROM `" + DB_TABLE_NAME + "` ORDER BY lastName, firstName";
		
		try {
			s = conn.createStatement();
			rs = s.executeQuery(SQL);
			
			rs.beforeFirst();
			
			return rs;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return rs;
	}
	
	// GENERATE STUDENT REPORT
	public void generateReport() {
		Statement s = null;
		ResultSet rs = null;
		
		String SQL = "SELECT course, COUNT(studentId) AS count FROM `" + DB_TABLE_NAME + "` GROUP BY course";
		
		try {
			s = conn.createStatement();
			rs = s.executeQuery(SQL);
			int total = 0;
			
			View.println("# GENERATING A REPORT OF ALL COURSES..." + View.NL);
			
			if(rs.next()) {
				rs.beforeFirst();
				while(rs.next()) {
					View.println(
						"TOTAL NUMBER OF [" + rs.getString("course").toUpperCase() + "]: " + rs.getString("count")
					);
					
					total = total + Integer.parseInt(rs.getString("count"));
				}
				
				View.println("---------------------------------+ ");
				View.println("TOTAL NUMBER OF STUDENTS: " + total);
			} else {
				View.println("# Unable to generate report.");
				View.println("# No student record(s) found!");
			}
			
			
			
		} catch (SQLException e) {
			View.println("# Error! Unable to generate report.");
			View.println("# SQL ERROR: " + e.getMessage());
			//e.printStackTrace();
		}
	}
}
