package chineseMateusz;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import chineseMateusz.Pawn.PlayerColor;
//TODO: Ask if the client should be in the same package as the rest of the classes
public class Client extends JFrame implements MouseListener{
	Human player;
	Socket socket = null;
	ObjectOutputStream out = null;
	ObjectInputStream in = null;


	public static void main(String args[]){
		Human p = new Human("TEST", PlayerColor.PINK); //TODO: Player must be sent from server
		Client window = new Client(p);
		window.setDefaultCloseOperation(EXIT_ON_CLOSE);
		window.setVisible(true);
		window.setResizable(false);
		window.setLocation(500, 200);
		window.listenSocket();
	}
	
	public void listenSocket(){
		try {
			socket = new Socket("localhost", 21372);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		}
		catch (UnknownHostException e){
			JOptionPane.showMessageDialog(null, "Unknown host!", "ERROR", JOptionPane.ERROR_MESSAGE); 
			System.exit(1);
		}
		catch (IOException e){
			JOptionPane.showMessageDialog(null, "Could not establish connection with server!", "ERROR", JOptionPane.ERROR_MESSAGE); 
			System.exit(1);
		}
	}	
	Client(Human h){
		super("Chinese Checkers, "+h.getName());
		this.player = h;
	}

	@Override
	public void mouseClicked(MouseEvent e){
		int x = e.getX();
		int y = e.getY();
		//TODO: send to server
		
	}

	//The remaining methods are not relevant, though they needed to be included in this class
	@Override
	public void mouseEntered(MouseEvent e){
	}

	@Override
	public void mouseExited(MouseEvent e){		
	}

	@Override
	public void mousePressed(MouseEvent e){
	}

	@Override
	public void mouseReleased(MouseEvent e){
	}
	
}
