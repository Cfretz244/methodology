package cs213.photoAlbum.model;

/**
 * Interface defines a public API with which the control and view can interact with the disk (DB).
 * @author Chris Fretz and Karan Kadaru
 */
public interface DataWriter {
	
	/**
	 * Method is responsible for loading a serialized user based on id.
	 * @param userid The id of the user you want to load.
	 * @return The user object that was requested.
	 */
	public User loadUser(String userid);
	
	/**
	 * Method is responsible for serializing a given user object.
	 * @param user The user object to serialize.
	 * @return Whether or not the operation was successful.
	 */	
	public boolean writeUser(User user);
	
	/**
	 * Method is responsible for removing serialized object files for the specified user.
	 * @param userid The ID of the user you would like to delete.
	 * @return Whether or not the operation was successful.
	 */
	public boolean deleteUser(String userid);
	
	/**
	 * Method is responsible for listing all users currently registered with the program.
	 * @return An array of the users currently registered with the program.
	 */
	public String[] listUsers();

}
