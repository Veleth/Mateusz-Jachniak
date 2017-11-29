package chineseMateusz;

import chineseMateusz.Pawn.PlayerColor;

public class Bot extends Player {
	
	public Bot (String name, PlayerColor playerColor){
		super(name, playerColor);
		setHuman(false);	
	}
	
	@Override
	protected void move(){
		//TODO: A lot of work
	}
}
