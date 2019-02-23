package exceptions;

public class StudentNotFoundException extends Exception {

	public StudentNotFoundException(String message) {
		super(message);
	}
	
	public StudentNotFoundException() {
		super("Student Not Found.");
	}
}
