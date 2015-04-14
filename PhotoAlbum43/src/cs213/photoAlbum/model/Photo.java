package cs213.photoAlbum.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * This class describes what a Photo is, and provides a protected interface for mutation, and a public
 * interface for viewing.
 * @author Chris Fretz
 */
public class Photo implements Serializable, Comparable<Photo>, Drawable {

	private String name, caption, userid;
	private Calendar date;
	private Map<String, Set<String>> tags;
	private Set<Album> containingAlbums;
	private static final long serialVersionUID = 1;

	/*----- Constructors -----*/

	/**
	 * Public constructor taking the album name, photo name, and date.
	 * @param containing The name of the album.
	 * @param name The name of the photo.
	 * @param userid The ID of the user this photo belongs to.
	 * @param date The date this photo was taken as a Unix timestamp.
	 */
	public Photo(Album containing, String name, String userid, long date) {
		setup();
		this.name = name;
		this.userid = userid;
		this.date.setTimeInMillis(date);
		containingAlbums.add(containing);
	}

	/**
	 * Public constructor taking the album name, photo name, caption, and date.
	 * @param containing The name of the album.
	 * @param name The name of the photo.
	 * @param caption A caption for the photo.
	 * @param userid The ID of the user this photo belongs to.
	 * @param date The date the photo was taken as a Unix timestamp.
	 */
	public Photo(Album containing, String name, String caption, String userid, long date) {
		setup();
		this.name = name;
		this.userid = userid;
		this.caption = caption;
		this.date.setTimeInMillis(date);
		containingAlbums.add(containing);
	}

	/*----- Private Helpers -----*/

	private void setup() {
		this.date = new GregorianCalendar();
		tags = new HashMap<String, Set<String>>();
		containingAlbums = new HashSet<Album>();
	}

	/*----- Protected Setters/Mutators -----*/

	/**
	 * Method sets the caption for a photo.
	 * @param caption Desired caption.
	 */
	protected void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * Method adds a tag to the photo.
	 * @param type Type of tag, used to index into the tag map.
	 * @param value Value of tag.
	 * @return Whether or not the operation succeeded.
	 */
	protected boolean addTag(String type, String value) {
		Set<String> category = tags.get(type);

		if (category == null) {
			category = new HashSet<String>();
			tags.put(type, category);
		}
		if (category.contains(value)) return false;

		category.add(value);
		return true;
	}

	/**
	 * Method removes all tags of a given type.
	 * @param type Type of tag to be removed
	 * @return Whether or not the operation succeeded.
	 */
	protected boolean removeTag(String type) {
		return tags.remove(type) != null;
	}

	/**
	 * Method removes a specific tag.
	 * @param type Type of the tag to be removed.
	 * @param value Value of tag to be removed.
	 * @return Whether or not the operation succeeded.
	 */
	protected boolean removeTag(String type, String value) {
		Set<String> category = tags.get(type);
		if (category == null) return false;
		return category.remove(value);
	}

	/**
	 * Method adds the current photo to album.
	 * @param album The album object we are adding this photo to.
	 * @return Whether or not the operation succeeded.
	 */
	protected boolean addToAlbum(Album album) {
		if (containingAlbums.contains(album)) return false;

		containingAlbums.add(album);
		return true;
	}

	/**
	 * Method removes current photo from album.
	 * @param album The album object we are removing this photo from.
	 * @return Whether or not the operation succeeded.
	 */
	protected boolean removeFromAlbum(Album album) {
		return containingAlbums.remove(album);
	}

	/*----- Public Getters -----*/

	/**
	 * Method returns the name of the photo.
	 * @return The name of the photo.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Method returns the caption of the photo.
	 * @return The caption of the photo
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * Method returns the userid of the user that owns this photo.
	 * @return The userid of the user that owns this photo.
	 */
	public String getUserid() {
		return userid;
	}

	/** 
	 * Method returns the date the photo was taken.
	 * @return Date the photo was taken as a Unix timestamp.
	 */
	public long getDate() {
		return date.getTimeInMillis();
	}

	/**
	 * Method returns the albums this photo is contained in.
	 * @return Array of the albums this photo is contained in.
	 */
	public Album[] getContainingAlbums() {
		Album[] albums = new Album[containingAlbums.size()];
		containingAlbums.toArray(albums);
		return albums;
	}

	/**
	 * Method returns all tags for the current photo.
	 * @return Array of tags in the form "type:value"
	 */
	public String[] getTags() {
		Set<String> allTags = new HashSet<String>();
		Iterator<String> keys = tags.keySet().iterator();

		while (keys.hasNext()) {
			String key = keys.next();
			Iterator<String> values = tags.get(key).iterator();
			while(values.hasNext()) allTags.add(key + ":" + values.next());
		}

		String[] results = new String[allTags.size()];
		allTags.toArray(results);

		return results;
	}

	/**
	 * Method returns all tags for the current photo with the given type.
	 * @param type The type of the tag you're looking for.
	 * @return An array of tags.
	 */
	public String[] getTags(String type) {
		Set<String> category = tags.get(type);
		if (category == null) return null;

		String[] results = new String[category.size()];
		category.toArray(results);
		return results;
	}

	/**
	 * Checks to see if photo has tags of the given type or value.
	 * @param tag The fragment of tag you're looking for.
	 * @param isType Whether or not the fragment you've supplied is a type or value.
	 * @return Whether or not the photo has tags of the given type.
	 */
	public boolean hasTag(String tag, boolean isType) {
		if (isType) {
			return tags.containsKey(tag);
		} else {
			Iterator<String> keys = tags.keySet().iterator();
			while (keys.hasNext()) if (tags.get(keys.next()).contains(tag)) return true;
			return false;
		}
	}

	/**
	 * Checks to see if photo has a specific tag.
	 * @param type Type of the tag you're looking for.
	 * @param value Value of the tag you're looking for.
	 * @return Whether or not the photo has the tag you're looking for.
	 */
	public boolean hasTag(String type, String value) {
		Set<String> category = tags.get(type);
		if (category == null) return false;
		return category.contains(value);
	}

	@Override
	public int compareTo(Photo other) {
		Long thisDate = new Long(date.getTimeInMillis());
		Long otherDate = new Long(other.getDate());
		return thisDate.compareTo(otherDate);
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Photo)) return false;

		Photo another = (Photo) other;
		return name == another.getName() && userid == another.getUserid();
	}
	
	@Override
	public String toString() {
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		return format.format(new Date(getDate()));
	}
	
	@Override
	public String getPath() {
		return name;
	}

}