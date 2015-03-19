package pieces;

import java.util.ArrayList;

import chess.Board;
import chess.Color;
import chess.Location;

public class Rook extends Piece {
	
	public Rook(int x, int y, Color team) {
		super(x, y, team);
		rank = "R";
	}
	
	@Override
	public ArrayList<ArrayList<Location>> validMoves() {
		ArrayList<ArrayList<Location>> moves = new ArrayList<ArrayList<Location>>();
		for (int i = 0; i <= Board.WEST; i++) moves.add(new ArrayList<Location>());

		boolean hasMoves = true;
		for (int i = 1; hasMoves; i++) {
			hasMoves = false;
			if (y + i < Board.HEIGHT) {
				hasMoves = true;
				moves.get(Board.NORTH).add(new Location(x, y + i));
			}
			if (x + i < Board.WIDTH) {
				hasMoves = true;
				moves.get(Board.EAST).add(new Location(x + i, y));
			}
			if (y - i >= 0) {
				hasMoves = true;
				moves.get(Board.SOUTH).add(new Location(x, y - i));
			}
			if (x - i >= 0) {
				hasMoves = true;
				moves.get(Board.WEST).add(new Location(x - i, y));
			}
		}
		
		return moves;
	}
	
}
