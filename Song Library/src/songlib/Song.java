/*
 * Chris Fretz and Karan Kadaru
 */

package songlib;

public class Song implements Comparable<Song> {
	
	private String name, artist, album, year;
	
	public Song(String name, String artist, String album, String year) {
		this.name = name;
		this.artist = artist;
		this.album = album;
		this.year = year;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public int compareTo(Song other) {
		return name.concat(artist).compareTo(other.getName().concat(other.getArtist()));
	}
	
	@Override
	public boolean equals(Object other) {
		if (other != null && other instanceof Song) {
			Song another = (Song) other;
			boolean nameStatus = name.toLowerCase() == another.name.toLowerCase();
			boolean artistStatus = artist.toLowerCase() == another.artist.toLowerCase();
			return nameStatus && artistStatus;
		} else {
			return false;
		}
	}
	
	public String getName() {
		return name;
	}
	
	public String getArtist() {
		return artist;
	}
	
	public String getAlbum() {
		if (album.equals("")) {
			return " ";
		} else {
			return album;
		}
	}
	
	public String getYear() {
		if (year.equals("")) {
			return " ";
		} else {
			return year;
		}
	}
}
