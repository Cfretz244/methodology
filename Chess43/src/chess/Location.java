package chess;

public class Location {

	public int x, y;
	
	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Location)) return false;
		Location other = (Location) o;
		return other.x == x && other.y == y;
	}
	
	@Override
	public String toString() {
		return x + ", " + y;
	}
}