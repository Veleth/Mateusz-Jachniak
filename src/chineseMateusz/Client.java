package chineseMateusz;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observer;

import javax.swing.*;


import static java.lang.System.exit;

public class Client extends JFrame {

    private int x1, y1, x2, y2;
	private Board board;

	private Socket socket = null;
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;

    public Client() throws IOException, ClassNotFoundException {
	    super("Chinese Checkers Client");
        addMouseListener(new MyMouseAdapter());
        listenSocket();
        initialize();
        setGUI();
    }

    public void setGUI() {
	    add(board);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    setLocation(100,100);
	    setSize(780,600);
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
            /*int readValue = (int) in.readObject(); //1st message received, if -1 new game created, no - added to created prev gm
            if(readValue == -1) {
                int players = amountOfPlayers();
                int humans = amountOfHumans(players);
                int amounts[] = {players, humans}; //TODO ogarnij to wysylanie ilosci graczy
                out.writeObject(amounts);
            }
*/

            Object temp = in.readObject();

            if(temp instanceof Board) {
                board = (Board) temp;
                for(int i=0; i<board.board[0].length; ++i) {
                    for(int j=0; j<board.board.length; ++j) {
                        if(board.board[j][i] == Board.Fields.BUSY) {
                            System.out.print("X");
                        }
                        else {
                            System.out.print(" ");
                        }
                    }
                    System.out.println();
                }

            } else {
                throw new ClassNotFoundException();
            }

        } catch (NumberFormatException e) {
            exit(-1);
        }
    }

    public void play() {
        while(true) {
//odbieranie komunikatow
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
        String[] values = new String[players];

        for(int i = 1; i <= players; ++i) {
            values[i-1] = i + "";
        }

        Object selected = JOptionPane.showInputDialog(null, "How many humans?", "Selection", JOptionPane.DEFAULT_OPTION, null, values, "1");
        if ( selected == null ) {
            exit(-1);
        }

        return Integer.parseInt(selected.toString());
    }

    public void closeStreams() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    class MyMouseAdapter extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
                int scaleY = 476 / board.board[0].length;
                int scaleX = 700 / board.board.length;

                if (x1 == -1 && y1 == -1) {
                    x1 = (e.getX() - 37) / scaleX;
                    y1 = (e.getY() - 32 - scaleY) / scaleY;

                } else {
                    try {
                        x2 = (e.getX() - 37) / scaleX;
                        y2 = (e.getY() - 32 - scaleY) / scaleY;

                        int[] coordinates = {x1, y1, x2, y2};
                        out.writeObject(coordinates);
                        x1 = x2 = y1 = y2 = -1;
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }

}
