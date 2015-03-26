package guiview;

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
	private Control control;
	private WindowAdapter windowHandler;
	private int state;
	
	public OverView() {
		username = new String();
		control = new Control();
		windowHandler = new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				control.shutdown();
			}
		};
		switchToLogin();
		add(base);
		pack();
		setVisible(true);
		addWindowListener(windowHandler);
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
		add(base);
		revalidate();
		repaint();
	}
	
	private void switchToAdmin() {
		state = ADMIN;
		setTitle("Administration");
		remove(base);
		base = new AdminPanel(dummy -> switchToLogin(), control);
		add(base);
		revalidate();
		repaint();
	}
	
	private void switchToAccount() {
		state = ACCOUNT;
		setTitle(username + " Photo Albums");
		remove(base);
		base = new AccountPanel(dummy -> switchToLogin(), control);
		add(base);
		revalidate();
		repaint();
	}
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		OverView view = new OverView();
	}
	
}
