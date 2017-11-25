package chineseMateusz;

public class Pawn {
	public enum PlayerColor{
		BLUE, RED, GREEN, YELLOW, ORANGE, PINK;
	}
	
	private PlayerColor playerColor;
	private int x, y;
	private boolean isInDestination;

	public Pawn(PlayerColor playerColor, int x, int y){
		this.playerColor = playerColor;
		this.x = x;
		this.y = y;
		this.isInDestination = false;
	}
	
	public PlayerColor getPlayerColor(){
		return this.playerColor;
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	public boolean isInDestination(){
		return this.isInDestination;
	}
	
	//SetPos as in SetPosition
	public void setPos(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void setinDestination(boolean b){
		this.isInDestination = b;
	}
}
