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

// Class represents a Board.
public class Board {

	// A 2d Tile array that represents the board.
	private Tile[][] board;

	// A Hashtable (HashMap) that maps piece names to sets (HashSet) of pieces. Uses the piece's toString method to generate the name.
	// This can be used for efficient access to all pieces. I imagine that it will be useful for checking if either side is in check.
	// (Example: A black pawn would produce the mapping "bp" -> {pawn_object}).
	private Map<String, Set<Piece>> pieces;

	// Constants I defined to make the code more readable.
	public static final int HEIGHT = 8, WIDTH = 8;
	public static final int KING_STARTING_X = 4, CASTLE = 8;
	public static final int NORTH = 0, EAST = 2, SOUTH = 4, WEST = 6;
	public static final int NEAST = 1, SEAST = 3, SWEST = 5, NWEST = 7;

	public Board() {
		// Handle instantiations.
		pieces = new HashMap<String, Set<Piece>>();
		board = new Tile[WIDTH][HEIGHT];

		// Nested loop iterates across all tiles on the board, inverting the color each time.
		boolean colored = true;
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++, colored = !colored) {
				// Instantiate a new tile with the current color.
				Color shading = colored ? Color.BLACK : Color.WHITE;
				Tile fresh = new Tile(x, y, shading);
				board[x][y] = fresh;

				// If y is currently less than 4, if we're adding a piece, it's going to be white. Otherwise black.
				Color team = y < 4 ? Color.WHITE : Color.BLACK;
				if (y == 0 || y == 7) {
					// Switch statement decides what type of piece we're adding based on what the current x value is.
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
					// We're adding a pawn of the specified team.
					fresh.piece = new Pawn(x, y, team);
				}
				// This if statement adds the current piece (if there is one) to the pieces hashtable. As you can see, it first instantiates
				// the Set the piece will be stored in (if it doesn't exist) using the name of the piece, and then stores the piece in the set.
				if (fresh.piece != null) {
					if (pieces.get(fresh.piece.toString()) == null) pieces.put(fresh.piece.toString(), new HashSet<Piece>());
					pieces.get(fresh.piece.toString()).add(fresh.piece);
				}
			}
			colored = !colored;
		}
	}

	// Method moves a piece from one location to another, assuming that the movement is legal.
	public int movePiece(Location from, Location to, Color currentColor, String promotion) {
		// Get the starting and ending tiles.
		Tile start = board[from.x][from.y];
		Tile end = board[to.x][to.y];
		if (start.piece == null || start.piece.getTeam() != currentColor) return -1;

		// Get the current piece, get a list of its valid moves and the directions in which the piece can move, and validate that
		// intervening tiles are empty (if the piece is not a knight). For an in depth explanation of Piece.validMoves, check the
		// Piece class.
		Piece piece = start.piece;
		ArrayList<ArrayList<Location>> moves = piece.validMoves();
		int[] directions = getDirections(piece);
		int chosenDirection = canMoveTo(piece, moves, directions, from, to);
		if (chosenDirection == -1) return 0;

		// Check if we are castling, and perform the move.
		Piece tmp = end.piece;
		if (chosenDirection != Board.CASTLE) {
			if (end.piece != null) {
				// We are capturing a piece. Make sure it isn't from our team, and then remove it from the pieces hashtable.
				if (end.piece.getTeam() == piece.getTeam()) return 0;
				pieces.get(end.piece.toString()).remove(end.piece);
			}

			// Move the piece.
			start.piece = null;
			end.piece = piece;
			piece.moveTo(to);
		} else {
			// We are castling. Check which type of castling is being performed, check that the intervening squares are unoccupied, and then
			// perform the castle.
			start.piece = null;
			if (to.x > from.x) {
				Piece rook = board[to.x + 1][to.y].piece;
				board[to.x + 1][to.y].piece = null;
				board[from.x + 1][from.y].piece = rook;
				board[from.x + 2][from.y].piece = piece;
				rook.moveTo(new Location(from.x + 1, from.y));
				piece.moveTo(new Location(from.x + 2, from.y));
			} else {
				Piece rook = board[to.x - 2][to.y].piece;
				board[to.x - 2][to.y].piece = null;
				board[from.x - 1][from.y].piece = rook;
				board[from.x - 2][from.y].piece = piece;
				rook.moveTo(new Location(from.x - 1, from.y));
				piece.moveTo(new Location(from.x - 2, from.y));
			}
		}

		boolean checked = inCheck(currentColor);
		if (checked && chosenDirection != Board.CASTLE) {
			start.piece = piece;
			end.piece = tmp;
			piece.moveTo(from);
			return 0;
		} else if (checked) {
			start.piece = piece;
			end.piece = tmp;
			piece.moveTo(from);
			tmp.moveTo(to);
			if (to.x > from.x) {
				board[from.x + 1][from.y].piece = null;
				board[from.x + 2][from.y].piece = null;
			} else {
				board[from.x - 1][from.y].piece = null;
				board[from.x - 2][from.y].piece = null;
			}
			return 0;
		}

		if (piece instanceof Pawn && (to.y == 7 || to.y == 0)) {
			Piece newPiece;
			switch (promotion) {
			case "R":
				newPiece = new Rook(to.x, to.y, currentColor);
				break;
			case "N":
				newPiece = new Knight(to.x, to.y, currentColor);
				break;
			case "B":
				newPiece = new Bishop(to.x, to.y, currentColor);
				break;
			case "Q":
			default:
				newPiece = new Queen(to.x, to.y, currentColor);
			}
			pieces.get(newPiece.toString()).add(newPiece);
			pieces.get(piece.toString()).remove(piece);
			end.piece = newPiece;
		}

		Color enemy = currentColor == Color.WHITE ? Color.BLACK : Color.WHITE;
		if (inCheck(enemy)) {
			if (mated(enemy)) return 3;
			else return 2;
		} else if (mated(enemy)){
			return 4;
		} else {
			return 1;
		}
	}

	// Method checks if the requested movement can be legally completed.
	public int canMoveTo(Piece piece, ArrayList<ArrayList<Location>> moves, int[] directions, Location from, Location to) {
		boolean valid = true;
		int chosenDirection = -1;
		// Directions is an array representing the directions that the piece can move in, and moves is an ArrayList of ArrayLists, with each
		// list representing the moves that the piece can make in a different direction, so this iterates across the directions a piece can move in
		// and checks that the requested move resides in one of the directions, and that all of the intevening tiles are unoccupied.
		for (int direction : directions) {
			// Check if the requested location exists in the current direction, and store its index if it does.
			int index = moves.get(direction).indexOf(new Location(to.x, to.y));
			if (index >= 0) {
				// We've found the direction we're moving in.
				chosenDirection = direction;

				// Loop iterates over all of the tiles contained in this direction, and if any are occupied, marks the movement as invalid.
				for (int i = 0; i < index && !(piece instanceof Knight) && direction != Board.CASTLE; i++) {
					Location locale = moves.get(direction).get(i);
					Tile current = board[locale.x][locale.y];
					if (current.piece != null) {
						valid = false;
						break;
					}
				}
				if (index == 0 || piece instanceof Knight) {
					Location locale = moves.get(direction).get(index);
					Tile current = board[locale.x][locale.y];
					if (current.piece != null && current.piece.getTeam() == piece.getTeam()) valid = false;
				}
				break;
			}
		}

		// Pawns and Kings have conditional movement (Pawns capturing another piece and a King castling), so this checks those conditions using the
		// pawnCheck and castleCheck methods.
		if (!(piece instanceof Pawn) && valid && chosenDirection != -1 && chosenDirection != Board.CASTLE) {
			return chosenDirection;
		} else if (piece instanceof Pawn && pawnCheck(from, to, piece)) {
			return 0;
		} else if (piece instanceof King && castleCheck(from, to, piece) && chosenDirection != -1) {
			return chosenDirection;
		} else {
			return -1;
		}
	}

	// Method returns the valid directions for a given piece.
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

	public boolean inCheck(Color team) {
		Piece king = pieces.get((team == Color.WHITE ? "w" : "b") + "K").iterator().next();

		for (Set<Piece> subset : pieces.values()) {
			for (Piece piece : subset) {
				if (piece.getTeam() != team) {
					ArrayList<ArrayList<Location>> moves = piece.validMoves();
					int[] directions = getDirections(piece);
					if (canMoveTo(piece, moves, directions, piece.getLocation(), king.getLocation()) != -1) return true;
				}
			}
		}

		return false;
	}

	public boolean mated(Color team) {
		for (Set<Piece> subset : pieces.values()) {
			for (Piece piece : subset) {
				if (piece.getTeam() == team) {
					ArrayList<ArrayList<Location>> moves = piece.validMoves();
					Location from = piece.getLocation();
					int[] directions = getDirections(piece);
					for (int direction : directions) {
						for (Location to : moves.get(direction)) {
							if (canMoveTo(piece, moves, directions, from, to) != -1) {
								Piece tmp = board[to.x][to.y].piece;
								boolean hasMoved = piece.hasMoved();
								board[from.x][from.y].piece = null;
								board[to.x][to.y].piece = piece;
								piece.moveTo(to);
								boolean checked = inCheck(team);
								piece.moveTo(from);
								if (!hasMoved) piece.resetMovement();
								board[from.x][from.y].piece = piece;
								board[to.x][to.y].piece = tmp;
								if (!checked) return false;	
							}
						}
					}
				}
			}
		}
		return true;
	}

	// Pawn movement is unique amongst chess pieces, so this method actually handles all validation for whether or not a pawn can move
	// from one square to another.
	public boolean pawnCheck(Location from, Location to, Piece piece) {
		if (from.x == to.x) {
			if ((from.y > to.y && from.y - to.y == 2 && board[to.x][to.y + 1].piece != null) || (from.y < to.y && to.y - from.y == 2 && board[to.x][to.y - 1].piece != null)) {
				return false;
			} else if (board[to.x][to.y].piece != null) {
				return false;
			}
		} else if (from.x != to.x) {
			if (board[to.x][to.y].piece == null && !(board[to.x][from.y].piece instanceof Pawn)) {
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
		
		if (from.x != to.x && board[to.x][to.y].piece == null && board[to.x][from.y].piece instanceof Pawn) {
			Piece captured = board[to.x][from.y].piece;
			pieces.get(captured.toString()).remove(captured);
			board[to.x][from.y].piece = null;
		}
		return true;
	}

	// Method checks whether or not a castling request is valid.
	public boolean castleCheck(Location from, Location to, Piece piece) {
		if (piece.hasMoved() || (to.x != 2 && to.x != 6) || (to.y != 0 && to.y != 7)) return false;

		Piece other;
		if (to.x > from.x) other = board[to.x + 1][to.y].piece;
		else other = board[to.x - 2][to.y].piece;
		if (other == null || !(other instanceof Rook) || other.hasMoved()) return false;

		if (to.x > from.x) {
			if (board[from.x + 1][from.y].piece != null || board[from.x + 2][from.y].piece != null) return false;
		} else {
			if (board[from.x - 1][from.y].piece != null || board[from.x - 2][from.y].piece != null || board[from.x - 3][from.y].piece != null) return false;
		}
		return true;
	}

	@Override
	// Method returns a string representation of the board according to the project specification.
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