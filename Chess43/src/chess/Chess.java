package chess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Chess {
	
	// Keeps track of the turn.
	static Color currentTurn = Color.WHITE;
	static boolean drawing = false, checked = false;
	
	// Entry vector for the program.
	public static void main(String[] args) throws IOException {
		// Instantiate a new board and begin reading input from the user.
		Board board = new Board();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String errorMsg = "Illegal move, try again\n", newline = "";

		while (true) {
			// Print out the board according the assignment specification and prompt the user for input.
			// Passing an object that is not a string to a function that expects a string causes the toString method to be implicitly called on the object.
			System.out.println(board);
			if (checked) puts("Check");
			if (currentTurn == Color.WHITE) System.out.print("White's move: ");
			else System.out.print("Black's move: ");
			
			// Read input from the user and break it up by spaces.
			String[] input = br.readLine().split(" ");
			puts(newline);
			if (input.length < 2) {
				// If one of the players is not resigning, this is an error condition, so we print the error message and re-initialize the loop.
				if (input[0].equals("resign")) return;
				else if (input[0].equals("draw") && drawing) return;
				puts(errorMsg);
				continue;
			}
			drawing = false;
			
			// This converts the input the user gives us into indexes that the Board object can use (example: a2 a4 -> [0, 1], [0, 3]).
			int file1, file2, rank1, rank2;
			try {
				file1 = ((int) input[0].charAt(0)) - ((int) 'a');
				file2 = ((int) input[1].charAt(0)) - ((int) 'a');
				rank1 = ((int) input[0].charAt(1)) - ((int) '1');
				rank2 = ((int) input[1].charAt(1)) - ((int) '1');
			} catch (Exception e) {
				// If the user passes us bad input that does not adhere to the [FileRank][FileRank] format specified for the assignment, charAt could throw
				// an out of bounds exception. This protects against that, reports the error the user, and re-initializes the loop.
				puts(errorMsg);
				continue;
			}
			
			// It's possible that the user could pass us arguments that are out of bounds, that wouldn't cause the previous conversion to crash.
			// This takes care of that.
			if (file1 > Board.WIDTH || file1 < 0 || file2 > Board.WIDTH || file2 < 0) {
				puts(errorMsg);
				continue;
			} else if (rank1 > Board.WIDTH || rank1 < 0 || rank2 > Board.WIDTH || rank2 < 0) {
				puts(errorMsg);
				continue;
			}
			
			// Assuming all of the previous checks pass, we ask the board to move the piece to the specified location, and assuming it is successful, we update
			// the turn.
			int status = board.movePiece(new Location(file1, rank1), new Location(file2, rank2), currentTurn, input.length == 3 ? input[2] : "");
			if (status > 0) {
				currentTurn = currentTurn == Color.WHITE ? Color.BLACK : Color.WHITE;

				if (input.length == 3 && input[2].equals("draw?") && !drawing) drawing = true;
				if (status == 4) {
					puts("Draw");
					return;
				} else if (status == 3) {
					puts((currentTurn == Color.WHITE ? "Black " : "White ") + "wins!");
					return;
				} else if (status == 2) {
					checked = true;
				} else {
					checked = false;
				}
			} else {
				puts(errorMsg);
			}
		}
	}
	
	// I got tired of typing System.out.println, and puts is the method to print in Ruby.
	public static void puts(String str) {
		System.out.println(str);
	}

}