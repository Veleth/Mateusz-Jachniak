package test.java.chineseMateuszTests;

import main.java.chineseMateusz.Bot;
import main.java.chineseMateusz.Pawn;
import main.java.chineseMateusz.Pawn.PlayerColor;
import main.java.chineseMateusz.PlayersFactory;
import main.java.chineseMateuszExceptions.BadCoordinateException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class PawnTest {

	@Test
	public void constructorTest() {
		Pawn p = new Pawn(PlayerColor.YELLOW, 10, 12);

		assertEquals(p.getX(),10);
		assertEquals(p.getY(),12);
		assertEquals(p.getPlayerColor(),PlayerColor.YELLOW);
		assertFalse(p.isInDestination());
	}

	@Test
	public void advancedTest() throws BadCoordinateException{
		Bot x = PlayersFactory.getInstance().createBot(PlayerColor.BLUE);
		Bot xy = PlayersFactory.getInstance().createBot(PlayerColor.BLUE);
		HashMap <Pawn[], Pawn.PlayerColor> h = new HashMap <>();
		HashMap <Pawn[], Pawn.PlayerColor> hb = new HashMap <>();
		h.put(x.getPawns(),x.getPlayerColor());
		hb.put(xy.getPawns(), xy.getPlayerColor());

		ArrayList<Pawn[]> arr = new ArrayList<Pawn[]>(h.keySet());
		ArrayList<Pawn[]> arg = new ArrayList<Pawn[]>(hb.keySet());
		
//		System.out.println(arr.get(0).getClass());
		Pawn p = arr.get(0)[0];
//		System.out.println(p.getX());
		Pawn tmp = arg.get(0)[0];
		p.setPos(0, 0);
//		System.out.println(tmp.getX());
		assertNotSame(p.getX(), tmp.getX());

	}
}