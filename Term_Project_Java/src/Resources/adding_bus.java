package Resources;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import Db.DatabaseConnection;

public class adding_bus extends JFrame {

	public adding_bus() {
		setTitle("Bus ticket reservation");

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		int halfScreenWidth = screenSize.width / 2;
		int halfScreenHeight = screenSize.height / 2;

		setSize(halfScreenWidth, halfScreenHeight);

		setLocationRelativeTo(null);

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);

		JLabel busNumberLabel = new JLabel("Bus number:");
		busNumberLabel.setFont(new Font("Arial", Font.BOLD, 16));
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(busNumberLabel, gbc);

		String[] busOptions = { "14-seat van", "20-seat minibus", "30-seat bus" };
		JComboBox<String> busNumberComboBox = new JComboBox<>(busOptions);
		busNumberComboBox.setPreferredSize(new Dimension(250, 35));
		gbc.gridx = 1;
		gbc.gridy = 0;
		panel.add(busNumberComboBox, gbc);

		JLabel depDateLabel = new JLabel("Departure date:");
		depDateLabel.setFont(new Font("Arial", Font.BOLD, 16));
		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(depDateLabel, gbc);

		JDateChooser depDateChooser = new JDateChooser();
		depDateChooser.setPreferredSize(new Dimension(250, 35));
		gbc.gridx = 1;
		gbc.gridy = 1;
		panel.add(depDateChooser, gbc);

		JLabel depTimeLabel = new JLabel("Departure time:");
		depTimeLabel.setFont(new Font("Arial", Font.BOLD, 16));
		gbc.gridx = 0;
		gbc.gridy = 2;
		panel.add(depTimeLabel, gbc);

		String[] depTimes = { "08:00 AM", "10:00 AM", "12:00 PM" };
		JComboBox<String> depTimeComboBox = new JComboBox<>(depTimes);
		depTimeComboBox.setPreferredSize(new Dimension(250, 35));
		gbc.gridx = 1;
		gbc.gridy = 2;
		panel.add(depTimeComboBox, gbc);

		JLabel depPlaceLabel = new JLabel("Departure place:");
		depPlaceLabel.setFont(new Font("Arial", Font.BOLD, 16));
		gbc.gridx = 0;
		gbc.gridy = 3;
		panel.add(depPlaceLabel, gbc);

		String[] depPlaces = { "Bangkok", "Pattaya", "Phuket", "Chiang Mai", "Ayutthaya", "Chonburi" };
		JComboBox<String> depPlaceComboBox = new JComboBox<>(depPlaces);
		depPlaceComboBox.setPreferredSize(new Dimension(250, 35));
		gbc.gridx = 1;
		gbc.gridy = 3;
		panel.add(depPlaceComboBox, gbc);
		;

		JLabel destPlaceLabel = new JLabel("Destination place:");
		destPlaceLabel.setFont(new Font("Arial", Font.BOLD, 16));
		gbc.gridx = 0;
		gbc.gridy = 4;
		panel.add(destPlaceLabel, gbc);

		String[] destPlaces = { "Bangkok", "Pattaya", "Phuket", "Chiang Mai", "Ayutthaya", "Chonburi" };
		JComboBox<String> destPlaceComboBox = new JComboBox<>(destPlaces);
		destPlaceComboBox.setPreferredSize(new Dimension(250, 35));
		gbc.gridx = 1;
		gbc.gridy = 4;
		panel.add(destPlaceComboBox, gbc);

		JLabel totalSeatsLabel = new JLabel("Total seats:");
		totalSeatsLabel.setFont(new Font("Arial", Font.BOLD, 16));
		gbc.gridx = 0;
		gbc.gridy = 5;
		panel.add(totalSeatsLabel, gbc);

		JTextField totalSeatsField = new JTextField();
		totalSeatsField.setPreferredSize(new Dimension(250, 35));
		gbc.gridx = 1;
		gbc.gridy = 5;
		panel.add(totalSeatsField, gbc);

		JButton addButton = new JButton("Add Bus");
		addButton.setPreferredSize(new Dimension(100, 45));
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.gridwidth = 2;
		panel.add(addButton, gbc);

		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedBusOption = (String) busNumberComboBox.getSelectedItem();
				String busNumber = getBusNumberFromOption(selectedBusOption);
				java.util.Date depDate = depDateChooser.getDate();
				String depTimeStr = (String) depTimeComboBox.getSelectedItem();
				Time depTime = convertToSqlTime(depTimeStr);
				String depPlace = (String) depPlaceComboBox.getSelectedItem();
				String destPlace = (String) destPlaceComboBox.getSelectedItem();
				int totalSeats = 0;

				try {
					String totalSeatsInput = totalSeatsField.getText();
					if (!totalSeatsInput.isEmpty()) {
						totalSeats = Integer.parseInt(totalSeatsInput);
					}
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Invalid total seats input. Please enter a valid number.");
					return;
				}

				try (Connection connection = DatabaseConnection.getConnection()) {
					String query = "INSERT INTO Bus (bus_number, departure_date, departure_time, departure_place, destination_place, total_seats) VALUES (?, ?, ?, ?, ?, ?)";
					PreparedStatement preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, busNumber);
					preparedStatement.setDate(2, new java.sql.Date(depDate.getTime()));
					preparedStatement.setTime(3, depTime);
					preparedStatement.setString(4, depPlace);
					preparedStatement.setString(5, destPlace);
					preparedStatement.setInt(6, totalSeats);
					preparedStatement.executeUpdate();
					JOptionPane.showMessageDialog(null, "Data added successfully!");
					new BusTicketDashboard().setVisible(true);
					dispose();
				} catch (SQLException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error adding data to database.");
				}
			}

			private String getBusNumberFromOption(String option) {
				switch (option) {
				case "14-seat van":
					return "14-seat van";
				case "20-seat minibus":
					return "20-seat minibus";
				case "30-seat bus":
					return "30-seat bus";
				default:
					return "";
				}
			}

			private Time convertToSqlTime(String timeStr) {
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
					java.util.Date date = sdf.parse(timeStr);
					return new Time(date.getTime());
				} catch (ParseException e) {
					e.printStackTrace();
					return null;
				}
			}

		});

		add(panel);

		setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(adding_bus::new);
	}
}