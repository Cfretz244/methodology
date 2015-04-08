package cs213.photoAlbum.guiview;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cs213.photoAlbum.control.Control;
import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Drawable;
import cs213.photoAlbum.model.Photo;

public class AlbumView extends JFrame implements ActionListener {
	
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
		}
		
		public void paintComponent(Graphics g) {
			g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		}
		
		public void setImage(Drawable data) {
			try {
				image = ImageIO.read(new File(data.getPath()));
			} catch (Exception e) {
				image = defaultImage;
			}
			repaint();
		}
		
		public void setImage(String path) {
			try {
				image = ImageIO.read(new File(path));
			} catch (Exception e) {
				image = defaultImage;
			}
			repaint();
		}

	}
	
	private static final long serialVersionUID = 1;
	private static final int NORMAL = 0, EDIT = 1, ADD_TAG = 2, DELETE_TAG = 3, MOVE = 4;
	private WindowAdapter windowHandler;
	private ComponentAdapter resizeHandler;
	private Control control;
	private Album current;
	private PhotoButton selected;
	private static final String defaultPhoto = "assets/404.png";
	private int currentPage;
	
	/*----- Unconditionally Visible Components -----*/
	private JTextField searchBar;
	private JComboBox<String> searchType;
	private PhotoPanel photoPanel;
	private JPanel operationPanel, tagPanel, movePanel;
	private PhotoDisplay photoDisplay;
	private JButton prev, next, search, addPhoto, deletePhoto, move, recaption, addTag, deleteTag;
	
	/*----- Components Visible When in Normal State -----*/
	private InfoPanel infoPanel;

	/*----- Components Visible When Adding a Photo -----*/
	private AddPanel addPanel;
	
	/*----- Components Visible When Adding or Deleting a Tag -----*/
	private JButton modifyTag, cancelTag; /* Buttons Visible when Adding or Deleting a Tag */
	private JTextField tagType, tagValue;
	
	/*----- Components Visible When Moving a Photo -----*/
	private JButton movePhoto, cancelMove;
	private JComboBox<String> moveTo;
	
	public AlbumView(Album current, Control control) {
		this.current = current;
		this.control = control;
		instantiate();
		bind();
		layoutViews();
		
		windowHandler = new WindowAdapter() {

			public void windowClosing(WindowEvent event) {
				// Decide what to do here.
			}

		};
		resizeHandler = new ComponentAdapter() {

			public void componentResized(ComponentEvent event) {
				photoPanel.resized(event.getComponent().getSize());
			}

		};
		
		photoDisplay.setImage(selected.getDrawable());
		transitionToState(NORMAL);
		pack();
		setVisible(true);
		addWindowListener(windowHandler);
		addComponentListener(resizeHandler);
	}
	
	private void transitionToState(int state) {
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
		if (state == NORMAL) {
			infoPanel.setPhoto((Photo) selected.getDrawable());
			add(infoPanel, gbc);
		} else if (state == ADD_TAG) {
			addPanel.setPhoto((Photo) selected.getDrawable());
			photoDisplay.setImage(defaultPhoto);
			add(addPanel, gbc);
		}
		repaint();
		revalidate();
	}
	
	private void addPhoto(String path) {
		System.out.println(path);
	}
	
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source instanceof PhotoButton) {
			PhotoButton button = (PhotoButton) source;
			selected.select();
			selected = button;
			selected.select();
			photoDisplay.setImage(selected.getDrawable());
			transitionToState(NORMAL);
		} else if (source instanceof JButton) {
			JButton button = (JButton) source;
			if (button == addPhoto) {
				transitionToState(ADD_TAG);
			} else if (button == deletePhoto) {
				
			} else if (button == move) {
				
			} else if (button == recaption) {
				
			} else if (button == addTag) {
				
			} else if (button == deleteTag) {
				
			} else if (button == next) {
				
			} else if (button == prev) {
				
			}
		}
	}
	
	private void instantiate() {
		setLayout(new GridBagLayout());

		searchBar = new JTextField(4);
		searchType = new JComboBox<String>(new String[] {"Date Range", "Tag"});
		photoPanel = new PhotoPanel(this, () -> Arrays.copyOfRange(current.getPhotos(), currentPage * 9, (currentPage + 1) * 9));
		selected = photoPanel.getButton(0);
		selected.select();
		operationPanel = new JPanel(new GridLayout(1, 6));
		infoPanel = new InfoPanel();
		addPanel = new AddPanel(fileName -> addPhoto(fileName), fileName -> photoDisplay.setImage(fileName), dummy -> transitionToState(NORMAL));
		tagPanel = new JPanel(new GridBagLayout());
		movePanel = new JPanel(new GridBagLayout());
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
		
		modifyTag = new JButton();
		cancelTag = new JButton("Cancel");
		tagType = new JTextField();
		tagValue = new JTextField();
		
		movePhoto = new JButton("Move");
		cancelMove = new JButton("Cancel");
		moveTo = new JComboBox<String>();
	}
	
	private void bind() {
		search.addActionListener(this);
		prev.addActionListener(this);
		next.addActionListener(this);
		addPhoto.addActionListener(this);
		deletePhoto.addActionListener(this);
		move.addActionListener(this);
		recaption.addActionListener(this);
		addTag.addActionListener(this);
		deleteTag.addActionListener(this);
		modifyTag.addActionListener(this);
		cancelTag.addActionListener(this);
		movePhoto.addActionListener(this);
		cancelMove.addActionListener(this);
	}
	
	private void layoutViews() {
		// Search Bar Constraints.
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		add(searchBar, gbc);
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		add(searchType, gbc);
		gbc.gridx = 3;
		add(search, gbc);
		
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
		gbc.weightx = 0.6;
		gbc.weighty = 0.6;
		gbc.fill = GridBagConstraints.BOTH;
		add(photoDisplay, gbc);
		
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
