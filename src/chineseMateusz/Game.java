package chineseMateusz;

import javax.swing.JFrame;

public class Game extends JFrame {

	public static void main(String args[]){
		JFrame window = new Game();
		window.setDefaultCloseOperation(EXIT_ON_CLOSE);
		window.setVisible(true);
		window.setResizable(false);
		window.setLocation(500, 200);
	}
	
	Game(){
		super("Chinese Checkers"); //TODO: to be modified later if multiple ongoing games are implemented
		
	}
}
