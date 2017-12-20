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
        double maxdist = -10;
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
        if (bestMoves.isEmpty()){
        	return null;
        }
        else {
        	int r = rand.nextInt(bestMoves.size());
        	return bestMoves.get(r);
        }
	}
	
	private double findDist(int[] startC, int[] endC){
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
