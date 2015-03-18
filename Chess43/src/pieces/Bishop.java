package pieces;

import chess.Color;
import chess.Tile;

public class Bishop extends Piece {
	
	public Bishop(int x, int y, Color team) {
		super(x, y, team);
		rank = "B";
	}
	
	public boolean moveTo(Tile dest) {
		return false;
	}

}
