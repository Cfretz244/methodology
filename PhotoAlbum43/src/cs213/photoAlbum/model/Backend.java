package cs213.photoAlbum.model;

import java.io.IOException;

/**
 * Class is a utility class that can be used to load and store data for users.
 * 
 * @author Chris Fretz and Karan Kadaru
 */
public class Backend {
	
	/*----- Static Utility Methods -----*/
	
	/**
	 * Method is responsible for loading a serialized user based on id.
	 * 
	 * @param userid The id of the user you want to load.
	 * @return The user object that was requested.
	 * @throws IOException ObjectInputStream can throw this.
	 * @throws ClassNotFoundException ObjectInputStream can throw this as well.
	 */
	public static User loadUser(String userid) throws IOException, ClassNotFoundException {
		// Implementation...
		return null;
	}
	
	/**
	 * Method is responsible for serializing a given user object.
	 * 
	 * @param user The user object to serialize.
	 * @return Whether or not the operation was successful.
	 * @throws IOException ObjectOutputStream can throw this.
	 */
	public static boolean writeUser(User user) throws IOException {
		// Implementation...
		return false;
	}

}