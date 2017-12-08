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
	private int x1, y1, x2, y2;
	private int gameNum;
	private Human currPlayer;
	private boolean isGameActive;
	
	Socket socket = null;
	ObjectOutputStream out = null;
	ObjectInputStream in = null;

	public static void main(String args[]) throws ClassNotFoundException, IOException, InterruptedException{
		Client window = new Client();
		window.setDefaultCloseOperation(EXIT_ON_CLOSE);
		window.setVisible(true);
		window.setResizable(false);
		window.setLocation(500, 200);
		window.listenSocket();
		window.initialize();
		window.play();
		System.exit(0);
	}

	private void play() throws ClassNotFoundException, IOException, InterruptedException {
		while(isGameActive){
			if(in.readObject() instanceof Human){
				//TODO:Move the Human
			}
			else if (in.readObject() instanceof Board){
				board = (Board)in.readObject();
				//TODO:Repaint
			}
			else if (in.readObject().getClass().isArray()){
				//TODO: Print the ranking
				Thread.sleep(2000);
				isGameActive=false;
			}
		}
	}

	private void initialize() {
		//TODO: Exchange data with the server, initialize the game (# of players, game number, etc.)
	}

	public Client(){
		super("Chinese Checkers Client");
		isGameActive=true;
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

	    for(int i = 0; i < currPlayer.pawns.length; ++i) {
            if(x == currPlayer.pawns[i].getX() && y == currPlayer.pawns[i].getY()) {
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
