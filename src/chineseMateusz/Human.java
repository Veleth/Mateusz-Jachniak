package chineseMateusz;

import chineseMateusz.Pawn.PlayerColor;

public class Human extends Player {

	public Human(String name, PlayerColor playerColor) {
		super(name, playerColor);
		setHuman(true);
	}
}
