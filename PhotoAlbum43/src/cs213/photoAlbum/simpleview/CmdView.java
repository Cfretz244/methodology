package cs213.photoAlbum.simpleview;

import java.io.BufferedReader;
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

	private static void addUser(PhotoSource control, String id, String name) {
		if (control.addUser(id, name)) {
			puts("created user " + id + " with name " + name);
		} else {
			puts("user " + id + " already exists with name " + name);
		}
	}

	private static void deleteUser(String id) {
		DataWriter backend = new Backend();
		if (backend.deleteUser(id)) {
			puts("deleted user " + id);
		} else {
			puts("user " + id + " does not exist");
		}
	}

	private static void listUsers() {
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

	private static void interactive(PhotoSource control, User currentUser) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		SimpleDateFormat format = new SimpleDateFormat("M/d/y-h:m:s");
		while (true) {
			try {
				String line = br.readLine();
				String numArgs = "Error: Incorrect number of arguments for command.";

				String[] args = line.split("\\s+");
				args = parseArgs(line, args);

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
							puts(photo.getName() + " - " + format.format(date.getTime()));
						}
					} 
				} else if (args[0].equals("addPhoto")) {
					if (args.length != 4) {
						puts(numArgs);
						continue;
					}

					int status = control.addPhotoToAlbum(args[3], args[1], args[2]);
					if (status > 0) {
						puts("Added photo " + args[1] + ":");
						if (status == 1) {
							puts(args[2] + " - Album: " + args[3]);
						} else {
							Photo photo = control.getPhoto(args[1]);
							puts(photo.getCaption() + " - Album: " + args[3]);
						}
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
					if (args.length != 4) {
						puts(numArgs);
						continue;
					}

					if (control.addTagToPhoto(args[1], args[2], args[3])) {
						puts("Added tag:");
						puts(args[1] + " " + args[2] + ":" + args[3]);
					} else {
						puts("Tag already exists for " + args[1] + " " + args[2] + ":" + args[3]);
					}
				} else if (args[0].equals("deleteTag")) {
					if (args.length != 4) {
						puts(numArgs);
						continue;
					}

					if (control.removeTagFromPhoto(args[1], args[2], args[3])) {
						puts("Deleted tag:");
						puts(args[1] + " " + args[2] + ":" + args[3]);
					} else {
						puts("Tag does not exist for " + args[1] + " " + args[2] + ":" + args[3]);
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
					puts("Date: " + format.format(date.getTime()));

					puts("Caption: " + photo.getCaption());

					puts("Tags:");
					String[] tags = photo.getTags();
					for (String tag : tags) puts(tag);
				} else if (args[0].equals("getPhotosByDate")) {
					if (args.length != 3) {
						puts(numArgs);
						continue;
					}

					Calendar startDate = new GregorianCalendar(), endDate = new GregorianCalendar();
					try {
						startDate.setTime(format.parse(args[1]));
						endDate.setTime(format.parse(args[2]));
					} catch (ParseException e) {
						puts("Error: Invalid date format. Format must conform to MM/DD/YYYY-HH:MM:SS");
						continue;
					}
					Photo[] photos = control.getPhotosByDate(startDate.getTimeInMillis(), endDate.getTimeInMillis());
					puts("Photos for user " + currentUser.getId() + " in range " + format.format(startDate.getTime()) + " to " + format.format(endDate.getTime()) + ":");
					printPhotos(photos, format);
				} else if (args[0].equals("getPhotosByTag")) {
					if (args.length < 2) {
						puts(numArgs);
						continue;
					}

					String output = "Photos for " + currentUser.getId() + " with tags ";
					Set<Photo> allPhotos = new HashSet<Photo>();
					boolean first = true;
					for (int i = 1; i < args.length; i++) {
						String tagPiece = args[i], type = "", value = args[i];
						if (tagPiece.indexOf("7¥p3:") == 0) {
							type = tagPiece.substring(5, tagPiece.length());
							value = args[++i];
							output += type + ":" + value + ", ";
						} else {
							output += tagPiece + ", ";
						}
						
						if (first) {
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

					puts(output.substring(0, output.length() - 2) + ":");
					printPhotos(printed, format);
				} else if (args[0].equals("logout")) {
					break;
				} else {
					System.out.println("Error: Unrecognized command.");
				}
			} catch (IOException e) {
				System.out.println("Error: Could not read line from terminal. Exiting...");
				return;
			}
		}
	}

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
	private static String[] parseArgs(String line, String[] args) {
		if (line.indexOf("\"") > 0 && !args[0].equals("getPhotosByDate")) {
			ArrayList<String> tmpArgs = new ArrayList<String>();
			String totalArg = new String();
			int index = -1;
			boolean collecting = false;
			for (String arg : args) {
				if (!collecting) {
					totalArg = arg;
					if ((index = totalArg.indexOf("\"")) >= 0) {
						if (totalArg.indexOf("\"", index + 1) < 0) {
							collecting = true;
						} else {
							if ((index = totalArg.indexOf(":")) > 0) {
								if (args[0].equals("getPhotosByTag") && totalArg.charAt(0) != ',') {
									tmpArgs.add("7¥p3:" + totalArg.substring(0, index));
								} else if (args[0].equals("getPhotosByTag")) {
									tmpArgs.add("7¥p3:" + totalArg.substring(1, index));
								} else {
									tmpArgs.add(totalArg.substring(0, index));
								}
								tmpArgs.add(totalArg.substring(index + 2, totalArg.length() - 1));
							} else {
								tmpArgs.add(totalArg.substring(1, totalArg.length() - 1));
							}
						}
					} else {
						if ((index = totalArg.indexOf(":")) > 0) {
							if (args[0].equals("getPhotosByTag") && totalArg.charAt(0) != ',') {
								tmpArgs.add("7¥p3:" + totalArg.substring(0, index));
							} else if (args[0].equals("getPhotosByTag")) {
								tmpArgs.add("7¥p3:" + totalArg.substring(1, index));
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
						if ((index = totalArg.indexOf(":")) > 0) {
							if (args[0].equals("getPhotosByTag") && totalArg.charAt(0) != ',') {
								tmpArgs.add("7¥p3:" + totalArg.substring(0, index));
							} else if (args[0].equals("getPhotosByTag")) {
								tmpArgs.add("7¥p3:" + totalArg.substring(1, index));
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
		} else if (line.indexOf(":") > 0 && !args[0].equals("getPhotosByDate")) {
			ArrayList<String> tmpArgs = new ArrayList<String>();

			for (String arg : args) {
				int index = arg.indexOf(":");
				if (index > 0) {
					if (args[0].equals("getPhotosByTag") && arg.charAt(0) != ',') {
						tmpArgs.add("7¥p3:" + arg.substring(0, index));
					} else if (args[0].equals("getPhotosByTag")) {
						tmpArgs.add("7¥p3:" + arg.substring(1, index));
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

	private static void puts(String str) {
		System.out.println(str);
	}

}