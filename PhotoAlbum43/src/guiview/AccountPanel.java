package guiview;

import java.awt.Color;
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
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import cs213.photoAlbum.control.Control;
import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Photo;

public class AccountPanel extends JPanel implements ActionListener, Resizable {
	
	private static final long serialVersionUID = 1;
	private Control control;
	private JPanel albumPanel, optionPanel, infoPanel;
	private JButton create, delete, rename, open, prev, next, submit, cancel;
	private JTextField albumName;
	private JLabel name, number, startDate, endDate;
	private ArrayList<AlbumButton> albumBtns;
	private AlbumButton selected;
	private Album beingModified;
	private int currentPage;
	
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
		constraints.insets = new Insets(10, 10, 10, 10);
		add(albumPanel, constraints);
		populateAlbumPanel();
		
		/* Optional Panel Constraints */
		constraints = new GridBagConstraints();
		constraints.gridx = 4;
		constraints.gridheight = 4;
		constraints.fill = GridBagConstraints.BOTH;
		add(optionPanel, constraints);
		
		constraints = new GridBagConstraints();
		optionPanel.add(create, constraints);

		constraints.gridy = 1;
		optionPanel.add(delete, constraints);
		
		constraints.gridy = 2;
		optionPanel.add(rename, constraints);
		
		constraints.gridy = 3;
		optionPanel.add(open, constraints);
		
		/* Info Panel Constraints */
		constraints = new GridBagConstraints();
		constraints.gridwidth = 2;
		infoPanel.add(albumName, constraints);
		
		constraints.gridwidth = 1;
		constraints.gridy = 1;
		infoPanel.add(cancel, constraints);
		
		constraints.gridx = 1;
		infoPanel.add(submit, constraints);
		
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
		int index = 0;
		for (int otherIndex = 9 * currentPage; index < 9 && otherIndex < albums.length; index++, otherIndex++) {
			albumBtns.get(index).setAlbum(albums[otherIndex]);
		}
		for (; index < 9; index++) albumBtns.get(index).setAlbum(null);

		if (selected == null) {
			selected = albumBtns.get(0);
			selected.select();
		}
		repaint();
		revalidate();
	}
	
	private void updateLabels() {
		Album current = selected.getAlbum();
		if (current != null) {
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			name.setText("Name: " + current.getName());
			Photo[] photos = control.getPhotosFromAlbum(current.getName());
			if (photos != null) {
				number.setText("# Photos: " + control.getPhotosFromAlbum(current.getName()).length);
			} else {
				number.setText("# Photos: 0");
			}

			long[] dates = current.getDateRange();
			if (dates != null) {
				startDate.setText("Start Date: " + format.format(new Date(dates[0])));
				endDate.setText("End Date: " + format.format(new Date(dates[1])));
			} else {
				startDate.setText("(N/A)");
				endDate.setText("(N/A)");
			}
		} else {
			name.setText("Name: (N/A)");
			number.setText("# Photos: 0");
			startDate.setText("(N/A)");
			endDate.setText("(N/A)");
		}
	}
	
	private void swapPanels(String name) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 4;
		constraints.fill = GridBagConstraints.BOTH;
		if (name == null) {
			remove(infoPanel);
			constraints.gridheight = 4;
			add(optionPanel, constraints);
		} else {
			remove(optionPanel);
			albumName.setText(name);
			constraints.gridheight = 2;
			constraints.gridy = 2;
			add(infoPanel, constraints);
		}
		repaint();
		revalidate();
	}
	
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source instanceof AlbumButton) {
			AlbumButton button = (AlbumButton) source;
			selected.select();
			selected = button;
			selected.select();
			updateLabels();
		} else if (source == create) {
			swapPanels("");
		} else if (source == delete) {
			control.removeAlbum(selected.getAlbum().getName());
			selected.setAlbum(null);
			populateAlbumPanel();
			updateLabels();
		} else if (source == rename) {
			Album album = selected.getAlbum();
			beingModified = album;
			swapPanels(album.getName());
		} else if (source == open) {
			
		} else if (source == cancel) {
			beingModified = null;
			swapPanels(null);
		} else if (source == submit) {
			String name = albumName.getText();
			if (beingModified != null) {
				control.changeAlbumName(beingModified.getName(), name);
				beingModified = null;
			} else {
				control.addAlbum(name);
			}
			populateAlbumPanel();
			updateLabels();
			swapPanels(null);
		} else if (source == next) {
			Album[] albums = control.getAlbums();
			if (albums.length >= 9 * (currentPage + 1)) {
				currentPage++;
				selected.select();
				selected = null;
				populateAlbumPanel();
				updateLabels();
			}
		} else if (source == prev) {
			if (currentPage > 0) {
				currentPage--;
				selected.select();
				selected = null;
				populateAlbumPanel();
				updateLabels();
			}
		}
	}
	
	public void resized(Dimension size) {
		for (AlbumButton button : albumBtns) button.resized(albumPanel.getSize());
	}

	private void instantiate() {
		albumPanel = new JPanel(new GridBagLayout());
		albumPanel.setBorder(new LineBorder(Color.black, 2, true));
		optionPanel = new JPanel(new GridBagLayout());
		infoPanel = new JPanel(new GridBagLayout());
		create = new JButton(" Create Album ");
		delete = new JButton(" Delete Album ");
		rename = new JButton("Rename Album");
		open = new JButton("  Open Album  ");
		prev = new JButton("Previous");
		next = new JButton("Next");
		submit = new JButton("Submit");
		cancel = new JButton("Cancel");
		albumName = new JTextField(8);
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
		submit.addActionListener(this);
		cancel.addActionListener(this);
		for (AlbumButton button : albumBtns) button.addActionListener(this);
	}
	
}
