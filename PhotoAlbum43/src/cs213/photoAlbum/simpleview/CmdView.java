package cs213.photoAlbum.simpleview;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import cs213.photoAlbum.control.Control;
import cs213.photoAlbum.control.PhotoSource;
import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Backend;
import cs213.photoAlbum.model.DataWriter;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.model.User;

/**
 * Class is an implementation of the initial command line view. It takes commands from the user,
 * validates their syntactic correctness, and calls methods on the control object to manipulate the
 * model accordingly. Class also contains the main method for the project.
 * @author Karan Kadaru
 */
public class CmdView {

	/**
	 * Method is the entry point for the entire project.
	 * @param args Array of command line arguments provided by the system.
	 */
	public static void main(String[] args) {
		// Define a much used error message, and instantiate the control object.
		String numArgs = "Error: Incorrect number of arguments for command";
		PhotoSource control = new Control();

		try {
			if (args[0].toLowerCase().equals("adduser")) {
				// User requested to add a new user.
				if (args.length != 3) {
					puts(numArgs);
					return;
				}
				addUser(control, args[1], args[2]);
			} else if (args[0].toLowerCase().equals("deleteuser")) {
				// User requested to remove an existing user.
				if (args.length != 2) {
					puts(numArgs);
					return;
				}
				deleteUser(args[1]);
			} else if (args[0].toLowerCase().equals("listusers")) {
				// User requested a list of the existing users.
				if (args.length != 1) {
					puts(numArgs);
					return;
				}
				listUsers();
			} else if (args[0].toLowerCase().equals("login")) {
				// User requested to enter interactive mode.
				if (args.length != 2) {
					puts(numArgs);
					return;
				}
				
				// Set the current user to the ID provided.
				control.setCurrentUser(args[1]);
				
				// Load data for the chosen user, or detect that a user with the given ID does not exist.
				if (control.loadUserData()) {
					// Provided ID was valid. Time to enter interactive mode.
					interactive(control, control.getUser(args[1]));
				} else {
					puts("user " + args[1] + " does not exist");
				}
			} else {
				// User provided an invalid argument.
				puts("Error: Unrecognized command line argument.");
			}
		} finally {
			// Guarantee that upon shutdown, the current user state will be written out to disk.
			control.shutdown();
		}
	}

	// Method is responsible for creating a new user with the requested information, or detecting that one already exists.
	private static void addUser(PhotoSource control, String id, String name) {
		if (control.addUser(id, name)) {
			puts("created user " + id + " with name " + name);
		} else {
			puts("user " + id + " already exists with name " + name);
		}
	}

	// Method is responsible for deleting an existing user with the requested information, or detecting that one does not exist.
	private static void deleteUser(String id) {
		// No need to involve the control for a task this simple. Simply request that the backend remove any existing files for the
		// specified user.
		DataWriter backend = new Backend();
		if (backend.deleteUser(id)) {
			puts("deleted user " + id);
		} else {
			puts("user " + id + " does not exist");
		}
	}

	// Method is responsible for listing all existing users.
	private static void listUsers() {
		// No need to involve the control for a task this simple. Simply request that the backend list the IDs of all currently
		// existing user files.
		DataWriter backend = new Backend();
		String[] users = backend.listUsers();
		if (users.length > 0) {
			for (String user : users) {
				puts(user);
			}
		} else {
			puts("no users exist");
		}
	}

	// Method is responsible for the vast majority of heavy lifting for the command line view, and implements interactive mode.
	private static void interactive(PhotoSource control, User currentUser) {
		// Instantiate a BufferedReader to read lines from the user, and a SimpleDateFormat to verify and manipulate specified
		// dates.
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy-hh:mm:ss");
		while (true) {
			try {
				// Get a line from the user, and define a much user error message.
				String line = br.readLine();
				String numArgs = "Error: Incorrect number of arguments for command.";

				// Parse the odd argument format specified for the project.
				String[] args = line.split("\\s+");
				args = parseArgs(line, args);
				args[0] = args[0].toLowerCase();

				if (args[0].equals("createalbum")) {
					// We're creating a new album. Verify args and do it.
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
				} else if (args[0].equals("deletealbum")) {
					// We're deleting an album. Verify args and do it.
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
				} else if (args[0].equals("listalbums")) {
					// We're listing all existing albums. Verify args and do it.
					if (args.length != 1) {
						puts(numArgs);
						continue;
					}

					Album[] albums = control.getAlbums();
					if (albums.length > 0) {
						puts("Albums for user " + currentUser.getId() + ":");

						for (Album album : albums) {
							long[] dates = album.getDateRange();
							if (dates != null) {
								Calendar start = new GregorianCalendar(), end = new GregorianCalendar();
								start.setTimeInMillis(dates[0]);
								end.setTimeInMillis(dates[1]);
								puts(album.getName() + " number of photos: " + album.getPhotos().length + ", " + format.format(start.getTime()) + " - " + format.format(end.getTime()));
							} else {
								puts(album.getName() + " number of photos: 0");
							}
						}
					} else {
						puts("No albums exist for user " + currentUser.getId());
					}
				} else if (args[0].equals("listphotos")) {
					// We're listing all photos within an album. Verifying args and do it.
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
							puts(photo.getName() + " - " + format.format(date.getTime()));
						}
					}
				} else if (args[0].equals("addphoto")) {
					// We're adding a photo to an album. Verify args and do it.
					if (args.length != 4) {
						puts(numArgs);
						continue;
					}
					
					args[1] = getFullPath(args[1]);
					int status = control.addPhotoToAlbum(args[3], args[1], args[2]);
					if (status > 0) {
						// Add operation was successful.
						puts("Added photo " + args[1] + ":");
						if (status == 1) {
							// Photo has never been added to the program before.
							puts(args[2] + " - Album: " + args[3]);
						} else {
							// Photo already exists in another album. Get its caption and print out the message.
							Photo photo = control.getPhoto(args[1]);
							puts(photo.getCaption() + " - Album: " + args[3]);
						}
					} else if (status == 0) {
						// Photo already exists in the specified album.
						puts("Photo " + args[1] + " already exists in album " + args[3]);
					} else if (status == -1) {
						// The specific file does not exist.
						puts("File " + args[1] + " does not exist");
					} else if (status == -2) {
						// File.getCanonicalPath() threw an IOException. Don't actually know what would cause this, or
						// what it would signify. Hopefully it'll never happen.
						puts("Error: Ran into an error while verifying the photo's fully qualified path.");
					}
				} else if (args[0].equals("movephoto")) {
					// We're moving a photo from one album to another. Verify args and do it.
					if (args.length != 4) {
						puts(numArgs);
						continue;
					}

					args[1] = getFullPath(args[1]);
					int status = control.movePhoto(args[2], args[3], args[1]);
					if (status > 0) {
						// Move was successful.
						puts("Moved photo " + args[1] + ":");
						puts(args[1] + " - From Album " + args[2] + " to album " + args[3]);
					} else if (status == -1) {
						// Photo did not exist in the fromAlbum.
						puts("Photo " + args[1] + " does not exist in " + args[2]);
					}
				} else if (args[0].equals("removephoto")) {
					// We're removing a photo from an album. Verify args and do it.
					if (args.length != 3) {
						puts(numArgs);
						continue;
					}

					args[1] = getFullPath(args[1]);
					if (control.removePhotoFromAlbum(args[2], args[1])) {
						puts("Removed photo:");
						puts(args[1] + " - From album " + args[2]);
					} else {
						puts("Photo " + args[1] + " is not in album " + args[2]);
					}
				} else if (args[0].equals("addtag")) {
					// We're adding a tag to a photo. Verify args and do it.
					if (args.length != 4) {
						puts(numArgs);
						continue;
					}

					args[1] = getFullPath(args[1]);
					if (control.addTagToPhoto(args[1], args[2], args[3])) {
						puts("Added tag:");
						puts(args[1] + " " + args[2] + ":" + args[3]);
					} else {
						puts("Tag already exists for " + args[1] + " " + args[2] + ":" + args[3]);
					}
				} else if (args[0].equals("deletetag")) {
					// We're deleting a tag from a photo. Verify args and do it.
					if (args.length != 4) {
						puts(numArgs);
						continue;
					}

					args[1] = getFullPath(args[1]);
					if (control.removeTagFromPhoto(args[1], args[2], args[3])) {
						puts("Deleted tag:");
						puts(args[1] + " " + args[2] + ":" + args[3]);
					} else {
						puts("Tag does not exist for " + args[1] + " " + args[2] + ":" + args[3]);
					}
				} else if (args[0].equals("listphotoinfo")) {
					// We're listing all information for a specific photo. Verify args and do it.
					if (args.length != 2) {
						puts(numArgs);
						continue;
					}

					// Get the requested photo and verify that it actually exists.
					args[1] = getFullPath(args[1]);
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
					puts("Date: " + format.format(date.getTime()));

					puts("Caption: " + photo.getCaption());

					puts("Tags:");
					String[] tags = photo.getTags();
					for (String tag : tags) puts(tag);
				} else if (args[0].equals("getphotosbydate")) {
					// We're getting photos within a date range. Verify args and do it.
					if (args.length != 3) {
						puts(numArgs);
						continue;
					}

					// Instantiate calendar objects for our starting and ending dates.
					Calendar startDate = new GregorianCalendar(), endDate = new GregorianCalendar();
					try {
						// Parse the dates given by the user.
						startDate.setTime(format.parse(args[1]));
						endDate.setTime(format.parse(args[2]));
					} catch (ParseException e) {
						// The user supplied and invalid date format.
						puts("Error: Invalid date format. Format must conform to MM/DD/YYYY-HH:MM:SS");
						continue;
					}
					// Get the photos.
					Photo[] photos = control.getPhotosByDate(startDate.getTimeInMillis(), endDate.getTimeInMillis());
					puts("Photos for user " + currentUser.getId() + " in range " + format.format(startDate.getTime()) + " to " + format.format(endDate.getTime()) + ":");
					printPhotos(photos, format);
				} else if (args[0].equals("getphotosbytag")) {
					// We're getting photos that contain a list of tags. Verify args and do it.
					if (args.length < 2) {
						puts(numArgs);
						continue;
					}

					// Create a set to hold the matching photos.
					String output = "Photos for " + currentUser.getId() + " with tags ";
					Set<Photo> allPhotos = new HashSet<Photo>();
					boolean first = true;
					for (int i = 1; i < args.length; i++) {
						// Parse out the tags.
						String tagPiece = args[i], type = "", value = args[i];
						if (tagPiece.indexOf("7yp3:") == 0) {
							// The current tag has a specified type.
							type = tagPiece.substring(5, tagPiece.length());
							value = args[++i];
							output += type + ":" + value + ", ";
						} else {
							// The current tag was given only a value.
							output += tagPiece + ", ";
						}
						
						if (first) {
							// The first iteration just adds all matching photos from the current album.
							first = false;

							Photo[] photos;
							if (!type.equals("")) {
								photos = control.getPhotosByTag(type, value);
							} else {
								Photo[] tmp = control.getPhotos();
								Set<Photo> tmpPhotos = new HashSet<Photo>();
								for (Photo photo : tmp) if (photo.hasTag(value, false)) tmpPhotos.add(photo);
								photos = new Photo[tmpPhotos.size()];
								tmpPhotos.toArray(photos);
							}
							for (Photo photo : photos) allPhotos.add(photo);
						} else {
							// All subsequent iterations simply remove the photos they don't contain as we're performing an intersection.
							Iterator<Photo> iterate = allPhotos.iterator();
							Set<Photo> intersection = new HashSet<Photo>();
							while (iterate.hasNext()) {
								Photo current = iterate.next();
								if (!type.equals("") && current.hasTag(type, value)) {
									intersection.add(current);
								} else if (current.hasTag(value, false)) {
									intersection.add(current);
								}
							}
							allPhotos = intersection;
						}
					}
					Photo[] printed = new Photo[allPhotos.size()];
					allPhotos.toArray(printed);

					// Print out the returned photos.
					puts(output.substring(0, output.length() - 2) + ":");
					printPhotos(printed, format);
				} else if (args[0].equals("logout")) {
					// We're done here.
					break;
				} else {
					// The user provided an invalid argument.
					System.out.println("Error: Unrecognized command.");
				}
			} catch (IOException e) {
				// This should never happen, but it means that the BufferedReader threw an IOException. I have no idea what would cause this.
				// Hopefully it'll never happen.
				System.out.println("Error: Could not read line from terminal. Exiting...");
				return;
			}
		}
	}

	// Method is responsible for printing a set of photos.
	private static void printPhotos(Photo[] photos, SimpleDateFormat format) {
		Calendar date = new GregorianCalendar();
		for (Photo photo : photos) {
			String output = photo.getCaption() + " - Album: ";
			Album[] albums = photo.getContainingAlbums();
			for (Album album : albums) output += album.getName() + ",";
			date.setTimeInMillis(photo.getDate());
			output = output.substring(0, output.length() - 1) + " - Date: " + format.format(date.getTime());
			puts(output);
		}
	}

	// This function makes me weep, but it works.
	// Very, very, ugly code to parse input from the user.
	// Most of the ugliness comes from having to support commands that have some arguments enclosed in quotations, others
	// that aren't, and one command that has a variable tag format.
	// Probably could be refactored into less of a mess, but I'm lazy and this works.
	private static String[] parseArgs(String line, String[] args) {
		// As getPhotosByDate takes in its dates in a very specific format, we do no parsing of those commands.
		if (line.indexOf("\"") > 0 && !args[0].toLowerCase().equals("getphotosbydate")) {
			// Supplied line includes at least one quote pair.
			ArrayList<String> tmpArgs = new ArrayList<String>();
			String totalArg = new String();
			int index = -1;
			boolean collecting = false;
			for (String arg : args) {
				// Simple state machine that differentiates between whether or not we're currently parsing a multi-word argument.
				if (!collecting) {
					totalArg = arg;
					if ((index = totalArg.indexOf("\"")) >= 0) {
						if (totalArg.indexOf("\"", index + 1) < 0) {
							collecting = true;
						} else {
							if ((index = totalArg.indexOf(":")) > 0 && totalArg.toLowerCase().indexOf("c:") < 0) {
								// getPhotosByTag needs to know whether each argument is a type or a value, so this allows for that.
								if (args[0].toLowerCase().equals("getphotosbytag") && totalArg.charAt(0) != ',') {
									tmpArgs.add("7yp3:" + totalArg.substring(0, index));
								} else if (args[0].toLowerCase().equals("getphotosbytag")) {
									tmpArgs.add("7yp3:" + totalArg.substring(1, index));
								} else {
									tmpArgs.add(totalArg.substring(0, index));
								}
								tmpArgs.add(totalArg.substring(index + 2, totalArg.length() - 1));
							} else {
								tmpArgs.add(totalArg.substring(1, totalArg.length() - 1));
							}
						}
					} else {
						if ((index = totalArg.indexOf(":")) > 0 && totalArg.toLowerCase().indexOf("c:") < 0) {
							if (args[0].toLowerCase().equals("getphotosbytag") && totalArg.charAt(0) != ',') {
								tmpArgs.add("7yp3:" + totalArg.substring(0, index));
							} else if (args[0].toLowerCase().equals("getphotosbytag")) {
								tmpArgs.add("7yp3:" + totalArg.substring(1, index));
							} else {
								tmpArgs.add(totalArg.substring(0, index));
							}
							tmpArgs.add(totalArg.substring(index + 1, totalArg.length()));
						} else {
							tmpArgs.add(totalArg);
						}
					}
				} else {
					totalArg += " " + arg;
					if (totalArg.indexOf("\"", index + 1) > 0) {
						collecting = false;
						if ((index = totalArg.indexOf(":")) > 0 && line.toLowerCase().indexOf("c:") < 0) {
							if (args[0].toLowerCase().equals("getphotosbytag") && totalArg.charAt(0) != ',') {
								tmpArgs.add("7yp3:" + totalArg.substring(0, index));
							} else if (args[0].toLowerCase().equals("getphotosbytag")) {
								tmpArgs.add("7yp3:" + totalArg.substring(1, index));
							} else {
								tmpArgs.add(totalArg.substring(0, index));
							}
							tmpArgs.add(totalArg.substring(index + 2, totalArg.length() - 1));
						} else {
							tmpArgs.add(totalArg.substring(1, totalArg.length() - 1));
						}
					}
				}
			}
			args = new String[tmpArgs.size()];
			tmpArgs.toArray(args);
		} else if (line.indexOf(":") > 0 && !args[0].toLowerCase().equals("getphotosbydate") && line.toLowerCase().indexOf("c:") < 0) {
			// Supplied line contains no quotes. Yay!
			ArrayList<String> tmpArgs = new ArrayList<String>();

			for (String arg : args) {
				int index = arg.indexOf(":");
				if (index > 0) {
					if (args[0].toLowerCase().equals("getphotosbytag") && arg.charAt(0) != ',') {
						tmpArgs.add("7yp3:" + arg.substring(0, index));
					} else if (args[0].toLowerCase().equals("getphotosbytag")) {
						tmpArgs.add("7yp3:" + arg.substring(1, index));
					} else {
						tmpArgs.add(arg.substring(0, index));
					}
					tmpArgs.add(arg.substring(index + 1, arg.length()));
				} else {
					tmpArgs.add(arg);
				}
			}

			args = new String[tmpArgs.size()];
			tmpArgs.toArray(args);
		}	
		return args;
	}
	
	private static String getFullPath(String path) {
		try {
			File file = new File(path);
			return file.getCanonicalPath();
		} catch (Exception e) {
			return "";
		}
	}

	// I got tired of typing out System.out.println, and I like coding in Ruby.
	private static void puts(String str) {
		System.out.println(str);
	}

}
