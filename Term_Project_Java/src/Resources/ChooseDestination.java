package Resources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.toedter.calendar.JDateChooser;
import Db.DatabaseConnection;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ChooseDestination extends JFrame {
	private JComboBox<String> departureComboBox;
	private JComboBox<String> destinationComboBox;
	private JComboBox<String> departureTimeComboBox;
	private String[] cities = { "Bangkok", "Chiang Mai", "Phuket", "Pattaya", "Hat Yai" };
	private String[] departureTimes = { "8:00 AM", "10:00 AM", "12:00 PM", "2:00 PM", "4:00 PM", "6:00 PM" };

	public ChooseDestination() {
		setTitle("Bus Ticket Reservation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 10, 10, 10);

		try {
			Connection connection = DatabaseConnection.getConnection();
			Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			ResultSet rsDeparturePlaces = statement.executeQuery("SELECT DISTINCT departure_place FROM Bus");
			rsDeparturePlaces.last();
			int departurePlaceCount = rsDeparturePlaces.getRow();
			rsDeparturePlaces.beforeFirst();
			String[] departurePlaces = new String[departurePlaceCount];
			int i = 0;
			while (rsDeparturePlaces.next()) {
				departurePlaces[i++] = rsDeparturePlaces.getString("departure_place");
			}

			JLabel departureLabel = new JLabel("Departure City:");
			departureLabel.setFont(new Font("Arial", Font.PLAIN, 24));
			gbc.gridx = 0;
			gbc.gridy = 0;
			add(departureLabel, gbc);

			departureComboBox = new JComboBox<>(departurePlaces);
			departureComboBox.setFont(new Font("Arial", Font.PLAIN, 24));
			departureComboBox.setPreferredSize(new Dimension(300, 40));
			gbc.gridx = 1;
			gbc.gridy = 0;
			gbc.gridwidth = 2;
			add(departureComboBox, gbc);

			JLabel destinationLabel = new JLabel("Destination City:");
			destinationLabel.setFont(new Font("Arial", Font.PLAIN, 24));
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.gridwidth = 1;
			add(destinationLabel, gbc);

			destinationComboBox = new JComboBox<>();
			destinationComboBox.setFont(new Font("Arial", Font.PLAIN, 24));
			destinationComboBox.setPreferredSize(new Dimension(300, 40));
			gbc.gridx = 1;
			gbc.gridy = 1;
			gbc.gridwidth = 2;
			add(destinationComboBox, gbc);

			departureComboBox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						updateDestinationPlaces((String) departureComboBox.getSelectedItem());
					}
				}
			});
			updateDestinationPlaces((String) departureComboBox.getSelectedItem());

			JLabel departureTimeLabel = new JLabel("Departure Time:");
			departureTimeLabel.setFont(new Font("Arial", Font.PLAIN, 24));
			gbc.gridx = 0;
			gbc.gridy = 3;
			gbc.gridwidth = 1;
			add(departureTimeLabel, gbc);

			departureTimeComboBox = new JComboBox<>(departureTimes);
			departureTimeComboBox.setFont(new Font("Arial", Font.PLAIN, 24));
			departureTimeComboBox.setPreferredSize(new Dimension(300, 40));
			gbc.gridx = 1;
			gbc.gridy = 3;
			gbc.gridwidth = 2;
			add(departureTimeComboBox, gbc);

			JLabel departureDateLabel = new JLabel("Departure Date:");
			departureDateLabel.setFont(new Font("Arial", Font.PLAIN, 24));
			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.gridwidth = 1;
			add(departureDateLabel, gbc);

			JDateChooser depDateChooser = new JDateChooser();
			depDateChooser.setFont(new Font("Arial", Font.PLAIN, 24));
			depDateChooser.setPreferredSize(new Dimension(300, 40));
			gbc.gridx = 1;
			gbc.gridy = 2;
			gbc.gridwidth = 2;
			add(depDateChooser, gbc);

			JButton bookTicketButton = new JButton("Continue");
			bookTicketButton.setFont(new Font("Arial", Font.PLAIN, 28));
			bookTicketButton.setPreferredSize(new Dimension(250, 60));
			bookTicketButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String departureCity = (String) departureComboBox.getSelectedItem();
					String destinationCity = (String) destinationComboBox.getSelectedItem();
					String departureTime = (String) departureTimeComboBox.getSelectedItem();
					java.util.Date depDate = depDateChooser.getDate();

					ArrayList<Integer> matchingIds = getMatchingIds(departureCity, destinationCity, departureTime,
							depDate);

					if (!matchingIds.isEmpty()) {
						int[] idArray = matchingIds.stream().mapToInt(Integer::intValue).toArray();
						System.out.println(idArray);
						 new TicketInformation(idArray);
						 dispose();
					} else {
						JOptionPane.showMessageDialog(null, "There is no schedule for the selected options.");
					}
				}
			});
			gbc.gridwidth = 3;
			gbc.gridx = 0;
			gbc.gridy = 4;
			add(bookTicketButton, gbc);

			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			int screenWidth = screenSize.width;
			int screenHeight = screenSize.height;
			int frameWidth = screenWidth / 2;
			int frameHeight = screenHeight / 2;
			setSize(frameWidth, frameHeight);

			setLocationRelativeTo(null);

			setVisible(true);

		} catch (SQLException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error connecting to database");

		}
	}

	private void updateDestinationPlaces(String selectedDeparturePlace) {
		try {
			Connection connection = DatabaseConnection.getConnection();
			PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT DISTINCT destination_place FROM Bus WHERE departure_place = ?");
			preparedStatement.setString(1, selectedDeparturePlace);
			ResultSet rsDestinationPlaces = preparedStatement.executeQuery();

			destinationComboBox.removeAllItems();
			while (rsDestinationPlaces.next()) {
				String destinationPlace = rsDestinationPlaces.getString("destination_place");
				destinationComboBox.addItem(destinationPlace);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error fetching destination places");
		}
	}

	private ArrayList<Integer> getMatchingIds(String departureCity, String destinationCity, String departureTime,
	        java.util.Date depDate) {
	    ArrayList<Integer> matchingIds = new ArrayList<>();
	    try {
	        Connection connection = DatabaseConnection.getConnection();
	        String query = "SELECT id FROM Bus WHERE departure_place = ? AND destination_place = ? AND DATE_FORMAT(departure_time, '%H:%i') = ? AND departure_date = ?";
	        PreparedStatement preparedStatement = connection.prepareStatement(query);
	        preparedStatement.setString(1, departureCity);
	        preparedStatement.setString(2, destinationCity);

	        SimpleDateFormat sdfInput = new SimpleDateFormat("h:mm a");
	        SimpleDateFormat sdfOutput = new SimpleDateFormat("HH:mm");
	        String formattedDepartureTime = sdfOutput.format(sdfInput.parse(departureTime));

	        preparedStatement.setString(3, formattedDepartureTime);
	        preparedStatement.setDate(4, new java.sql.Date(depDate.getTime()));

	        ResultSet resultSet = preparedStatement.executeQuery();
	        while (resultSet.next()) {
	            matchingIds.add(resultSet.getInt("id"));
	        }
	    } catch (SQLException | ParseException ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(null, "Error fetching matching IDs");
	    }
	    return matchingIds;
	}


	public static void main(String[] args) {
		SwingUtilities.invokeLater(ChooseDestination::new);
	}
}
