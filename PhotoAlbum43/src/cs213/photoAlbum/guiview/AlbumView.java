package cs213.photoAlbum.guiview;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cs213.photoAlbum.control.Control;
import cs213.photoAlbum.model.Album;

public class AlbumView extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1;
	private Control control;
	private Album current;
	private int currentPage;
	
	/*----- Unconditionally Visible Components -----*/
	private JComboBox<String> searchType;
	private JTextField searchBar;
	private PhotoPanel photoPanel;
	private JPanel operationPanel, infoPanel, addPanel, tagPanel, movePanel;
	private JButton prev, next, search, addPhoto, deletePhoto, move, recaption, addTag, deleteTag;

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
	}
	
	public void actionPerformed(ActionEvent event) {
		
	}
	
	private void instantiate() {
		setLayout(new GridBagLayout());

		searchType = new JComboBox<String>(new String[] {"Date Range", "Tag"});
		searchBar = new JTextField();
		photoPanel = new PhotoPanel(this, () -> Arrays.copyOfRange(current.getPhotos(), currentPage * 9, (currentPage + 1) * 9));
		operationPanel = new JPanel();
		infoPanel = new JPanel(new GridBagLayout());
		addPanel = new JPanel(new GridBagLayout());
		tagPanel = new JPanel(new GridBagLayout());
		movePanel = new JPanel(new GridBagLayout());
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

}
