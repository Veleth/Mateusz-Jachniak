package chineseMateuszExceptions;

public class InvalidNumberOfPlayersException extends Exception {

	public InvalidNumberOfPlayersException(int x) {
		super(x+" is an invalid number of players. Try with 2, 3, 4 or 6.");
	}

}
