package chineseMateusz;

import javax.swing.JFrame;
import chineseMateusz.Pawn.PlayerColor;
import chineseMateuszExceptions.InvalidNumberOfHumansException;
import chineseMateuszExceptions.InvalidNumberOfPlayersException;

public class Game{
Player [] players;	
	
	public void runGame() throws InvalidNumberOfPlayersException, InvalidNumberOfHumansException{
		Game g = new Game(6, 1); //TODO: needs to be changed so that the number of players can be customized
		do {
			
		}
		while(!g.gameFinished());
	}

	public Game(int x, int humans) throws InvalidNumberOfPlayersException, InvalidNumberOfHumansException{
		if (humans < 1 || humans > x){
			throw new InvalidNumberOfHumansException(humans);
		}
		switch (x){
			case 2:
			case 3:
			case 4: 
			case 6:
			createPlayers(x, humans);
			break;
			default: throw new InvalidNumberOfPlayersException(x);
		}
			
	}
	
	private void createPlayers(int x, int humans) {
		for (int i = 0; i < humans; i++){
			players[i] = new Human("Player "+i,  getPColor(x, i));
		}
		
		for (int j = humans; j < x-humans; j++){
			players[j] = new Bot("Bot "+j, getPColor(x, j));
		}
		//TODO: Order the PlayerColors depending on the number of players
	}

	private PlayerColor getPColor(int x, int j) {
		PlayerColor [] colors = new PlayerColor[x];
		switch (x){
			case 2:
				colors[0] = PlayerColor.BLUE;
				colors[1] = PlayerColor.RED;
				break;
			case 3:
				colors[0] = PlayerColor.BLUE;
				colors[1] = PlayerColor.GREEN;
				colors[2] = PlayerColor.ORANGE;
				break;
			case 4:
				colors[0] = PlayerColor.GREEN;
				colors[1] = PlayerColor.YELLOW;
				colors[2] = PlayerColor.ORANGE;
				colors[3] = PlayerColor.PINK;
				break;
			case 6:
				colors[0] = PlayerColor.BLUE;
				colors[1] = PlayerColor.RED;
				colors[2] = PlayerColor.GREEN;
				colors[3] = PlayerColor.YELLOW;
				colors[4] = PlayerColor.ORANGE;
				colors[5] = PlayerColor.PINK;
				break;				
		}
		return colors[j];
	}

	public boolean gameFinished(){
		for (Player p : players){
			if(!p.hasFinished()){
				return false;
			}
		}
		return true;
	}
}

