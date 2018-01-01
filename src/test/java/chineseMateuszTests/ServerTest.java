package test.java.chineseMateuszTests;

import main.java.chineseMateusz.Pawn;
import main.java.chineseMateusz.Pawn.PlayerColor;
import main.java.chineseMateusz.Server;
import main.java.chineseMateuszExceptions.BadCoordinateException;
import main.java.chineseMateuszExceptions.GameException;
import main.java.chineseMateuszExceptions.InvalidNumberOfHumansException;
import main.java.chineseMateuszExceptions.InvalidNumberOfPlayersException;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class ServerTest {

    @Test
    public void gameCreationTest() throws Exception {

        Server server = new Server(21370);

        Socket s1 = new Socket("localhost", 21370);
        Socket s1a = server.getServer().accept();
        ObjectOutputStream out = new ObjectOutputStream(s1.getOutputStream());
        out.writeObject(new int[]{2,2});
        server.setFirstPlayer(s1a);
        assertEquals(server.getGame().getNoOfPlayers(),2);

        Socket s2 = new Socket("localhost", 21370);
        Socket s2a = server.getServer().accept();
        ObjectOutputStream out2 = new ObjectOutputStream(s2.getOutputStream());
        server.setAnotherPlayers(1, s2a);
        assertNotNull(server.getGame().getPlayers()[1]);

        HashMap< Pawn[], PlayerColor> boardMap = new HashMap<>();
        server.initBoardMap(boardMap);

        assertSame(boardMap.containsValue(PlayerColor.BLUE), true);

        s1.close();
        s2.close();
        server.closeStreams();
    }
    
	@Test(expected=GameException.class)
   	public void gameExceptionTest() throws IOException, ClassNotFoundException, InvalidNumberOfHumansException, InvalidNumberOfPlayersException, GameException, BadCoordinateException{
   	
	   Server server = new Server(21371);
       Socket so = new Socket("localhost", 21371);
       Socket so2 = server.getServer().accept();
       ObjectOutputStream out = new ObjectOutputStream(so.getOutputStream());
       server.setAnotherPlayers(1, so2);
       server.setFirstPlayer(so);
       
       server.closeStreams();
   }
	
	@Ignore("Endless loop in server")
	public void thoroughTest() throws IOException, GameException, BadCoordinateException, ClassNotFoundException, InvalidNumberOfHumansException, InvalidNumberOfPlayersException, InterruptedException{
		
		Server server = new Server(21374);
	    Socket so = new Socket("localhost", 21374);
	    Socket so2 = server.getServer().accept();
        server.gameHandling();
	       
	    server.closeStreams();
	}
}

