package pieces;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import chess.Board;
import chess.Color;

public class Queen extends Piece {
	
	public Queen(int x, int y, Color team) {
		super(x, y, team);
		rank = "Q";
	}
	
	public Set<List<Integer>> validMoves() {
		Set<List<Integer>> moves = new HashSet<List<Integer>>();
		
		boolean hasMoves = true;
		for (int i = 1; hasMoves; i++) {
			hasMoves = false;
			if (x - i >= 0) {
				hasMoves = true;
				if (y + i < Board.HEIGHT) moves.add(Arrays.asList(x - i, y + i));
				if (y - i >= 0) moves.add(Arrays.asList(x - i, y - i));
				moves.add(Arrays.asList(x - i, y));
			}
			if (y + i < Board.HEIGHT) {
				hasMoves = true;
				moves.add(Arrays.asList(x, y + i));
			}
			if (y - i >= 0) {
				hasMoves = true;
				moves.add(Arrays.asList(x, y - i));
			}
			if (x + i < Board.WIDTH) {
				hasMoves = true;
				if (y + i < Board.HEIGHT) moves.add(Arrays.asList(x + i, y + i));
				if (y - i >= 0) moves.add(Arrays.asList(x + i, y - i));
				moves.add(Arrays.asList(x + i, y));
			}
		}
		
		return moves;
	}
	
}
