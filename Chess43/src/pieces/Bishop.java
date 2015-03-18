package pieces;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import chess.Board;
import chess.Color;

public class Bishop extends Piece {
	
	public Bishop(int x, int y, Color team) {
		super(x, y, team);
		rank = "B";
	}
	
	public Set<List<Integer>> validMoves() {
		Set<List<Integer>> moves = new HashSet<List<Integer>>();
		
		boolean hasMoves = true;
		for (int i = 1; hasMoves; i++) {
			hasMoves = false;
			if (x - i >= 0 && y + i < Board.HEIGHT) {
				hasMoves = true;
				moves.add(Arrays.asList(x - i, y + i));
			}
			if (x + i < Board.WIDTH && y + i < Board.HEIGHT) {
				hasMoves = true;
				moves.add(Arrays.asList(x + i, y + i));
			}
			if (x - i >= 0 && y - i >= 0) {
				hasMoves = true;
				moves.add(Arrays.asList(x - i, y - i));
			}
			if (x + i < Board.WIDTH && y - i >= 0) {
				hasMoves = true;
				moves.add(Arrays.asList(x + i, y - i));
			}
		}
		
		return moves;
	}

}
