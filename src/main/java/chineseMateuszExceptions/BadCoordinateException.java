package main.java.chineseMateuszExceptions;

public class BadCoordinateException extends Exception {

	public BadCoordinateException(int x, int y) {
		super(x + " and " +  y + "aren't appropriate coordinates!");
	}
}
