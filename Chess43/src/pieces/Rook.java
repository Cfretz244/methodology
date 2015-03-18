package pieces;

import chess.Color;
import chess.Tile;

public class Rook extends Piece {
	
	public Rook(int x, int y, Color team) {
		super(x, y, team);
		rank = "R";
	}
	
	public boolean moveTo(Tile dest) {
		return false;
	}

}
