package chineseMateusz;

import chineseMateuszExceptions.BadCoordinateException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class PlayersFactory {

    private static PlayersFactory playersFactory;

    private PlayersFactory() {
    }

    public Human createHuman(Game g, ObjectInputStream in, ObjectOutputStream out, Pawn.PlayerColor playerColor) throws IOException, BadCoordinateException {
        Human human = new Human(g, in, out, playerColor);
        setPlayerStartEnd(human);
        return human;
    }

    public Bot createBot(Pawn.PlayerColor playerColor) throws BadCoordinateException {
        Bot bot = new Bot(playerColor);
        setPlayerStartEnd(bot);
        bot.setTarget();
        return bot;
    }

    public static PlayersFactory getInstance() {

        if(playersFactory == null) {
            playersFactory = new PlayersFactory();
        }

        return playersFactory;
    }

    private void placePawns (Player p, boolean up, int x, int y) throws BadCoordinateException {

        int startPositions[][] = {{12,0,1},{3,7,0},{3,9,1},{12,16,0},{21,7,0},{21,9,1}};
        boolean goodCoordinate = false;

        for(int i = 0; i < startPositions.length; ++i) {
            if(x == startPositions[i][0] && y == startPositions[i][1] && ((up)?(1):(0)) == startPositions[i][2]) {
                goodCoordinate = true;
                break;
            }
        }

        if(!(goodCoordinate)) {
            throw new BadCoordinateException(x, y);
        }

        int pawnsPositions[][];

        if(up) {
            int upPositions[][] = {{x,y},{x-1,y+1},{x+1,y+1},{x-2,y+2},{x,y+2},{x+2,y+2},{x-3,y+3},{x-1,y+3},{x+1,y+3},{x+3,y+3}};
            pawnsPositions = upPositions;
        } else {
            int downPositions[][] = {{x,y},{x-1,y-1},{x+1,y-1},{x-2,y-2},{x,y-2},{x+2,y-2},{x-3,y-3},{x-1,y-3},{x+1,y-3},{x+3,y-3}};
            pawnsPositions = downPositions;
        }

        p.pawns = new Pawn[pawnsPositions.length];

        for(int i = 0; i < pawnsPositions.length; ++i) {
            p.pawns[i] = new Pawn(p.getPlayerColor(), pawnsPositions[i][0], pawnsPositions[i][1]);
        }
    }

    private void setEndCoordinates(Player p, boolean up, int x, int y) {
        ArrayList<int[]> coordinates = new ArrayList<>();

        int endPositions[][];

        if(up) {
            int upPositions[][] = {{x,y},{x-1,y+1},{x+1,y+1},{x-2,y+2},{x,y+2},{x+2,y+2},{x-3,y+3},{x-1,y+3},{x+1,y+3},{x+3,y+3}};
            endPositions = upPositions;
        } else {
            int downPositions[][] = {{x,y},{x-1,y-1},{x+1,y-1},{x-2,y-2},{x,y-2},{x+2,y-2},{x-3,y-3},{x-1,y-3},{x+1,y-3},{x+3,y-3}};
            endPositions = downPositions;
        }

        for(int i = 0; i < endPositions.length; ++i) {
            coordinates.add(endPositions[i]);
        }

        p.setEndCoordinates(coordinates);
    }

    private void setPlayerStartEnd (Player p) throws BadCoordinateException {

        switch(p.getPlayerColor()) {
            case BLUE:
                placePawns(p, true, 12, 0);
                setEndCoordinates(p, false, 12, 16);
                break;
            case RED:
                placePawns(p, false, 12, 16);
                setEndCoordinates(p, true, 12, 0);
                break;
            case GREEN:
                placePawns(p, true, 3, 9);
                setEndCoordinates(p, false, 21, 7);
                break;
            case YELLOW:
                placePawns(p, false, 21, 7);
                setEndCoordinates(p, true, 3, 9);
                break;
            case ORANGE:
                placePawns(p, true, 21, 9);
                setEndCoordinates(p, false, 3, 7);
                break;
            case PINK:
                placePawns(p, false, 3, 7);
                setEndCoordinates(p, true, 21, 9);
        }
    }

}
