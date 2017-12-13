package chineseMateusz;

import chineseMateusz.Pawn.PlayerColor;

import java.net.Socket;

public class Human extends Player implements Runnable {

    private Game game;
    private Socket socket;


    public Human(Game game, Socket socket, PlayerColor playerColor) {
		super(playerColor);
		this.game = game;
		this.socket = socket;
		setHuman(true);
	}

    @Override
    public void run() {

    }

    public int[] move() {
        return null; //to implement
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
