package cs213.photoAlbum.simpleview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.GregorianCalendar;

import cs213.photoAlbum.control.Control;
import cs213.photoAlbum.control.PhotoSource;
import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Backend;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.model.User;

/**
 * 
 * 
 * @author Karan Kadaru
 */
public class CmdView {

	public static void main(String[] args) {
		String numArgs = "Error: Incorrect number of arguments for command";
		PhotoSource control = new Control();

		try {
			if (args[0].equals("adduser")) {
				if (args.length != 3) {
					puts(numArgs);
					return;
				}
				addUser(control, args[1], args[2]);
			} else if (args[0].equals("deleteuser")) {
				if (args.length != 2) {
					puts(numArgs);
					return;
				}
				deleteUser(args[1]);
			} else if (args[0].equals("listusers")) {
				if (args.length != 1) {
					puts(numArgs);
					return;
				}
				listUsers();
			} else if (args[0].equals("login")) {
				if (args.length != 2) {
					puts(numArgs);
					return;
				}
				control.setCurrentUser(args[1]);
				if (control.loadUserData()) {
					interactive(control, control.getUser(args[1]));
				} else {
					puts("user " + args[1] + " does not exist");
				}
			} else {
				puts("Error: Unrecognized command line argument.");
			}
		} finally {
			control.shutdown();
		}
	}

	private static void addUser(PhotoSource control, String name, String id) {
		if (control.addUser(name, id)) {
			puts("created user " + id + " with name " + name);
		} else {
			puts("user " + id + " already exists with name " + name);
		}
	}

	private static void deleteUser(String id) {
		if (Backend.deleteUser(id)) {
			puts("deleted user " + id);
		} else {
			puts("user " + id + " does not exist");
		}
	}

	private static void listUsers() {
		String[] users = Backend.listUsers();
		if (users.length > 0) {
			for (String user : users) {
				puts(user);
			}
		} else {
			puts("no users exist");
		}
	}

	private static void interactive(PhotoSource control, User currentUser) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			try {
				String line = br.readLine();
				String numArgs = "Error: Incorrect number of arguments for command.";

				String[] args = line.split("\\s+");
				if (args[0].equals("createAlbum")) {
					if (args.length != 2) {
						puts(numArgs);
						continue;
					}

					if (control.addAlbum(args[1])) {
						puts("created album for user " + currentUser.getId() + ":");
					} else {
						puts("album exists for user " + currentUser.getId() + ":");
					}
					puts(args[1]);
				} else if (args[0].equals("deleteAlbum")) {
					if (args.length != 2) {
						puts(numArgs);
						continue;
					}

					if (control.removeAlbum(args[1])) {
						puts("deleted album from user " + currentUser.getId() + ":");
					} else {
						puts("album does not exist for user " + currentUser.getId() + ":");
					}
					puts(args[1]);
				} else if (args[0].equals("listAlbums")) {
					if (args.length != 1) {
						puts(numArgs);
						continue;
					}

					Album[] albums = control.getAlbums();
					if (albums.length > 0) {
						puts("Albums for user " + currentUser.getId() + ":");

						for (Album album : albums) {
							long[] dates = album.getDateRange();
							Calendar start = new GregorianCalendar(), end = new GregorianCalendar();
							start.setTimeInMillis(dates[0]);
							end.setTimeInMillis(dates[1]);
							puts(album.getName() + " number of photos: " + album.getPhotos().length + ", " + start + " - " + end);
						}
					} else {
						puts("No albums exist for user " + currentUser.getId());
					}
				} else if (args[0].equals("listPhotos")) {
					if (args.length != 2) {
						puts(numArgs);
						continue;
					}

					puts("Photos for album " + args[1] + ":");
					Photo[] photos = control.getPhotosFromAlbum(args[1]);
					if (photos.length > 0) {
						Calendar date = new GregorianCalendar();
						for (Photo photo : photos) {
							date.setTimeInMillis(photo.getDate());
							puts(photo.getName() + " - " + date);
						}
					} else {
						puts("No photos exist for album " + args[1]);
					}
				} else if (args[0].equals("addPhoto")) {
					if (args.length != 4) {
						puts(numArgs);
						continue;
					}
					
					int status = control.addPhotoToAlbum(args[3], args[1], args[2]);
					if (status > 0) {
						puts("Added photo " + args[1] + ":");
						puts(args[2] + " - Album: " + args[3]);
					} else if (status == 0) {
						puts("Photo " + args[1] + " already exists in album " + args[3]);
					} else if (status == -1) {
						puts("File " + args[1] + " does not exist");
					}
				} else if (args[0].equals("movePhoto")) {
					if (args.length != 4) {
						puts(numArgs);
						continue;
					}
					
					int status = control.movePhoto(args[2], args[3], args[1]);
					if (status > 0) {
						puts("Moved photo " + args[1] + ":");
						puts(args[1] + " - From Album " + args[2] + " to album " + args[3]);
					} else if (status == -1) {
						puts("Photo " + args[1] + " does not exist in " + args[2]);
					}
				} else if (args[0].equals("removePhoto")) {
					if (args.length != 3) {
						puts(numArgs);
						continue;
					}
					
					if (control.removePhotoFromAlbum(args[2], args[1])) {
						puts("Removed photo:");
						puts(args[1] + " - From album " + args[2]);
					} else {
						puts("Photo " + args[1] + " is not in album " + args[2]);
					}
				} else if (args[0].equals("addTag")) {
					if (args.length != 3) {
						puts(numArgs);
						continue;
					}
					
					int index = args[2].indexOf(":");
					if (index > 0) {
						String type = args[2].substring(0, index);
						String value = args[2].substring(index + 1, args[2].length());
						if (control.addTagToPhoto(args[1], type, value)) {
							puts("Added tag:");
							puts(args[1] + " " + type + ":" + value);
						} else {
							puts("Tag already exists for " + args[1] + " " + type + ":" + value);
						}
					} else {
						puts("Error: Invalid format for tag.");
					}
				} else if (args[0].equals("deleteTag")) {
					if (args.length != 3) {
						puts(numArgs);
						continue;
					}

					int index = args[2].indexOf(":");
					if (index > 0) {
						String type = args[2].substring(0, index);
						String value = args[2].substring(index + 1, args[2].length());
						if (control.removeTagFromPhoto(args[1], type, value)) {
							puts("Deleted tag:");
							puts(args[1] + " " + type + ":" + value);
						} else {
							puts("Tag does not exist for " + args[1] + " " + type + ":" + value);
						}
					} else {
						puts("Error: Invalid format for tag.");
					}
				} else if (args[0].equals("listPhotoInfo")) {
					if (args.length != 2) {
						puts(numArgs);
						continue;
					}
					
					Photo photo = control.getPhoto(args[1]);
					if (photo == null) {
						puts("Photo " + args[1] + " does not exist");
						continue;
					}
					puts("Photo file name: " + photo.getName());

					String albums = "Album: ";
					for (Album album : photo.getContainingAlbums()) albums += album.getName() + ",";
					puts(albums.substring(0, albums.length() - 1));
					
					Calendar date = new GregorianCalendar();
					date.setTimeInMillis(photo.getDate());
					puts("Date: " + date);
					
					puts("Caption: " + photo.getCaption());
					
					puts("Tags:");
					String[] tags = photo.getTags();
					for (String tag : tags) puts(tag);
				} else if (args[0].equals("getPhotosByDate")) {

				} else if (args[0].equals("getPhotosByTag")) {

				} else if (args[0].equals("logout")) {

				} else {
					System.out.println("Error: Unrecognized command.");
				}
			} catch (IOException e) {
				System.out.println("Error: Could not read line from terminal. Exiting...");
				return;
			}
		}
	}

	private static void puts(String str) {
		System.out.println(str);
	}

}
