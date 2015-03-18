package pieces;

import java.util.HashSet;
import java.util.Set;

import chess.Board;
import chess.Color;

public class Knight extends Piece {
	
	public Knight(int x, int y, Color team) {
		super(x, y, team);
		super.rank = "N";
	}
	
	public Set<Integer[]> validMoves() {
		Set<Integer[]> moves = new HashSet<Integer[]>();
		
		for (int i = -2; i <= 2; i++) {
			if (i == 0) continue;
			for (int j = -2; j <= 2; j++) {
				if (j == 0 || i == j) continue;
				if (x + i >= 0 && x + i < Board.WIDTH && y + j >= 0 && y + j < Board.HEIGHT) moves.add(new Integer[] {x + i, y + j});
			}
		}
		
		return moves;
	}
	
}
