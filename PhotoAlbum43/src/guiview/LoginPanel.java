package guiview;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cs213.photoAlbum.control.Control;

public class LoginPanel extends JPanel implements ActionListener, Resizable {
	
	private static final long serialVersionUID = 1;
	private Control control;
	private JLabel label, error;
	private JTextField username;
	private JButton loginButton;
	private Consumer<String> loginCall;
	
	public LoginPanel(Consumer<String> loginCall, Control control) {
		this.control = control;
		setLayout(new GridBagLayout());
		label = new JLabel("Please enter your username to log in:");
		error = new JLabel(" ");
		error.setForeground(Color.RED);
		username = new JTextField(10);
		loginButton = new JButton("Login");
		this.loginCall = loginCall;

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(50, 50, 20, 50);
		add(label, constraints);
		
		constraints = new GridBagConstraints();
		constraints.insets = new Insets(0, 50, 20, 50);
		constraints.gridy = 1;
		add(username, constraints);
		
		constraints = new GridBagConstraints();
		constraints.insets = new Insets(0, 50, 20, 50);
		constraints.gridy = 2;
		add(loginButton, constraints);
		
		constraints = new GridBagConstraints();
		constraints.insets = new Insets(0, 50, 20, 50);
		constraints.gridy = 3;
		add(error, constraints);
		
		loginButton.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent event) {
		if (username.getText().equals("admin")) {
			loginCall.accept("admin");
		} else {
			String user = username.getText();
			control.setCurrentUser(user);
			if (control.loadUserData()) loginCall.accept(user);
			else error.setText("Invalid username. Please enter another");
		}
	}
	
	public void resized(Dimension size) {
		
	}

}
