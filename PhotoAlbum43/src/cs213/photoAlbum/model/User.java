package cs213.photoAlbum.model;

import java.io.Serializable;
import java.util.Map;

/**
 * Class represents an individual user, and keeps track of their albums and photos.
 * 
 * @author Chris Fretz
 */
public class User implements Serializable {
	
	private String name, id;
	private Map<String, Photo> allPhotos;
	private Map<String, Album> albums;
	private static final long serialVersionUID = 1;
	
	/*----- Constructors -----*/
	
	/**
	 * Public constructor for user. Takes a name and an id.
	 * 
	 * @param name The chosen name of the user.
	 * @param id The chosen id of the user.
	 */
	public User(String name, String id) {
		// Implementation...
	}
	
	/*----- Private Methods -----*/
	
	private boolean addPhoto(Photo photo, String album) {
		// Implementation...
		return false;
	}
	
	private boolean removeTag(Photo photo, String tagType, String tagValue) {
		// Implementation...
		return false;
	}
	
	/*----- Public Setters/Mutators -----*/
	
	/**
	 * Method changes the name of the user.
	 * 
	 * @param name The new name for the user.
	 * @return Whether or not the operation succeeded.
	 */
	public boolean setName(String name) {
		// Implementation...
		return false;
	}
	
	/**
	 * Method changes the id of the user.
	 * 
	 * @param id The new id for the user.
	 * @return Whether or not the operation succeeded.
	 */
	public boolean setId(String id) {
		// Implementation...
		return false;
	}
	
	/**
	 * Method adds a new album to the user.
	 * 
	 * @param album Name of the album.
	 * @return Whether or not the operation succeeded.
	 */
	public boolean addAlbum(String album) {
		// Implementation...
		return false;
	}
	
	/**
	 * Method removes an album from the user.
	 * 
	 * @param album Name of the album.
	 * @return Whether or not the operation succeeded.
	 */
	public boolean removeAlbum(String album) {
		// Implementation...
		return false;
	}
	
	/**
	 * Method adds a photo to an already existing album.
	 * 
	 * @param photo Photo object you want to add.
	 * @param album Name of album photo should be added to.
	 * @return Whether or not the operation succeeded.
	 */
	public boolean addPhotoToAlbum(Photo photo, String album) {
		// Implementation...
		return false;
	}
	
	/**
	 * Method adds a photo to an already existing album.
	 * 
	 * @param name Name of photo you want to add to the album
	 * @param date Date the photo was taken.
	 * @param album Name of album you want to add the photo to.
	 * @return Whether or not the operation succeeded.
	 */
	public boolean addPhotoToAlbum(String name, long date, String album) {
		// Implementation...
		return false;
	}
	
	/**
	 * Method adds a photo to an already existing album.
	 * 
	 * @param name The name of the photo to be added.
	 * @param caption The caption of the photo.
	 * @param date The date the photo was taken (or modified in this case).
	 * @param album The album to add the photo to.
	 * @return Whether or not the operation succeeded.
	 */
	public boolean addPhotoToAlbum(String name, String caption, long date, String album) {
		// Implementation...
		return false;
	}
	
	/**
	 * Method removes a photo from all of the user's albums.
	 * 
	 * @param name Name of the photo to be removed.
	 * @return The photo that was removed.
	 */
	public Photo removePhoto(String name) {
		// Implementation...
		return null;
	}
	
	/**
	 * Method removes a photo from a specific album.
	 * 
	 * @param name Name of the photo to remove.
	 * @param album Name of the album to remove from.
	 * @return Whether or not the operation succeeded.
	 */
	public Photo removePhotoFromAlbum(String name, String album) {
		// Implementation...
		return null;
	}
	
	/**
	 * Method adds a tag to a specific photo.
	 * 
	 * @param photoName The name of the photo we are adding the tag to.
	 * @param tagType The type of the tag.
	 * @param tagValue The value of the tag.
	 * @return Whether or not the operation succeeded.
	 */
	public boolean addTagToPhoto(String photoName, String tagType, String tagValue) {
		// Implementation...
		return false;
	}
	
	/**
	 * Method removes all tag from a specific photo.
	 * 
	 * @param photoName Name of the photo to remove tags from.
	 * @return Whether or not the operation was successful.
	 */
	public boolean removeTagsFromPhoto(String photoName) {
		// Implementation...
		return false;
	}
	
	/**
	 * Method removes all tags of a given type from a photo.
	 * 
	 * @param photoName Name of the photo to remove tags from.
	 * @param tagType Type of tag to remove.
	 * @return Whether or not the operation was successful.
	 */
	public boolean removeTagFromPhoto(String photoName, String tagType) {
		// Implementation...
		return false;
	}
	
	/**
	 * Method removes a specific tag from a specific photo.
	 * 
	 * @param photoName Name of the photo to remove tags from.
	 * @param tagType Type of tag to remove.
	 * @param tagValue Value of tag to remove.
	 * @return Whether or not the operation succeeded.
	 */
	public boolean removeTagFromPhoto(String photoName, String tagType, String tagValue) {
		// Implementation...
		return false;
	}
	
	/*----- Public Getters -----*/
	
	/**
	 * Method returns all albums for the user.
	 * 
	 * @return An array of albums.
	 */
	public Album[] getAlbums() {
		// Implementation...
		return null;
	}
	
	/**
	 * Method returns a specific photo.
	 * 
	 * @param name Name of the requested photo.
	 * @return A photo object.
	 */
	public Photo getPhoto(String name) {
		// Implementation...
		return null;
	}
	
	/**
	 * Method returns all photos for the user.
	 * 
	 * @return An array of photos.
	 */
	public Photo[] getPhotos() {
		// Implementation...
		return null;
	}
	
	/**
	 * Method returns all photos contained within a specific album.
	 * 
	 * @param album Name of album to get photos from.
	 * @return An array of photos.
	 */
	public Photo[] getPhotos(String album) {
		// Implementation...
		return null;
	}
	
	/**
	 * Method gets all photos with a specific tag.
	 * 
	 * @param tagType The type of the tag.
	 * @param tagValue The value of the tag.
	 * @return An array of photos.
	 */
	public Photo[] getPhotos(String tagType, String tagValue) {
		// Implementation...
		return null;
	}
	
	/**
	 * Method gets all photos within a given date range.
	 * 
	 * @param startDate The starting date.
	 * @param endDate The ending date.
	 * @return An array of photos.
	 */
	public Photo[] getPhotos(long startDate, long endDate) {
		// Implementation...
		return null;
	}

}