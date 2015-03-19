package pieces;

import java.util.ArrayList;

import chess.Board;
import chess.Color;
import chess.Location;

public class Knight extends Piece {
	
	public Knight(int x, int y, Color team) {
		super(x, y, team);
		super.rank = "N";
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
		 * X -			- X
		 * W	  K	  	  E
		 * X - 			- X
		 * 		|	|
		 * 		X S X
		 */
		for (int i = -2; i <= 2; i++) {
			if (i == 0) continue;
			for (int j = -2; j <= 2; j++) {
				if (j == 0 || Math.abs(i) == Math.abs(j)) continue;
				
				if (x + i >= 0 && x + i < Board.WIDTH && y + j >= 0 && y + j < Board.HEIGHT) {
					ArrayList<Location> direction;
					if (Math.abs(i) == 1 && j > 0) {
						direction = moves.get(Board.NORTH);
					} else if (i > 0 && Math.abs(j) == 1) {
						direction = moves.get(Board.EAST);
					} else if (Math.abs(i) == 1 && j < 0) {
						direction = moves.get(Board.SOUTH);
					} else {
						direction = moves.get(Board.WEST);
					}
					direction.add(new Location(x + i, y + j));
				}
			}
		}
		
		return moves;
	}
	
}
