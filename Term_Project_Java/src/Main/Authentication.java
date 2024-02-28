package Main;

import java.awt.*;
import javax.swing.*;

import Db.DatabaseConnection;
import Resources.BusTicketDashboard;
import Resources.ChooseDestination;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Authentication extends JFrame {
	private JTextField usernameField;
	private JPasswordField passwordField;

	public Authentication() {
		setTitle("Bus Ticket Reservation");

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		int halfScreenWidth = screenSize.width / 2;
		int halfScreenHeight = screenSize.height / 2;

		setSize(halfScreenWidth, halfScreenHeight);

		setLocationRelativeTo(null);

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);

		ImageIcon originalIcon = new ImageIcon("images/login_image.jpg");
		Image originalImage = originalIcon.getImage();
		Image resizedImage = originalImage.getScaledInstance(200, 100, Image.SCALE_SMOOTH);
		ImageIcon resizedIcon = new ImageIcon(resizedImage);


		JLabel imageLabel = new JLabel(resizedIcon);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		panel.add(imageLabel, gbc);

		JLabel welcomeLabel = new JLabel("Welcome to Bus Ticket Reservation Application");
		welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
		welcomeLabel.setForeground(Color.BLUE);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(10, 5, 5, 5);
		panel.add(welcomeLabel, gbc);

		JLabel subtitleLabel = new JLabel("Please login to continue");
		subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 14));
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(5, 5, 5, 5);
		panel.add(subtitleLabel, gbc);

		JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setFont(new Font("Arial", Font.BOLD, 16));
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		panel.add(usernameLabel, gbc);

		usernameField = new JTextField();
		usernameField.setPreferredSize(new Dimension(250, 35));
		gbc.gridx = 1;
		gbc.gridy = 3;
		panel.add(usernameField, gbc);

		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setFont(new Font("Arial", Font.BOLD, 16));
		gbc.gridx = 0;
		gbc.gridy = 4;
		panel.add(passwordLabel, gbc);

		passwordField = new JPasswordField();
		passwordField.setPreferredSize(new Dimension(250, 35));
		gbc.gridx = 1;
		gbc.gridy = 4;
		panel.add(passwordField, gbc);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton loginButton = new JButton("Login");
		loginButton.setPreferredSize(new Dimension(100, 45));
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = usernameField.getText();
				String password = new String(passwordField.getPassword());

				if (authenticate(username, password)) {
					JOptionPane.showMessageDialog(null, "Login successful!");
					new BusTicketDashboard().setVisible(true);
					dispose();

				} else {
					JOptionPane.showMessageDialog(null, "Invalid username or password. Please try again.");
				}

				System.out.println("Username: " + username);
				System.out.println("Password: " + password);
			}
		});
		buttonPanel.add(loginButton);
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 2;
		panel.add(buttonPanel, gbc);

		add(panel);

		setLocationRelativeTo(null);

		setVisible(true);
	}

	private boolean authenticate(String username, String password) {
		try (Connection connection = DatabaseConnection.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(
						"SELECT * FROM user WHERE username='" + username + "' AND password='" + password + "'")) {

			return resultSet.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

}




