package guiview;

import javax.swing.AbstractListModel;

import cs213.photoAlbum.control.PhotoSource;

public class UserList extends AbstractListModel<String> {
	
	private static final long serialVersionUID = 1;
	
	private PhotoSource control;
	private String[] users;
	
	public UserList(PhotoSource control) {
		this.control = control;
		users = control.listUsers();
	}
	
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
	
	public String getElementAt(int index) {
		if (index < 0 || index >= users.length) {
			return "";
		} else {
			return users[index];
		}
	}
	
	public int getSize() {
		return users.length;
	}

}