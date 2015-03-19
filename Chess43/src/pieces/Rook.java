package pieces;

import java.util.ArrayList;

import chess.Board;
import chess.Color;
import chess.Location;

public class Rook extends Piece {
	
	public Rook(int x, int y, Color team) {
		super(x, y, team);
		rank = "R";
	}
	
	@Override
	// Method implements abstract superclass method.
	public ArrayList<ArrayList<Location>> validMoves() {
		// Handle instantiations.
		ArrayList<ArrayList<Location>> moves = new ArrayList<ArrayList<Location>>();
		for (int i = 0; i <= Board.WEST; i++) moves.add(new ArrayList<Location>());

		// Loop enumerates all locations a rook can move to along the 4 cardinal directions, stopping when there are no more valid
		// locations on the board.
		boolean hasMoves = true;
		for (int i = 1; hasMoves; i++) {
			hasMoves = false;
			if (y + i < Board.HEIGHT) {
				// We've found a new location in the northern direction.
				hasMoves = true;
				moves.get(Board.NORTH).add(new Location(x, y + i));
			}
			if (x + i < Board.WIDTH) {
				// We've found a new location in the eastern direction.
				hasMoves = true;
				moves.get(Board.EAST).add(new Location(x + i, y));
			}
			if (y - i >= 0) {
				// We've found a new location in the southern direction.
				hasMoves = true;
				moves.get(Board.SOUTH).add(new Location(x, y - i));
			}
			if (x - i >= 0) {
				// We've found a new location in the western direction.
				hasMoves = true;
				moves.get(Board.WEST).add(new Location(x - i, y));
			}
		}
		
		return moves;
	}
	
}
