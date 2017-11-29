package chineseMateuszTests;

import static org.junit.Assert.*;

import org.junit.Test;

import chineseMateusz.Pawn;
import chineseMateusz.Pawn.PlayerColor;

public class PawnTest {

	@Test
	public void constructorTest() {
		Pawn p = new Pawn(PlayerColor.YELLOW, 10, 12);

		assertEquals(p.getX(),10);
		assertEquals(p.getY(),12);
		assertEquals(p.getPlayerColor(),PlayerColor.YELLOW);
		assertFalse(p.isInDestination());
	}

}