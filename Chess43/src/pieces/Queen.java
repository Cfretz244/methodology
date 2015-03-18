package pieces;

import java.util.HashSet;
import java.util.Set;

import chess.Board;
import chess.Color;

public class Queen extends Piece {
	
	public Queen(int x, int y, Color team) {
		super(x, y, team);
		rank = "Q";
	}
	
	public Set<Integer[]> validMoves() {
		Set<Integer[]> moves = new HashSet<Integer[]>();
		
		boolean hasMoves = true;
		for (int i = 1; hasMoves; i++) {
			hasMoves = false;
			if (x - i >= 0) {
				hasMoves = true;
				if (y + i < Board.HEIGHT) moves.add(new Integer[] {x - i, y + i});
				if (y - i >= 0) moves.add(new Integer[] {x - i, y - i});
				moves.add(new Integer[] {x - i, y});
			}
			if (y + i < Board.HEIGHT) {
				hasMoves = true;
				moves.add(new Integer[] {x, y + i});
			}
			if (y - i >= 0) {
				hasMoves = true;
				moves.add(new Integer[] {x, y - i});
			}
			if (x + i < Board.WIDTH) {
				hasMoves = true;
				if (y + i < Board.HEIGHT) moves.add(new Integer[] {x + i, y + i});
				if (y - i >= 0) moves.add(new Integer[] {x + i, y - i});
				moves.add(new Integer[] {x + i, y});
			}
		}
		
		return moves;
	}
	
}
