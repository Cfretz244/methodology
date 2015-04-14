package cs213.photoAlbum.guiview;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import cs213.photoAlbum.model.Album;

public class MovePanel extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1;
	private JButton moveButton, cancelButton;
	private JComboBox<String> moveTo;
	private Consumer<String> movePhoto;
	private Consumer<?> cancel;
	private Supplier<Album[]> supplier;
	
	public MovePanel(Consumer<String> movePhoto, Supplier<Album[]> supplier, Consumer<?> cancel) {
		this.movePhoto = movePhoto;
		this.cancel = cancel;
		this.supplier = supplier;
		
		setLayout(new GridBagLayout());
		instantiate();
		bind();
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(moveTo, gbc);
		gbc = new GridBagConstraints();
		gbc.gridy = 1;
		add(cancelButton, gbc);
		gbc.gridx = 1;
		add(moveButton, gbc);
	}
	
	public void update() {
		moveTo.removeAllItems();
		Album[] albums = supplier.get();
		for (Album album : albums) moveTo.addItem(album.getName());
	}
	
	public void actionPerformed(ActionEvent event) {
		JButton button = (JButton) event.getSource();
		
		if (button == moveButton) {
			movePhoto.accept(moveTo.getItemAt(moveTo.getSelectedIndex()));
		} else {
			cancel.accept(null);
		}
	}
	
	private void instantiate() {
		moveButton = new JButton("Move");
		cancelButton = new JButton("Cancel");
		moveTo = new JComboBox<String>();
	}
	
	private void bind() {
		moveButton.addActionListener(this);
		cancelButton.addActionListener(this);
	}

}
