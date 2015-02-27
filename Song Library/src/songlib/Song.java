/*
 * Chris Fretz and Karan Kadaru
 */

package songlib;

// Class represents an individual song.
public class Song implements Comparable<Song> {

	private String name, artist, album, year;

	public Song(String name, String artist, String album, String year) {
		this.name = name;
		this.artist = artist;
		this.album = album;
		this.year = year;
	}
	public Song(String name, String artist){
		this.name = name;
		this.artist = artist;
		this.album = "";
		this.year = "";
	}

	@Override
	public String toString() {
		return name;
	}

	// Method is used by SongList for insertions and removals.
	@Override
	public int compareTo(Song other) {
		return name.concat(artist).compareTo(other.getName().concat(other.getArtist()));
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

	public boolean equals(Object t){


		if(t != null && t instanceof Song){
			Song temp = (Song)t;

			if(name.equals(temp.getName()) && artist.equals(temp.getArtist())){
				return true;
			}
			else{
				return false;
			}

		}
		else{
			return false;

		}


	}
}
