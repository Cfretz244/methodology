/*
 * Chris Fretz and Karan Kadaru
 */

package songlib;

import javax.swing.AbstractListModel;

// Class works as a go-between for the JList (view) and the SongList.
public class SongListModel extends AbstractListModel<Song> {

	private static final long serialVersionUID = 1;
	private SongList songs;

	public SongListModel(SongList songs) {
		this.songs = songs;
	}

	// Updates the model by adding the given song to the underlying SongList
	// and calling fireIntervalAdded to let the JList know to redraw.
	public void addSong(Song song) {
		songs.add(song);
		int index = songs.indexOf(song);
		fireIntervalAdded(this, index, index);
	}

	// Works the same as addSong, except that it removes the original version of
	// the song before adding the updated one and then calls fireContentsChanged.
	public void updateSong(Song orig, Song update) {
		int index = songs.indexOf(orig);
		songs.remove(orig);
		songs.add(update);
		fireContentsChanged(this, index, index);
	}

	// Removes a song from the model and alerts the JList.
	public void removeSong(Song song) {
		int index = songs.indexOf(song);
		songs.remove(song);
		fireIntervalRemoved(this, index, index);
	}

	// Returns a flat array of its songs. Used by the confighandler.
	public Song[] getSongs() {
		Song[] copy = new Song[songs.size()];
		return songs.toArray(copy);
	}

	// These two methods are required by the JList.
	public Song getElementAt(int index) {
		if (songs.size() > 0 && index < 0) {
			return songs.get(0);
		} else if (songs.size() > 0) {
			return songs.get(index);
		}
		return new Song("Empty", "");
	}

	public int getSize() {
		return songs.size();
	}

	public boolean contains(String name, String artist){

		Song temp = new Song(name,artist);

		if(songs.contains(temp) == true){
			return true;
		}
		else{
			return false;
		}


	}

}
