package cs213.photoAlbum.model;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

/**
 * Class represents an individual photo album for a user. It keeps track of all of its photos, and 
 * maintains several different tables of photos allowing efficient access.
 * 
 * @author Chris Fretz
 */
public class Album implements Serializable {
	
	private String name;
	private Map<String, Photo> photos;
	private Map<String, Set<Photo>> tags;
	private SortedSet<Photo> dates;
	private static final long serialVersionUID = 1;
	
	/*----- Constructors -----*/
	
	/**
	 * Public constructor for Album. Takes a name and initializes an album object.
	 * 
	 * @param name The name of the album.
	 */
	public Album(String name) {
		// Implementation...
	}
	
	/*----- Protected Setters/Mutators -----*/
	
	/**
	 * Method adds a photo to the album.
	 * 
	 * @param photo Photo to be added.
	 * @return Status of operation.
	 */
	protected boolean addPhoto(Photo photo) {
		// Implementation...
		return false;
	}
	
	/**
	 * Method removes a photo from the album.
	 * 
	 * @param photo Name of photo to be removed.
	 * @return The photo that was removed.
	 */
	protected Photo removePhoto(String photo) {
		// Implementation...
		return null;
	}
	
	/**
	 * Method is called upon the album object when the tag of a photo has changed so that the album
	 * can update its state to reflect the fact.
	 * 
	 * @param photo The photo object whose tags have changed.
	 */
	protected void tagsChanged(Photo photo) {
		// Implementation...
	}
	
	/*----- Public Getters -----*/
	
	/**
	 * Returns the name of the album.
	 * 
	 * @return The name.
	 */
	public String getName() {
		// Implementation...
		return null;
	}
	
	/**
	 * Returns an array of the photos the album contains.
	 * 
	 * @return An array of the photos.
	 */
	public Photo[] getPhotos() {
		// Implementation...
		return null;
	}
	
	/**
	 * Returns an array of photos that have the given tag.
	 * 
	 * @param tagType The type of the tag.
	 * @param tagValue The value of the tag.
	 * @return An array of photos that have the given tag.
	 */
	public Photo[] getPhotos(String tagType, String tagValue) {
		// Implementation...
		return null;
	}
	
	/**
	 * Returns an array of photos within the given time range.
	 * 
	 * @param startDate The starting date.
	 * @param endDate The ending date.
	 * @return An array of photos within the given dates.
	 */
	public Photo[] getPhotos(long startDate, long endDate) {
		// Implementation...
		return null;
	}

}