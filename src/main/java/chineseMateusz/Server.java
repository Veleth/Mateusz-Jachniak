package main.java.chineseMateusz;

import main.java.chineseMateuszExceptions.BadCoordinateException;
import main.java.chineseMateuszExceptions.GameException;
import main.java.chineseMateuszExceptions.InvalidNumberOfHumansException;
import main.java.chineseMateuszExceptions.InvalidNumberOfPlayersException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {

    private Game game;
    private ServerSocket server;

    public Server(int i) throws IOException {
        server = new ServerSocket(i);
        System.out.println("Server works!");
    }

    public void gameHandling() throws IOException, ClassNotFoundException, InvalidNumberOfHumansException, InvalidNumberOfPlayersException, InterruptedException, GameException, BadCoordinateException {

            while(true) {
                Socket s = server.accept();
                setFirstPlayer(s);

                for (int i = 1; i < game.humansAmount; ++i) {
                    Socket s1 = server.accept();
                    setAnotherPlayers(i, s1);
                }

                for (int i = game.humansAmount; i < game.getNoOfPlayers(); ++i) {
                    game.setPlayer(PlayersFactory.getInstance().createBot(game.getPColor(game.getNoOfPlayers(), i)), i);
                }

                HashMap<Pawn[], Pawn.PlayerColor> boardMap = new HashMap<>();
                initBoardMap(boardMap);

                game.board = new Board(boardMap);
                game.board.updateBoard();

                for (int i = 0; i < game.humansAmount; ++i) {
                    ((Human) game.getPlayers()[i]).sendBoardToClient(game.getBoard());
                }

                for (int i = 0; i < game.humansAmount; ++i) {
                    Thread t = new Thread((Human) game.getPlayers()[i]);
                    t.start();
                }

                game.playGame();
                game = null;
            }

    }

    public void initBoardMap(HashMap<Pawn[], Pawn.PlayerColor> boardMap) {

        for(Player p : game.getPlayers()) {
             boardMap.put(p.pawns, p.getPlayerColor());
        }
    }

    public void closeStreams() throws IOException {
        server.close();
        System.out.println("Server ends work!");
    }

    public void setFirstPlayer(Socket s) throws IOException, InvalidNumberOfHumansException, InvalidNumberOfPlayersException, ClassNotFoundException, GameException, BadCoordinateException {

        ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(s.getInputStream());

        out.writeObject(true);
        int[] gameDetails = (int[]) in.readObject();

        if(game == null) {
            game = new Game(gameDetails[0], gameDetails[1]);
            game.setPlayer(PlayersFactory.getInstance().createHuman(game, in, out, game.getPColor(game.getNoOfPlayers(),0)), 0);

        } else {
            throw new GameException();
        }

    }

    public void setAnotherPlayers(int i, Socket s) throws IOException, GameException, BadCoordinateException {

        ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(s.getInputStream());

        out.writeObject(false);

        if(game == null) {
            throw new GameException();
        }

        game.setPlayer(PlayersFactory.getInstance().createHuman(game, in, out, game.getPColor(game.getNoOfPlayers(), i)), i);
    }

    public ServerSocket getServer() {
        return server;
    }

    public Game getGame() {
        return game;
    }

}

