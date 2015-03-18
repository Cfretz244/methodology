package pieces;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import chess.Board;
import chess.Color;

public class Rook extends Piece {
	
	public Rook(int x, int y, Color team) {
		super(x, y, team);
		rank = "R";
	}
	
	public Set<List<Integer>> validMoves() {
		Set<List<Integer>> moves = new HashSet<List<Integer>>();

		if (!hasMoved) moves.add(Arrays.asList(Board.KING_STARTING_X, y));
		boolean hasMoves = true;
		for (int i = 1; hasMoves; i++) {
			hasMoves = false;
			if (x - i >= 0) {
				hasMoves = true;
				moves.add(Arrays.asList(x - i, y));
			}
			if (x + i < Board.WIDTH) {
				hasMoves = true;
				moves.add(Arrays.asList(x + i, y));
			}
			if (y - i >= 0) {
				hasMoves = true;
				moves.add(Arrays.asList(x, y - i));
			}
			if (y + i < Board.HEIGHT) {
				hasMoves = true;
				moves.add(Arrays.asList(x, y + i));
			}
		}
		
		return moves;
	}
	
}
