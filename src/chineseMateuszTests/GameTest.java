package chineseMateuszTests;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import chineseMateusz.Game;
import chineseMateusz.Pawn.PlayerColor;
import chineseMateusz.PlayersFactory;
import chineseMateuszExceptions.BadCoordinateException;
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
		public void playerColorAssignmentTest() throws InvalidNumberOfPlayersException, InvalidNumberOfHumansException, BadCoordinateException{
			Game a = new Game(6, 2);
			Game b = new Game(3, 1);
			Game c = new Game(4, 3);

			assertEquals(a.getPColor(6, 0), PlayerColor.BLUE);
			assertEquals(a.getPColor(6, 2), PlayerColor.ORANGE);
			assertNotSame(a.getPColor(6, 5), PlayerColor.RED);
		
			assertEquals(b.getPColor(3, 1), PlayerColor.ORANGE);
			assertEquals(b.getPColor(3, 2), PlayerColor.GREEN);
			
			assertNotSame(c.getPColor(4, 0), PlayerColor.BLUE);
			assertEquals(c.getPColor(4, 2), PlayerColor.GREEN);

		}
		
		@Test
		public void endGame() throws InvalidNumberOfPlayersException, InvalidNumberOfHumansException, BadCoordinateException{
			Game e = new Game(2, 1);
			e.setPlayer(PlayersFactory.getInstance().createBot(PlayerColor.BLUE), 0);
			e.setPlayer(PlayersFactory.getInstance().createBot(PlayerColor.RED), 1);
			e.getPlayer(0).setFinished(true);
			assertFalse(e.gameFinished());
			e.getPlayer(1).setFinished(true);
			assertTrue(e.gameFinished());
		}
}
