package cs213.photoAlbum.guiview;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import cs213.photoAlbum.control.Control;
import cs213.photoAlbum.model.Album;

public class AlbumView extends JFrame implements ActionListener {
	
	private class PhotoDisplay extends JPanel {
		
		private BufferedImage image;
		private static final long serialVersionUID = 1;
		
		public void paintComponent(Graphics g) {
			g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		}
		
		public void setImage(String path) {
			try {
				image = ImageIO.read(new File(path));
			} catch (IOException e) {
				image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
				Graphics2D g = image.createGraphics();
				g.drawString("Photo Not Found", getWidth(), getHeight());
				g.dispose();
			}
			repaint();
		}

	}
	
	private static final long serialVersionUID = 1;
	private WindowAdapter windowHandler;
	private ComponentAdapter resizeHandler;
	private Control control;
	private Album current;
	private int currentPage;
	
	/*----- Unconditionally Visible Components -----*/
	private JTextField searchBar;
	private JComboBox<String> searchType;
	private PhotoPanel photoPanel;
	private JPanel operationPanel, infoPanel, addPanel, tagPanel, movePanel;
	private PhotoDisplay photoDisplay;
	private JButton prev, next, search, addPhoto, deletePhoto, move, recaption, addTag, deleteTag;
	
	/*----- Components Visible When in Normal State -----*/
	private JLabel photoName, photoCaption, photoDate;
	private JScrollPane tags;

	/*----- Components Visible When Adding a Photo -----*/
	private JButton selectPhoto, addNewPhoto, cancelAdd;
	private JTextField caption;
	
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
				// Send resize event to children.
			}

		};
		
		pack();
		setVisible(true);
		addWindowListener(windowHandler);
		addComponentListener(resizeHandler);
	}
	
	public void actionPerformed(ActionEvent event) {
		
	}
	
	private void instantiate() {
		setLayout(new GridBagLayout());

		searchBar = new JTextField();
		searchType = new JComboBox<String>(new String[] {"Date Range", "Tag"});
		photoPanel = new PhotoPanel(this, () -> Arrays.copyOfRange(current.getPhotos(), currentPage * 9, (currentPage + 1) * 9));
		operationPanel = new JPanel();
		infoPanel = new JPanel(new GridBagLayout());
		addPanel = new JPanel(new GridBagLayout());
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
		
		selectPhoto = new JButton("Select Photo");
		addNewPhoto = new JButton("Submit");
		cancelAdd = new JButton("Cancel");
		caption = new JTextField();
		
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
		selectPhoto.addActionListener(this);
		addNewPhoto.addActionListener(this);
		cancelAdd.addActionListener(this);
		modifyTag.addActionListener(this);
		cancelTag.addActionListener(this);
		movePhoto.addActionListener(this);
		cancelMove.addActionListener(this);
	}
	
	private void layoutViews() {
		// Search Bar Constraints.
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = 2;
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
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		gbc.fill = GridBagConstraints.BOTH;
		photoDisplay.setImage("assets/404.png");
		add(photoDisplay, gbc);
		
		// InfoPanel Constraints.
		gbc = new GridBagConstraints();
		gbc.gridx = 4;
		gbc.gridy = 4;
		gbc.gridheight = 2;
		gbc.fill = GridBagConstraints.BOTH;
		add(infoPanel, gbc);
		gbc = new GridBagConstraints();
		infoPanel.add(photoName, gbc);
		gbc.gridy = 1;
		infoPanel.add(photoCaption, gbc);
		gbc.gridy = 2;
		infoPanel.add(photoDate, gbc);
		gbc.gridy = 3;
		infoPanel.add(tags, gbc);
		
		// OperationalPanel Constraints.
		gbc = new GridBagConstraints();
		gbc.gridy = 7;
		gbc.gridwidth = 7;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(operationPanel, gbc);
	}

}
