package cs213.photoAlbum.guiview;

import javax.swing.AbstractListModel;

import cs213.photoAlbum.control.PhotoSource;

/**
 * Class works as a ListModel for the JList in the admin panel.
 * @author cfretz
 */
public class UserList extends AbstractListModel<String> {
	
	private static final long serialVersionUID = 1;
	
	private PhotoSource control;
	private String[] users;
	
	public UserList(PhotoSource control) {
		this.control = control;
		users = control.listUsers();
	}
	
	/**
	 * Adds a user to the list.
	 * @param name The name of the user.
	 * @param id The id of the user.
	 * @return Whether it succeeded.
	 */
	public boolean addUser(String name, String id) {
		if (control.addUser(id, name)) {
			String[] newUsers = control.listUsers();
			int index = -1;
			for (int i = 0; i < newUsers.length; i++) {
				try {
					if (!newUsers[i].equals(users[i])) {
						index = i;
						break;
					}
				} catch (IndexOutOfBoundsException e) {
					index = i;
					break;
				}
			}
			users = newUsers;
			fireIntervalAdded(this, index, index);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Removes a user from the JList.
	 * @param id The id of the user.
	 * @return Whether it succeeded.
	 */
	public boolean deleteUser(String id) {
		if (control.removeUser(id)) {
			String[] newUsers = control.listUsers();
			int index = newUsers.length;
			for (int i = 0; i < newUsers.length; i++) {
				try {
					if (!newUsers[i].equals(users[i])) {
						index = i;
						break;
					}
				} catch (IndexOutOfBoundsException e) {
					index = i;
					break;
				}
			}
			users = newUsers;
			fireIntervalRemoved(this, index, index);
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public String getElementAt(int index) {
		if (index < 0 || index >= users.length) {
			return "";
		} else {
			return users[index];
		}
	}
	
	@Override
	public int getSize() {
		return users.length;
	}

}