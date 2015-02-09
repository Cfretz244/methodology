package songlib;

import java.io.IOException;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class SongLib {
	
	public static void main(String[] args) throws IOException {
		SongListModel model = ConfigHandler.loadConfig();
		model.addListDataListener(new ListDataListener() {
			private void handleChange(ListDataEvent e) {
				Object changed = e.getSource();
				if (changed instanceof SongListModel) {
					SongListModel model = (SongListModel) changed;
					try {
						ConfigHandler.writeConfig(model.getSongs());
					} catch (IOException exception) {
						System.out.println("Exception in model event handler: " + exception.getMessage());
					}
				}
			}
			
			public void contentsChanged(ListDataEvent e) {
				handleChange(e);
			}
			
			public void intervalAdded(ListDataEvent e) {
				handleChange(e);
			}
			
			public void intervalRemoved(ListDataEvent e) {
				handleChange(e);
			}
		});
		SongFrame frame = new SongFrame(model);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(SongFrame.EXIT_ON_CLOSE);
	}

}