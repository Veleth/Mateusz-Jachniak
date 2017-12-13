package chineseMateuszExceptions;

public class GameAlreadyInitedException extends Exception {

    public GameAlreadyInitedException() {

        super("Game has been already initialized!");
    }
}
