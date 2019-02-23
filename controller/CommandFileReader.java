package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import exceptions.StudentFieldException;
import model.Student;
import view.View;

public class CommandFileReader {
	
	private static DatabaseManager dbm		= null;
	private static File commandFile			= null;
	private static Scanner scanner			= null;
	private static String input;

	public CommandFileReader(DatabaseManager databaseManager, File commandFile) {
		dbm = databaseManager;
		CommandFileReader.commandFile = commandFile;
		readFile();
	}
	
	
	
	
	
	private void readFile() {
		try {
			View.println("# Locating command file...");
			
			scanner = new Scanner(commandFile);
			scanner.useDelimiter("\n");
			
			commandScanner(scanner);
			
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
			View.println("# Error! File not found!");
			View.println(View.hr(2));
		}
		
	}
	
	private void commandScanner(Scanner scanner) {
		boolean read = true;
		View.println("# Reading command file...");
		View.println(View.hr(2));
		
		while(scanner.hasNext() && read) {
			switch (input = scanner.next().toString().toUpperCase().trim()) {
			case "A":
				// View.printCommand(input);
				commandA();
				break;
			case "S":
				// View.printCommand(input);
				commandS();
				break;
			case "D":
				// View.printCommand(input);
				commandD();
				break;
			case "R":
				// View.printCommand(input);
				commandR();
				break;
			case "L":
				// View.printCommand(input);
				commandL();
				break;
			case "P":
				// View.printCommand(input);
				commandP();
				break;
			case "Q":
				// View.printCommand(input);
				View.println("# QUITTING APPLICATION...");
				View.println(View.hr(2));
				View.println("# Program Terminating... Good Bye!");
				read = false;
				break;
			default:
				break;
			}
		}
		
		scanner.close();
	}

	
	// COMMANDS =================================================================================================
	private void commandP() {
		dbm.clearTable();
		View.println(View.hr(1));
	}

	private void commandL() {
		try {
			View.println("# LISTING ALL STUDENT RECORDS...");
			
			ResultSet all = dbm.getAllRecords();
			
			if(all.next()) {
				View.printSearchResults(all);
			} else {
				View.println(View.NL + "# No student(s) found!");
			}
			
		} catch (SQLException e) {
			//e.printStackTrace();
			View.println("# Error! Unable to list all student records.");
		} finally {
			View.println(View.hr(1));			
		}
	}

	private void commandR() {
		dbm.generateReport();
		View.println(View.hr(1));
	}

	private void commandD() {
		input = scanner.next().toString().trim();
		
		View.println("# DELETE COMMAND INITIATED...");
		
		if(input != null && !input.isEmpty()) {
			View.println("# Retrieving student records with id of `" + input + "`..." + View.NL);
			dbm.delete(input);
		} else {
			View.println(View.NL + "# Error! Unable to perform deletion of record.");
			View.println("# Delete command requires student ID field.");
		}
		
		View.println(View.hr(1));
	}

	private void commandS() {
		input = scanner.next().trim().toString();
		
		try {
			
			if(input != null && !input.isEmpty()) {
				View.println("# SEARCHING FOR `" + input + "`" + View.NL);
				
				View.printSearchResults(dbm.search(input));
				
			} else {
				View.println("# Error! Unable to perform search");
				View.println("# Search field empty.");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			View.println("# Error! Unable to perform search.");
			View.println("# Failed to print results.");
		}
		
		View.println(View.hr(1));
	}

	private void commandA() {
		// initialize a student
		Student student = new Student();
		
		try {
			View.println("# ADDING A NEW STUDENT..." + View.NL);
			
			// Accept 5 parameters
			for(byte parameter=1; parameter<=5; parameter++) {
				
				input = scanner.next().trim();
				
				if(input != null && !input.isEmpty()) {
					switch(parameter) {
					case 1:
						if(input.length() <= 10 && input.length() >= 9) {
							
							// check if id exists							
							if(!dbm.search(input).next()) {
								student.setID(input);
							} else {
								View.print(View.NL);
								throw new StudentFieldException(View.NL + "# Student already exists.");
							}
							
						} else {
							throw new StudentFieldException("# Invalid Student ID Format!");
						}
						
						break;
					case 2:
						student.setLastName(input);
						break;
					case 3:
						student.setFirstName(input);
						break;
					case 4:
						if(input.length() <= 5) {
							student.setCourse(input.toUpperCase());							
						} else {
							throw new StudentFieldException("# Invalid Course Format.");
						}
						break;
					case 5:
						if(Integer.parseInt(input) <= 210) {
							student.setUnits_taken(Integer.parseInt(input));
						} else {
							throw new StudentFieldException("# Invalid Amount of Units.");
						}
						break;
					}
				} else {
					throw new StudentFieldException("# Empty Student Field.");
				}
			}
			
			View.print(View.NL);
			// PRINT STUDENT DETAILS
			View.printStudent(
				student.getID(), 
				student.getLastName(),
				student.getFirstName(), 
				student.getCourse(), 
				student.getUnits_taken(), 
				student.getYearLevel(), 
				student.isGraduated()
			);
			
			View.println("# Created a new student!");
			
			// INSERT STUDENT TO DATABASE
			dbm.insert(student);
			
			View.println("# Successfully inserted student into the database...");
			
			
		} catch (StudentFieldException e) {
			View.addStudentError();
			View.println(e.getMessage());
			
		} catch (Exception e) {
			View.addStudentError();
			
		} finally {
			View.println(View.hr(1));
		}
	}
	

}
