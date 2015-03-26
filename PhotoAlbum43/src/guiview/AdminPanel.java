package guiview;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cs213.photoAlbum.control.Control;

public class AdminPanel extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1;
	private Control control;
	private UserList model;
	private JList<String> userList;
	private JButton addButton, deleteButton, doneButton, cancelButton;
	private JLabel nameLabel, idLabel, error;
	private JTextField nameField, idField;
	private JPanel controlPanel, detailPanel;
	
	public AdminPanel(Consumer<Integer> leave, Control control) {
		this.control = control;
		instantiate();
		bind();
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;
		add(userList, constraints);
		
		constraints = new GridBagConstraints();
		constraints.weighty = 0.5;
		constraints.insets = new Insets(0, 10, 0, 10);
		constraints.anchor = GridBagConstraints.PAGE_END;
		controlPanel.add(addButton, constraints);
		
		constraints = new GridBagConstraints();
		constraints.gridy = 1;
		constraints.weighty = 0.5;
		constraints.insets = new Insets(0, 10, 0, 10);
		constraints.anchor = GridBagConstraints.PAGE_START;
		controlPanel.add(deleteButton, constraints);
		
		constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.LAST_LINE_START;
		detailPanel.add(nameLabel, constraints);
		
		constraints = new GridBagConstraints();
		constraints.weighty = 0.3;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		detailPanel.add(nameField, constraints);
		
		constraints = new GridBagConstraints();
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.LAST_LINE_START;
		detailPanel.add(idLabel, constraints);
		
		constraints = new GridBagConstraints();
		constraints.weighty = 0.3;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		detailPanel.add(idField, constraints);
		
		constraints = new GridBagConstraints();
		constraints.gridy = 4;
		constraints.gridwidth = 2;
		detailPanel.add(error, constraints);
		
		constraints = new GridBagConstraints();
		constraints.gridy = 5;
		constraints.weighty = 0.3;
		detailPanel.add(doneButton, constraints);
		
		constraints = new GridBagConstraints();
		constraints.gridy = 5;
		constraints.gridx = 1;
		constraints.weighty = 0.3;
		detailPanel.add(cancelButton, constraints);
		
		switchControlPanel();
	}
	
	private void instantiate() {
		model = new UserList(control);
		userList = new JList<String>(model);
		addButton = new JButton("   Add User   ");
		deleteButton = new JButton("Remove User");
		doneButton = new JButton("Done");
		cancelButton = new JButton("Cancel");
		nameLabel = new JLabel("User Name:");
		idLabel = new JLabel("User ID:");
		error = new JLabel(" ");
		error.setForeground(Color.RED);
		nameField = new JTextField(10);
		idField = new JTextField(10);
		controlPanel = new JPanel(new GridBagLayout());
		detailPanel = new JPanel(new GridBagLayout());
		setLayout(new GridBagLayout());
	}
	
	private void bind() {
		addButton.addActionListener(this);
		deleteButton.addActionListener(this);
		doneButton.addActionListener(this);
		cancelButton.addActionListener(this);
	}
	
	private void switchControlPanel() {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;
		remove(detailPanel);
		add(controlPanel, constraints);
		revalidate();
		repaint();
	}
	
	private void switchDetailPanel() {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;
		remove(controlPanel);
		add(detailPanel, constraints);
		revalidate();
		repaint();
	}
	
	public void actionPerformed(ActionEvent event) {
		JButton clicked = (JButton) event.getSource();
		
		if (clicked == addButton) {
			switchDetailPanel();
		} else if (clicked == deleteButton) {
			String selected = userList.getSelectedValue();
			if (selected != null) {
				model.deleteUser(selected);
			}
		} else if (clicked == doneButton) {
			if (model.addUser(nameField.getText(), idField.getText())) {
				nameField.setText("");
				idField.setText("");
				switchControlPanel();
			} else {
				error.setText("User ID already taken");
			}
		} else if (clicked == cancelButton) {
			switchControlPanel();
		}
	}

}