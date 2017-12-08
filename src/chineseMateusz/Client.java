package chineseMateusz;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import chineseMateusz.Pawn.PlayerColor;
//TODO: Ask if the client should be in the same package as the rest of the classes
public class Client extends JFrame {

    private Board board;
	private Human player;
	private int x1, y1, x2, y2;
	Socket socket = null;
	ObjectOutputStream out = null;
	ObjectInputStream in = null;

	public static void main(String args[]){
		Human p = new Human("TEST", PlayerColor.PINK); //TODO: Player must be sent from server, how to receive board from Game??
		Client window = new Client(p, new Board(new Player[6]));
		window.setDefaultCloseOperation(EXIT_ON_CLOSE);
		window.setVisible(true);
		window.setResizable(false);
		window.setLocation(500, 200);
		window.listenSocket();
	}

	public Client(Human h, Board b){
		super("Chinese Checkers, "+ h.getName());
		this.board = b;
		this.player = h;
		addMouseListener(new MyMouseAdapter());
	}

    public void listenSocket(){
        try {
            socket = new Socket("localhost", 21372);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            x1 = x2 = y1 = y2 = -1;
        }
        catch (UnknownHostException e){
            JOptionPane.showMessageDialog(null, "Unknown host!", "ERROR", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        catch (IOException e){
            JOptionPane.showMessageDialog(null, "Could not establish connection with server!", "ERROR", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    protected boolean isPawn(int x, int y) {

	    for(int i = 0; i < player.pawns.length; ++i) {
            if(x == player.pawns[i].getX() && y == player.pawns[i].getY()) {
                return true;
            }
        }

        return false;
    }

	class MyMouseAdapter extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e){
			if(x1 == -1 && y1 == -1) {
			    x1 = e.getX();
			    y1 = e.getY();

			    if(!(isPawn(x1, y1))) {
			        x1 = y1 = -1;
                }
                //paint x1, y1
            } else {
                try {
                    x2 = e.getX();
                    y2 = e.getY();
                    int[][] coordinates = {{x1, y1}, {x2, y2}};
                    out.writeObject(coordinates);
                    x1 = x2 = y1 = y2 = -1;
                    //server takes first coordinates and checks possible moves to them - for: players saves to arraylist, another one with only a[0]=x and a[1]=y, server updates pawns
                    //board.updateboard()
                    //repaint

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

		}
	}

}
