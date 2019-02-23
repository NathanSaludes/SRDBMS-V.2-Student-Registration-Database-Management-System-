package model;

public class Student {
	
	// Student Fields
	private String ID			= null;
	private String lastName		= null;
	private String firstName	= null;
	private String course		= null;
	private int units_taken		= 0;
	private final int units		= 210;		
	private int yearLevel		= 0;
	private boolean graduated	= false;
	
	public Student() {
	}
	
	public Student(
		String ID,
		String lastName,
		String firstName,
		String course,
		int units_taken
	){
		
	}
	
	
	public String getID() {
		return ID;
	}
	public void setID(String studentID) {
		this.ID = studentID;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public int getUnits() {
		return units;
	}
	
	
	
	public int getUnits_taken() {
		return units_taken;
	}
	public void setUnits_taken(int units_taken) {
		this.units_taken = units_taken;
	}
	
	public boolean isGraduated() {
		if(units_taken < units) {
			graduated = false;
		} else {
			graduated = true;
		}
		
		return graduated;
	}

	public int getYearLevel() {
		
		if(units_taken < 52.5) {
			this.yearLevel = 1;
		} else if (units_taken < 105) {
			this.yearLevel = 2;
		} else if (units_taken < 157.5) {
			this.yearLevel = 3;
		} else if (units_taken > 157.5) {
			this.yearLevel = 4;
		}
		
		return yearLevel;
	}	
}
