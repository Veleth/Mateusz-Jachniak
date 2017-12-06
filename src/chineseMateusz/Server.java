package chineseMateusz;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import chineseMateuszExceptions.BadCoordinateException;

public class Server {
ArrayList<Game> games;

ServerSocket server = null;
Socket client = null;
ObjectInputStream in = null;
ObjectOutputStream out = null;

public static void main(String[] args){

	Server server = new Server();
	server.listenSocket();
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

	while(!games.isEmpty()){
/*		try {
//TODO: Implement comms between server and client
		} 
		catch (IOException e){
			finalize();
			System.exit(-1);
		}*/ 
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
	
}

