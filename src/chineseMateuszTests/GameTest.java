package chineseMateuszTests;

import static org.junit.Assert.*;

import org.junit.Test;

import chineseMateusz.Game;
import chineseMateusz.Pawn.PlayerColor;
import chineseMateuszExceptions.InvalidNumberOfHumansException;
import chineseMateuszExceptions.InvalidNumberOfPlayersException;

public class GameTest {

		@Test(expected=InvalidNumberOfHumansException.class)
		public void constructorTestHumans() throws InvalidNumberOfPlayersException, InvalidNumberOfHumansException{
			new Game(6, 0);
		}
		
		@Test(expected=InvalidNumberOfHumansException.class)
		public void constructorTestHumans2() throws InvalidNumberOfPlayersException, InvalidNumberOfHumansException{
			new Game(3, 4);
		}
		
		@Test(expected=InvalidNumberOfPlayersException.class)
		public void constructorTestPlayers() throws InvalidNumberOfPlayersException, InvalidNumberOfHumansException{
			new Game(7, 1);
		}
		
		@Test
		public void playerColorAssignmentTest() throws InvalidNumberOfPlayersException, InvalidNumberOfHumansException{
			Game a = new Game(6, 2);
			Game b = new Game(3, 1);
			Game c = new Game(4, 3);
			
			assertEquals(a.getPlayer(0).getPlayerColor(), PlayerColor.BLUE);
			assertEquals(a.getPlayer(2).getPlayerColor(), PlayerColor.GREEN);
		
			assertEquals(b.getPlayer(1).getPlayerColor(), PlayerColor.GREEN);
			assertEquals(b.getPlayer(2).getPlayerColor(), PlayerColor.ORANGE);
			
			assertNotSame(c.getPlayer(0).getPlayerColor(), PlayerColor.BLUE);
			assertEquals(c.getPlayer(2).getPlayerColor(), PlayerColor.ORANGE);
			
		}
		
		@Test
		public void endGame() throws InvalidNumberOfPlayersException, InvalidNumberOfHumansException{
			Game e = new Game(2, 1);
			e.getPlayer(0).setFinished(true);
			assertFalse(e.gameFinished());
			e.getPlayer(1).setFinished(true);
			assertTrue(e.gameFinished());
		}
}
