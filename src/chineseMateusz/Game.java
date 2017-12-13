package chineseMateusz;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observer;

import javax.swing.JFrame;

import chineseMateusz.Board.Fields;
import chineseMateusz.Pawn.PlayerColor;
import chineseMateuszExceptions.BadCoordinateException;
import chineseMateuszExceptions.InvalidNumberOfHumansException;
import chineseMateuszExceptions.InvalidNumberOfPlayersException;

public class Game {

    protected Player[] players;
    int humansAmount;
    protected Board board;
    int availablePlace;
    int[] currentMove;

    public Game(int x, int humans) throws InvalidNumberOfPlayersException, InvalidNumberOfHumansException{
		if (humans < 1 || humans > x) {
			throw new InvalidNumberOfHumansException(humans);
		}
		switch (x) {
			case 2:
			case 3:
			case 4: 
			case 6:
            humansAmount = humans;
			createGame(x, humansAmount);
			break;
			default: throw new InvalidNumberOfPlayersException(x);
		}
			
	}
	
	private void createGame(int x, int humans) {
		players = new Player[x];
		humansAmount = humans;
		board = new Board(players);
		board.updateBoard();
		availablePlace = 1;
	}

    public void playGame() throws InterruptedException, IOException, ClassNotFoundException {
        int index = (int) (Math.random() * players.length);
        do {
            for(int i = index; i < index + players.length; ++i) {
                i = i % players.length;

                if(!(players[i].hasFinished())) {
                    ArrayList<int[]> moves = checkPossibleMoves(players[i]);

                    if(moves.isEmpty()) {
                        //informacja
                        continue;
                    }

                    if(players[i] instanceof Human) {
                        Human currPlayer = (Human) players[i];
                        boolean moved = false;

                        while(!moved) {
                            int[] move = currPlayer.move();
                            //if checkpossiblemoves conatins move
                            //curMove = move;
                            //set everything
                            //send to all and moved=T
                        }


                    } else {
                        Bot currBot = (Bot) players[i];
                        currentMove = currBot.move(checkPossibleMoves(players[i]));
                    }

                    if (checkFinished(players[i])) {
                        players[i].setPlace(availablePlace);
                        availablePlace++;
                        players[i].setFinished(true);
                    }
                }
            }
        } while(!gameFinished());
    }

    public String gameStats() {
        String stats = "";
        stats = stats.concat("Miejsca -\n");
        for(int i = 1; i <= players.length; ++i) {
            for(int j = 0; j < players.length; ++j) {
                if(players[j].getPlace() == i) {
                    stats = stats.concat("Player " + j + " - msc " + i + "\n");
                }
            }
        }
        return stats;
    }

	public PlayerColor getPColor(int x, int j) {
		PlayerColor [] colors = new PlayerColor[x];
		switch (x){
			case 2:
				colors[0] = PlayerColor.BLUE;
				colors[1] = PlayerColor.RED;
				break;
			case 3:
				colors[0] = PlayerColor.BLUE;
				colors[1] = PlayerColor.ORANGE;
				colors[2] = PlayerColor.GREEN;
				break;
			case 4:
				colors[0] = PlayerColor.YELLOW;
				colors[1] = PlayerColor.ORANGE;
				colors[2] = PlayerColor.GREEN;
				colors[3] = PlayerColor.PINK;
				break;
			case 6:
				colors[0] = PlayerColor.BLUE;
				colors[1] = PlayerColor.YELLOW;
				colors[2] = PlayerColor.ORANGE;
				colors[3] = PlayerColor.RED;
				colors[4] = PlayerColor.GREEN;
				colors[5] = PlayerColor.PINK;
				break;				
		}
		return colors[j];
	}

	public boolean gameFinished() {
		for (Player p : players) {
			if(!p.hasFinished()) {
				return false;
			}
		}
		return true;
	}
	
	public Player getPlayer(int x){
		return players[x];
	}

	ArrayList<int[]> checkPossibleMoves(Player p) {
        ArrayList<int[]> movesPossible = new ArrayList<>();

	    for(int i = 0; i < p.pawns.length; ++i) {
            int x = p.pawns[i].getX();
            int y = p.pawns[i].getY();

            int[][] closerCoordinates = {{x - 1, y - 1}, {x + 1, y - 1}, {x - 2, y}, {x + 2, y}, {x - 1, y + 1}, {x + 1, y + 1}};
            int[][] furtherCoordinates = {{x - 2, y - 2}, {x + 2, y - 2}, {x - 4, y}, {x + 4, y}, {x - 2, y + 2}, {x + 2, y + 2}};

            if(!(p.pawns[i].isInDestination())) {
                for (int j = 0; j < closerCoordinates.length; ++j) {
                    int xTemp =  closerCoordinates[j][0];
                    int yTemp = closerCoordinates[j][1];
                    if(xTemp < 0 || xTemp >= board.board.length || yTemp < 0 || yTemp >= board.board[0].length) {
                        continue;
                    }
                    if (board.board[xTemp][yTemp] != Fields.NOTUSED) {
                        if (board.board[xTemp][yTemp] == Fields.BUSY) {
                            int xTemp2 =  furtherCoordinates[j][0];
                            int yTemp2 = furtherCoordinates[j][1];
                            if(xTemp2 < 0 || xTemp2 >= board.board.length || yTemp2 < 0 || yTemp2 >= board.board[0].length) {
                                continue;
                            }
                            if (board.board[xTemp2][yTemp2] == Fields.EMPTY) {
                                int[] temp = {p.pawns[i].getX(),p.pawns[i].getY(), furtherCoordinates[j][0], furtherCoordinates[j][1]};
                                movesPossible.add(temp);
                            }
                        } else {
                            int[] temp = {p.pawns[i].getX(),p.pawns[i].getY(), closerCoordinates[j][0], closerCoordinates[j][1]};
                            movesPossible.add(temp);
                        }
                    }
                }
            } else {
                ArrayList<int[]> destCoordinates = p.getEndCoordinates();
                for (int j = 0; j < closerCoordinates.length; ++j) {
                    int xTemp =  closerCoordinates[j][0];
                    int yTemp = closerCoordinates[j][1];
                    if(xTemp < 0 || xTemp >= board.board.length || yTemp < 0 || y >= board.board[0].length) {
                        continue;
                    }
                    if (board.board[xTemp][yTemp] != Fields.NOTUSED) {
                        if (board.board[xTemp][yTemp] == Fields.BUSY) {
                            int xTemp2 =  furtherCoordinates[j][0];
                            int yTemp2 = furtherCoordinates[j][1];
                            if(xTemp2 < 0 || xTemp2 >= board.board.length || yTemp2 < 0 || yTemp2 >= board.board[0].length) {
                                continue;
                            }
                            if (board.board[xTemp2][yTemp2] == Fields.EMPTY) {

                                for(int[] c : destCoordinates) {
                                    if(c[0] == furtherCoordinates[j][0] && c[1] == furtherCoordinates[j][1]) {
                                        int[] temp = {p.pawns[i].getX(),p.pawns[i].getY(), furtherCoordinates[j][0], furtherCoordinates[j][1]};
                                        movesPossible.add(temp);
                                        break;
                                    }
                                }
                            }
                        } else {

                            for(int[] c : destCoordinates) {
                                if(c[0] == closerCoordinates[j][0] && c[1] == closerCoordinates[j][1]) {
                                    int[] temp = {p.pawns[i].getX(),p.pawns[i].getY(), closerCoordinates[j][0], closerCoordinates[j][1]};
                                    movesPossible.add(temp);
                                    break;
                                }
                            }
                        }
                    }

                }

            }

        }
        return movesPossible;
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

