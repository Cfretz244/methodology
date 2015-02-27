package cs213.photoAlbum.control;

import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.model.User;

/**
 * Class serves as a bridge between the View and Model. The view can call methods on the control object
 * to influence the model.
 * All method descriptions are provided in the PhotoSource interface.
 * 
 * @author Karan Kadaru
 */
public class Control implements PhotoSource {
	
	private String currentUser;

	@Override
	public void setCurrentUser(String userid) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void loadUserData() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean addUser(String userid, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeUser(String userid) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAlbum(String album) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAlbum(String album) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addPhotoToAlbum(String album, String name, String caption) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean movePhoto(String fromAlbum, String toAlbum, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removePhoto(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removePhotoFromAlbum(String album, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addTagToPhoto(String name, String tagType, String tagValue) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addTagToAlbum(String album, String tagType, String tagValue) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeTagFromPhoto(String name, String tagType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeTagFromPhoto(String name, String tagType,
			String tagValue) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeTagFromAlbum(String album, String tagType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeTagFromAlbum(String album, String tagType,
			String tagValue) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public User getUser(String userid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Album[] getAlbums() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Photo[] getPhotos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Photo[] getPhotosFromAlbum(String album) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Photo[] getPhotosByDate(String start, String end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Photo[] getPhotosByTag(String tagType, String tagValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getTagsForPhoto(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}