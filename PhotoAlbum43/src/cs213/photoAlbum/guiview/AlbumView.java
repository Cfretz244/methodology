package cs213.photoAlbum.guiview;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.FocusManager;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cs213.photoAlbum.control.Control;
import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Drawable;
import cs213.photoAlbum.model.Photo;

/**
 * Class handles displaying a specific album.
 * @author cfretz
 */
public class AlbumView extends JFrame implements ActionListener {

	/**
	 * Class is responsible for drawing the currently selected photo.
	 * @author cfretz
	 */
	private class PhotoDisplay extends JPanel {

		private BufferedImage image, defaultImage;
		private static final long serialVersionUID = 1;
		private static final String defaultPath = "assets/404.png";

		public PhotoDisplay() {
			try {
				defaultImage = ImageIO.read(new File(defaultPath));
			} catch (IOException e) {
				image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
				Graphics2D g = image.createGraphics();
				g.drawString("Photo Not Found", getWidth(), getHeight());
				g.dispose();
			}
			setPreferredSize(new Dimension(500, 500));
		}

		public void paintComponent(Graphics g) {
			g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		}

		/**
		 * Method updates the preferred and minimum sizes when the parent view changes.
		 * @param size The new size.
		 */
		public void resized(Dimension size) {
			double legSize = size.getHeight() > size.getWidth() ? size.getWidth() : size.getHeight();
			legSize *= 0.9;
			double minimum = legSize * 0.8;
			setPreferredSize(new Dimension((int) legSize, (int) legSize));
			setMinimumSize(new Dimension((int) minimum, (int) minimum));
		}

		/**
		 * Method reads in the selected image as a BufferedImage.
		 * @param path The path of the image to be loaded.
		 */
		private void readImage(String path) {
			try {
				image = ImageIO.read(new File(path));
			} catch (Exception e) {
				image = defaultImage;
			}
			repaint();
		}

		/**
		 * Method sets the current image.
		 * @param data The image.
		 */
		public void setImage(Drawable data) {
			readImage(data != null ? data.getPath() : defaultPath);
		}

		public void setImage(String path) {
			readImage(path);
		}

	}

	/**
	 * Method handles drawing a textfield with placeholder text. Don't know why this isn't a
	 * default swing feature.
	 * @author cfretz
	 *
	 */
	protected static class PlaceHolderField extends JTextField {

		private static final long serialVersionUID = 1;
		private String placeholder;
		
		public PlaceHolderField(String placeholder) {
			this.placeholder = placeholder;
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (getText().isEmpty() && FocusManager.getCurrentKeyboardFocusManager().getFocusOwner() != this) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setBackground(Color.LIGHT_GRAY);
				g2.setFont(getFont());
				g2.drawString(placeholder, 10, 20);
				g2.dispose();
			}
		}

	}

	/**
	 * Enum represents the current state of the albumview.
	 * @author cfretz
	 */
	protected enum State {
		NORMAL,
		ADD,
		EDIT,
		ADD_TAG,
		DELETE_TAG,
		MOVE
	}

	private static final long serialVersionUID = 1;
	private Control control;
	private Album current;
	private Photo[] matches;
	private PhotoButton selected;
	private static final String defaultPhoto = "assets/404.png";
	private int currentPage;

	/*----- Unconditionally Visible Components -----*/
	private PlaceHolderField searchBar;
	private JComboBox<String> searchType;
	private PhotoPanel photoPanel;
	private JPanel operationPanel, photoHolder;
	private PhotoDisplay photoDisplay;
	private JButton prev, next, search, addPhoto, deletePhoto, move, recaption, addTag, deleteTag;

	/*----- Conditionally Visible Panels -----*/
	private InfoPanel infoPanel;
	private AddPanel addPanel;
	private TagPanel tagPanel;
	private MovePanel movePanel;

	/*----- Save Search Results Panel -----*/
	private JPanel savePanel;
	private JButton saveButton;
	private PlaceHolderField saveName;

	public AlbumView(Album current, Control control, Consumer<?> updateParent) {
		this.current = current;
		this.control = control;
		instantiate();
		bind();
		layoutViews();

		WindowAdapter windowHandler = new WindowAdapter() {

			public void windowClosing(WindowEvent event) {
				updateParent.accept(null);
			}

		};
		ComponentAdapter resizeHandler = new ComponentAdapter() {

			public void componentResized(ComponentEvent event) {
				photoDisplay.resized(event.getComponent().getSize());
			}

		};

		photoDisplay.setImage(selected.getDrawable());
		transitionToState(State.NORMAL);
		setVisible(true);
		addWindowListener(windowHandler);
		photoHolder.addComponentListener(resizeHandler);
	}

	/**
	 * Handles all events.
	 */
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source instanceof PhotoButton) {
			PhotoButton button = (PhotoButton) source;
			selected.select();
			selected = button;
			selected.select();
			photoDisplay.setImage(selected.getDrawable());
			transitionToState(State.NORMAL);
		} else if (source instanceof JButton) {
			JButton button = (JButton) source;
			if (button == addPhoto) {
				transitionToState(State.ADD);
			} else if (button == deletePhoto) {
				control.removePhotoFromAlbum(current.getName(), ((Photo) selected.getDrawable()).getName());
				photoPanel.updatePhotos();
				photoDisplay.setImage((Photo) selected.getDrawable());
			} else if (button == move) {
				transitionToState(State.MOVE);
			} else if (button == recaption) {
				transitionToState(State.EDIT);
			} else if (button == addTag) {
				transitionToState(State.ADD_TAG);
			} else if (button == deleteTag) {
				transitionToState(State.DELETE_TAG);
			} else if (button == next) {
				Photo[] photos = current.getPhotos();
				if (photos.length >= 9 * (currentPage + 1)) {
					currentPage++;
					selected.select();
					selected = photoPanel.getButton(0);
					selected.select();
					photoPanel.updatePhotos();
					transitionToState(State.NORMAL);
				}
			} else if (button == prev) {
				if (currentPage > 0) {
					currentPage--;
					selected.select();
					selected = photoPanel.getButton(0);
					selected.select();
					photoPanel.updatePhotos();
					transitionToState(State.NORMAL);
				}
			} else if (button == search) {
				String search = searchBar.getText().trim();
				String type = searchType.getItemAt(searchType.getSelectedIndex());
				if (!search.equals("")) {
					if (type.equals("Date Range")) {
						searchDates(search);
					} else {
						searchTags(search);
					}
				} else {
					matches = null;
					savePanel.setVisible(false);
				}
				photoPanel.updatePhotos();
			} else if (button == saveButton) {
				if (saveName.getText() != "") {
					String name = saveName.getText();
					saveName.setText("");
					control.addAlbum(name);
					for (Photo photo : matches) control.addPhotoToAlbum(name, photo.getName(), photo.getCaption());
				}
			}
		}
	}

	/**
	 * Method handles all nastiness necessary to switch from one state to another.
	 * @param state The state to switch to.
	 */
	private void transitionToState(State state) {
		remove(infoPanel);
		remove(addPanel);
		remove(tagPanel);
		remove(movePanel);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 4;
		gbc.gridy = 4;
		gbc.gridheight = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.6;
		gbc.weighty = 0.1;
		if (state == State.NORMAL) {
			addPhoto.setEnabled(true);
			deletePhoto.setEnabled(true);
			recaption.setEnabled(true);
			move.setEnabled(true);
			addTag.setEnabled(true);
			deleteTag.setEnabled(true);
			next.setEnabled(true);
			prev.setEnabled(true);
			infoPanel.setPhoto((Photo) selected.getDrawable());
			photoPanel.enable();
			add(infoPanel, gbc);
			photoDisplay.setImage((Photo) selected.getDrawable());
		} else {
			addPhoto.setEnabled(false);
			deletePhoto.setEnabled(false);
			recaption.setEnabled(false);
			move.setEnabled(false);
			addTag.setEnabled(false);
			deleteTag.setEnabled(false);
			next.setEnabled(false);
			prev.setEnabled(false);
			if (state == State.ADD) {
				addPanel.setPhoto(null);
				photoDisplay.setImage(defaultPhoto);
				photoPanel.disable();
				add(addPanel, gbc);
			} else if (state == State.MOVE) {
				movePanel.update();
				photoPanel.disable();
				add(movePanel, gbc);
			} else if (state == State.EDIT) {
				addPanel.setPhoto((Photo) selected.getDrawable());
				photoDisplay.setImage(selected.getDrawable());
				photoPanel.disable();
				add(addPanel, gbc);
			} else if (state == State.ADD_TAG || state == State.DELETE_TAG) {
				tagPanel.setPhoto((Photo) selected.getDrawable(), state);
				photoPanel.disable();
				add(tagPanel, gbc);
			}
		}
		repaint();
		revalidate();
	}

	/**
	 * Method adds a new photo using the control object.
	 * @param properties Information on the new photo.
	 */
	private void addPhoto(String[] properties) {
		if (properties[0] != null) control.addPhotoToAlbum(current.getName(), properties[0], properties[1]);
		else control.changeCaptionForPhoto(((Photo) selected.getDrawable()).getName(), properties[1]);

		photoPanel.updatePhotos();
		transitionToState(State.NORMAL);
	}

	/**
	 * Method moves a photo using the control object.
	 * @param toAlbum Album to mvoe current photo to.
	 */
	private void movePhoto(String toAlbum) {
		control.movePhoto(current.getName(), toAlbum, ((Photo) selected.getDrawable()).getName());
		photoPanel.updatePhotos();
		transitionToState(State.NORMAL);
	}

	/**
	 * Method modifies tags using the control object.
	 * @param tagInfo The tag type and value
	 */
	private void modifyTags(String[] tagInfo) {
		if (tagInfo[2].equals("add")) {
			control.addTagToPhoto(((Photo) selected.getDrawable()).getName(), tagInfo[0], tagInfo[1]);
		} else {
			control.removeTagFromPhoto(((Photo) selected.getDrawable()).getName(), tagInfo[0], tagInfo[1]);
		}
		transitionToState(State.NORMAL);
	}

	/**
	 * Method handles everything related to searching for a specific date range.
	 * @param search The string to be searched for.
	 */
	private void searchDates(String search) {
		SimpleDateFormat parser = new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss");
		String[] dates = search.split(" ");
		if (dates.length == 2) {
			try {
				Date start = parser.parse(dates[0]), end = parser.parse(dates[1]);
				matches = current.getPhotos(start.getTime(), end.getTime());
				currentPage = 0;
				savePanel.setVisible(true);
			} catch (ParseException e) {
				savePanel.setVisible(false);
			}
		} else {
			savePanel.setVisible(false);
		}
	}

	/**
	 * Method handles everything related to searching for a specific tag set.
	 * @param search The search string.
	 */
	private void searchTags(String search) {
		String[] tags = search.split(",");
		for (int i = 0; i < tags.length; i++) tags[i] = tags[i].trim();
		Set<Photo> matchSet = new HashSet<Photo>();
		boolean first = true, successful = true;
		for (String tag : tags) {
			int colonIndex = tag.indexOf(":"), quoteIndex = tag.indexOf("\"");
			List<String> matches = new ArrayList<String>();
			if (quoteIndex >= 0) {
				Pattern regex = Pattern.compile("[\"]([^\"]*)[\"]");
				Matcher quotes = regex.matcher(tag);
				while (quotes.find()) matches.add(quotes.group(1));
			}

			try {
				String tagType = null, tagValue = null;
				if (colonIndex >= 0 && quoteIndex >= 0) {
					if (matches.size() == 2) {
						tagType = matches.get(0);
						tagValue = matches.get(1);
					} else {
						tagType = quoteIndex > colonIndex ? tag.substring(0, colonIndex) : matches.get(0);
						tagValue = quoteIndex > colonIndex ? matches.get(0) : tag.substring(colonIndex + 1, tag.length());
					}
				} else if (quoteIndex >= 0) {
					tagValue = matches.get(0);
				} else if (colonIndex >= 0) {
					tagType = tag.substring(0, colonIndex);
					tagValue = tag.substring(colonIndex + 1, tag.length());
				} else {
					tagValue = tag;
				}

				if (first) {
					if (tagType != null) {
						Photo[] results = current.getPhotos(tagType, tagValue);
						for (Photo result : results) matchSet.add(result);
					} else {
						Photo[] results = current.getPhotos();
						for (Photo result : results) if (result.hasTag(tagValue, false)) matchSet.add(result);
					}
				} else {
					Set<Photo> intersection = new HashSet<Photo>();
					Iterator<Photo> iterate = matchSet.iterator();
					while (iterate.hasNext()) {
						Photo current = iterate.next();
						if (tagType != null && current.hasTag(tagType, tagValue)) {
							intersection.add(current);
						} else if (tagType == null && current.hasTag(tagValue, false)) {
							intersection.add(current);
						}
					}
					matchSet = intersection;
				}
			} catch (Exception e) {
				successful = false;
				break;
			}
			first = false;
		}

		if (successful) savePanel.setVisible(true);
		else savePanel.setVisible(false);

		matches = new Photo[matchSet.size()];
		matchSet.toArray(matches);
	}


	/**
	 * Method makes all instantiations necessary to run the class.
	 */
	private void instantiate() {
		setLayout(new GridBagLayout());
		searchBar = new PlaceHolderField("Search Query");
		searchType = new JComboBox<String>(new String[] {"Date Range", "Tag"});
		photoPanel = new PhotoPanel(this, () -> Arrays.copyOfRange(matches == null ? current.getPhotos() : matches, currentPage * 9, (currentPage + 1) * 9));
		selected = photoPanel.getButton(0);
		selected.select();
		operationPanel = new JPanel(new GridLayout(1, 6));
		infoPanel = new InfoPanel();
		addPanel = new AddPanel(fileName -> addPhoto(fileName), fileName -> photoDisplay.setImage(fileName), dummy -> transitionToState(State.NORMAL));
		tagPanel = new TagPanel(tagInfo -> modifyTags(tagInfo), dummy -> transitionToState(State.NORMAL));
		movePanel = new MovePanel(albumName -> movePhoto(albumName), () -> control.getAlbums(), dummy -> transitionToState(State.NORMAL));
		savePanel = new JPanel(new GridBagLayout());
		saveButton = new JButton("Save Search");
		saveName = new PlaceHolderField("Album Name");
		photoHolder = new JPanel(new GridBagLayout());
		photoDisplay = new PhotoDisplay();
		prev = new JButton("Previous");
		next = new JButton("Next");
		search = new JButton("Search");
		addPhoto = new JButton("Add Photo");
		deletePhoto = new JButton("Remove Photo");
		move = new JButton("Move Photo");
		recaption = new JButton("Recaption Photo");
		addTag = new JButton("Add Tag");
		deleteTag = new JButton("Remove Tag");
	}

	/**
	 * Method adds all necessary event listeners.
	 */
	private void bind() {
		search.addActionListener(this);
		saveButton.addActionListener(this);
		prev.addActionListener(this);
		next.addActionListener(this);
		addPhoto.addActionListener(this);
		deletePhoto.addActionListener(this);
		move.addActionListener(this);
		recaption.addActionListener(this);
		addTag.addActionListener(this);
		deleteTag.addActionListener(this);
	}

	/**
	 * Method handles all of the nastiness related to setting constraints on subviews.
	 */
	private void layoutViews() {
		// Set size of frame.
		Toolkit tk = Toolkit.getDefaultToolkit();
		int xSize = ((int) tk.getScreenSize().getWidth());
		int ySize = ((int) tk.getScreenSize().getHeight());
		setSize(xSize,ySize);
		
		// Search Bar Constraints.
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		gbc.insets = new Insets(0, 10, 0, 10);
		add(searchBar, gbc);
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		add(searchType, gbc);
		gbc.gridx = 3;
		add(search, gbc);

		// Save Search Constraints
		gbc = new GridBagConstraints();
		gbc.gridx = 4;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(savePanel, gbc);
		gbc = new GridBagConstraints();
		gbc.gridwidth = 2;
		gbc.weightx = 0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LINE_START;
		savePanel.add(saveName, gbc);
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		savePanel.add(saveButton, gbc);
		savePanel.setVisible(false);

		// PhotoPanel Constraints.
		gbc = new GridBagConstraints();
		gbc.gridwidth = 4;
		gbc.gridheight = 5;
		gbc.gridy = 1;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(10, 10, 10, 10);
		add(photoPanel, gbc);

		// Next and Previous Button Constraints.
		gbc = new GridBagConstraints();
		gbc.gridy = 6;
		add(prev, gbc);
		gbc.gridx = 3;
		add(next, gbc);

		// PhotoDisplay Constraints.
		gbc = new GridBagConstraints();
		gbc.gridx = 4;
		gbc.gridy = 1;
		gbc.gridwidth = 3;
		gbc.gridheight = 3;
		gbc.weightx = 0.9;
		gbc.weighty = 0.9;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(10, 0, 0, 0);
		add(photoHolder, gbc);
		photoHolder.add(photoDisplay, new GridBagConstraints());

		// InfoPanel Constraints.
		gbc = new GridBagConstraints();
		gbc.gridx = 4;
		gbc.gridy = 4;
		gbc.gridheight = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.6;
		gbc.weighty = 0.1;
		gbc.insets = new Insets(10, 0, 10, 0);
		add(infoPanel, gbc);

		// OperationalPanel Constraints.
		gbc = new GridBagConstraints();
		gbc.gridy = 7;
		gbc.gridwidth = 7;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		add(operationPanel, gbc);
		operationPanel.add(addPhoto);
		operationPanel.add(deletePhoto);
		operationPanel.add(move);
		operationPanel.add(recaption);
		operationPanel.add(addTag);
		operationPanel.add(deleteTag);
	}

}
