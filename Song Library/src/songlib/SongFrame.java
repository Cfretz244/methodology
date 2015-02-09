package songlib;

import java.awt.BorderLayout;
import java.awt.Dimension;
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

public class SongFrame extends JFrame {
	
	private static final long serialVersionUID = 1;
	private JPanel details, centerPanel, northPanel;
	private JList<Song> list;
	private SongListModel model;
	private boolean isAdding = false;
	
	public SongFrame(SongListModel model) {
		this.model = model;

		centerPanel = new JPanel(new GridLayout(1, 2));
		list = new JList<Song>(model);
		details = new JPanel(new GridLayout(4, 1));
		JScrollPane scroll = new JScrollPane(list);
		centerPanel.add(scroll);
		centerPanel.add(details);
		
		setupNorth();
		attachListListener();
		
		add(centerPanel, BorderLayout.CENTER);
		add(northPanel, BorderLayout.NORTH);
		pack();
		setLocationRelativeTo(null);
		list.setSelectedIndex(0);
	}
	
	public void setupNorth() {
		northPanel = new JPanel(new GridLayout(1, 4));
		JButton addButton = new JButton("Add");
		JButton editButton = new JButton("Edit");
		JButton removeButton = new JButton("Remove");
		JButton doneButton = new JButton("Done");
		ActionListener handler = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object source = e.getSource();
				if (source instanceof JButton) {
					JButton clicked = (JButton) source;
					String text = clicked.getText();
					int index = list.getSelectedIndex();
					Song current = model.getElementAt(index);
					if (text.equals("Add") || text.equals("Edit")) {
						boolean editing = text.equals("Edit");
						isAdding = !editing;

						String nameStr = editing ? current.getName() : "";
						String artistStr = editing ? current.getArtist() : "";
						String albumStr = editing ? current.getAlbum() : "";
						String yearStr = editing ? current.getYear() : "";

						JTextField name = new JTextField(nameStr);
						JTextField artist = new JTextField(artistStr);
						JTextField album = new JTextField(albumStr);
						JTextField year = new JTextField(yearStr);
						
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
						
						northPanel.removeAll();
						northPanel.add(doneButton);
						northPanel.revalidate();
						northPanel.repaint();
					} else if (text.equals("Remove")) {
						model.removeSong(current);
					} else if (text.equals("Done")) {
						String name = ((JTextField) details.getComponent(1)).getText();
						String author = ((JTextField) details.getComponent(3)).getText();
						String album = ((JTextField) details.getComponent(5)).getText();
						String year = ((JTextField) details.getComponent(7)).getText();
						if (isAdding) {
							Song added = new Song(name, author, album, year);
							model.addSong(added);
						} else {
							Song update = new Song(name, author, album, year);
							model.updateSong(current, update);
						}
						
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
		addButton.addActionListener(handler);
		editButton.addActionListener(handler);
		removeButton.addActionListener(handler);
		doneButton.addActionListener(handler);
		northPanel.add(addButton);
		northPanel.add(editButton);
		northPanel.add(removeButton);
	}
	
	public void attachListListener() {
		list.addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					int index = list.getSelectedIndex();
					if (index < 0) {
						list.setSelectedIndex(0);
						index = 0;
					}
					Song song = list.getModel().getElementAt(index);
					centerPanel.remove(centerPanel.getComponent(1));
					JLabel name = new JLabel("Name: " + song.getName());
					JLabel artist = new JLabel("Artist: " + song.getArtist());
					JLabel album = new JLabel("Album: " + song.getAlbum());
					JLabel year = new JLabel("Year: " + song.getYear());
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
