package pieces;

import java.util.HashSet;
import java.util.Set;

import chess.Board;
import chess.Color;

public class King extends Piece {
	
	public King(int x, int y, Color team) {
		super(x, y, team);
		rank = "K";
	}
	
	public Set<Integer[]> validMoves() {
		Set<Integer[]> moves = new HashSet<Integer[]>();
		
		if (x - 1 >= 0) {
			moves.add(new Integer[] {x - 1, y});
			if (y + 1 < Board.HEIGHT) moves.add(new Integer[] {x - 1, y + 1});
			if (y - 1 >= 0) moves.add(new Integer[] {x - 1, y - 1});
		}
		if (y + 1 < Board.HEIGHT) moves.add(new Integer[] {x, y + 1});
		if (y - 1 >= 0) moves.add(new Integer[] {x, y - 1});
		if (x + 1 < Board.WIDTH) {
			moves.add(new Integer[] {x + 1, y});
			if (y + 1 < Board.HEIGHT) moves.add(new Integer[] {x + 1, y + 1});
			if (y - 1 >= 0) moves.add(new Integer[] {x + 1, y - 1});
		}
		
		return moves;
	}

}
