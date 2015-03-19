package pieces;

import java.util.ArrayList;

import chess.Color;
import chess.Location;

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
	
	public abstract ArrayList<ArrayList<Location>> validMoves();
	
	public void moveTo(Location dest) {
		hasMoved = true;
		x = dest.x;
		y = dest.y;
	}
	
	public String toString() {
		return team == Color.WHITE ? "w" + rank : "b" + rank;
	}
	
	public boolean hasMoved() {
		return hasMoved;
	}
	
	public Color getTeam() {
		return team;
	}
	
}
