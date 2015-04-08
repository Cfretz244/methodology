package cs213.photoAlbum.guiview;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import cs213.photoAlbum.control.Control;

public class OverView extends JFrame {

	private static final long serialVersionUID = 1;
	private static final int LOGIN = 0, ADMIN = 1, ACCOUNT = 2;
	private String username;
	private JPanel base;
	private Resizable child;
	private Control control;
	private WindowAdapter windowHandler;
	private ComponentAdapter sizeHandler;
	private int state;
	
	public OverView() {
		username = new String();
		control = new Control();

		windowHandler = new WindowAdapter() {

			public void windowClosing(WindowEvent event) {
				control.shutdown();
			}

		};

		sizeHandler = new ComponentAdapter() {

			public void componentResized(ComponentEvent event) {
				child.resized(event.getComponent().getSize());
			}

		};

		switchToLogin();
		pack();
		setVisible(true);
		addWindowListener(windowHandler);
		addComponentListener(sizeHandler);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private void switchToLogin() {
		state = LOGIN;
		setTitle("Login");
		if (base != null) remove(base);
		base = new LoginPanel(username -> {
			if (username.equals("admin")) {
				switchToAdmin();
			} else {
				switchToAccount();
			}
		}, control);
		child = (Resizable) base;
		add(base);
		revalidate();
		repaint();
		pack();
	}
	
	private void switchToAdmin() {
		state = ADMIN;
		setTitle("Administration");
		remove(base);
		base = new AdminPanel(dummy -> switchToLogin(), control);
		child = (Resizable) base;
		add(base);
		revalidate();
		repaint();
	}
	
	private void switchToAccount() {
		state = ACCOUNT;
		setTitle(username + " Photo Albums");
		remove(base);
		base = new AccountPanel(dummy -> switchToLogin(), control);
		child = (Resizable) base;
		add(base);
		revalidate();
		repaint();
		pack();
	}
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		OverView view = new OverView();
	}
	
}
