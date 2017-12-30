package main.java.chineseMateusz;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Board extends JPanel implements Serializable {
    private static final long serialVersionUID = -5139437953272450049L;

    public enum Fields {
		NOTUSED, EMPTY, BUSY
	}

	int[] paintPoint;
	private Fields board[][];
    private Pawn.PlayerColor currentColor;
    private Pawn.PlayerColor playerColor;
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

    public void setPaintPoint(int x, int y) {
	    paintPoint = new int[]{x,y};
    }

    public void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0,0, this.getWidth(), this.getHeight());

        g2d.setFont(new Font("Arial",Font.BOLD, 16));
        g2d.setColor(colors.get(currentColor));
        g2d.drawString("Color to turn",10,20);
        g2d.fillRect(10,35,20,20);
        g2d.setColor(colors.get(playerColor));
        g2d.drawString("Your color",600,20);
        g2d.fillRect(600,35,20,20);

        int height = board[0].length;
        int width = board.length;
        int scaleY = 476 / height;
        int scaleX = 700 / width;
        boolean alreadyDrawn;

        for(int y=0; y<height; ++y) {
            for (int x = 0; x < width; ++x) {
                switch (board[x][y]) {
                    case EMPTY:
                        g2d.setColor(Color.GRAY);
                        g2d.fillOval(30 + x * scaleX, 30 + y * scaleY, scaleX, scaleY);
                        break;
                    case BUSY:
                        alreadyDrawn = false;
                        for (Map.Entry<Pawn[], Pawn.PlayerColor> map : pawns.entrySet()) {

                            g2d.setColor(colors.get(map.getValue()));


                            if(map.getValue() == Pawn.PlayerColor.BLUE) {
                                g2d.drawString("BLUE DESTINATION", 300, 525);
                            } else if(map.getValue() == Pawn.PlayerColor.GREEN) {
                                g2d.drawString("GREEN DESTINATION", 540, 130);
                            } else if(map.getValue() == Pawn.PlayerColor.ORANGE) {
                                g2d.drawString("ORANGE DESTINATION", 30, 130);
                            } else if(map.getValue() == Pawn.PlayerColor.PINK) {
                                g2d.drawString("PINK DESTINATION", 540, 415);
                            } else if(map.getValue() == Pawn.PlayerColor.RED) {
                                g2d.drawString("RED DESTINATION", 300, 25);
                            } else if(map.getValue() == Pawn.PlayerColor.YELLOW) {
                                g2d.drawString("YELLOW DESTINATION", 30, 415);
                            }

                            for (Pawn p : map.getKey()) {
                                if (p.getX() == x && p.getY() == y) {
                                    g2d.fillOval(30 + x * scaleX, 30 + y * scaleY, scaleX, scaleY);
                                    alreadyDrawn = true;
                                    break;
                                }
                            }
                            if (alreadyDrawn) {
                                break;
                            }
                        }
                        break;
                }

            }
        }

        if(paintPoint != null) {
            g2d.setColor(Color.BLACK);
            g2d.fillOval(paintPoint[0], paintPoint[1], 10, 10);
            paintPoint = null;
        }
    }

    public Fields[][] getBoard(){
    	return this.board;
    }

    public void setCurrentColor(Pawn.PlayerColor currentColor) {
        this.currentColor = currentColor;
    }

    public void setPlayerColor(Pawn.PlayerColor playerColor) {
        this.playerColor = playerColor;
    }
}
