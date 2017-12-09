package chineseMateusz;

import chineseMateusz.Pawn.PlayerColor;

import java.io.Serializable;

public class Human extends Player implements Serializable{

    private static final long serialVersionUID = 7088028988849123679L;

    public Human(String name, PlayerColor playerColor) {
		super(name, playerColor);
		setHuman(true);
	}
}
