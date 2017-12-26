package chineseMateuszTests;

import chineseMateusz.Pawn;
import chineseMateusz.Pawn.PlayerColor;
import chineseMateusz.Server;
import org.junit.Test;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

import static org.junit.Assert.*;

public class ServerTest {

    @Test
    public void gameCreationTest() throws Exception {

        Server server = new Server();

        Socket s1 = new Socket("localhost", 21372);
        Socket s1a = server.getServer().accept();
        ObjectOutputStream out = new ObjectOutputStream(s1.getOutputStream());
        out.writeObject(new int[]{2,2});
        server.setFirstPlayer(s1a);
        assertEquals(server.getGame().getNoOfPlayers(),2);

        Socket s2 = new Socket("localhost", 21372);
        Socket s2a = server.getServer().accept();
        ObjectOutputStream out2 = new ObjectOutputStream(s2.getOutputStream());
        server.setAnotherPlayers(1, s2a);
        assertNotNull(server.getGame().getPlayers()[1]);

        HashMap< Pawn[], PlayerColor> boardMap = new HashMap<>();
        server.initBoardMap(boardMap);

        assertSame(boardMap.containsValue(PlayerColor.BLUE), true);

        server.closeStreams();
    }
}

