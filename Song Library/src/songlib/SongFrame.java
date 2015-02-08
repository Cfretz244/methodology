package songlib;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class SongFrame extends JFrame {
	
	private static final long serialVersionUID = 1;
	
	public SongFrame(SongListModel model) {
		setLayout(new GridLayout(1, 2));
		JList<Song> list = new JList<Song>(model);
		list.setPreferredSize(new Dimension(200, 200));
		list.addListSelectionListener(new ListSelectionListener() {
			int previousIndex = -1;
			
			public void valueChanged(ListSelectionEvent e) {
				Object changed = e.getSource();
				if (changed instanceof JList<?> && !e.getValueIsAdjusting()) {
					@SuppressWarnings("unchecked")
					JList<Song> list = (JList<Song>) changed;
					int index;
					if (previousIndex == e.getFirstIndex()) {
						index = e.getLastIndex();
					} else {
						index = e.getFirstIndex();
					}
					previousIndex = index;
					Song song = list.getModel().getElementAt(index);
					Component[] views = getContentPane().getComponents();
					remove(getContentPane().getComponent(1));
					JPanel details = new JPanel(new GridLayout(4, 1));
					JLabel name = new JLabel("Name: " + song.getName());
					JLabel artist = new JLabel("Artist: " + song.getArtist());
					JLabel album = new JLabel("Album: " + song.getAlbum());
					JLabel year = new JLabel("Year: " + song.getYear());
					details.add(name);
					details.add(artist);
					details.add(album);
					details.add(year);
					add(details);
					pack();
				}
			}
		});
		
		JScrollPane scroll = new JScrollPane(list);
		add(scroll);

		JPanel details = new JPanel(new GridLayout(4, 1));
		add(new JPanel());
		pack();
		setLocationRelativeTo(null);
		list.setSelectedIndex(0);
	}

}
