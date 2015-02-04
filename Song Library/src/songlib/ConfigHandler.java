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
	
	public static ArrayList<Song> loadConfig() throws IOException {
		BufferedReader reader;
		ArrayList<String> lines = new ArrayList<String>();
		try {
			reader = new BufferedReader(new FileReader("config.txt"));
		} catch (FileNotFoundException e) {
			return null;
		}
		
		try {
			String line = reader.readLine();
			
			while (line != null) {
				lines.add(line);
				line = reader.readLine();
			}
		} finally {
			reader.close();
		}
		
		ArrayList<Song> songs = new ArrayList<Song>();
		Iterator<String> iterate = lines.iterator();
		while (iterate.hasNext()) {
			String[] details = iterate.next().split("\\|");
			if (details.length != 4) throw new IOException();
			songs.add(new Song(details[0], details[1], details[2], details[3].trim()));
		}
		
		return songs;
	}
	
	public static boolean writeConfig(ArrayList<Song> songs) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter("config.txt"));
		
		try {
			Iterator<Song> iterate = songs.iterator();
			while (iterate.hasNext()) writer.write(iterate.next() + "\n");
		} catch(IOException e) {
			return false;
		} finally {
			writer.close();
		}
		
		return true;
	}
	
}
