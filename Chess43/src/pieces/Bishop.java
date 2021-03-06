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
	
	@Override
	// Method implements abstract superclass method.
	public ArrayList<ArrayList<Location>> validMoves() {
		// Handle instantiations.
		ArrayList<ArrayList<Location>> moves = new ArrayList<ArrayList<Location>>();
		for (int i = 0; i <= Board.NWEST; i++) moves.add(new ArrayList<Location>());
		
		// Loop enumerates all locations a bishop can move to along the 4 diagonal directions, stopping when there are no more valid
		// locations on the board.
		boolean hasMoves = true;
		for (int i = 1; hasMoves; i++) {
			hasMoves = false;
			if (x + i < Board.WIDTH && y + i < Board.HEIGHT) {
				// We've found a new location in the northeastern direction.
				hasMoves = true;
				moves.get(Board.NEAST).add(new Location(x + i, y + i));
			}
			if (x + i < Board.WIDTH && y - i >= 0) {
				// We've found a new location in the southeastern direction.
				hasMoves = true;
				moves.get(Board.SEAST).add(new Location(x + i, y - i));
			}
			if (x - i >= 0 && y - i >= 0) {
				// We've found a new location in the southwestern direction.
				hasMoves = true;
				moves.get(Board.SWEST).add(new Location(x - i, y - i));
			}
			if (x - i >= 0 && y + i < Board.HEIGHT) {
				// We've found a new location in the northwestern direction.
				hasMoves = true;
				moves.get(Board.NWEST).add(new Location(x - i, y + i));
			}
		}
		
		return moves;
	}

}
