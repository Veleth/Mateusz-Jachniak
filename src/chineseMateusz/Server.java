package chineseMateusz;

import java.util.ArrayList;

import chineseMateuszExceptions.BadCoordinateException;

public class Server {

	public void placePawns (Player p, boolean up, int x, int y) throws BadCoordinateException {
		
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
		
		p.pawns = new Pawn[10]; 
		
		for(int i = 0; i < pawnsPositions.length; ++i) {
			p.pawns[i] = new Pawn(p.getPlayerColor(), pawnsPositions[i][0], pawnsPositions[i][1]);
		}
	}
	
	private void setEndCoordinates(Player p, boolean up, int x, int y) {
		p.setEndCoordinates(new ArrayList<>());
		
		int endPositions[][];
		
		if(up) {
			int upPositions[][] = {{x,y},{x-1,y+1},{x+1,y+1},{x-2,y+2},{x,y+2},{x+2,y+2},{x-3,y+3},{x-1,y+3},{x+1,y+3},{x+3,y+3}}; 
			endPositions = upPositions;
		} else {
			int downPositions[][] = {{x,y},{x-1,y-1},{x+1,y-1},{x-2,y-2},{x,y-2},{x+2,y-2},{x-3,y-3},{x-1,y-3},{x+1,y-3},{x+3,y-3}}; 
			endPositions = downPositions;
		}
		
		for(int i = 0; i < endPositions.length; ++i) {
			p.getEndCoordinates().add(endPositions[i]);
		}
	}
	
	private void setPlayerStartEnd (Player p) {
		try {
			switch(p.getPlayerColor()) {
			case BLUE:
				placePawns(p, true, 12, 0);
				setEndCoordinates(p, false, 12, 16);
				break;
			case RED:
				placePawns(p, false, 12, 16);
				setEndCoordinates(p, true, 12, 0);
				break;
			case GREEN:
				placePawns(p, true, 4, 9);
				setEndCoordinates(p, false, 21, 7);
				break;
			case YELLOW:
				placePawns(p, false, 21, 7);
				setEndCoordinates(p, true, 4, 9);
				break;
			case ORANGE:
				placePawns(p, true, 21, 9);
				setEndCoordinates(p, false, 4, 7);
				break;
			case PINK:
				placePawns(p, false, 4, 7);
				setEndCoordinates(p, true, 21, 9);	
			}
		} catch (BadCoordinateException e) {
			
		}
	}
	
	protected boolean checkFinished (Player p) {
		 ArrayList<int[]> tempEndCoord = p.getEndCoordinates();
		 
		 for(int i = 0; i < p.pawns.length; ++i) {
			 int coordinates[] = new int[2];
			 coordinates[0] = p.pawns[i].getX();
			 coordinates[1] = p.pawns[i].getY();
			 
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

