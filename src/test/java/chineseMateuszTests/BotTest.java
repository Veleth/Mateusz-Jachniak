package test.java.chineseMateuszTests;

import main.java.chineseMateusz.Bot;
import main.java.chineseMateusz.Pawn.PlayerColor;
import main.java.chineseMateusz.PlayersFactory;
import main.java.chineseMateuszExceptions.BadCoordinateException;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class BotTest {

	@Test
	public void constructorTest() throws BadCoordinateException {
		Bot b = PlayersFactory.getInstance().createBot(PlayerColor.BLUE);

		assertFalse(b.isHuman());
		assertEquals(b.getPawn(0).getX(), 12);
		assertEquals(b.getPawn(0).getY(), 0);
		for(int i = 0; i < 10; i++){
			assertTrue(b.getTarget()[0] != b.getPawn(i).getX() || b.getTarget()[1] != b.getPawn(i).getY());
		}
		boolean yes = false;
		for(int[] coord: b.getEndCoordinates()){
			if(coord[0] == b.getTarget()[0] && coord[1] == b.getTarget()[1]){
			yes = true;
			break;
			}
		}
		assertTrue(yes);
	}
	
	@Test
	public void moveTest() throws BadCoordinateException {
		Bot bot = PlayersFactory.getInstance().createBot(PlayerColor.RED);
		
		ArrayList<int[]> moves = new ArrayList<>();
		int[] temp = {1, 1, 3, 3};
		moves.add(temp);
		int[] temp1 = {1, 1, 4, 4};
		moves.add(temp1);
		int[] temp2 = {1, 6, 14, 15};
		moves.add(temp2);
		
		//System.out.println(bot.getTarget()[0]+" "+bot.getTarget()[1]);
		int[] test1 = bot.move(moves);
		assertEquals(temp2[0], test1[0]);
		
		moves.clear();
		int[] test2 = bot.move(moves);
		assertNull(test2);
		
	}
	
	@Test
	public void bonusTest() throws BadCoordinateException{
		Bot winner = PlayersFactory.getInstance().createBot(PlayerColor.BLUE);
		winner.setPlace(1);
		assertEquals(winner.getPlace(), 1);
	}
}
