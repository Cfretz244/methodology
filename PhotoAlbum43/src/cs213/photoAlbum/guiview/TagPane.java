package cs213.photoAlbum.guiview;

import java.util.StringJoiner;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import cs213.photoAlbum.model.Photo;

public class TagPane extends JScrollPane {
	
	private static final long serialVersionUID = 1;
	private JTextArea tagView;
	private Photo current;
	
	public TagPane() {
		tagView = new JTextArea();
		setViewportView(tagView);
	}
	
	public void setPhoto(Photo photo) {
		current = photo;
		String[] tags = current.getTags();
		StringJoiner joiner = new StringJoiner("\n");
		for (String tag : tags) joiner.add(tag);
		tagView.setText(joiner.toString());
	}

}
