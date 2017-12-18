package chineseMateusz;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;

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

    private void setGUI() {
	    add(board);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    setLocation(100,100);
	    setSize(780,600);
        setResizable(false);
        setVisible(true);
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
            boolean readValue = (boolean) in.readObject(); //1st message received, if -1 new game created, no - added to created prev gm
            if(readValue) {
                int players = amountOfPlayers();
                int humans = amountOfHumans(players);
                int amounts[] = {players, humans};
                out.writeObject(amounts);
            }


            Object temp = in.readObject();

            if(temp instanceof Board) {
                board = (Board) temp;

            } else {
                throw new ClassNotFoundException();
            }

        } catch (NumberFormatException e) {
            exit(-1);
        }
    }

    public void play() throws IOException, ClassNotFoundException, InterruptedException {

        while(true) {

            Object o = in.readObject();

            if(o.getClass().isArray()) {
                int[] move = (int[]) o;

                if(move.length == 4) {
                    boolean updated = false;

                    for (Map.Entry<Pawn[], Pawn.PlayerColor> map : board.pawns.entrySet()) {
                        for(Pawn p : map.getKey()) {
                            if(move[0] == p.getX() && move[1] == p.getY()) {
                                p.setPos(move[2], move[3]);
                                board.updateBoard();
                                updated = true;
                                break;
                            }
                        }
                        if(updated) {
                            board.repaint();
                            break;
                        }
                    }
                } else {
                     throw new IOException();
                }
            } else if (o instanceof String) {
                String s = (String) o;

                if(s.startsWith("ABORTED GAME")) {
                    JOptionPane.showMessageDialog(null, "Game has been aborted by one of the humans..\nLet's find him and kill him!", "Game result", JOptionPane.ERROR_MESSAGE, null);
                    exit(0);
                } else if(s.startsWith("GAME END")) {
                    JOptionPane.showMessageDialog(null, s, "Game result", JOptionPane.PLAIN_MESSAGE, null);
                    exit(0);
                } else if(s.startsWith("MOVE NOT POSSIBLE")) {
                    JOptionPane.showMessageDialog(null, s, "Game info", JOptionPane.INFORMATION_MESSAGE, null);
                }
            } else if(o instanceof Pawn.PlayerColor) {
                board.setCurrentColor((Pawn.PlayerColor) o);
                board.repaint();
            }
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
                int scaleY = 476 / board.getBoard()[0].length;
                int scaleX = 700 / board.getBoard().length;

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
