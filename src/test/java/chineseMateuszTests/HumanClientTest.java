package test.java.chineseMateuszTests;

import main.java.chineseMateusz.Board;
import main.java.chineseMateusz.Client;
import main.java.chineseMateusz.Human;
import main.java.chineseMateusz.Pawn;
import main.java.chineseMateusz.Server;
import main.java.chineseMateuszExceptions.BadCoordinateException;

import org.junit.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class HumanClientTest {

    @Test
    public void communicationTest() throws Exception {
        Server server = new Server(21373);
        Socket s1 = new Socket("localhost",21373);
        Socket s2 = server.getServer().accept();

        ObjectOutputStream out = new ObjectOutputStream(s1.getOutputStream());
        out.writeObject(new int[]{2,2});
        server.setFirstPlayer(s2);
        ObjectInputStream in = new ObjectInputStream(s1.getInputStream());
        assertEquals(in.readObject(),true);

        Human h = (Human) server.getGame().getPlayers()[0];

        h.sendMoveToClient(new int[]{2,3,4,5});
        assertEquals(((int[]) in.readObject())[0],2);

        Board board = new Board(new HashMap<Pawn[], Pawn.PlayerColor>());
        h.sendBoardToClient(board);
        assertEquals((in.readObject()).getClass(), Board.class);

        h.sendTextToClient("GAME ABORTED");
        assertEquals((in.readObject()), "GAME ABORTED");

        h.sendMoverToClient(Pawn.PlayerColor.RED);
        assertEquals(in.readObject(), Pawn.PlayerColor.RED);

        h.setConnected(false);
        assertEquals(h.move()[0], -100);

        Method method = Human.class.getDeclaredMethod("isPawn", int.class, int.class);
        method.setAccessible(true);
        assertEquals(method.invoke(h, 12, 0), true);

        server.closeStreams();
    }
    
    @Test(expected=IOException.class)
    public void clientTest() throws ClassNotFoundException, IOException{
    	Client window = new Client(21376);
    	window.closeStreams();
    }
    
/*    @Test
	public void constructorTest() throws BadCoordinateException {
		Human h = PlayersFactory.getInstance().createHuman(PlayerColor.BLUE);

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
}
*/

}
