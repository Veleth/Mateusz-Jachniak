package chineseMateusz;

public class Pawn {
	public enum Color{
		BLUE, RED, GREEN, YELLOW, ORANGE, PINK;
	}
	
	private Color color;
	private int x, y;
	private boolean isInDestination;

	public Pawn(Color color, int x, int y){
		this.color = color;
		this.x = x;
		this.y = y;
		this.isInDestination = false;
	}
	
	public Color getColor(){
		return this.color;
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
