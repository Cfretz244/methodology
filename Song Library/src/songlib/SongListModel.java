package songlib;

import javax.swing.AbstractListModel;

public class SongListModel extends AbstractListModel<Song> {
	
	private static final long serialVersionUID = 1;
	private SongList songs;
	
	public SongListModel(SongList songs) {
		this.songs = songs;
	}
	
	public void addSong(Song song) {
		songs.add(song);
		int index = songs.indexOf(song);
		fireIntervalAdded(this, index, index);
	}
	
	public void updateSong(Song orig, Song update) {
		int index = songs.indexOf(orig);
		songs.remove(orig);
		songs.add(update);
		fireContentsChanged(this, index, index);
	}
	
	public void removeSong(Song song) {
		int index = songs.indexOf(song);
		songs.remove(song);
		fireIntervalRemoved(this, index, index);
	}
	
	public Song[] getSongs() {
		Song[] copy = new Song[songs.size()];
		return songs.toArray(copy);
	}
	
	public Song getElementAt(int index) {
		return songs.get(index);
	}
	
	public int getSize() {
		return songs.size();
	}

}