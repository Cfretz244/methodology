package cs213.photoAlbum.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data/" + userid));
			Object deserialized = ois.readObject();
			return (User) deserialized;
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * Method is responsible for serializing a given user object.
	 * 
	 * @param user The user object to serialize.
	 * @return Whether or not the operation was successful.
	 * @throws IOException ObjectOutputStream can throw this.
	 */
	public static boolean writeUser(User user) throws IOException {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data/" + user.getId()));
			oos.writeObject(user);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}