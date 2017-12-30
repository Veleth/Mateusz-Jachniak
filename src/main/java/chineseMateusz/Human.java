package main.java.chineseMateusz;

import main.java.chineseMateusz.Pawn.PlayerColor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Human extends Player implements Runnable {

    private Game game;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean isMoving;
    private boolean isConnected;
    private volatile int[] currentMove;

    public Human(Game game, ObjectInputStream in, ObjectOutputStream out, PlayerColor playerColor) throws IOException {
		super(playerColor);
		this.game = game;
        this.in = in;
        this.out = out;
		isMoving = false;
		isConnected = true;
		setHuman(true);
    }

    @Override
    public void run() {
        try {

            while (true) {
                Object o = in.readObject();

                if (isMoving) {
                    if (o.getClass().isArray()) {
                        int[] tempArray = (int[]) o;

                        if(tempArray[0] == -200 && tempArray[1] == -200 && tempArray[2] == -200 && tempArray[3] == -200) {}
                        else if(!(isPawn(tempArray[0], tempArray[1]))) {
                            continue;
                        }

                        isMoving = false;
                        currentMove = tempArray;
                    }
                }
            }
        }
        catch (Exception e) {
            isConnected = false;
            game.abortedGame();
        }
    }

    public int[] move() {

        while (currentMove == null) {
            if(!isConnected) {
                int[] temp = {-100,-100,-100,-100 };
                currentMove = temp;
            }
        }

        return currentMove;
    }

    public void sendMoveToClient(int[] move) throws IOException {
        if(isConnected) {
            out.writeObject(move);
        }
    }

    public void sendBoardToClient(Board board) throws IOException {
        if(isConnected) {
            Board b = board;
            b.setPlayerColor(getPlayerColor());
            out.writeObject(b);
        }
    }

    public void sendTextToClient(String string) throws IOException {
        if(isConnected) {
            out.writeObject(string);
        }
    }

    public void sendMoverToClient(PlayerColor playerColor) throws IOException {
        if(isConnected) {
            out.writeObject(playerColor);
        }
    }

    private boolean isPawn(int x, int y) {

        for(int i = 0; i < pawns.length; ++i) {
            if(x == pawns[i].getX() && y == pawns[i].getY()) {
                return true;
            }
        }
        return false;
    }

    public void nullCurrentMove() {
        currentMove = null;
    }

    public void setisMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
