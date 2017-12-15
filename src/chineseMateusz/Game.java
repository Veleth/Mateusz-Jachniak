package chineseMateusz;

import java.io.IOException;
import java.io.Serializable;
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

public class Game implements Serializable {

    private static final long serialVersionUID = 5063734558649943931L;
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
			    createGame(x, humans);
			break;
			default: throw new InvalidNumberOfPlayersException(x);
		}
			
	}
	
	private void createGame(int x, int humans) {
        players = new Player[x];
        humansAmount = humans;
        availablePlace = 1;
	}

    public void playGame() throws InterruptedException, IOException, ClassNotFoundException {
        int index = (int) (Math.random() * players.length);
        do {
            for(int i = index; i < index + players.length; ++i) {
                i = i % players.length;

                if(!(players[i].hasFinished())) {

                    ArrayList<int[]> moves = checkPossibleMoves(players[i]);
                    System.out.println(players[i].getPlayerColor());
                    if(moves.isEmpty()) {
                        //TODO informacja, że nie ma dostepnego ruchu
                        continue;
                    }

                    if(players[i].isHuman()) {
                        for(int[] m : moves) {
                            System.out.println("Ruch - " + m[0] + " " + m[1] + "->" + m[2] + " " + m[3]);
                        }
                        System.out.println("\n");
                        Human currPlayer = (Human) players[i];
                        boolean moved = false;

                        while(!moved) {
                            currPlayer.setisMoving(true);
                            int[] move = currPlayer.move();
                            boolean goodMove = false;
                            for(int[] m : moves) {
                                 if(m[0] == move[0] && m[1] == move[1] && m[2] == move[2] && m[3] == move[3]) {
                                     System.out.println("RUSZAM");
                                     goodMove = true;
                                     currentMove = move;
                                     break;
                                 }
                            }
                            currPlayer.nullCurrentMove();
                            if(!goodMove) {
                                continue;
                            }

                            checkingPawnsDestination(players[i]);

                            for(Player p : players) {
                                if(p instanceof Human) {
                                    (( Human) p).sendMoveToClient(currentMove);
                                }
                            }
                            moved = true;
                            currPlayer.setisMoving(false);
                        }
                    } else {
                        Bot currBot = (Bot) players[i];
                        currentMove = currBot.move(checkPossibleMoves(players[i]));

                        if(currentMove == null) {
                            //spasowal
                            continue;
                        }

                        checkingPawnsDestination(players[i]);

                        for(Player p : players) {
                            if(p instanceof Human) {
                                (( Human) p).sendMoveToClient(currentMove);
                            }
                        }
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



	public boolean gameFinished() {
		for (Player p : players) {
			if(!p.hasFinished()) {
				return false;
			}
		}
		return true;
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
                    if(xTemp < 0 || xTemp >= board.getBoard().length || yTemp < 0 || yTemp >= board.getBoard()[0].length) {
                        continue;
                    }
                    if (board.getBoard()[xTemp][yTemp] != Fields.NOTUSED) {
                        if (board.getBoard()[xTemp][yTemp] == Fields.BUSY) {
                            int xTemp2 =  furtherCoordinates[j][0];
                            int yTemp2 = furtherCoordinates[j][1];
                            if(xTemp2 < 0 || xTemp2 >= board.getBoard().length || yTemp2 < 0 || yTemp2 >= board.getBoard()[0].length) {
                                continue;
                            }
                            if (board.getBoard()[xTemp2][yTemp2] == Fields.EMPTY) {
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
                    if(xTemp < 0 || xTemp >= board.getBoard().length || yTemp < 0 || yTemp >= board.getBoard()[0].length) {
                        continue;
                    }
                    if (board.getBoard()[xTemp][yTemp] != Fields.NOTUSED) {
                        if (board.getBoard()[xTemp][yTemp] == Fields.BUSY) {
                            int xTemp2 =  furtherCoordinates[j][0];
                            int yTemp2 = furtherCoordinates[j][1];
                            if(xTemp2 < 0 || xTemp2 >= board.getBoard().length || yTemp2 < 0 || yTemp2 >= board.getBoard()[0].length) {
                                continue;
                            }
                            if (board.getBoard()[xTemp2][yTemp2] == Fields.EMPTY) {

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

         int counter = 0;
		 for(int i = 0; i < p.pawns.length; ++i) {
			 int coordinates[] = new int[2];
			 coordinates[0] = p.pawns[i].getX();
			 coordinates[1] = p.pawns[i].getY();


			 for(int j = 0; j < tempEndCoord.size(); ++j) {
				 if(tempEndCoord.get(i)[0] == coordinates[0] && tempEndCoord.get(i)[1] == coordinates[1]) {
					 counter++;
					 break;
				 }
			 }
		 }

		 return (counter == tempEndCoord.size());
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

    public Player getPlayer(int x){
        return players[x];
    }

    public Board getBoard() {
        return board;
    }

    private void checkingPawnsDestination(Player player) {
        for(Pawn p : player.pawns) {
            if(p.getX() == currentMove[0] && p.getY() == currentMove[1]) {
                p.setPos(currentMove[2], currentMove[3]);

                for(int[] endPos: player.getEndCoordinates()) {
                    if(endPos[0] == p.getX() && endPos[1] == p.getY()) {
                        p.setinDestination(true);
                        break;
                    }
                }

                board.getBoard()[currentMove[0]][currentMove[1]] = Fields.EMPTY;
                board.getBoard()[currentMove[2]][currentMove[3]] = Fields.BUSY;
                board.updateBoard();
            }
        }
    }
}

