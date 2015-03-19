package chess;

import java.util.ArrayList;

import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

public class Board {

	private Tile[][] board;
	public static final int HEIGHT = 8, WIDTH = 8;
	public static final int KING_STARTING_X = 4, CASTLE = 8;
	public static final int NORTH = 0, EAST = 2, SOUTH = 4, WEST = 6;
	public static final int NEAST = 1, SEAST = 3, SWEST = 5, NWEST = 7;

	public Board() {
		board = new Tile[WIDTH][HEIGHT];
		boolean colored = true;
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++, colored = !colored) {
				Color shading = colored ? Color.BLACK : Color.WHITE;
				Tile fresh = new Tile(x, y, shading);
				board[x][y] = fresh;

				Color team = y < 4 ? Color.WHITE : Color.BLACK;
				if (y == 0 || y == 7) {
					switch (x) {
					case 0:
					case 7:
						fresh.piece = new Rook(x, y, team);
						break;
					case 1:
					case 6:
						fresh.piece = new Knight(x, y, team);
						break;
					case 2:
					case 5:
						fresh.piece = new Bishop(x, y, team);
						break;
					case 3:
						fresh.piece = new Queen(x, y, team);
						break;
					case 4:
						fresh.piece = new King(x, y, team);
						break;
					}
				} else if (y == 1 || y == 6) {
					board[x][y].piece = new Pawn(x, y, team);
				}
			}
			colored = !colored;
		}
	}

	public boolean movePiece(Location from, Location to) {
		Tile start = board[from.x][from.y];
		Tile end = board[to.x][to.y];
		if (start.piece == null) return false;

		Piece piece = start.piece;
		ArrayList<ArrayList<Location>> moves = piece.validMoves();
		int[] directions;
		if (piece instanceof Pawn) directions = new int[] {Board.NORTH, Board.SOUTH};
		else if (piece instanceof Rook) directions = new int[] {Board.NORTH, Board.EAST, Board.SOUTH, Board.WEST, Board.CASTLE};
		else if (piece instanceof Knight) directions = new int[] {Board.NORTH, Board.EAST, Board.SOUTH, Board.WEST};
		else if (piece instanceof Bishop) directions = new int[] {Board.NEAST, Board.SEAST, Board.SWEST, Board.SEAST};
		else if (piece instanceof Queen) directions = new int[] {Board.NORTH, Board.NEAST, Board.EAST, Board.SEAST, Board.SOUTH, Board.SWEST, Board.WEST, Board.NWEST};
		else directions = new int[] {Board.NORTH, Board.NEAST, Board.EAST, Board.SEAST, Board.SOUTH, Board.SWEST, Board.WEST, Board.NWEST, Board.CASTLE};

		boolean valid = true;
		int chosenDirection = -1;
		for (int direction : directions) {
			int index = moves.get(direction).indexOf(new Location(end.getX(), end.getY()));
			if (index >= 0) {
				chosenDirection = direction;
				for (int i = 0; i < index && !(piece instanceof Knight) && direction != Board.CASTLE; i++) {
					Location locale = moves.get(direction).get(i);
					Tile current = board[locale.x][locale.y];
					if (current.piece != null) {
						valid = false;
						break;
					}
				}
				break;
			}
		}
		if (!valid || chosenDirection == -1 || !pawnCheck(from, to, piece) || !castleCheck(from, to, piece)) {
			return false;
		}

		if (chosenDirection != Board.CASTLE) {
			start.piece = null;
			end.piece = piece;
			piece.moveTo(to);
		} else {
			Piece other = board[to.x][to.y].piece;
			start.piece = other;
			end.piece = piece;
			piece.moveTo(to);
			other.moveTo(from);
		}
		return true;
	}

	public boolean pawnCheck(Location from, Location to, Piece piece) {
		if (!(piece instanceof Pawn)) {
			return true;
		} else if (board[to.x][to.y].piece == null) {
			return false;
		} else if (to.x != from.x - 1 && to.x != from.x + 1) {
			return false;
		} else if (piece.getTeam() == Color.WHITE && to.y != from.y + 1) {
			return false;
		} else if (piece.getTeam() == Color.BLACK && to.y != from.y - 1) {
			return false;
		}
		return true;
	}

	public boolean castleCheck(Location from, Location to, Piece piece) {
		if (!(piece instanceof Rook) && !(piece instanceof King)) {
			return true;
		} else {
			Piece other = board[to.x][to.y].piece;
			if (other == null || (!(other instanceof King) && !(other instanceof Knight))) return false;
			return !piece.hasMoved() && !other.hasMoved();
		}
	}

	public String toString() {
		String str = new String();
		for (int y = HEIGHT - 1; y >= 0; y--) {
			for (int x = 0; x < WIDTH; x++) {
				str += board[x][y] + " ";
			}
			str += (y + 1) + "\n";
		}
		str += " a  b  c  d  e  f  g  h\n";
		return str;
	}

}
