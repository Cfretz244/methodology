package pieces;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import chess.Board;
import chess.Color;

public class Pawn extends Piece {
	
	public Pawn(int x, int y, Color team) {
		super(x, y, team);
		rank = "p";
	}
	
	public Set<List<Integer>> validMoves() {
		Set<List<Integer>> moves = new HashSet<List<Integer>>();

		if (!hasMoved) moves.add(Arrays.asList(x, y + 2));
		if (y + 1 < Board.HEIGHT) moves.add(Arrays.asList(x, y + 1));

		return moves;
	}
	
}
