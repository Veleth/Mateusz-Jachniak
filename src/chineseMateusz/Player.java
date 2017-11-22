package chineseMateusz;

import java.util.ArrayList;

public abstract class Player {
	protected String name;
	protected Color color;
	protected boolean isHuman;
	protected boolean hasFinished;
	protected int place;
	
	protected Pawn[] pawns;
	protected ArrayList<Integer[]> endCoordinates;
	
	abstract protected void move();
	
	protected void placePawns(boolean up, int x, int y) {
		int positions[][];
		
		if(up) {
			int upPositions[][] = {{x,y},{x-1,y+1}}; // these coordinates will be added
			positions = upPositions;
		} else {
			int upPositions[][] = {{x,y},{x-1,y+1}}; //these coordinates will be added
			positions = upPositions;
		}
	}
	
	protected boolean checkFinished() {
		 boolean finish = true;
		 
		 ArrayList<Integer[]> tempEndCoord = endCoordinates;
		 
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

	
}
