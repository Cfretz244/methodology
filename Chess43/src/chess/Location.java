package chess;

// Class represents a location on the board.
public class Location {

	public int x, y;
	
	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	// Method checks if two locations are equal.
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Location)) return false;
		Location other = (Location) o;
		return other.x == x && other.y == y;
	}
	
	@Override
	// Method returns a string representation of a location.
	public String toString() {
		return x + ", " + y;
	}
}
