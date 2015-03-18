package pieces;

import chess.Color;
import chess.Tile;

public class Queen extends Piece {
	
	public Queen(int x, int y, Color team) {
		super(x, y, team);
		rank = "Q";
	}
	
	public boolean moveTo(Tile dest) {
		return false;
	}

}
