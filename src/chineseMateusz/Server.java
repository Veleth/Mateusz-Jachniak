package chineseMateusz;

import chineseMateuszExceptions.BadCoordinateException;
import chineseMateuszExceptions.GameException;
import chineseMateuszExceptions.InvalidNumberOfHumansException;
import chineseMateuszExceptions.InvalidNumberOfPlayersException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {

    private Game game;
    private ServerSocket server;

    public Server() throws IOException {
        server = new ServerSocket(21372);
        System.out.println("Server works!");
    }

    public void listenSocket() throws IOException, ClassNotFoundException, InvalidNumberOfHumansException, InvalidNumberOfPlayersException, InterruptedException, GameException, BadCoordinateException {


            Socket s = server.accept();
            setFirstPlayer(s);

            for(int i = 1; i < game.humansAmount; ++i) {
                Socket s1 = server.accept();
                setAnotherPlayers(i, s1);
            }

            for(int i = game.humansAmount; i < game.players.length; ++i) {
                game.players[i] = PlayersFactory.getInstance().createBot(game.getPColor(game.players.length, i));
            }

            HashMap<Pawn[], Pawn.PlayerColor> boardMap = new HashMap<>();
            initBoardMap(boardMap);
            game.board = new Board(boardMap);
            game.board.updateBoard();

            for(int i = 0; i < game.humansAmount; ++i) {
                ((Human) game.players[i]).sendBoardToClient(game.getBoard());
            }

            for(int i = 0; i < game.humansAmount; ++i) {
                Thread t = new Thread((Human) game.players[i]);
                t.start();
            }

            game.playGame();

    }

    private void initBoardMap(HashMap<Pawn[], Pawn.PlayerColor> boardMap) {

        for(Player p : game.players) {
             boardMap.put(p.pawns, p.getPlayerColor());
        }
    }

    public void closeStreams() throws IOException {
        server.close();
        System.out.println("Server ends work!");
    }

    private void setFirstPlayer(Socket s) throws IOException, InvalidNumberOfHumansException, InvalidNumberOfPlayersException, ClassNotFoundException, GameException, BadCoordinateException {


        if(game == null) {
            game = new Game(6, 2); // TODO na razie na sztywno
            game.players[0] = PlayersFactory.getInstance().createHuman(game, s, game.getPColor(game.players.length,0));

        } else {
            throw new GameException();
        }

    }

    private void setAnotherPlayers(int i, Socket s) throws IOException, GameException, BadCoordinateException {

//TODO przekazuj info jak w 1 playerze
        if(game == null) {
            throw new GameException();
        }

        game.players[i] = PlayersFactory.getInstance().createHuman(game, s, game.getPColor(game.players.length, i));
    }
}

