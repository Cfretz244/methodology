package pieces;

import java.util.HashSet;
import java.util.Set;

import chess.Board;
import chess.Color;

public class Pawn extends Piece {
	
	public Pawn(int x, int y, Color team) {
		super(x, y, team);
		rank = "p";
	}
	
	public Set<Integer[]> validMoves() {
		Set<Integer[]> moves = new HashSet<Integer[]>();

		if (!hasMoved) moves.add(new Integer[] {x, y + 2});
		if (y + 1 < Board.HEIGHT) moves.add(new Integer[] {x, y + 1});

		return moves;
	}
	
}
