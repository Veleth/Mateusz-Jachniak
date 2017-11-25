package chineseMateusz;

import java.util.ArrayList;

import chineseMateusz.Pawn.PlayerColor;
import chineseMateuszExceptions.BadCoordinateException;

public abstract class Player {
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
		setPlayerStartEnd(playerColor);	
	}
	
	abstract protected void move();
	
	private void placePawns(boolean up, int x, int y) throws BadCoordinateException {
		
		int startPositions[][] = {{12,0,1},{4,7,0},{4,9,1},{12,16,0},{21,7,0},{21,9,1}};
		boolean goodCoordinate = false;
		
		for(int i = 0; i < startPositions.length; ++i) {
			if(x == startPositions[i][0] && y == startPositions[i][1] && ((up)?(1):(0)) == startPositions[i][2]) {
				goodCoordinate = true;
				break;
			}
		}
		
		if(!(goodCoordinate)) {
			throw new BadCoordinateException(x, y);
		}
		
		int pawnsPositions[][];
		
		if(up) {
			int upPositions[][] = {{x,y},{x-1,y+1},{x+1,y+1},{x-2,y+2},{x,y+2},{x+2,y+2},{x-3,y+3},{x-1,y+3},{x+1,y+3},{x+3,y+3}}; 
			pawnsPositions = upPositions;
		} else {
			int downPositions[][] = {{x,y},{x-1,y-1},{x+1,y-1},{x-2,y-2},{x,y-2},{x+2,y-2},{x-3,y-3},{x-1,y-3},{x+1,y-3},{x+3,y-3}}; 
			pawnsPositions = downPositions;
		}
		
		pawns = new Pawn[10]; 
		
		for(int i = 0; i < pawnsPositions.length; ++i) {
			pawns[i] = new Pawn(playerColor, pawnsPositions[i][0], pawnsPositions[i][1]);
		}
	}
	
	private void setEndCoordinates(boolean up, int x, int y) {
		endCoordinates = new ArrayList<>();
		
		int endPositions[][];
		
		if(up) {
			int upPositions[][] = {{x,y},{x-1,y+1},{x+1,y+1},{x-2,y+2},{x,y+2},{x+2,y+2},{x-3,y+3},{x-1,y+3},{x+1,y+3},{x+3,y+3}}; 
			endPositions = upPositions;
		} else {
			int downPositions[][] = {{x,y},{x-1,y-1},{x+1,y-1},{x-2,y-2},{x,y-2},{x+2,y-2},{x-3,y-3},{x-1,y-3},{x+1,y-3},{x+3,y-3}}; 
			endPositions = downPositions;
		}
		
		for(int i = 0; i < endPositions.length; ++i) {
			endCoordinates.add(endPositions[i]);
		}
	}
	
	private void setPlayerStartEnd(PlayerColor playerColor) {
		try {
			switch(playerColor) {
			case BLUE:
				placePawns(true, 12, 0);
				setEndCoordinates(false, 12, 16);
				break;
			case RED:
				placePawns(false, 12, 16);
				setEndCoordinates(true, 12, 0);
				break;
			case GREEN:
				placePawns(true, 4, 9);
				setEndCoordinates(false, 21, 7);
				break;
			case YELLOW:
				placePawns(false, 21, 7);
				setEndCoordinates(true, 4, 9);
				break;
			case ORANGE:
				placePawns(true, 21, 9);
				setEndCoordinates(false, 4, 7);
				break;
			case PINK:
				placePawns(false, 4, 7);
				setEndCoordinates(true, 21, 9);	
			}
		} catch (BadCoordinateException e) {
			
		}
	}
	
	protected boolean checkFinished() {
		 ArrayList<int[]> tempEndCoord = endCoordinates;
		 
		 for(int i = 0; i < pawns.length; ++i) {
			 int coordinates[] = new int[2];
			 coordinates[0] = pawns[i].getX();
			 coordinates[1] = pawns[i].getY();
			 
			 for(int j = 0; j < tempEndCoord.size(); ++j) {
				 if(tempEndCoord.get(i)[0] == coordinates[0] && tempEndCoord.get(i)[1] == coordinates[1]) {
					 tempEndCoord.remove(i);
					 break;
				 }
			 }
		 }
		 
		 return tempEndCoord.isEmpty();	 
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
	
}
