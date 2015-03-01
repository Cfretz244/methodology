package cs213.photoAlbum.control;

import java.io.IOException;

import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.model.User;

/**
 * Interface defines a public API with which the view can interact with the control object.
 * Control is currently the only class that implements this interface.
 * 
 * @author Karan Kadaru
 */
public interface PhotoSource {
	
	/*----- State Control -----*/
	
	/**
	 * Method sets the current user. View would call this when the user logs in, and it defines what user
	 * the control object will be operating upon for the rest of the session.
	 * 
	 * @param userid The id of the current user.
	 */
	public void setCurrentUser(String userid);
	
	/**
	 * Method is called after setCurrentUser, and de-serializes the user data and loads it into memory.
	 * Should be called directly after setCurrentUser, and before all other interactive methods.
	 */
	public boolean loadUserData();
	
	/**
	 * Method is responsible for writing the current user out to disk and doing anything else that needs
	 * to be done for a clean shutdown.
	 */
	public boolean shutdown();
	
	/*---- Setters/Mutators -----*/

	/**
	 * Method creates a new user with the given id and name.
	 * 
	 * @param userid ID for the new user.
	 * @param name Name for the new user.
	 * @return Whether or not the operation was successful.
	 */
	public boolean addUser(String userid, String name);
	
	/**
	 * Method removes a user with the given id.
	 * 
	 * @param userid ID of the user to remove.
	 * @return Whether or not the operation was successful.
	 */
	public boolean removeUser(String userid);
	
	/**
	 * Method adds an album to the current user.
	 * 
	 * @param album Name of album to be created.
	 * @return Whether or not the operation was successful.
	 */
	public boolean addAlbum(String album);
	
	/**
	 * Method removes an album from the current user.
	 * 
	 * @param album Name of album to be removed.
	 * @return Whether or not the operation was successful.
	 */
	public boolean removeAlbum(String album);
	
	/**
	 * Method adds a photo to an album.
	 * 
	 * @param album The name of the album to add to.
	 * @param name The name of the photo you're adding.
	 * @param caption The caption for the photo.
	 * @return Whether or not the operation was successful.
	 */
	public boolean addPhotoToAlbum(String album, String name, String caption);
	
	/**
	 * Method moves a photo between albums.
	 * 
	 * @param fromAlbum The name of the album to move from.
	 * @param toAlbum The name of the album to move to.
	 * @param name The name of the photo.
	 * @return Whether or not the operation was successful.
	 */
	public boolean movePhoto(String fromAlbum, String toAlbum, String name);
	
	/**
	 * Removes a photo from all albums of the current user.
	 * 
	 * @param name Name of the photo to remove.
	 * @return Whether or not the operation was successful.
	 */
	public boolean removePhoto(String name);
	
	/**
	 * Removes a photo from a specific album of the current user.
	 * 
	 * @param album Name of the album to remove from.
	 * @param name Name of the photo to remove.
	 * @return Whether or not the operation was successful.
	 */
	public boolean removePhotoFromAlbum(String album, String name);
	
	/**
	 * Method adds a tag to a given photo.
	 * 
	 * @param name Name of the photo to receive the tag.
	 * @param tagType Type of the tag to add.
	 * @param tagValue Value of the tag to add.
	 * @return Whether or not the operation was successful.
	 */
	public boolean addTagToPhoto(String name, String tagType, String tagValue);
	
	/**
	 * Method adds a tag to all photos in a specific album.
	 * 
	 * @param album Name of the album to add the tag to.
	 * @param tagType Type of the tag to be added.
	 * @param tagValue Value of the tag to be added.
	 * @return Whether or not the operation was successful.
	 */
	public boolean addTagToAlbum(String album, String tagType, String tagValue);
	
	/**
	 * Method removes all tags of a given type from a photo.
	 * 
	 * @param name Name of the photo you want to remove the tags from.
	 * @param tagType The type of tags you want to remove.
	 * @return Whether or not the operation was successful.
	 */
	public boolean removeTagFromPhoto(String name, String tagType);
	
	/**
	 * Method removes a specific tag from a photo.
	 * 
	 * @param name Name of the photo you want to remove the tag from.
	 * @param tagType Type of the tag to be removed.
	 * @param tagValue Value of the tag to be removed.
	 * @return Whether or not the operation was successful.
	 */
	public boolean removeTagFromPhoto(String name, String tagType, String tagValue);
	
	/**
	 * Method removes all tags of a given type from all photos within an album.
	 * 
	 * @param album Name of the album to remove the tags from.
	 * @param tagType The type of tag to remove.
	 * @return Whether or not the operation was successful.
	 */
	public boolean removeTagFromAlbum(String album, String tagType);
	
	/**
	 * Method removes a specific tag from all photos within an album.
	 * 
	 * @param album The name of the album to remove the tags from.
	 * @param tagType The type of the tag to remove.
	 * @param tagValue The value of the tag to remove.
	 * @return Whether or not the operation was successful.
	 */
	public boolean removeTagFromAlbum(String album, String tagType, String tagValue);
	
	/*----- Getters -----*/
	
	/**
	 * Method returns a user object for a given id.
	 * 
	 * @param userid The id of the user you want.
	 * @return The User object.
	 */
	public User getUser(String userid);
	
	/**
	 * Method returns an array of all albums for the current user.
	 * 
	 * @return An array of album objects.
	 */
	public Album[] getAlbums();
	
	/**
	 * Method returns an array of all photos for the current user.
	 * 
	 * @return An array of photo objects.
	 */
	public Photo[] getPhotos();

	/**
	 * Method returns an array of all photos within a given album.
	 * 
	 * @param album The album to get photos from.
	 * @return An array of photo objects.
	 */
	public Photo[] getPhotosFromAlbum(String album);
	
	/**
	 * Method returns a sorted array of photos within a given date range.
	 * 
	 * @param start The start date.
	 * @param end The end date.
	 * @return An array of photo objects.
	 */
	public Photo[] getPhotosByDate(String start, String end);
	
	/**
	 * Method returns an array of all photos with a given tag.
	 * 
	 * @param tagType The type of the tag to search for.
	 * @param tagValue The value of the tag to search for.
	 * @return An array of photos.
	 */
	public Photo[] getPhotosByTag(String tagType, String tagValue);
	
	/**
	 * Method returns an array of tags for a given photo.
	 * 
	 * @param name The name of the photo to get tags from.
	 * @return An array of tags in the format "<type>:<value>"
	 */
	public String[] getTagsForPhoto(String name);

}