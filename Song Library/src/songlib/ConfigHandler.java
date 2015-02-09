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

public class ConfigHandler {
	
	public static SongListModel loadConfig() throws IOException {
		BufferedReader reader;
		ArrayList<String> lines = new ArrayList<String>();
		try {
			reader = new BufferedReader(new FileReader("config.txt"));
		} catch (FileNotFoundException e) {
			return new SongListModel(new SongList());
		}
		
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
		
		SongList songs = new SongList();
		Iterator<String> iterate = lines.iterator();
		while (iterate.hasNext()) {
			String[] details = iterate.next().split("\\|");
			if (details.length != 4) throw new IOException();
			songs.add(new Song(details[0], details[1], details[2], details[3].trim()));
		}
		
		return new SongListModel(songs);
	}
	
	public static boolean writeConfig(Song[] songs) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter("config.txt"));
		
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