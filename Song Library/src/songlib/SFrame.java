package songlib;

import java.awt.GridLayout;

import javax.swing.JFrame;

public class SFrame extends JFrame {
	
	public SFrame() {
		this.setLayout(new GridLayout(1, 2));
		add(new SPanel());
		add(new SPanel());
	}

}
