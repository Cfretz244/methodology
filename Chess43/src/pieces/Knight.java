package pieces;

import java.util.ArrayList;

import chess.Board;
import chess.Color;
import chess.Location;

public class Knight extends Piece {
	
	public Knight(int x, int y, Color team) {
		super(x, y, team);
		rank = "N";
	}
	
	@Override
	// Method implements abstract superclass method.
	public ArrayList<ArrayList<Location>> validMoves() {
		// Handle instantiations.
		ArrayList<ArrayList<Location>> moves = new ArrayList<ArrayList<Location>>();
		for (int i = 0; i <= Board.WEST; i++) moves.add(new ArrayList<Location>());
		
		// Knights are weird, so this loop iterates across all locations a knight can move to in one move,
		// and groups together positions into the 4 cardinal directions.
		// Example:
		/*
		 * 		X	X
		 * 		| N |
		 * X -		   - X
		 * W	  K	  	 E
		 * X - 		   - X
		 * 		|	|
		 * 		X S X
		 */
		for (int i = -2; i <= 2; i++) {
			// No legal positions coincide with i == 0, so we skip it.
			if (i == 0) continue;
			for (int j = -2; j <= 2; j++) {
				// No legal positions coincide with j == 0 or when the absolute value of i and j are equal, so we skip them.
				if (j == 0 || Math.abs(i) == Math.abs(j)) continue;
				
				if (x + i >= 0 && x + i < Board.WIDTH && y + j >= 0 && y + j < Board.HEIGHT) {
					ArrayList<Location> direction;
					if (Math.abs(i) == 1 && j > 0) {
						// We found a new location in the northern direction.
						direction = moves.get(Board.NORTH);
					} else if (i > 0 && Math.abs(j) == 1) {
						// We found a new location in the eastern direction.
						direction = moves.get(Board.EAST);
					} else if (Math.abs(i) == 1 && j < 0) {
						// We found a new location in the southern direction.
						direction = moves.get(Board.SOUTH);
					} else {
						// We found a new location in the western direction.
						direction = moves.get(Board.WEST);
					}
					direction.add(new Location(x + i, y + j));
				}
			}
		}
		
		return moves;
	}
	
}
