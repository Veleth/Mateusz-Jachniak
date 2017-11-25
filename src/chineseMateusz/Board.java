package chineseMateusz;

public class Board {
	public enum Fields {
		NOTUSED, EMPTY, BUSY;
	}
	
	Fields board[][];
	
	public Board() {
		prepareBoard();
	}
	
	private void prepareBoard() {
		board = new Fields[25][17];
		
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
}
