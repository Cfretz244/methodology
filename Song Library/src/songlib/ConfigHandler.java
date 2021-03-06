/*
 * Chris Fretz and Karan Kadaru
 */

package songlib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

// A simple utility class for reading in the songlist during startup,
// and writing out changes as they are made.
public class ConfigHandler {
	
	// Loads the songs during startup.
	public static SongListModel loadConfig() throws IOException {
		BufferedReader reader;
		ArrayList<String> lines = new ArrayList<String>();
		
		// Open file for reading if it exists. Otherwise, we're done, return
		// an empty SongListModel.
		try {
			reader = new BufferedReader(new FileReader("config.txt"));
		} catch (FileNotFoundException e) {
			return new SongListModel(new SongList());
		}
		
		// Read file line by line. If we encounter an exception, just return an
		// empty list.
		try {
			String line = reader.readLine();
			
			while (line != null) {
				lines.add(line);
				line = reader.readLine();
			}
		} catch (IOException e) {
			return new SongListModel(new SongList());
		} finally {
			reader.close();
		}
		
		// Iterate over the lines of the file, splitting by the pipe character,
		// and create a Song object for each one.
		SongList songs = new SongList();
		Iterator<String> iterate = lines.iterator();
		while (iterate.hasNext()) {
			String[] details = iterate.next().split("\\|");
			songs.add(new Song(details[0], details[1], details[2], details[3].trim()));
		}
		
		// Return the completed model.
		return new SongListModel(songs);
	}
	
	// Writes the songs out to the file whenever a change is made.
	public static boolean writeConfig(Song[] songs) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter("config.txt"));
		
		// Iterate over the songs and write a line *for each.
		try {
			for (Song song : songs) {
				String line = song.getName() + "|" + song.getArtist() + "|";
				line += song.getAlbum() + "|" + song.getYear() + "\n";
				writer.write(line);
			}
		} catch(IOException e) {
			return false;
		} finally {
			writer.close();
		}
		
		return true;
	}
	
}
