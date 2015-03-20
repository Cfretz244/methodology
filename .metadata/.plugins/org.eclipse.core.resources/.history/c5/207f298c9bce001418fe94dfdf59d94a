package pieces;

import java.util.ArrayList;

import chess.Board;
import chess.Color;
import chess.Location;

public class Queen extends Piece {
	
	public Queen(int x, int y, Color team) {
		super(x, y, team);
		rank = "Q";
	}
	
	@Override
	// Method implements abstract superclass method.
	public ArrayList<ArrayList<Location>> validMoves() {
		// Handle instantiations.
		ArrayList<ArrayList<Location>> moves = new ArrayList<ArrayList<Location>>();
		for (int i = 0; i <= Board.NWEST; i++) moves.add(new ArrayList<Location>());
		
		// Queens can move in both diagonal and cardinal directions, so this is a combination of the rook and bishop logic.
		boolean hasMoves = true;
		for (int i = 1; hasMoves; i++) {
			hasMoves = false;
			if (y + i < Board.HEIGHT) {
				// We've found a new location in the northern direction.
				hasMoves = true;
				moves.get(Board.NORTH).add(new Location(x, y + i));
			}
			if (x + i < Board.WIDTH) {
				hasMoves = true;

				// Check if we have a location in the northeastern direction
				if (y + i < Board.HEIGHT) moves.get(Board.NEAST).add(new Location(x + i, y + i));
				
				// We've found a location in the eastern direction.
				moves.get(Board.EAST).add(new Location(x + i, y));
				
				// Check if we have a location in the southeastern direction.
				if (y - i >= 0) moves.get(Board.SEAST).add(new Location(x + i, y - i));
			}
			if (y - i >= 0) {
				hasMoves = true;
				
				// We've found a location in the southern direction.
				moves.get(Board.SOUTH).add(new Location(x, y - i));
			}
			if (x - i >= 0) {
				hasMoves = true;
				// Check if we have a location in the southwestern direction.
				if (y - i >= 0) moves.get(Board.SWEST).add(new Location(x - i, y - i));
				
				// We've found a location in the western direction.
				moves.get(Board.WEST).add(new Location(x - i, y));
				
				// Check if we have a location in the northwester direction.
				if (y + i < Board.HEIGHT) moves.get(Board.NWEST).add(new Location(x - i, y + i));
			}
		}
		
		return moves;
	}
	
}
