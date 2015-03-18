package chess;

import pieces.Piece;

public class Tile {
	
	private int x, y;
	private Color color;
	
	public Piece piece;
	
	public Tile(int x, int y, Color color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Color getColor() {
		return color;
	}
	
	public String toString() {
		if (piece == null) {
			return color == Color.WHITE ? "  " : "##";
		} else {
			return piece.toString();
		}
	}

}
