package chess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Chess {
	
	static Color currentTurn = Color.WHITE;
	
	public static void main(String[] args) throws IOException {
		Board board = new Board();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String errorMsg = "Illegal move, try again\n", newline = "";
		while (true) {
			System.out.println(board);
			if (currentTurn == Color.WHITE) System.out.print("White's move: ");
			else System.out.print("Black's move: ");
			
			String[] input = br.readLine().split(" ");
			puts(newline);
			if (input.length < 2) {
				if (input[0].equals("resign")) return;
				puts(errorMsg);
				continue;
			}
			
			int file1, file2, rank1, rank2;
			try {
				file1 = ((int) input[0].charAt(0)) - ((int) 'a');
				file2 = ((int) input[1].charAt(0)) - ((int) 'a');
				rank1 = ((int) input[0].charAt(1)) - ((int) '1');
				rank2 = ((int) input[1].charAt(1)) - ((int) '1');
			} catch (Exception e) {
				puts(errorMsg);
				continue;
			}
			if (file1 > Board.WIDTH || file1 < 0 || file2 > Board.WIDTH || file2 < 0) {
				puts(errorMsg);
				continue;
			} else if (rank1 > Board.WIDTH || rank1 < 0 || rank2 > Board.WIDTH || rank2 < 0) {
				puts(errorMsg);
				continue;
			}
			if (board.movePiece(new Location(file1, rank1), new Location(file2, rank2), currentTurn)) currentTurn = currentTurn == Color.WHITE ? Color.BLACK : Color.WHITE;
			else puts(errorMsg);
		}
	}
	
	public static void puts(String str) {
		System.out.println(str);
	}

}
