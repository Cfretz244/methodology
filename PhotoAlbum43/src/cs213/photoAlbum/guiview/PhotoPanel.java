package cs213.photoAlbum.guiview;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import cs213.photoAlbum.model.Drawable;

/**
 * Class handles displaying a collection of photobuttons.
 * @author cfretz
 */
public class PhotoPanel extends JPanel {

	private static final long serialVersionUID = 1;
	private List<PhotoButton> buttons;
	private Supplier<Drawable[]> updater;

	public PhotoPanel(ActionListener listener, Supplier<Drawable[]> updater) {
		this.updater = updater;
		setLayout(new GridBagLayout());
		setBorder(new LineBorder(Color.black, 2, true));
		buttons = new ArrayList<PhotoButton>();
		ComponentAdapter resizeHandler = new ComponentAdapter() {

			public void componentResized(ComponentEvent event) {
				for (PhotoButton button : buttons) button.resized(event.getComponent().getSize());
			}

		};
		addComponentListener(resizeHandler);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.weightx = 0.3;
		constraints.weighty = 0.3;
		constraints.insets = new Insets(10, 10, 10, 10);
		for (int i = 0; i < 3; i++) {
			constraints.gridy = i;
			for (int j = 0; j < 3; j++) {
				constraints.gridx = j;
				PhotoButton button = new PhotoButton((3 * i) + j);
				button.addActionListener(listener);
				buttons.add(button);
				add(button, constraints);
			}
		}
		updatePhotos();
	}
	
	/**
	 * Method upates the photos displayed in the panel.
	 */
	public void updatePhotos() {
		Drawable[] data = updater.get();
		int index = 0;
		for (; index < data.length; index++) {
			Drawable current = data[index];
			buttons.get(index).setDrawable(current);
		}
		for (; index < 9; index++) buttons.get(index).setDrawable(null);
		repaint();
	}
	
	/**
	 * Enables all buttons.
	 */
	public void enable() {
		for (PhotoButton button : buttons) button.setEnabled(true);
	}
	
	/**
	 * Disables all buttons.
	 */
	public void disable() {
		for (PhotoButton button : buttons) button.setEnabled(false);
	}

	/**
	 * Gets the button at the specified index.
	 * @param index The index.
	 * @return The button.
	 */
	public PhotoButton getButton(int index) {
		return buttons.get(index);
	}

}
