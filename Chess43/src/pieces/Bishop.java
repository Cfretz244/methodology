package pieces;

import java.util.ArrayList;

import chess.Board;
import chess.Color;
import chess.Location;

public class Bishop extends Piece {
	
	public Bishop(int x, int y, Color team) {
		super(x, y, team);
		rank = "B";
	}
	
	public ArrayList<ArrayList<Location>> validMoves() {
		ArrayList<ArrayList<Location>> moves = new ArrayList<ArrayList<Location>>();
		for (int i = 0; i <= Board.NWEST; i++) moves.add(new ArrayList<Location>());
		
		boolean hasMoves = true;
		for (int i = 1; hasMoves; i++) {
			hasMoves = false;
			if (x + i < Board.WIDTH && y + i < Board.HEIGHT) {
				hasMoves = true;
				moves.get(Board.NEAST).add(new Location(x + i, y + i));
			}
			if (x + i < Board.WIDTH && y - i >= 0) {
				hasMoves = true;
				moves.get(Board.SEAST).add(new Location(x + i, y - i));
			}
			if (x - i >= 0 && y - i >= 0) {
				hasMoves = true;
				moves.get(Board.SWEST).add(new Location(x - i, y - i));
			}
			if (x - i >= 0 && y + i < Board.HEIGHT) {
				hasMoves = true;
				moves.get(Board.NWEST).add(new Location(x - i, y + i));
			}
		}
		
		return moves;
	}

}
