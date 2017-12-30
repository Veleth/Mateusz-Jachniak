package main.java.chineseMateusz;

import main.java.chineseMateusz.Pawn.PlayerColor;

import java.util.ArrayList;

public abstract class Player {

	private PlayerColor playerColor;
	private boolean isHuman;
	private boolean hasFinished;
	private int place;
	protected Pawn[] pawns;
	
	private ArrayList<int[]> endCoordinates;
	
	public Player(PlayerColor playerColor) {
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
	
	public Pawn getPawn(int x){
		return this.pawns[x];
	}
	
	public Pawn[] getPawns(){
		return this.pawns;
	}
}
