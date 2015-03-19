package pieces;

import java.util.ArrayList;

import chess.Board;
import chess.Color;
import chess.Location;

public class Pawn extends Piece {
	
	public Pawn(int x, int y, Color team) {
		super(x, y, team);
		rank = "p";
	}
	
	public ArrayList<ArrayList<Location>> validMoves() {
		ArrayList<ArrayList<Location>> moves = new ArrayList<ArrayList<Location>>();
		for (int i = 0; i <= Board.SOUTH; i++) moves.add(new ArrayList<Location>());

		if (team == Color.WHITE && y + 1 < Board.HEIGHT) moves.get(Board.NORTH).add(new Location(x, y + 1));
		else if (team == Color.BLACK && y - 1 >= 0) moves.get(Board.SOUTH).add(new Location(x, y - 1));

		if (!hasMoved) moves.get(team == Color.WHITE ? Board.NORTH : Board.SOUTH).add(team == Color.WHITE ? new Location(x, y + 2) : new Location(x, y - 2));

		return moves;
	}
	
}
