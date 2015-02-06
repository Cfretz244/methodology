package songlib;

import java.util.ArrayList;

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
			if (current.compareTo(current) == 0) {
				return mid;
			} else if (current.compareTo(song) > 0) {
				high = mid - 1;
				if (low > high) {
					return mid + 1;
				}
			} else {
				low = mid + 1;
				if (low > high) {
					return mid;
				}
			}
		}
	}

}
