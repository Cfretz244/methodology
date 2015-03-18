package pieces;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import chess.Board;
import chess.Color;

public class King extends Piece {
	
	public King(int x, int y, Color team) {
		super(x, y, team);
		rank = "K";
	}
	
	public Set<List<Integer>> validMoves() {
		Set<List<Integer>> moves = new HashSet<List<Integer>>();
		
		if (!hasMoved) {
			moves.add(Arrays.asList(0, y));
			moves.add(Arrays.asList(Board.WIDTH, y));
		}
		if (x - 1 >= 0) {
			moves.add(Arrays.asList(x - 1, y));
			if (y + 1 < Board.HEIGHT) moves.add(Arrays.asList(x - 1, y + 1));
			if (y - 1 >= 0) moves.add(Arrays.asList(x - 1, y - 1));
		}
		if (y + 1 < Board.HEIGHT) moves.add(Arrays.asList(x, y + 1));
		if (y - 1 >= 0) moves.add(Arrays.asList(x, y - 1));
		if (x + 1 < Board.WIDTH) {
			moves.add(Arrays.asList(x + 1, y));
			if (y + 1 < Board.HEIGHT) moves.add(Arrays.asList(x + 1, y + 1));
			if (y - 1 >= 0) moves.add(Arrays.asList(x + 1, y - 1));
		}
		
		return moves;
	}

}
