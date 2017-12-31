package test.java.chineseMateuszTests;

import main.java.chineseMateusz.*;
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

}
