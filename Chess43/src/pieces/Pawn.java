package pieces;

import java.util.ArrayList;

import chess.Board;
import chess.Color;
import chess.Location;

// Class represents a pawn.
public class Pawn extends Piece {
	
	public Pawn(int x, int y, Color team) {
		super(x, y, team);
		rank = "p";
	}
	
	@Override
	// Method implements abstract superclass method.
	public ArrayList<ArrayList<Location>> validMoves() {
		// Handle instantiations.
		ArrayList<ArrayList<Location>> moves = new ArrayList<ArrayList<Location>>();
		for (int i = 0; i <= Board.SOUTH; i++) moves.add(new ArrayList<Location>());

		// Pawns move in different directions based on the team they're on. This handles that.
		if (team == Color.WHITE && y + 1 < Board.HEIGHT) moves.get(Board.NORTH).add(new Location(x, y + 1));
		else if (team == Color.BLACK && y - 1 >= 0) moves.get(Board.SOUTH).add(new Location(x, y - 1));

		// Pawns can move two square if they haven't moved before. This handles that.
		if (!hasMoved) moves.get(team == Color.WHITE ? Board.NORTH : Board.SOUTH).add(team == Color.WHITE ? new Location(x, y + 2) : new Location(x, y - 2));

		return moves;
	}
	
}
