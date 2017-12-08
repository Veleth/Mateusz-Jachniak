package chineseMateusz;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import chineseMateuszExceptions.BadCoordinateException;
import chineseMateuszExceptions.InvalidNumberOfHumansException;
import chineseMateuszExceptions.InvalidNumberOfPlayersException;

public class Server {

private ArrayList<Game> games;
private int counter=1;
private ServerSocket server = null;
private Socket client = null;
private ObjectInputStream in = null;
private ObjectOutputStream out = null;

public static void main(String[] args) {
	try{
	Server server = new Server();
	server.listenSocket();
	server.clientServerCommunication();
	}
	//TODO: add more catchesssss, modify the existing one
	catch(Exception e){
		e.printStackTrace();
	}
}	

public Server(){ 

	try {
		server = new ServerSocket(21372); 
	}
	catch (IOException e){
		JOptionPane.showMessageDialog(null, "Cannot connect to port!", "SERVER INIT ERROR!", JOptionPane.ERROR_MESSAGE); 
		System.exit(-1);
	}
}

private void clientServerCommunication() throws IOException, ClassNotFoundException, InvalidNumberOfPlayersException, InvalidNumberOfHumansException {
	Object temp;
	int [] tempArr;
	int tempInt;
	while(!allGamesFinished()){
		temp = in.readObject();
		if (temp.getClass().isArray()){
			tempArr = (int [])temp;
			if(tempArr.length == 2){
				games.add(new Game(tempArr[0], tempArr[1]));
				out.writeObject(counter);
				counter++;
			}
			else if(tempArr.length == 5){
				//TODO:Move active player in game tempArr[0] from tA[1],tA[2] to tA[3],tA[4]
			}
		}
		else if(temp instanceof Number){
			tempInt = (int)temp;
			games.get(tempInt).runGame();
		}
		
	}
	finalize();
	System.exit(-1);
}
public void listenSocket(){
	try {
		client = server.accept();
	} 
	catch (IOException e){
		JOptionPane.showMessageDialog(null, "SERVER: Client could not be accepted", "ERROR!", JOptionPane.ERROR_MESSAGE); 
		System.exit(-1);
	}

	try {
		out = new ObjectOutputStream(client.getOutputStream());
		in = new ObjectInputStream(client.getInputStream());
	}	 	
	catch (IOException e){
		JOptionPane.showMessageDialog(null, "SERVER: I/O Streams Could not be established with the client", "ERROR!", JOptionPane.ERROR_MESSAGE); 
		System.exit(-1);
	}
}

protected void finalize(){

	try {
		in.close();	
		out.close();
		client.close();
		server.close();
	} 
	
	catch (IOException e){
		JOptionPane.showMessageDialog(null, "Error encountered while closing I/O, server, or client", "ERROR", JOptionPane.ERROR_MESSAGE); 
		System.exit(-1);
	}
}

public boolean allGamesFinished(){
	if(games.isEmpty()){
		return false;
	}
	for(Game g : games){
		if(!g.gameFinished()){
			return false;
		}
	}
	return true;
}
}

