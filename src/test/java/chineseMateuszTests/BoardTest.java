package test.java.chineseMateuszTests;

import main.java.chineseMateusz.Board;
import main.java.chineseMateusz.Board.Fields;
import main.java.chineseMateusz.Bot;
import main.java.chineseMateusz.Pawn;
import main.java.chineseMateusz.Pawn.PlayerColor;
import main.java.chineseMateusz.PlayersFactory;
import main.java.chineseMateuszExceptions.BadCoordinateException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class BoardTest {

	@Test
	public void consructorTest() throws BadCoordinateException{
		Bot x = PlayersFactory.getInstance().createBot(PlayerColor.BLUE);
		HashMap <Pawn[], Pawn.PlayerColor> h = new HashMap <>();
		h.put(x.getPawns(),x.getPlayerColor());
		Board b = new Board(h);
		
		ArrayList<Pawn[]> arr = new ArrayList<Pawn[]>(h.keySet());
		
		assertEquals(b.getBoard()[0][0], Fields.NOTUSED);
		assertEquals(b.getBoard()[12][6], Fields.EMPTY);
		Pawn p = arr.get(0)[0];
		p.setPos(12, 6);
		assertEquals(b.getBoard()[12][6], Fields.EMPTY);		
		b.updateBoard();
		b.repaint();
		assertEquals(b.getBoard()[12][6], Fields.BUSY);

		b.setCurrentColor(PlayerColor.BLUE);//Works
		b.setPaintPoint(1, 1);//Works
	}
	
}
