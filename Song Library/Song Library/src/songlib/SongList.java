/*
 * Chris Fretz and Karan Kadaru
 */

package songlib;

import java.util.ArrayList;

// Subclass of ArrayList that maintains alphabetical order
// through binary insertion and search. Uses ArrayList's original
// add and remove methods to do the actual insertions and removals.
public class SongList extends ArrayList<Song> {

	private static final long serialVersionUID = 1;

	public boolean add(Song song) {
		super.add(binarySearch(song), song);

		return true;
	}
	
	public int indexOf(Song song) {
		return binarySearch(song);
	}
	
	public boolean remove(Song song) {
		super.remove(binarySearch(song));
		return true;
	}

	private int binarySearch(Song song) {
		if (size() == 0) return 0;
		int low = 0, high = size() - 1;
		while (true) {
			int mid = (low + high) / 2;
			Song current = get(mid);
			if (song.compareTo(current) == 0) {
				return mid;
			} else if (song.compareTo(current) > 0) {
				low = mid + 1;
				if (low > high) {
					return mid + 1;
				}
			} else {
				high = mid - 1;
				if (low > high) {
					return mid;
				}
			}
		}
	}

}
