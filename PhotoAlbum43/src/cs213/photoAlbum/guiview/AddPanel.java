package cs213.photoAlbum.guiview;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import cs213.photoAlbum.model.Photo;

public class AddPanel extends JPanel implements ActionListener {
	
	private class ImageFilter extends FileFilter {
		
		public boolean accept(File file) {
			try {
				String path = file.getCanonicalPath();
				String ext = path.substring(path.lastIndexOf(".") + 1, path.length()).toLowerCase();
				return ext.equals("jpg") || ext.equals("png") || ext.equals("bmp") || ext.equals("gif") || ext.equals("jpeg") || ext.equals("tiff");
			} catch (Exception e) {
				return false;
			}
		}
		
		public String getDescription() {
			return "JPEG, PNG, BMP, GIF, and TIFF files";
		}
		
	}
	
	private static final long serialVersionUID = 1;
	private Photo photo;
	private JButton selectPhoto, addNewPhoto, cancelAdd;
	private JTextField caption;
	private JFileChooser chooser;
	private Consumer<String> addPhoto, updateUI;
	private Consumer<?> cancel;
	private File chosen;
	
	public AddPanel(Consumer<String> addPhoto, Consumer<String> updateUI, Consumer<?> cancel) {
		this.addPhoto = addPhoto;
		this.updateUI = updateUI;
		this.cancel = cancel;

		setLayout(new GridBagLayout());
		instantiate();
		bind();
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = 2;
		add(selectPhoto, gbc);
		gbc.gridy = 1;
		add(caption, gbc);
		gbc = new GridBagConstraints();
		gbc.gridy = 2;
		add(cancelAdd, gbc);
		gbc.gridx = 1;
		add(addNewPhoto, gbc);
	}
	
	public void setPhoto(Photo photo) {
		this.photo = photo;
		selectPhoto.setEnabled(this.photo != null);
		caption.setText(this.photo != null ? photo.getCaption() : "");
	}
	
	private void instantiate() {
		selectPhoto = new JButton("Select Photo");
		addNewPhoto = new JButton("Submit");
		cancelAdd = new JButton("Cancel");
		caption = new JTextField();
		chooser = new JFileChooser();
		chooser.setFileFilter(new ImageFilter());
	}
	
	private void bind() {
		selectPhoto.addActionListener(this);
		addNewPhoto.addActionListener(this);
		cancelAdd.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent event) {
		JButton button = (JButton) event.getSource();
		if (button == addNewPhoto) {
			try {
				if (chosen != null) {
					File temp = chosen;
					chosen = null;
					photo = null;
					addPhoto.accept(temp.getCanonicalPath());
				} else {
					// TODO: Need to show an error message if this happens.
				}
			} catch (IOException e) {
				// TODO: Add handling for this.
			}
		} else if (button == selectPhoto) {
			int returnVal = chooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				chosen = chooser.getSelectedFile();
				try {
					updateUI.accept(chosen.getCanonicalPath());
				} catch (IOException e) {
					// Should never happen.
				}
			}
		} else {
			photo = null;
			chosen = null;
			cancel.accept(null);
		}
	}

}