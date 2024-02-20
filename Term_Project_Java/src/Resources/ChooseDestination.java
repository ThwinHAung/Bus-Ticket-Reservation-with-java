package Resources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.toedter.calendar.JDateChooser;

public class ChooseDestination extends JFrame {
    private JComboBox<String> departureComboBox;
    private JComboBox<String> destinationComboBox;
    private JComboBox<String> timeComboBox;

    private JDateChooser dateChooser;
    private String[] cities = {"Bangkok", "Chiang Mai", "Phuket", "Pattaya", "Hat Yai"};
    private String[] times = {"06:00 AM", "09:00 AM", "03:00 PM", "06:00PM"};

    public ChooseDestination() {
        setTitle("Ticket Booking");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel departureLabel = new JLabel("Departure City:");
        departureLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        departureComboBox = new JComboBox<>(cities);
        departureComboBox.setFont(new Font("Arial", Font.PLAIN, 20));
        add(departureLabel, gbc);
        add(departureComboBox, gbc);

        JLabel destinationLabel = new JLabel("Destination City:");
        destinationLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        destinationComboBox = new JComboBox<>(cities);
        destinationComboBox.setFont(new Font("Arial", Font.PLAIN, 20));
        add(destinationLabel, gbc);
        add(destinationComboBox, gbc);

        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        dateChooser = new JDateChooser();
        dateChooser.setFont(new Font("Arial", Font.PLAIN, 20));
        add(dateLabel, gbc);
        add(dateChooser, gbc);


        JLabel timeLabel = new JLabel("Departure Time:");
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        timeComboBox = new JComboBox<>(times);
        timeComboBox.setFont(new Font("Arial", Font.PLAIN, 20));
        add(timeLabel, gbc);
        add(timeComboBox, gbc);


        JButton bookTicketButton = new JButton("Book Ticket");
        bookTicketButton.setFont(new Font("Arial", Font.PLAIN, 24));
        bookTicketButton.setPreferredSize(new Dimension(200, 50));
        bookTicketButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String departureCity = (String) departureComboBox.getSelectedItem();
                String destinationCity = (String) destinationComboBox.getSelectedItem();
                String selectedDate = String.format("%tF", dateChooser.getDate());
                String selectedTime = (String) timeComboBox.getSelectedItem();
                JOptionPane.showMessageDialog(null, "Ticket booked from " + departureCity + " to " + destinationCity + " on " + selectedDate + " at " + selectedTime);
            }
        });
        add(bookTicketButton, gbc);


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int frameWidth = screenWidth / 2;
        int frameHeight = screenHeight / 2;
        setSize(frameWidth, frameHeight);


        setLocationRelativeTo(null);


        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ChooseDestination();
            }
        });
    }
}


