package main.java.chineseMateuszExceptions;

public class InvalidNumberOfHumansException extends Exception {
	public InvalidNumberOfHumansException(int h) {
		super("The given number of human players is invalid: "+h+".\n Remember that the number of human players must be at least 1 and cannot exceed the total number of players.");
	}
}
