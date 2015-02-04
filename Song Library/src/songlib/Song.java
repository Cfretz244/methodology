package songlib;

public class Song {
	
	private String name, artist, album, year;
	
	public Song(String name, String artist, String album, String year) {
		this.name = name;
		this.artist = artist;
		this.album = album;
		this.year = year;
	}
	
	@Override
	public String toString() {
		return name + " - " + artist;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other != null && other instanceof Song) {
			Song another = (Song) other;
			boolean first = name == another.name && artist == another.artist;
			boolean rest = album == another.album && year == another.year;
			return first && rest;
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
		return album;
	}
	
	public String getYear() {
		return year;
	}
}
