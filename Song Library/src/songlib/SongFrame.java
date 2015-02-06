package songlib;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class SongFrame extends JFrame {
	
	private static final long serialVersionUID = 1;
	
	public SongFrame(SongListModel model) {
		setLayout(new GridLayout(1, 2));
		JList<Song> list = new JList<Song>(model);
		list.setPreferredSize(new Dimension(200, 200));
		list.setSelectedIndex(0);
		JScrollPane scroll = new JScrollPane(list);
		add(scroll);
		add(new JPanel());
		pack();
		setLocationRelativeTo(null);
	}

}
