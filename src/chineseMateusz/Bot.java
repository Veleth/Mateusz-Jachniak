package chineseMateusz;

import chineseMateusz.Pawn.PlayerColor;

import java.io.Serializable;
import java.util.ArrayList;

public class Bot extends Player implements Serializable{

    private static final long serialVersionUID = 7143836408201513677L;

    public Bot (PlayerColor playerColor){
		super(playerColor);
		setHuman(false);	
	}

	protected int[] move(ArrayList<int[]> possibleMoves){
		//TODO: A lot of work
        return null;
        /*
        what should be done:
        check possible moves for this player - if empty dont continue whats below
        take one of destination fields which isnt occupied by bot's pawn
        compare the moves - which distance to the field will be the closest after the move
        make this move
         */
	}
}
