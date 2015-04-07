package guiview;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cs213.photoAlbum.control.Control;
import cs213.photoAlbum.model.Album;

public class AccountPanel extends JPanel implements ActionListener, Resizable {
	
	private static final long serialVersionUID = 1;
	private Control control;
	private JPanel albumPanel;
	private JButton create, delete, rename, open, prev, next;
	private JLabel name, number, startDate, endDate;
	private ArrayList<AlbumButton> albumBtns;
	private int currentPage, currentAlbum;
	
	public AccountPanel(Consumer<Integer> leave, Control control) {
		this.control = control;
		currentPage = 0;
		instantiate();
		bind();
		
		/* Album Panel Constraints */
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.weightx = 0.6;
		constraints.weighty = 0.9;
		constraints.gridwidth = 4;
		constraints.gridheight = 4;
		constraints.fill = GridBagConstraints.BOTH;
		add(albumPanel, constraints);
		populateAlbumPanel();
		
		/* Side Buttons Constraints */
		constraints = new GridBagConstraints();
		constraints.gridx = 4;
		add(create, constraints);
		
		constraints.gridy = 1;
		add(delete, constraints);
		
		constraints.gridy = 2;
		add(rename, constraints);
		
		constraints.gridy = 3;
		add(open, constraints);
		
		/* Previous and Next Button Constraints */
		constraints = new GridBagConstraints();
		constraints.insets = new Insets(0, 0, 10, 0);
		constraints.gridy = 4;
		add(prev, constraints);
		
		constraints.gridx = 3;
		add(next, constraints);
		
		/* Label Constraints */
		constraints = new GridBagConstraints();
		constraints.gridy = 5;
		constraints.weightx = 0.25;
		constraints.ipadx = 10;
		add(name, constraints);
		
		constraints.gridx = 1;
		add(number, constraints);
		
		constraints.gridx = 2;
		add(startDate, constraints);
		
		constraints.gridx = 3;
		add(endDate, constraints);
		
		updateLabels();
	}
	
	private void populateAlbumPanel() {
		Album[] albums = control.getAlbums();
		for (int index = 0, otherIndex = 9 * currentPage; index < 9 && otherIndex < albums.length; index++, otherIndex++) {
			albumBtns.get(index).setAlbum(albums[otherIndex]);
		}
		repaint();
		revalidate();
	}
	
	private void updateLabels() {
		Album current = albumBtns.get(currentAlbum).getAlbum();
		if (current != null) {
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			name.setText("Name: " + current.getName());
			number.setText("# Photos: " + control.getPhotosFromAlbum(current.getName()).length);

			long[] dates = current.getDateRange();
			startDate.setText("Start Date: " + format.format(new Date(dates[0])));
			endDate.setText("End Date: " + format.format(new Date(dates[1])));
		}
	}
	
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source instanceof AlbumButton) {
			currentAlbum = ((AlbumButton) source).getIndex();
			updateLabels();
		}
	}
	
	public void resized(Dimension size) {
		for (AlbumButton button : albumBtns) button.resized(albumPanel.getSize());
	}

	private void instantiate() {
		albumPanel = new JPanel(new GridBagLayout());
		create = new JButton(" Create Album ");
		delete = new JButton(" Delete Album ");
		rename = new JButton("Rename Album");
		open = new JButton("  Open Album  ");
		prev = new JButton("Previous");
		next = new JButton("Next");
		name = new JLabel(" ");
		number = new JLabel(" ");
		startDate = new JLabel(" ");
		endDate = new JLabel(" ");
		
		albumBtns = new ArrayList<AlbumButton>();
		for (int i = 0; i < 9; i++) albumBtns.add(new AlbumButton(i));
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.weightx = 0.3;
		constraints.weighty = 0.3;
		constraints.insets = new Insets(10, 10, 10, 10);
		for (int i = 0; i < 3; i++) {
			constraints.gridy = i;
			for (int j = 0; j < 3; j++) {
				constraints.gridx = j;
				albumPanel.add(albumBtns.get((3 * i) + j), constraints);
			}
		}
		
		setLayout(new GridBagLayout());
	}
	
	private void bind() {
		create.addActionListener(this);
		delete.addActionListener(this);
		rename.addActionListener(this);
		open.addActionListener(this);
		prev.addActionListener(this);
		next.addActionListener(this);
		for (AlbumButton button : albumBtns) button.addActionListener(this);
	}
	
}
