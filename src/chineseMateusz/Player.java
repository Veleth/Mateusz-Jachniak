package chineseMateusz;

import java.io.Serializable;
import java.util.ArrayList;

import chineseMateusz.Pawn.PlayerColor;
import chineseMateuszExceptions.BadCoordinateException;

public abstract class Player implements Serializable{
    private static final long serialVersionUID = -7335349467728601104L;

    private String name;
	private PlayerColor playerColor;
	private boolean isHuman;
	private boolean hasFinished;
	private int place;

	protected Pawn[] pawns;
	private ArrayList<int[]> endCoordinates;
	
	public Player(String name, PlayerColor playerColor) {
		this.name = name;
		this.playerColor = playerColor;
		hasFinished = false;
		place = -1;
	}
	
	public boolean hasFinished() {
		return hasFinished;
	}

	public void setFinished(boolean hasFinished) {
		this.hasFinished = hasFinished;
	}

	public int getPlace() {
		return place;
	}

	public void setPlace(int place) {
		this.place = place;
	}

	public String getName() {
		return name;
	}

	public void setHuman(boolean isHuman) {
		this.isHuman = isHuman;
	}
	
	public boolean isHuman() {
		return isHuman;
	}  

	public PlayerColor getPlayerColor() {
		return playerColor;
	}

	public ArrayList<int[]> getEndCoordinates() {
		return endCoordinates;
	}

	public void setEndCoordinates(ArrayList<int[]> endCoordinates) {
		this.endCoordinates = endCoordinates;
	}
	
}
