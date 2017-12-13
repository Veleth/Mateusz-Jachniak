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
import chineseMateuszExceptions.GameException;
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

    public void listenSocket() throws IOException, ClassNotFoundException, InvalidNumberOfHumansException, InvalidNumberOfPlayersException, InterruptedException, GameException {
        while(true) {
            Socket s = server.accept();
            setFirstPlayer(s);

            for(int i = 1; i < game.humansAmount; ++i) {
                Socket s1 = server.accept();
                setAnotherPlayers(i, s1);
                System.out.println("sda");
            }

            for(int i = game.humansAmount; i < game.players.length; ++i) {
                game.players[i] = PlayersFactory.getInstance().createBot(game.getPColor(game.players.length, i));
            }

            for(int i = 0; i < game.humansAmount; ++i) {
                Thread t = new Thread((Human) game.players[i]);
                t.start();
            }

            game.playGame();

        }
    }

    public void closeStreams() throws IOException {
        server.close();
        System.out.println("Server ends work!");
    }

    public void setFirstPlayer(Socket s) throws IOException, InvalidNumberOfHumansException, InvalidNumberOfPlayersException, ClassNotFoundException, GameException {


        if(game == null) {
            game = new Game(4, 2); // na razie na sztywno
            game.players[0] = PlayersFactory.getInstance().createHuman(game, s, game.getPColor(game.players.length,0));

        } else {
            throw new GameException();
        }

    }

    public void setAnotherPlayers(int i, Socket s) throws IOException, GameException {


        if(game == null) {
            throw new GameException();
        }

        game.players[i] = PlayersFactory.getInstance().createHuman(game, s, game.getPColor(game.players.length, i));
    }
}

