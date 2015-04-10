package cs213.photoAlbum.guiview;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cs213.photoAlbum.control.Control;
import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Photo;


public class AccountPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1;
	private Control control;
	private JPanel optionPanel, infoPanel;
	private PhotoPanel albumPanel;
	private JButton create, delete, rename, open, prev, next, submit, cancel;
	private JTextField albumName;
	private JLabel name, number, startDate, endDate, infoName, errorLabel;
	private PhotoButton selected;
	private Album beingModified;
	private List<AlbumView> windows;
	private int currentPage;

	public AccountPanel(Consumer<Integer> leave, Control control) {
		this.control = control;
		currentPage = 0;
		instantiate();
		bind();
		layoutViews();
		updateLabels();
	}

	private void updateLabels() {
		Album current = (Album) selected.getDrawable();
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
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 4;
		gbc.fill = GridBagConstraints.BOTH;
		if (name == null) {
			remove(infoPanel);
			gbc.gridheight = 4;
			add(optionPanel, gbc);
		} else {
			remove(optionPanel);
			albumName.setText(name);
			gbc.gridheight = 2;
			gbc.gridy = 2;
			add(infoPanel, gbc);
		}
		repaint();
		revalidate();
	}

	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source instanceof PhotoButton) {
			PhotoButton button = (PhotoButton) source;
			selected.select();
			selected = button;
			selected.select();
			updateLabels();
		} else if (source == create) {
			swapPanels("");
		} else if (source == delete) {
			Album current = (Album) selected.getDrawable();
			control.removeAlbum(current.getName());
			selected.setDrawable(null);
			albumPanel.updatePhotos();
			updateLabels();
		} else if (source == rename) {
			Album album = (Album) selected.getDrawable();
			beingModified = album;
			swapPanels(album.getName());
		} else if (source == open) {
			Album album = (Album) selected.getDrawable();
			if (album != null) windows.add(new AlbumView(album, control, dummy -> albumPanel.updatePhotos()));
		} else if (source == cancel) {
			beingModified = null;
			errorLabel.setVisible(false);
			swapPanels(null);
		} else if (source == submit) {
			String name = albumName.getText();
			if (!name.equals("")) {
				if (beingModified != null) {
					control.changeAlbumName(beingModified.getName(), name);
					beingModified = null;
				} else {
					control.addAlbum(name);
				}
				albumPanel.updatePhotos();
				errorLabel.setVisible(false);
				updateLabels();
				swapPanels(null);
			} else {
				errorLabel.setVisible(true);
			}
		} else if (source == next) {
			Album[] albums = control.getAlbums();
			if (albums.length >= 9 * (currentPage + 1)) {
				currentPage++;
				selected.select();
				selected = albumPanel.getButton(0);
				selected.select();
				albumPanel.updatePhotos();
				updateLabels();
			}
		} else if (source == prev) {
			if (currentPage > 0) {
				currentPage--;
				selected.select();
				selected = albumPanel.getButton(0);
				selected.select();
				albumPanel.updatePhotos();
				updateLabels();
			}
		}
	}

	private void instantiate() {
		setLayout(new GridBagLayout());
		albumPanel = new PhotoPanel(this, () -> Arrays.copyOfRange(control.getAlbums(), currentPage * 9, (currentPage + 1) * 9));
		selected = albumPanel.getButton(0);
		selected.select();
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
		infoName = new JLabel("Album Name");
		errorLabel = new JLabel("Enter a valid album name");
		errorLabel.setForeground(Color.RED);
		errorLabel.setVisible(false);
		windows = new ArrayList<AlbumView>();
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
	}

	private void layoutViews() {
		/* Album Panel Constraints */
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 0.6;
		gbc.weighty = 0.9;
		gbc.gridwidth = 4;
		gbc.gridheight = 4;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(10, 10, 10, 10);
		add(albumPanel, gbc);

		/* Optional Panel Constraints */
		gbc = new GridBagConstraints();
		gbc.gridx = 4;
		gbc.gridheight = 4;
		gbc.fill = GridBagConstraints.BOTH;
		add(optionPanel, gbc);

		gbc = new GridBagConstraints();
		optionPanel.add(create, gbc);

		gbc.gridy = 1;
		optionPanel.add(delete, gbc);

		gbc.gridy = 2;
		optionPanel.add(rename, gbc);

		gbc.gridy = 3;
		optionPanel.add(open, gbc);

		/* Info Panel Constraints */
		gbc = new GridBagConstraints();
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		infoPanel.add(infoName, gbc);
		gbc.gridy = 1;
		infoPanel.add(albumName, gbc);
		gbc = new GridBagConstraints();
		gbc.gridy = 2;
		infoPanel.add(cancel, gbc);
		gbc.gridx = 1;
		infoPanel.add(submit, gbc);
		gbc = new GridBagConstraints();
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		infoPanel.add(errorLabel, gbc);


		/* Previous and Next Button Constraints */
		gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 10, 0);
		gbc.gridy = 4;
		add(prev, gbc);

		gbc.gridx = 3;
		add(next, gbc);

		/* Label Constraints */
		gbc = new GridBagConstraints();
		gbc.gridy = 5;
		gbc.weightx = 0.25;
		gbc.ipadx = 10;
		add(name, gbc);

		gbc.gridx = 1;
		add(number, gbc);

		gbc.gridx = 2;
		add(startDate, gbc);

		gbc.gridx = 3;
		add(endDate, gbc);
	}

}
