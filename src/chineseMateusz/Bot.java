package chineseMateusz;

import chineseMateusz.Pawn.PlayerColor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Bot extends Player implements Serializable{

    private static final long serialVersionUID = 7143836408201513677L;
    int[] target;

    public Bot (PlayerColor playerColor){
    	super(playerColor);
		setHuman(false);
	}

	public int[] move(ArrayList<int[]> possibleMoves){
		Random rand = new Random();
        double maxdist = 0;
        ArrayList<int[]> bestMoves = new ArrayList<>();
        for(int[] move: possibleMoves){
        	if ((findDist(move, target) == maxdist)){
        		bestMoves.add(move);
        	}
        	else if(findDist(move, target) > maxdist){
        		maxdist = findDist(move, target);
        		bestMoves.clear();
        		bestMoves.add(move);
        	}
        }
        if (bestMoves.isEmpty() || !(maxdist >= 0)){
        	return null;
        }
        else {
        	int r = rand.nextInt(bestMoves.size());
        	return bestMoves.get(r);
        }
	}
	
	private double findDist(int[] startC, int[] endC){
		return (Math.sqrt(Math.pow((startC[0]-endC[0]), 2)+Math.pow((startC[1]-endC[1]), 2)) - Math.sqrt(Math.pow((startC[2]-endC[0]), 2)+Math.pow((startC[3]-endC[1]), 2)));
	}
	
	public int[] getTarget(){
		return target;
	}
}
