package pieces;

import java.util.ArrayList;

import chess.Color;
import chess.Location;

// Class is the parent class of all other pieces, and defines some base functionality.
public abstract class Piece {
	
	// Coordinates.
	protected int x, y;
	
	// Whether or not the piece has moved. Useful for castling.
	protected boolean hasMoved;

	// Whether the piece is white or black.
	protected Color team;
	
	// The rank of the piece ("p" for pawn, "R" for rook, "B" for bishop, etc).
	protected String rank;
	
	public Piece(int x, int y, Color team) {
		this.x = x;
		this.y = y;
		this.team = team;
		hasMoved = false;
	}
	
	/* Abstract method must be overridden by all subclasses.
	 * Concept of the method is to return all valid locations that the given piece could move to if it were the only piece on the board.
	 * The design choice that I made was to separate the board logic from the pieces such that a piece would return all moves that it can legally make
	 * without taking into account where the other pieces are, and then the board makes decisions about which moves the piece can actually make
	 * based on the locations of the other pieces.
	 * Specifically, the moves are returned as an array of arrays, where each of the constituent arrays contain moves that the piece can make in a specific
	 * direction. Thus, code of the form:
	 * 		ArrayList<ArrayList<Location>> moves = piece.validMoves();
	 * 		moves.get(Board.NORTH)
	 * would return an ArrayList of all the locations the specified piece can move to in the specified direction.
	 * This allows the board to efficiently calculate whether or not a piece can move to the specified location by checking if all intervening tiles are
	 * unoccupied (if the piece isn't a Knight, as they can jump over other pieces).
	 */
	public abstract ArrayList<ArrayList<Location>> validMoves();
	
	// Method moves a piece.
	public void moveTo(Location dest) {
		hasMoved = true;
		x = dest.x;
		y = dest.y;
	}
	
	@Override
	// Method returns a string representation of the piece ("bp" for black pawn, "wK" for white king, etc).
	public String toString() {
		return team == Color.WHITE ? "w" + rank : "b" + rank;
	}
	
	public Location getLocation() {
		return new Location(x, y);
	}
	
	public boolean hasMoved() {
		return hasMoved;
	}
	
	public void resetMovement() {
		hasMoved = false;
	}
	
	public Color getTeam() {
		return team;
	}
	
}