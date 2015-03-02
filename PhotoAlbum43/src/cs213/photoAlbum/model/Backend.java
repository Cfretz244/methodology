package cs213.photoAlbum.model;

import java.io.Closeable;
import java.io.File;
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
	public static User loadUser(String userid) {
		ObjectInputStream ois = openInputStream("data/" + userid + ".ser");
		try {
			Object deserialized = ois.readObject();
			return (User) deserialized;
		} catch (Exception e) {
			return null;
		} finally {
			closeStream(ois);
		}

	}

	/**
	 * Method is responsible for serializing a given user object.
	 * 
	 * @param user The user object to serialize.
	 * @return Whether or not the operation was successful.
	 */
	public static boolean writeUser(User user) {
		ObjectOutputStream oos = openOutputStream("data/" + user.getId() + ".ser");
		try {
			oos.writeObject(user);
		} catch (Exception e) {
			return false;
		} finally {
			closeStream(oos);
		}
		return true;
	}
	
	public static boolean deleteUser(String userid) {
		File user = new File("data/" + userid + ".ser");
		if (user.exists()) {
			user.delete();
			return true;
		} else {
			return false;
		}
	}
	
	public static String[] listUsers() {
		File dir = new File("data");
		File[] contents = dir.listFiles();
		String[] users = new String[contents.length];
		
		for (int i = 0; i < contents.length; i++) {
			File file = contents[i];
			users[i] = file.getName().substring(0, file.getName().indexOf("."));
		}
		
		return users;
	}
	
	private static ObjectInputStream openInputStream(String path) {
		try {
			return new ObjectInputStream(new FileInputStream(path));
		} catch (IOException e) {
			return null;
		}
	}
	
	private static ObjectOutputStream openOutputStream(String path) {
		try {
			return new ObjectOutputStream(new FileOutputStream(path));
		} catch (IOException e) {
			return null;
		}
	}
	
	private static boolean closeStream(Closeable stream) {
		try {
			stream.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

}