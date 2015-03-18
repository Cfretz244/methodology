package pieces;

import java.util.Set;

import chess.Color;
import chess.Tile;

public abstract class Piece {
	
	protected int x, y;
	protected boolean hasMoved;
	protected Color team;
	protected String rank;
	
	public Piece(int x, int y, Color team) {
		this.x = x;
		this.y = y;
		this.team = team;
		hasMoved = false;
	}
	
	public abstract Set<Integer[]> validMoves();
	
	public void moveTo(Tile dest) {
		hasMoved = true;
		x = dest.getX();
		y = dest.getY();
	}
	
	public String toString() {
		return team == Color.WHITE ? "w" + rank : "b" + rank;
	}
	
}
