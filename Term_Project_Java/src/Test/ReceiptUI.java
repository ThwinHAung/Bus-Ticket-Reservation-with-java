package Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class ReceiptUI extends JFrame {
	private JLabel headerLabel, busIdLabel, busNumberLabel, departureLabel, destinationLabel, dateLabel, timeLabel;
	private JButton printButton, sendEmailButton;
	private JPanel centerPanel;

	public ReceiptUI(String busNumber, String departure, String destination, String date, String time,
			String customer_name, String phone_number, int price) {
		setTitle("Bus Ticket Reservation System");
		setSize(600, 600);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setLayout(new BorderLayout());

		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));
		add(headerPanel, BorderLayout.NORTH);

		JPanel headerCenterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		headerLabel = new JLabel("Bus Pass Receipt");
		headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
		headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		headerCenterPanel.add(headerLabel);
		headerPanel.add(headerCenterPanel, BorderLayout.CENTER);

		centerPanel = new JPanel(new GridLayout(9, 1, 0, 0));
		add(centerPanel, BorderLayout.CENTER);

		addLabel("Date:         " + date);
		addLabel("Time:         " + time);
		addLabel("Departure:    " + departure);
		addLabel("Destination:  " + destination);
		addLabel("Bus Number:   " + busNumber);
		addLabel("Name:         " + customer_name);
		addLabel("Phone Number: " + phone_number);
		addLabel("Price:        " + price);

		printButton = new JButton("Print Receipt");
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		bottomPanel.add(printButton);
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		add(bottomPanel, BorderLayout.SOUTH);

		printButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(ReceiptUI.this, "Printing functionality will be implemented later.");
			}
		});
	}

	private void addLabel(String text) {
		JLabel label = new JLabel(text);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		centerPanel.add(label);
	}

	public static void main(String[] args) {
		// Sample usage
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ReceiptUI receiptUI = new ReceiptUI("Bus123", "City A", "City B", "2024-02-19", "10:00 AM", "Tom",
						"+234234", 250);
				receiptUI.setVisible(true);
			}
		});
	}
}
