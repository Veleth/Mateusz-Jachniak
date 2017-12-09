package chineseMateusz;

import chineseMateusz.Pawn.PlayerColor;

import java.io.Serializable;

public class Bot extends Player implements Serializable{

    private static final long serialVersionUID = 7143836408201513677L;

    public Bot (String name, PlayerColor playerColor){
		super(name, playerColor);
		setHuman(false);	
	}

	protected void move(){
		//TODO: A lot of work

        /*
        what should be done:
        check possible moves for this player - if empty dont continue whats below
        take one of destination fields which isnt occupied by bot's pawn
        compare the moves - which distance to the field will be the closest after the move
        make this move
         */
	}
}
