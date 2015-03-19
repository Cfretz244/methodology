package pieces;

import java.util.ArrayList;

import chess.Board;
import chess.Color;
import chess.Location;

public class Queen extends Piece {
	
	public Queen(int x, int y, Color team) {
		super(x, y, team);
		rank = "Q";
	}
	
	public ArrayList<ArrayList<Location>> validMoves() {
		ArrayList<ArrayList<Location>> moves = new ArrayList<ArrayList<Location>>();
		for (int i = 0; i <= Board.NWEST; i++) moves.add(new ArrayList<Location>());
		
		boolean hasMoves = true;
		for (int i = 1; hasMoves; i++) {
			hasMoves = false;
			if (y + i < Board.HEIGHT) {
				hasMoves = true;
				moves.get(Board.NORTH).add(new Location(x, y + i));
			}
			if (x + i < Board.WIDTH) {
				hasMoves = true;
				if (y + i < Board.HEIGHT) moves.get(Board.NEAST).add(new Location(x + i, y + i));
				moves.get(Board.EAST).add(new Location(x + i, y));
				if (y - i >= 0) moves.get(Board.SEAST).add(new Location(x + i, y - i));
			}
			if (y - i >= 0) {
				hasMoves = true;
				moves.get(Board.SOUTH).add(new Location(x, y - i));
			}
			if (x - i >= 0) {
				hasMoves = true;
				if (y - i >= 0) moves.get(Board.SWEST).add(new Location(x - i, y - i));
				moves.get(Board.WEST).add(new Location(x - i, y));
				if (y + i < Board.HEIGHT) moves.get(Board.NWEST).add(new Location(x - i, y + i));
			}
		}
		
		return moves;
	}
	
}
