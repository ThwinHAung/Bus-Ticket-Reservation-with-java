package Resources;

import javax.swing.*;

import Db.DatabaseConnection;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.awt.event.ActionEvent;

public class TicketInformation extends JFrame {
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField priceField;
    private JComboBox<String> busTypeDropdown;
    private String selectedSeat;
    private int[] selectedID;

    public TicketInformation(int[] selectedID) {

    	this.selectedID = selectedID;
        for (int id : selectedID) {
            System.out.print(id + " ");
        }
        setTitle("Bus Ticket Reservation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        Font font = new Font("Arial", Font.PLAIN, 16);

        JLabel busTypeLabel = new JLabel("Bus Type:");
        busTypeLabel.setFont(font);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(busTypeLabel, gbc);

        busTypeDropdown = new JComboBox<>();
        busTypeDropdown.setFont(font);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(busTypeDropdown, gbc);

        fetchAndPopulateBusNumbers();

        JLabel nameLabel = new JLabel("Customer's Name:");
        nameLabel.setFont(font);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(nameLabel, gbc);

        nameField = new JTextField();
        nameField.setFont(font);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(nameField, gbc);

        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setFont(font);
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(phoneLabel, gbc);

        phoneField = new JTextField();
        phoneField.setFont(font);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(phoneField, gbc);

        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setFont(font);
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(priceLabel, gbc);

        priceField = new JTextField();
        priceField.setFont(font);
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(priceField, gbc);

        JPanel seatPanel = new JPanel();
        seatPanel.setLayout(new GridLayout(0, 6));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(seatPanel, gbc);

        JButton clearButton = new JButton("Clear");
        clearButton.setFont(font);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        add(clearButton, gbc);

        JButton submitButton = new JButton("Submit");
        submitButton.setFont(font);
        gbc.gridx = 1;
        gbc.gridy = 5;
        add(submitButton, gbc);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.getWidth() / 2);
        int height = (int) (screenSize.getHeight() / 2);
        setSize(width, height);

        setLocationRelativeTo(null);

        setVisible(true);


        busTypeDropdown.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> combo = (JComboBox<String>) e.getSource();
                String selectedBusNumber = (String) combo.getSelectedItem();
                int selectedIndex = combo.getSelectedIndex();
                int selectedBusID = selectedID[selectedIndex]; 
                int totalSeats = getTotalSeatsForBus(selectedBusID); 
                updateSeatButtons(seatPanel, totalSeats);
            }
        });
        

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String passengerName = nameField.getText();
                String phoneNumber = phoneField.getText();
                String seatNumber = selectedSeat;
                String ticketPriceStr = priceField.getText();

                if (passengerName.isEmpty() || phoneNumber.isEmpty() || seatNumber == null || ticketPriceStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields.");
                    return;
                }

                double ticketPrice;
                try {
                    ticketPrice = Double.parseDouble(ticketPriceStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid ticket price.");
                    return;
                }

                int selectedIndex = busTypeDropdown.getSelectedIndex();
                int busID = selectedID[selectedIndex];

                boolean success = insertTicket(busID, passengerName, phoneNumber, seatNumber, ticketPrice);
                if (success) {
                    JOptionPane.showMessageDialog(null, "Ticket booked successfully!");
					new BusTicketDashboard().setVisible(true);
					dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to book ticket.");
                }
            }
        });

    }

    private int getTotalSeatsForBus(int busID) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT total_seats FROM Bus WHERE id = ?");
            preparedStatement.setInt(1, busID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("total_seats");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching total seats for bus");
        }
        return 0;
    }


    private void updateSeatButtons(JPanel seatPanel, int totalSeats) {
        seatPanel.removeAll(); 
        seatPanel.setLayout(new GridLayout(0, 6));
        Font font = new Font("Arial", Font.PLAIN, 16);
        for (int i = 1; i <= totalSeats; i++) {
            JButton seatButton = new JButton(String.valueOf(i));
            seatButton.setFont(font);
            seatButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Object source = e.getSource();
                    if (source instanceof JButton) {
                        JButton button = (JButton) source;
                        selectedSeat = button.getText();
                    }
                }
            });

            seatPanel.add(seatButton);
        }
        revalidate();
        repaint();
    }

    private void fetchAndPopulateBusNumbers() {
        try {
            Connection connection = DatabaseConnection.getConnection();
            StringBuilder queryBuilder = new StringBuilder("SELECT bus_number FROM Bus WHERE id IN (");
            for (int i = 0; i < selectedID.length; i++) {
                queryBuilder.append("?");
                if (i < selectedID.length - 1) {
                    queryBuilder.append(",");
                }
            }
            queryBuilder.append(")");

            PreparedStatement preparedStatement = connection.prepareStatement(queryBuilder.toString());
            for (int i = 0; i < selectedID.length; i++) {
                preparedStatement.setInt(i + 1, selectedID[i]);
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String busNumber = resultSet.getString("bus_number");
                busTypeDropdown.addItem(busNumber);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching bus numbers");
        }
    }




    private boolean insertTicket(int busID, String passengerName, String phoneNumber, String seatNumber, double ticketPrice) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            if (isSeatBooked(busID, seatNumber)) {
                JOptionPane.showMessageDialog(null, "Seat " + seatNumber + " is already booked.");
                return false;
            }
            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO Ticket (bus_id, passenger_name, phone_number, seatNumber, ticket_price, date) VALUES (?, ?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, busID);
            preparedStatement.setString(2, passengerName);
            preparedStatement.setString(3, phoneNumber);
            preparedStatement.setString(4, seatNumber);
            preparedStatement.setDouble(5, ticketPrice);
            preparedStatement.setString(6, currentDate);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error inserting ticket information");
            return false;
        }
    }



    private boolean isSeatBooked(int busID, String seatNumber) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM Ticket WHERE bus_id = ? AND seatNumber = ?");
            preparedStatement.setInt(1, busID);
            preparedStatement.setString(2, seatNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error checking seat availability");
            return true;
        }
    }


}
