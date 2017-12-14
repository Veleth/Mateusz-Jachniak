package chineseMateusz;

import chineseMateusz.Pawn.PlayerColor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class Human extends Player implements Runnable, Serializable {

    private static final long serialVersionUID = -8499720961482595815L;

    private Game game;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean isMoving;
    private Player[] players;

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
                if (isMoving) {

                    Object o = in.readObject();

                    if (o.getClass().isArray()) {
                        int[] tempArray = (int[]) o;
                        //bierze ruch, isPawn paczy i jak okej to przypisuje do currentMove

                        //zeruje currentMove, a wysyla tylko gdy currentMove nie jest nulem
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int[] move() {
        return null; //to implement
    }

    public void sendMoveToClient(int[] move) throws IOException {
        out.writeObject(move);
    }

    public void sendBoardToClient(Board board) throws IOException {
        out.writeObject(board);
        System.out.println("wyslalem boarda");
    }

    private boolean isPawn(int x, int y) {

        for(int i = 0; i < pawns.length; ++i) {
            if(x == pawns[i].getX() && y == pawns[i].getY()) {
                return true;
            }
        }
        return false;
    }

}
