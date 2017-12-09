package chineseMateusz;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.*;

import chineseMateusz.Pawn.PlayerColor;
import chineseMateuszExceptions.InvalidNumberOfHumansException;
import chineseMateuszExceptions.InvalidNumberOfPlayersException;

import static java.lang.System.exit;

public class Client extends JFrame {

    private Board board;
	private int x1, y1, x2, y2;
	private int gameNum;
	private Human currPlayer;
	private boolean isGameActive;
	
	Socket socket = null;
	ObjectOutputStream out = null;
	ObjectInputStream in = null;

    public Client() {
	    super("Chinese Checkers Client");
        isGameActive = true;
        addMouseListener(new MyMouseAdapter());
    }

    public void setGUI() {
	    add(board);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    setLocation(100,100);
	    setSize(600,600);
        setResizable(false);
        setVisible(true);
    }

    public void communication() throws InterruptedException, IOException, ClassNotFoundException {
	    listenSocket();
	    initialize();
    }

    private void listenSocket() {
        try {
            socket = new Socket("localhost", 21372);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            x1 = x2 = y1 = y2 = -1;
        }
        catch (UnknownHostException e){
            JOptionPane.showMessageDialog(null, "Unknown host!", "ERROR", JOptionPane.ERROR_MESSAGE);
            exit(1);
        }
        catch (IOException e){
            JOptionPane.showMessageDialog(null, "Could not establish connection with server!", "ERROR", JOptionPane.ERROR_MESSAGE);
            exit(1);
        }
    }

    private void initialize() throws IOException, ClassNotFoundException {
        try {
            int players = amountOfPlayers();
            int humans = amountOfHumans(players);
            int amounts[] = {players, humans};
            out.writeObject(amounts);

            //sleep - time for init the game?

            Object temp = in.readObject();
            if(!(temp instanceof Number)) {
                exit(-1);
            }
            gameNum = (int) temp;

            Object tempBoard = in.readObject();
            if(!(tempBoard instanceof Board)) {
                exit(-1);
            }
            board = (Board) tempBoard;

            out.writeObject(gameNum);

        } catch (NumberFormatException e) {
            exit(-1);
        }
    }

    private int amountOfPlayers() {
        String[] values = {"2", "3", "4", "6"};

        Object selected = JOptionPane.showInputDialog(null, "How many players?", "Selection", JOptionPane.DEFAULT_OPTION, null, values, "2");
        if ( selected == null ) {
            exit(-1);
        }

        return Integer.parseInt(selected.toString());
    }

    private int amountOfHumans(int players) {
        String[] values = new String[players-1];

        for(int i = 1; i < players; ++i) {
            values[i-1] = i + "";
        }

        Object selected = JOptionPane.showInputDialog(null, "How many humans?", "Selection", JOptionPane.DEFAULT_OPTION, null, values, "1");
        if ( selected == null ) {
            exit(-1);
        }

        return Integer.parseInt(selected.toString());
    }

    public void play() throws ClassNotFoundException, IOException, InterruptedException {

	    while(isGameActive) {
		    Object temp;
		    if((temp = in.readObject()) != null) {
                if (temp instanceof Human) {
                    currPlayer = (Human) temp;

                } else if (temp instanceof Board) {
                    board = (Board) in.readObject();
                    board.repaint();

                } else if (temp.getClass().isArray()) {
                    System.out.println("Game ended"); //TODO Print results
                    Thread.sleep(2000);
                    isGameActive = false;
                }
            }
		}
	}

    public void closeStreams() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    class MyMouseAdapter extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e){
			if(currPlayer == null) {
                return;
            }
            if(x1 == -1 && y1 == -1) {
                    x1 = e.getX();
                    y1 = e.getY();

                    if(!(isPawn(x1, y1))) {
                        x1 = y1 = -1;
                    }
                System.out.println("Pawn marked");//paint x1, y1
                } else {
                    try {
                        x2 = e.getX();
                        y2 = e.getY();
                        int[] coordinates = {1, x1, y1, x2, y2}; //1 is a number of the game, it will be extended for many clients
                        out.writeObject(coordinates);
                        x1 = x2 = y1 = y2 = -1;
                        currPlayer = null;
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
		}


    private boolean isPawn(int x, int y) {

        for(int i = 0; i < currPlayer.pawns.length; ++i) {
            if(x == currPlayer.pawns[i].getX() && y == currPlayer.pawns[i].getY()) {
                return true;
            }
        }

        return false;
    }
}
