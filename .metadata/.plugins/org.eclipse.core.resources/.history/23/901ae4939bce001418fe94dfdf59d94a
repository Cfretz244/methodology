package pieces;

import java.util.ArrayList;

import chess.Board;
import chess.Color;
import chess.Location;

public class King extends Piece {
	
	public King(int x, int y, Color team) {
		super(x, y, team);
		rank = "K";
	}
	
	@Override
	// Method implements abstract superclass method.
	public ArrayList<ArrayList<Location>> validMoves() {
		// Handle instantiations.
		ArrayList<ArrayList<Location>> moves = new ArrayList<ArrayList<Location>>();
		for (int i = 0; i <= Board.CASTLE; i++) moves.add(new ArrayList<Location>());
		
		// King can move a single tile in all directions, so this is a combination of the rook and bishop logic.
		if (y + 1 < Board.HEIGHT) moves.get(Board.NORTH).add(new Location(x, y + 1));
		if (x + 1 < Board.WIDTH) {
			if (y + 1 < Board.HEIGHT) moves.get(Board.NEAST).add(new Location(x + 1, y + 1));
			moves.get(Board.EAST).add(new Location(x + 1, y));
			if (y - 1 >= 0) moves.get(Board.SEAST).add(new Location(x + 1, y - 1));
		}
		if (y - 1 >= 0) moves.get(Board.SOUTH).add(new Location(x, y - 1));
		if (x - 1 >= 0) {
			if (y - 1 >= 0) moves.get(Board.SWEST).add(new Location(x - 1, y - 1));
			moves.get(Board.WEST).add(new Location(x - 1, y));
			if (y + 1 < Board.HEIGHT) moves.get(Board.NWEST).add(new Location(x - 1, y + 1));
		}
		
		// We also need to remember that kings can castle if they haven't moved yet.
		if (!hasMoved) {
			moves.get(Board.CASTLE).add(new Location(0, y));
			moves.get(Board.CASTLE).add(new Location(Board.WIDTH - 1, y));
		}
		
		return moves;
	}

}
