package chess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

public class Board {

	private Tile[][] board;
	private Map<String, Set<Piece>> pieces;
	public static final int HEIGHT = 8, WIDTH = 8;
	public static final int KING_STARTING_X = 4, CASTLE = 8;
	public static final int NORTH = 0, EAST = 2, SOUTH = 4, WEST = 6;
	public static final int NEAST = 1, SEAST = 3, SWEST = 5, NWEST = 7;

	public Board() {
		pieces = new HashMap<String, Set<Piece>>();
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
					fresh.piece = new Pawn(x, y, team);
				}
				if (fresh.piece != null) {
					if (pieces.get(fresh.piece.toString()) == null) pieces.put(fresh.piece.toString(), new HashSet<Piece>());
					pieces.get(fresh.piece.toString()).add(fresh.piece);
				}
			}
			colored = !colored;
		}
	}

	public boolean movePiece(Location from, Location to, Color currentColor) {
		Tile start = board[from.x][from.y];
		Tile end = board[to.x][to.y];
		if (start.piece == null || start.piece.getTeam() != currentColor) return false;

		Piece piece = start.piece;
		ArrayList<ArrayList<Location>> moves = piece.validMoves();
		int[] directions = getDirections(piece);
		int chosenDirection = canMoveTo(piece, moves, directions, from, to);
		if (chosenDirection == -1) return false;

		if (chosenDirection != Board.CASTLE) {
			if (end.piece != null) {
				if (end.piece.getTeam() == piece.getTeam()) return false;
				pieces.get(end.piece.toString()).remove(end.piece);
			}
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
	
	public int canMoveTo(Piece piece, ArrayList<ArrayList<Location>> moves, int[] directions, Location from, Location to) {
		boolean valid = true;
		int chosenDirection = -1;
		for (int direction : directions) {
			int index = moves.get(direction).indexOf(new Location(to.x, to.y));
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
		if (!(piece instanceof Pawn) && valid && chosenDirection != -1) {
			return chosenDirection;
		} else if (piece instanceof Pawn && pawnCheck(from, to, piece)) {
			return 0;
		} else if (piece instanceof King && castleCheck(from, to, piece) && chosenDirection != -1) {
			return chosenDirection;
		} else {
			return -1;
		}
	}
	
	public int[] getDirections(Piece piece) {
		int[] directions;
		if (piece instanceof Pawn) directions = new int[] {Board.NORTH, Board.SOUTH};
		else if (piece instanceof Rook) directions = new int[] {Board.NORTH, Board.EAST, Board.SOUTH, Board.WEST};
		else if (piece instanceof Knight) directions = new int[] {Board.NORTH, Board.EAST, Board.SOUTH, Board.WEST};
		else if (piece instanceof Bishop) directions = new int[] {Board.NEAST, Board.SEAST, Board.SWEST, Board.NWEST};
		else if (piece instanceof Queen) directions = new int[] {Board.NORTH, Board.NEAST, Board.EAST, Board.SEAST, Board.SOUTH, Board.SWEST, Board.WEST, Board.NWEST};
		else directions = new int[] {Board.NORTH, Board.NEAST, Board.EAST, Board.SEAST, Board.SOUTH, Board.SWEST, Board.WEST, Board.NWEST, Board.CASTLE};
		return directions;
	}

	public boolean pawnCheck(Location from, Location to, Piece piece) {
		if (from.x == to.x && board[to.x][to.y].piece != null) {
			return false;
		} else if (from.x != to.x) {
			if (board[to.x][to.y].piece == null) {
				return false;
			} else if (piece.getTeam() == Color.WHITE && to.y != from.y + 1) {
				return false;
			} else if (piece.getTeam() == Color.BLACK && to.y != from.y - 1) {
				return false;
			}
		} else if (piece.getTeam() == Color.WHITE && from.y > to.y) {
			return false;
		} else if (piece.getTeam() == Color.BLACK && from.y < to.y) {
			return false;
		}
		return true;
	}

	public boolean castleCheck(Location from, Location to, Piece piece) {
		Piece other = board[to.x][to.y].piece;
		if (other == null || (!(other instanceof King) && !(other instanceof Knight))) return false;
		return !piece.hasMoved() && !other.hasMoved();
	}

	@Override
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