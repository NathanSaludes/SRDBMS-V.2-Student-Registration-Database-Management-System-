package exceptions;

public class StudentFieldException extends Exception{
	
	public StudentFieldException() {
		super("Invalid student field.");
	}
	
	public StudentFieldException(String message) {
		super(message);
	}
}
