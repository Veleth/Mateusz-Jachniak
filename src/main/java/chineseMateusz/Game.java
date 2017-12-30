package main.java.chineseMateusz;

import main.java.chineseMateusz.Board.Fields;
import main.java.chineseMateusz.Pawn.PlayerColor;
import main.java.chineseMateuszExceptions.InvalidNumberOfHumansException;
import main.java.chineseMateuszExceptions.InvalidNumberOfPlayersException;

import java.io.IOException;
import java.util.ArrayList;

public class Game {

    private Player[] players;
    int humansAmount;
    public Board board;
    private int availablePlace;
    int lastPlace;
    private int[] currentMove;
    private boolean aborted;
    private boolean jumped;
    private int xLast, yLast;
    private boolean alreadyMoved;

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
        lastPlace = x;
        aborted = false;
        jumped = false;
        alreadyMoved = false;
        xLast = yLast = -100;
	}

    public void playGame() throws IOException {
        int index = (int) (Math.random() * players.length);
        do {
            for(int i = index; i < index + players.length && !gameFinished(); ++i) {

                i = i % players.length;

                if(!(players[i].hasFinished())) {
                    for(Player p : players) {
                        if(p instanceof Human) {
                            (( Human) p).sendMoverToClient(players[i].getPlayerColor());
                        }
                    }

                    ArrayList<int[]> moves;

                    if(!alreadyMoved) {
                        moves = checkPossibleMoves(players[i]);
                    } else {
                        moves = checkJumpMoves(players[i]);
                    }

                    if(moves.isEmpty()) {
                        if(players[i] instanceof Human) {
                            ((Human) players[i]).sendTextToClient("MOVE NOT POSSIBLE\nYou can't move now!");
                        }
                        continue;
                    }

                    if(players[i].isHuman()) {
                        Human currPlayer = (Human) players[i];
                        boolean moved = false;
                        jumped = false;

                        while(!moved) {
                            currPlayer.setisMoving(true);
                            int[] move = currPlayer.move();

                            if(move[0] == -100 && move[1] == -100 && move[2] == -100 && move[3] == -100) {
                                aborted = true;
                                break;
                            }
                            if(move[0] == -200 && move[1] == -200 && move[2] == -200 && move[3] == -200) {
                                moved = true;
                                jumped = false;
                                alreadyMoved = false;
                                currPlayer.setisMoving(false);
                                currPlayer.nullCurrentMove();
                                xLast = yLast = -100;
                                continue;
                            }

                            boolean goodMove = false;
                            for(int[] m : moves) {
                                 if(m[0] == move[0] && m[1] == move[1] && m[2] == move[2] && m[3] == move[3]) {
                                     goodMove = true;
                                     currentMove = move;
                                     break;
                                 }
                            }
                            currPlayer.nullCurrentMove();
                            if(!goodMove) {
                                continue;
                            }

                            int x, y;
                            if (alreadyMoved) {
                                x = xLast;
                                y = yLast;
                            } else {
                                x = currentMove[0];
                                y = currentMove[1];
                            }

                            int[][] furtherCoordinates = {{x - 2, y - 2}, {x + 2, y - 2}, {x - 4, y}, {x + 4, y}, {x - 2, y + 2}, {x + 2, y + 2}};
                            for(int[] f: furtherCoordinates) {
                                if(x == currentMove[0] && y == currentMove[1] && f[0] == currentMove[2] && f[1] == currentMove[3]) {
                                    xLast = currentMove[2];
                                    yLast = currentMove[3];
                                    jumped = true;
                                    alreadyMoved = true;
                                    break;
                                }
                            }

                            if(alreadyMoved && !jumped) {
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

                        if(aborted) {
                            break;
                        }

                    } else {
                        Bot currBot = (Bot) players[i];
                        //todo set forbidden moves
                        currentMove = currBot.move(checkPossibleMoves(players[i]));

                        if(currentMove == null) {
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
                        jumped = false;
                        alreadyMoved = false;
                    }
                    if(jumped) {
                        i--;
                        alreadyMoved = true;
                    } else {
                        xLast = yLast = -100;
                        alreadyMoved = false;
                    }
                }
            }
        } while(!gameFinished() && !aborted);

        if(!aborted) {
        	for (Player p : players) {
                if (p instanceof Human) {
                    ((Human) p).sendTextToClient(gameStats());
                }
            }
        }
    }

    private String gameStats() {
        String stats = "";
        stats = stats.concat("GAME END -\n");
        for(int i = 1; i <= players.length; ++i) {
            for(int j = 0; j < players.length; ++j) {
                if(players[j].getPlace() == i) {
                    stats = stats.concat(players[j].getPlayerColor() + " - pos " + i + "\n");
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

	private ArrayList<int[]> checkPossibleMoves(Player p) {
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

    private ArrayList<int[]> checkJumpMoves(Player p) {
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
                    }

                }

            }
        }

        return movesPossible;
    }

	private boolean checkFinished(Player p) {
		 ArrayList<int[]> tempEndCoord = p.getEndCoordinates();

         int counter = 0;
		 for(int i = 0; i < p.pawns.length; ++i) {
			 int coordinates[] = new int[2];
			 coordinates[0] = p.pawns[i].getX();
			 coordinates[1] = p.pawns[i].getY();


			 for(int j = 0; j < tempEndCoord.size(); ++j) {
				 if(tempEndCoord.get(j)[0] == coordinates[0] && tempEndCoord.get(j)[1] == coordinates[1]) {
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
    
    public int getNoOfPlayers(){
    	return players.length;
    }
    
    public Player[] getPlayers(){
    	return players;
    }

    public Player getPlayer(int x){
        return players[x];
    }
    
    public void setPlayer (Player p, int x){
    	players[x] = p;
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

    public void abortedGame() {
        try {
            for (Player p : players) {
                if (p instanceof Human) {
                    ((Human) p).sendTextToClient("ABORTED GAME");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

