package chineseMateusz;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import chineseMateuszExceptions.BadCoordinateException;
import chineseMateuszExceptions.GameAlreadyInitedException;
import chineseMateuszExceptions.InvalidNumberOfHumansException;
import chineseMateuszExceptions.InvalidNumberOfPlayersException;

import static java.lang.System.exit;

public class Server {

    private Game game;
    private ServerSocket server;

    public Server() throws IOException {
        server = new ServerSocket(21372);
        System.out.println("Server works!");
    }

    public void listenSocket() throws IOException, ClassNotFoundException, InvalidNumberOfHumansException, InvalidNumberOfPlayersException, GameAlreadyInitedException {
        while(true) {
            Socket s = server.accept();
            setFirstPlayer(s);

            for(int i = 1; i < game.humansAmount; ++i) {
                game.players[i] = PlayersFactory.getInstance().createHuman(game, server.accept(), game.getPColor(game.players.length, i));
            }
            for(int i = game.humansAmount; i < game.players.length; ++i) {
                game.players[i] = PlayersFactory.getInstance().createBot(game.getPColor(game.players.length, i));
            }
            //serwer podlacza wszystkich ludzi
            // zaczyna startowac watski, w watku na poczatku getBoard()

        }
    }

    public void closeStreams() throws IOException {
        server.close();
        System.out.println("Server ends work!");
    }

    public void setFirstPlayer(Socket s) throws IOException, InvalidNumberOfHumansException, InvalidNumberOfPlayersException, ClassNotFoundException, GameAlreadyInitedException {
        ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(s.getInputStream());

        if(game == null) {
            out.writeObject(-1);
            Object o = in.readObject();
            int[] temp = (int[]) o;
            game = new Game(temp[0], temp[1]);
            game.players[0] = PlayersFactory.getInstance().createHuman(game, s, game.getPColor(game.players.length,0));
        } else {
            throw new GameAlreadyInitedException();
        }

        out.close();
        in.close();
    }
}

