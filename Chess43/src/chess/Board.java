package chess;

import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Queen;
import pieces.Rook;

public class Board {
	
	private Tile[][] board;
	private final int HEIGHT = 8, WIDTH = 8;
	
	public Board() {
		board = new Tile[8][8];
		boolean colored = true;
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++, colored = !colored) {
				Color shading = colored ? Color.BLACK : Color.WHITE;
				Tile fresh = new Tile(x, y, shading);
				board[x][y] = fresh;
				
				Color team = y < 4 ? Color.WHITE : Color.BLACK;
				if (y == 0 || y == 7) {
					switch (x) {
					case 0:
					case 7:
						fresh.piece = new Rook(x, y, team);
						break;
					case 1:
					case 6:
						fresh.piece = new Knight(x, y, team);
						break;
					case 2:
					case 5:
						fresh.piece = new Bishop(x, y, team);
						break;
					case 3:
						fresh.piece = new Queen(x, y, team);
						break;
					case 4:
						fresh.piece = new King(x, y, team);
						break;
					}
				} else if (y == 1 || y == 6) {
					board[x][y].piece = new Pawn(x, y, team);
				}
			}
			colored = !colored;
		}
	}
	
	public String toString() {
		String str = new String();
		for (int y = HEIGHT - 1; y >= 0; y--) {
			for (int x = 0; x < WIDTH; x++) {
				str += board[x][y] + " ";
			}
			str += (y + 1) + "\n";
		}
		str += " a  b  c  d  e  f  g  h";
		return str;
	}

}