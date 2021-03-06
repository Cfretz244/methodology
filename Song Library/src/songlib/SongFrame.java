/*
 * Chris Fretz and Karan Kadaru
 */

package songlib;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

// Class represents the base frame of the program, and unfortunately
// also does almost all of the work. It's ugly code, but it works.
public class SongFrame extends JFrame {

	private static final long serialVersionUID = 1;
	private JPanel details, centerPanel, northPanel;
	private JButton addButton, removeButton, editButton, doneButton;
	private JList<Song> list;
	private SongListModel model;
	private boolean isAdding = false;

	// Creates a new SongFrame, keeping the default BorderLayout
	// and displaying whatever songs were in the config file.
	public SongFrame(SongListModel model) {
		this.model = model;

		// Center zone of BorderLayout contains a jpanel setup with a GridLayout with
		// 1 row and 2 columns. The first column contains the JScrollPane and JList 
		// (which shows the songs) and the other contains the details panel.
		centerPanel = new JPanel(new GridLayout(1, 2));
		list = new JList<Song>(model);
		details = new JPanel(new GridLayout(4, 1));
		JScrollPane scroll = new JScrollPane(list);
		centerPanel.add(scroll);
		centerPanel.add(details);

		// Add the add, edit, and remove buttonsm and attach the listener to update the
		// details panel.
		setupNorth();
		attachListListener();

		// Add the panels we've created to the SongFrame.
		add(centerPanel, BorderLayout.CENTER);
		add(northPanel, BorderLayout.NORTH);
		pack();
		setLocationRelativeTo(null);
		list.setSelectedIndex(0);
	}

	// Function is responsible for setting up the Add, Edit, and Remove buttons, and also defines
	// handlers for their click events.
	public void setupNorth() {
		northPanel = new JPanel(new GridLayout(1, 4));
		addButton = new JButton("Add");
		editButton = new JButton("Edit");
		removeButton = new JButton("Remove");
		doneButton = new JButton("Done");

		// This is ugly, and I wouldn't do it if this were a larger scale project, but this anonymous
		// ActionListener class handles all necessary updates when the add, edit, or remove buttons
		// are clicked.
		ActionListener handler = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Object source = e.getSource();
				if (source instanceof JButton) {
					// Get the button that was clicked, and all the necessary info about it.
					JButton clicked = (JButton) source;
					int index = list.getSelectedIndex();
					Song current = model.getElementAt(index);

					if (clicked == addButton ||clicked == editButton) {
						// Putting both the add and edit buttons in the same clause avoids code
						// duplication.
						boolean editing = clicked == editButton;
						isAdding = !editing;
						
						// Ternary operator, returns the first clause if the condition is true
						// and the second clause otherwise. Essentially its an inline if else.
						String nameStr = editing ? current.getName() : "";
						String artistStr = editing ? current.getArtist() : "";
						String albumStr = editing ? current.getAlbum() : "";
						String yearStr = editing ? current.getYear() : "";

						// Create the text fields and pre-populate them with either the current
						// song's info, or the empty string.
						JTextField name = new JTextField(nameStr);
						JTextField artist = new JTextField(artistStr);
						JTextField album = new JTextField(albumStr);
						JTextField year = new JTextField(yearStr);

						// Remove the previous components, and add our labels and textfields.
						details.removeAll();
						details.setLayout(new GridLayout(4, 2));
						details.add(new JLabel("Name:"));
						details.add(name);
						details.add(new JLabel("Artist:"));
						details.add(artist);
						details.add(new JLabel("Album:"));
						details.add(album);
						details.add(new JLabel("Year:"));
						details.add(year);
						details.revalidate();
						details.repaint();

						// Remove the add, edit, and remove buttons and replace them with the done button.
						northPanel.removeAll();
						northPanel.add(doneButton);
						northPanel.revalidate();
						northPanel.repaint();
					} else if (clicked == removeButton) {
						// Just remove the currently selected song. The JList automatically redraws because the model
						// calls the fireIntervalRemoved method.
						model.removeSong(current);
					} else if (clicked == doneButton) {
						// Get the entered details, and either call add or update depending on what was being done.
						String name = ((JTextField) details.getComponent(1)).getText();
						String artist = ((JTextField) details.getComponent(3)).getText();
						String album = ((JTextField) details.getComponent(5)).getText();
						String year = ((JTextField) details.getComponent(7)).getText();

						if(model.contains(name, artist)){
							doneButton.setForeground(Color.RED);
							doneButton.setText("Please enter a nonexisting song! Then try again.");
							return;
							}
						if(name.equals("") || artist.equals("")){
							doneButton.setForeground(Color.RED);
							doneButton.setText("You must enter a song name and artist at the very least.");
							return;
						}
						doneButton.setForeground(Color.black);
						doneButton.setText("Done");



						if (isAdding) {
							Song added = new Song(name, artist, album, year);
							model.addSong(added);
						} else {
							Song update = new Song(name, artist, album, year);
							model.updateSong(current, update);
						}

						// Remove the textfields and replace them with labels updated with the new information.
						details.removeAll();
						details.setLayout(new GridLayout(4, 1));
						current = model.getElementAt(index);
						details.add(new JLabel("Name: " + current.getName()));
						details.add(new JLabel("Artist: " + current.getArtist()));
						details.add(new JLabel("Album: " + current.getAlbum()));
						details.add(new JLabel("Year: " + current.getYear()));
						details.revalidate();
						details.repaint();

						northPanel.removeAll();
						northPanel.add(addButton);
						northPanel.add(editButton);
						northPanel.add(removeButton);
						northPanel.revalidate();
						northPanel.repaint();
					}
				}
			}

		};

		// Attach that big ugly ActionListener we just defined.
		addButton.addActionListener(handler);
		editButton.addActionListener(handler);
		removeButton.addActionListener(handler);
		doneButton.addActionListener(handler);

		// Add the buttons to the panel.
		northPanel.add(addButton);
		northPanel.add(editButton);
		northPanel.add(removeButton);
	}

	public void attachListListener() {
		list.addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {
				// Only update the view when the list selection has finished changing.
				if (!e.getValueIsAdjusting()) {
					int index = list.getSelectedIndex();
					if (index < 0) {
						list.setSelectedIndex(0);
						index = 0;
					}

					// Get the selected list, and update the details panel with its information.
					Song song = list.getModel().getElementAt(index);
					centerPanel.remove(centerPanel.getComponent(1));
					JLabel name = new JLabel("Name: " + song.getName());
					JLabel artist = new JLabel("Artist: " + song.getArtist());
					JLabel album = new JLabel("Album: " + song.getAlbum());
					JLabel year = new JLabel("Year: " + song.getYear());
					
					northPanel.removeAll();
					northPanel.add(addButton);
					northPanel.add(editButton);
					northPanel.add(removeButton);

					// Remove the old labels and add the new ones, and resize the window.
					details.removeAll();
					details.add(name);
					details.add(artist);
					details.add(album);
					details.add(year);
					centerPanel.add(details);
					pack();
				}
			}

		});
	}

}
