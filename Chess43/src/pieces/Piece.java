package pieces;

import chess.Color;
import chess.Tile;

public abstract class Piece {
	
	protected int x, y;
	protected Color team;
	protected String rank;
	
	public Piece(int x, int y, Color team) {
		this.x = x;
		this.y = y;
		this.team = team;
	}
	
	public abstract boolean moveTo(Tile dest);
	
	public String toString() {
		return team == Color.WHITE ? "w" + rank : "b" + rank;
	}
	
}
