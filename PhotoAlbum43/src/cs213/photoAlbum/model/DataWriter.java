package cs213.photoAlbum.model;

public interface DataWriter {
	
	public User loadUser(String userid);
	public boolean writeUser(User user);
	public boolean deleteUser(String userid);
	public String[] listUsers();

}
