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
	
	public void removeSong(String name, String artist) {
		Song temp = new Song(name, artist, "", "");
		int index = songs.indexOf(temp);
		songs.remove(temp);
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
