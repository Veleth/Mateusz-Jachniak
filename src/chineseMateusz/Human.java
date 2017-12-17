package chineseMateusz;

import chineseMateusz.Pawn.PlayerColor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Human extends Player implements Runnable {

    private Game game;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean isMoving;
    private volatile int[] currentMove;

    public Human(Game game, Socket socket, PlayerColor playerColor) throws IOException {
		super(playerColor);
		this.game = game;
        this.socket = socket;
        in = new ObjectInputStream(socket.getInputStream());
		out = new ObjectOutputStream(socket.getOutputStream());
		isMoving = false;
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

                        if(!(isPawn(tempArray[0], tempArray[1]))) {
                            continue;
                        }

                        currentMove = tempArray;
                        isMoving = false;
                    }
                }
            }
        }
        catch (Exception e) {
            game.abortedGame();
        }
    }

    public int[] move() {

        while (currentMove == null) {
            if(hasFinished()) {
                int[] temp = {-100,-100,-100,-100 };
                currentMove = temp;
            }
        }

        return currentMove;
    }

    public void sendMoveToClient(int[] move) throws IOException {
        out.writeObject(move);
    }

    public void sendBoardToClient(Board board) throws IOException {
        Board b = board;
        b.setPlayerColor(getPlayerColor());
        out.writeObject(b);
    }

    public void sendTextToClient(String string) throws IOException {
        out.writeObject(string);
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

    public void sendMoverToClient(PlayerColor playerColor) throws IOException {
        out.writeObject(playerColor);
    }
}
