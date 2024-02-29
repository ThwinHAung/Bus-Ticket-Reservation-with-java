package Resources;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import Db.DatabaseConnection;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BusTicketDashboard extends JFrame implements ActionListener {

    private JPanel panel;
    private JLabel title, totalTickets, totalRevenue;
    private JTable table;
    private JButton book, addNewBus;
    private Connection con;

    public BusTicketDashboard() {
        super("Bus Ticket Dashboard");
        setSize(800, 600);
        setLocation(100, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.LIGHT_GRAY);
        getContentPane().add(panel);

        title = new JLabel("Bus Ticket Dashboard");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(Color.BLUE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBounds(200, 20, 400, 50);
        panel.add(title);

        totalTickets = new JLabel("Total Booked Tickets: 0");
        totalTickets.setFont(new Font("Arial", Font.PLAIN, 20));
        totalTickets.setForeground(Color.BLACK);
        totalTickets.setHorizontalAlignment(SwingConstants.LEFT);
        totalTickets.setBounds(50, 100, 300, 30);
        panel.add(totalTickets);

        totalRevenue = new JLabel("Total Revenue: 0.0");
        totalRevenue.setFont(new Font("Arial", Font.PLAIN, 20));
        totalRevenue.setForeground(Color.BLACK);
        totalRevenue.setHorizontalAlignment(SwingConstants.LEFT);
        totalRevenue.setBounds(50, 150, 300, 30);
        panel.add(totalRevenue);

        table = new JTable();
        table.setModel(new DefaultTableModel(new Object[][] {},
                new String[] { "Ticket ID", "Customer Name", "Date", "Departure Time", "Departure Place",
                        "Destination Place", "Phone Number", "Seat Number", "Price" }));

        table.setRowHeight(25);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(150);
        table.getColumnModel().getColumn(5).setPreferredWidth(150);
        table.getColumnModel().getColumn(6).setPreferredWidth(150);
        table.getColumnModel().getColumn(7).setPreferredWidth(100);
        table.getColumnModel().getColumn(8).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 200, 700, 300);
        panel.add(scrollPane);

        book = new JButton("Book Ticket");
        book.setFont(new Font("Arial", Font.PLAIN, 20));
        book.setForeground(Color.BLACK);
        book.setBounds(450, 100, 150, 30);
        book.addActionListener(this);
        panel.add(book);

        addNewBus = new JButton("Add New Bus");
        addNewBus.setFont(new Font("Arial", Font.PLAIN, 20));
        addNewBus.setForeground(Color.BLACK);
        addNewBus.setBounds(450, 150, 150, 30);
        addNewBus.addActionListener(this);
        panel.add(addNewBus);

        // Database connection setup
        try {
            con = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        fetchAndUpdateData();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == book) {
            new ChooseDestination();
            dispose();
        } else if (e.getSource() == addNewBus) {
            new adding_bus();
            dispose();
        }
    }

    void fetchAndUpdateData() {
        try {

            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            PreparedStatement pstmt = con.prepareStatement(
                    "SELECT COUNT(id), SUM(ticket_price) FROM Ticket WHERE date = ?");
            pstmt.setString(1, currentDate);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int totalTicketsCount = rs.getInt(1);
                double totalRevenueAmount = rs.getDouble(2);
                totalTickets.setText("Total Booked Tickets for Today: " + totalTicketsCount);
                totalRevenue.setText("Total Revenue for Today: " + totalRevenueAmount);
            }
            rs.close();
            pstmt.close();

            // Update the table
            pstmt = con.prepareStatement(
                    "SELECT t.id, t.passenger_name, t.date, b.departure_time, b.departure_place, b.destination_place, t.phone_number, t.seatNumber, t.ticket_price FROM Ticket t INNER JOIN Bus b ON t.bus_id = b.id WHERE t.date >= ?");
            pstmt.setString(1, currentDate);
            rs = pstmt.executeQuery();

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0); // Clear existing rows

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("passenger_name");
                String date = rs.getString("date");
                String departureTime = rs.getString("departure_time");
                String departurePlace = rs.getString("departure_place");
                String destinationPlace = rs.getString("destination_place");
                String phoneNumber = rs.getString("phone_number");
                String seatNumber = rs.getString("seatNumber");
                double price = rs.getDouble("ticket_price");

                model.addRow(new Object[] { id, name, date, departureTime, departurePlace, destinationPlace,
                        phoneNumber, seatNumber, price });
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


//    public static void main(String[] args) {
//        try {
//            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        new BusTicketDashboard().setVisible(true);
//    }
}
