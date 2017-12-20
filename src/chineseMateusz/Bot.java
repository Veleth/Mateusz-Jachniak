package chineseMateusz;

import chineseMateusz.Pawn.PlayerColor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Bot extends Player {

    int[] target;

    public Bot (PlayerColor playerColor){
    	super(playerColor);
		setHuman(false);
	}

    public int[] move(ArrayList<int[]> possibleMoves){
        Random rand = new Random();

        ArrayList<int[]> bestMoves = new ArrayList<>();

        double maxDist = findDist(possibleMoves.get(0), target);

        for(int[] move: possibleMoves) {
            if ((findDist(move, target) == maxDist)){
                bestMoves.add(move);
            }
            else if(findDist(move, target) > maxDist){
                maxDist = findDist(move, target);
                bestMoves.clear();
                bestMoves.add(move);
            }
        }
        if (bestMoves.isEmpty()) {
            return null;
        }

        int r = rand.nextInt(bestMoves.size());
        return bestMoves.get(r);
//TODO ogarnac idiotyzm bota
    }

    public void setTarget() {

        int[] beginField = {pawns[0].getX(), pawns[0].getY()};
        double targetDist = 0;

        for (int[] endCoord: getEndCoordinates()) {
            if(findTarget(beginField, endCoord) > targetDist) {
                targetDist = findTarget(beginField, endCoord);
                target = endCoord;
            }
        }
    }

    private double findTarget(int[] pawn, int[] endC) {
        return Math.sqrt(Math.pow((pawn[0]-endC[0]), 2)+Math.pow((pawn[1]-endC[1]), 2));
    }

    private double findDist(int[] startC, int[] endC){ //todo to improve
        int dx = Math.abs(startC[0]-endC[0]);
        int dy = Math.abs(startC[1]-endC[1]);
        int dxt = Math.abs(startC[2]-endC[0]);
        int dyt = Math.abs(startC[3]-endC[1]);
        if (dx > dy){
            dx *= 2;
            dxt *= 2;
        }
        else{
            dy *= 2;
            dyt *= 2;
        }
        return (Math.sqrt(Math.pow((dx), 2)+Math.pow(dy, 2)) - Math.sqrt(Math.pow((dxt), 2)+Math.pow(dyt, 2)));
    }

    public int[] getTarget(){
        return target;
    }
}
