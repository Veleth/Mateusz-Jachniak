package chineseMateusz;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Board extends JPanel implements Serializable{
    private static final long serialVersionUID = -5139437953272450049L;

    public enum Fields {
		NOTUSED, EMPTY, BUSY;
	}
	
	Fields board[][];

    HashMap<Pawn.PlayerColor, Color> colors;
	HashMap<Pawn[], Pawn.PlayerColor> pawns;

	public Board(HashMap<Pawn[], Pawn.PlayerColor> pawns) {
        board = new Fields[25][17];
        this.pawns = pawns;
        setColors();
        prepareBoard();
	}

    private void setColors() {
	    colors = new HashMap<>();
	    colors.put(Pawn.PlayerColor.BLUE, Color.BLUE);
	    colors.put(Pawn.PlayerColor.GREEN, Color.GREEN);
	    colors.put(Pawn.PlayerColor.ORANGE, Color.ORANGE);
	    colors.put(Pawn.PlayerColor.PINK, Color.PINK);
	    colors.put(Pawn.PlayerColor.RED, Color.RED);
	    colors.put(Pawn.PlayerColor.YELLOW, Color.YELLOW);
    }

    private void prepareBoard() {
		
		for(int y = 0; y < board[0].length; ++y) {
			for(int x = 0; x < board.length; ++x) {
				board[x][y] = Fields.NOTUSED;
			}
		}
		
		int[] fieldsAmountInRow = {1,2,3,4,13,12,11,10,9,10,11,12,13,4,3,2,1};
		for(int y = 0; y < board[0].length; ++y) {
			
			int xCoordinate = ((board.length - 1) / 2) - (fieldsAmountInRow[y] / 2) * 2 + (y%2);
			for(int i = 0; i < fieldsAmountInRow[y]; ++i) {
				board[xCoordinate][y] = Fields.EMPTY;
				xCoordinate+=2;	
			}
		}
	}

    public void updateBoard() {
        prepareBoard();

        for(Map.Entry<Pawn[], Pawn.PlayerColor> map : pawns.entrySet()) {
            for(Pawn p : map.getKey()) {
                int x = p.getX();
                int y = p.getY();
                board[x][y] = Fields.BUSY;
            }
        }
    }

    public void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        int height = board[0].length;
        int width = board.length;
        int scaleY = 476 / height;
        int scaleX = 700 / width;
        boolean alreadyDrawn;

        for(int y=0; y<height; ++y)
            for(int x=0; x<width; ++x)
            {
                switch(board[x][y])
                {
                    case EMPTY:
                        g2d.setColor(Color.GRAY);
                        g2d.fillOval(30+x*scaleX, 30+y*scaleY, scaleX, scaleY);
                        break;
                    case BUSY:
                        alreadyDrawn = false;
                        for(Map.Entry<Pawn[], Pawn.PlayerColor> map : pawns.entrySet()) {
                            for(Pawn p : map.getKey()) {
                                if(p.getX() == x && p.getY() == y) {
                                    g2d.setColor(colors.get(map.getValue()));
                                    g2d.fillOval(30+x*scaleX, 30+y*scaleY, scaleX, scaleY);
                                    alreadyDrawn = true;
                                    break;
                                }
                            }
                            if(alreadyDrawn) {
                                break;
                            }
                        }
                        break;
                }

            }
    }
}
