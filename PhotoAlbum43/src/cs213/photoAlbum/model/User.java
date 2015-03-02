package cs213.photoAlbum.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
		this.name = name;
		this.id = id;
		allPhotos = new HashMap<String, Photo>();
		albums = new HashMap<String, Album>();
	}
	
	/*----- Private Helpers -----*/
	
	private boolean addPhoto(Photo photo, String album) {
		Album current = albums.get(album);
		if (current == null) return false;

		if (current.addPhoto(photo)) {
			allPhotos.put(photo.getName(), photo);
			return true;
		}

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
		this.name = name;
		return true;
	}
	
	/**
	 * Method changes the id of the user.
	 * 
	 * @param id The new id for the user.
	 * @return Whether or not the operation succeeded.
	 */
	public boolean setId(String id) {
		this.id = id;
		return true;
	}
	
	/**
	 * Method adds a new album to the user.
	 * 
	 * @param album Name of the album.
	 * @return Whether or not the operation succeeded.
	 */
	public boolean addAlbum(String album) {
		if (!albums.containsKey(album)) {
			albums.put(album, new Album(album));
			return true;
		}
		return false;
	}
	
	/**
	 * Method removes an album from the user.
	 * 
	 * @param album Name of the album.
	 * @return Whether or not the operation succeeded.
	 */
	public boolean removeAlbum(String album) {
		return albums.remove(album) != null;
	}
	
	/**
	 * Method adds a photo to an already existing album.
	 * 
	 * @param photo Photo object you want to add.
	 * @param album Name of album photo should be added to.
	 * @return Whether or not the operation succeeded.
	 */
	public boolean addPhotoToAlbum(Photo photo, String album) {
		return addPhoto(photo, album);
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
		return addPhoto(new Photo(albums.get(album), name, id, date), album);
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
		return addPhoto(new Photo(albums.get(album), name, caption, id, date), album);
	}
	
	/**
	 * Method removes a photo from all of the user's albums.
	 * 
	 * @param name Name of the photo to be removed.
	 * @return The photo that was removed.
	 */
	public Photo removePhoto(String name) {
		Iterator<String> keys = albums.keySet().iterator();
		Photo result = null;

		while (keys.hasNext()) {
			Album current = albums.get(keys.next());
			Photo tmp = current.removePhoto(name);
			if (tmp != null) result = tmp;
		}
		
		return result;
	}
	
	/**
	 * Method removes a photo from a specific album.
	 * 
	 * @param name Name of the photo to remove.
	 * @param album Name of the album to remove from.
	 * @return Whether or not the operation succeeded.
	 */
	public Photo removePhotoFromAlbum(String name, String album) {
		Album current = albums.get(album);
		if (current == null) return null;
		return current.removePhoto(name);
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
		Photo photo = allPhotos.get(photoName);
		if (photo == null) return false;
		
		return photo.addTag(tagType, tagValue);
	}
	
	/**
	 * Method removes all tag from a specific photo.
	 * 
	 * @param photoName Name of the photo to remove tags from.
	 * @return Whether or not the operation was successful.
	 */
	public boolean removeTagsFromPhoto(String photoName) {
		Photo photo = allPhotos.get(photoName);
		if (photo == null) return false;
		
		String[] tags = photo.getTags();
		for (String tag : tags) {
			String type = tag.substring(0, tag.indexOf(":"));
			photo.removeTag(type);
		}
		return true;
	}
	
	/**
	 * Method removes all tags of a given type from a photo.
	 * 
	 * @param photoName Name of the photo to remove tags from.
	 * @param tagType Type of tag to remove.
	 * @return Whether or not the operation was successful.
	 */
	public boolean removeTagFromPhoto(String photoName, String tagType) {
		Photo photo = allPhotos.get(photoName);
		if (photo == null) return false;
		
		return photo.removeTag(tagType);
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
		Photo photo = allPhotos.get(photoName);
		if (photo == null) return false;
		
		return photo.removeTag(tagType, tagValue);
	}
	
	/*----- Public Getters -----*/
	
	public String getName() {
		return name;
	}
	
	public String getId() {
		return id;
	}
	
	public Album getAlbum(String album) {
		return albums.get(album);
	}
	
	/**
	 * Method returns all albums for the user.
	 * 
	 * @return An array of albums.
	 */
	public Album[] getAlbums() {
		Album[] result = new Album[albums.size()];
		albums.values().toArray(result);
		return result;
	}
	
	/**
	 * Method returns a specific photo.
	 * 
	 * @param name Name of the requested photo.
	 * @return A photo object.
	 */
	public Photo getPhoto(String name) {
		return allPhotos.get(name);
	}
	
	/**
	 * Method returns all photos for the user.
	 * 
	 * @return An array of photos.
	 */
	public Photo[] getPhotos() {
		Photo[] photos = new Photo[allPhotos.size()];
		allPhotos.values().toArray(photos);
		return photos;
	}
	
	/**
	 * Method returns all photos contained within a specific album.
	 * 
	 * @param album Name of album to get photos from.
	 * @return An array of photos.
	 */
	public Photo[] getPhotos(String album) {
		Album current = albums.get(album);
		if (current == null) return null;
		return current.getPhotos();
	}
	
	/**
	 * Method gets all photos with a specific tag.
	 * 
	 * @param tagType The type of the tag.
	 * @param tagValue The value of the tag.
	 * @return An array of photos.
	 */
	public Photo[] getPhotos(String tagType, String tagValue) {
		Iterator<String> iterate = albums.keySet().iterator();
		Set<Photo> results = new HashSet<Photo>();
		
		while (iterate.hasNext()) {
			Album current = albums.get(iterate.next());
			Photo[] currentPhotos = current.getPhotos(tagType, tagValue);
			for (Photo photo : currentPhotos) results.add(photo);
		}
		
		Photo[] resultsArray = new Photo[results.size()];
		results.toArray(resultsArray);
		return resultsArray;
	}
	
	/**
	 * Method gets all photos within a given date range.
	 * 
	 * @param startDate The starting date.
	 * @param endDate The ending date.
	 * @return An array of photos.
	 */
	public Photo[] getPhotos(long startDate, long endDate) {
		Iterator<String> iterate = albums.keySet().iterator();
		Set<Photo> results = new HashSet<Photo>();
		
		while (iterate.hasNext()) {
			Album current = albums.get(iterate.next());
			Photo[] currentPhotos = current.getPhotos(startDate, endDate);
			for (Photo photo : currentPhotos) results.add(photo);
		}
		
		Photo[] resultsArray = new Photo[results.size()];
		results.toArray(resultsArray);
		return resultsArray;
	}

}