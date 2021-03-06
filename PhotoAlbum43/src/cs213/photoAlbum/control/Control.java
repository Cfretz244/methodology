package cs213.photoAlbum.control;

import java.io.File;
import java.io.IOException;

import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Backend;
import cs213.photoAlbum.model.DataWriter;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.model.User;

/**
 * Class serves as a bridge between the View and Model. The view can call methods on the control object to influence the model.
 * All method descriptions are provided in the PhotoSource interface.
 * @author Karan Kadaru
 */
public class Control implements PhotoSource {
	
	private String currentUserid;
	private User currentUser;
	private DataWriter backend;
	
	/**
	 * Public constructor.
	 */
	public Control() {
		backend = new Backend();
	}

	@Override
	public void setCurrentUser(String userid) {
		currentUserid = userid;
	}
	
	@Override
	public boolean loadUserData() {
		if (currentUserid == null) return false;
		currentUser = backend.loadUser(currentUserid);
		return currentUser != null;
	}
	
	@Override
	public boolean shutdown() {
		if (currentUser == null) return false;
		
		int e_count = 0;
		boolean status = backend.writeUser(currentUser);
		while (!status) {
			if (++e_count > 5) return false;
			status = backend.writeUser(currentUser);
		}
		return true;
	}

	@Override
	public boolean addUser(String userid, String name) {
		currentUserid = userid;
		if (backend.loadUser(userid) == null) {
			User user = new User(name, userid);
			if (backend.writeUser(user)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public boolean removeUser(String userid) {
		return backend.deleteUser(userid);
	}

	@Override
	public boolean addAlbum(String album) {
		if (currentUser == null) return false;
		return currentUser.addAlbum(album);
	}
	
	@Override
	public boolean changeAlbumName(String oldName, String newName) {
		if (currentUser == null) return false;
		if (!currentUser.addAlbum(newName)) return false;

		Photo[] photos = currentUser.getPhotos(oldName);
		for (Photo photo : photos) movePhoto(oldName, newName, photo.getName());
		currentUser.removeAlbum(oldName);
		return true;
	}

	@Override
	public boolean removeAlbum(String album) {
		if (currentUser == null) return false;
		return currentUser.removeAlbum(album);
	}

	@Override
	public int addPhotoToAlbum(String album, String name, String caption) {
		if (currentUser == null) return -1;
		
		File photoFile = new File(name);
		if (!photoFile.exists()) return -1;
		try {
			name = photoFile.getCanonicalPath();
		} catch (IOException e) {
			return -2;
		}
		return currentUser.addPhotoToAlbum(name, caption, photoFile.lastModified(), album);
	}

	@Override
	public int movePhoto(String fromAlbum, String toAlbum, String name) {
		if (currentUser == null) return -1;
		
		Photo photo = currentUser.removePhotoFromAlbum(name, fromAlbum);
		if (photo == null) return -1;
		if (currentUser.addPhotoToAlbum(photo, toAlbum)) {
			return 1;
		} else {
			currentUser.addPhotoToAlbum(photo, fromAlbum);
			return 0;
		}
	}

	@Override
	public boolean removePhoto(String name) {
		if (currentUser == null) return false;
		
		return currentUser.removePhoto(name) != null;
	}

	@Override
	public boolean removePhotoFromAlbum(String album, String name) {
		if (currentUser == null) return false;
		
		return currentUser.removePhotoFromAlbum(name, album) != null;
	}
	
	@Override
	public boolean changeCaptionForPhoto(String name, String caption) {
		if (currentUser == null) return false;
		
		return currentUser.recaptionPhoto(name, caption);
	}

	@Override
	public boolean addTagToPhoto(String name, String type, String value) {
		if (currentUser == null) return false;
		
		return currentUser.addTagToPhoto(name, type, value);
	}

	@Override
	public boolean addTagToAlbum(String album, String type, String value) {
		if (currentUser == null) return false;
		
		Photo[] photos = currentUser.getPhotos(album);
		for (Photo photo : photos) {
			currentUser.addTagToPhoto(photo.getName(), type, value);
		}
		
		return true;
	}

	@Override
	public boolean removeTagFromPhoto(String name, String type) {
		if (currentUser == null) return false;
		
		return currentUser.removeTagFromPhoto(name, type);
	}

	@Override
	public boolean removeTagFromPhoto(String name, String type, String value) {
		if (currentUser == null) return false;
		
		return currentUser.removeTagFromPhoto(name, type, value);
	}

	@Override
	public boolean removeTagFromAlbum(String album, String type) {
		if (currentUser == null) return false;
		
		Photo[] photos = currentUser.getPhotos(album);
		for (Photo photo : photos) {
			currentUser.removeTagFromPhoto(photo.getName(), type);
		}
		
		return true;
	}

	@Override
	public boolean removeTagFromAlbum(String album, String type, String value) {
		if (currentUser == null) return false;
		
		Photo[] photos = currentUser.getPhotos(album);
		for (Photo photo : photos) {
			currentUser.removeTagFromPhoto(photo.getName(), type, value);
		}
		
		return true;
	}
	
	@Override
	public String[] listUsers() {
		return backend.listUsers();
	}

	@Override
	public User getUser(String userid) {
		if (userid.equals(currentUserid)) return currentUser;
		return backend.loadUser(userid);
	}

	@Override
	public Album[] getAlbums() {
		if (currentUser == null) return null;
		return currentUser.getAlbums();
	}
	
	public Photo getPhoto(String name) {
		if (currentUser == null) return null;
		return currentUser.getPhoto(name);
	}

	@Override
	public Photo[] getPhotos() {
		if (currentUser == null) return null;
		return currentUser.getPhotos();
	}

	@Override
	public Photo[] getPhotosFromAlbum(String album) {
		if (currentUser == null) return null;
		return currentUser.getPhotos(album);
	}

	@Override
	public Photo[] getPhotosByDate(long start, long end) {
		if (currentUser == null) return null;
		
		return currentUser.getPhotos(start, end);
	}
	
	@Override
	public Photo[] getPhotosByTag(String type, String value) {
		if (currentUser == null) return null;
		
		return currentUser.getPhotos(type, value);
	}
	
	@Override
	public String[] getTagsForPhoto(String name) {
		if (currentUser == null) return null;
		
		Photo photo = currentUser.getPhoto(name);
		return photo.getTags();
	}

}
