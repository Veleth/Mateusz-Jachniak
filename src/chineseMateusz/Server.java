package chineseMateusz;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import chineseMateuszExceptions.BadCoordinateException;
import chineseMateuszExceptions.InvalidNumberOfHumansException;
import chineseMateuszExceptions.InvalidNumberOfPlayersException;

import static java.lang.System.exit;

public class Server {

private ArrayList<Game> games;
private int counter = 0;
private ServerSocket server = null;
private Socket client = null;
private ObjectInputStream in = null;
private ObjectOutputStream out = null;

    public Server() throws IOException {
        server = new ServerSocket(21372);
        System.out.println("Server works!");
        games = new ArrayList<>();
    }

    public void listenSocket() throws IOException {

        client = server.accept();
        out = new ObjectOutputStream(client.getOutputStream());
        in = new ObjectInputStream(client.getInputStream());
    }

    public void clientServerCommunication() throws IOException, ClassNotFoundException, InvalidNumberOfPlayersException, InvalidNumberOfHumansException {
        Object temp;
        int tempArr[];
        int tempInt;

        while(!allGamesFinished()) {
            temp = in.readObject();

            if (temp.getClass().isArray()) {
                tempArr = (int[]) temp;

                if(tempArr.length == 2) {

                    if(counter != 0) {
                        exit(-1); //TODO it will be changed if there will be many games at the same time possible
                    }
                    games.add(new Game(tempArr[0], tempArr[1]));
                    out.writeObject(counter);
                    out.writeObject(games.get(counter).board);
                    counter++;
                }
                else if(tempArr.length == 5) {
                    Game g = games.get(tempArr[0]);
                    int x1 = tempArr[1];
                    int y1 = tempArr[2];
                    int x2 = tempArr[3];
                    int y2 = tempArr[4];
                    boolean moved = false;
                    for(Player p: g.players) {
                        for(Pawn pw: p.pawns) {
                            if(x1 == pw.getX() && y1 == pw.getY()) {
                                pw.setPos(x2, y2);
                                g.board.updateBoard();
                                out.writeObject(g.board);
                                moved = true;
                                break;
                            }
                        }
                        if(moved) {
                            break;
                        }
                    }
                }
            }
            else if(temp instanceof Number){
                tempInt = (int) temp;
                runGame(games.get(tempInt));
            }
        }

        closeStreams();
        exit(0);
    }

    private void runGame(Game g) {
        int index = (int) (Math.random() * g.players.length);
        do {
            for(int i = index; i < index + g.players.length; ++i) {
                index = index % g.players.length;

                if(!(g.players[index].hasFinished())) {
                    ArrayList<int[]> moves = g.checkPossibleMoves(g.players[index]);
                    if(moves.isEmpty()) {
                        //wyslij do klienta info ze ruchu nie ma
                        continue;
                    }
                    //metoda na ruch w serwerze (wysylanie do klienta aktywnego gracza i czekanie na jego ruchy)
                    g.board.updateBoard();

                    if (g.checkFinished(g.players[index])) {
                        g.players[index].setPlace(g.availablePlace);
                        g.availablePlace++;
                        g.players[index].setFinished(true);
                    }
                }
            }
        } while(!g.gameFinished());
    }

    public void closeStreams() throws IOException {

        in.close();
        out.close();
        client.close();
        server.close();
        System.out.println("Server ends work!");
    }

    private boolean allGamesFinished() {
        if(games.isEmpty()) {
            return false;
        }
        for(Game g : games) {
            if(!g.gameFinished()) {
                return false;
            }
        }
        return true;
    }
}

