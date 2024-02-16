package Resources;

import java.awt.*;
import javax.swing.*;
//import com.toedter.calendar.JDateChooser;

public class ChooseDestination extends JFrame {
	public ChooseDestination() {
		setTitle("Destination and Date Selector");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 200);
		setLocationRelativeTo(null);

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(5, 5, 5, 5);

		JLabel fromLabel = new JLabel("From:");
		constraints.gridx = 0;
		constraints.gridy = 0;
		panel.add(fromLabel, constraints);

		String[] destinations = { "Destination 1", "Destination 2", "Destination 3" };
		JComboBox<String> fromComboBox = new JComboBox<>(destinations);
		constraints.gridx = 1;
		constraints.gridy = 0;
		panel.add(fromComboBox, constraints);

		JLabel toLabel = new JLabel("To:");
		constraints.gridx = 0;
		constraints.gridy = 1;
		panel.add(toLabel, constraints);

		JComboBox<String> toComboBox = new JComboBox<>(destinations);
		constraints.gridx = 1;
		constraints.gridy = 1;
		panel.add(toComboBox, constraints);

		JLabel dateLabel = new JLabel("Date:");
		constraints.gridx = 0;
		constraints.gridy = 2;
		panel.add(dateLabel, constraints);

//		JDateChooser dateChooser = new JDateChooser();
//		constraints.gridx = 1;
//		constraints.gridy = 2;
//		panel.add(dateChooser, constraints);

		add(panel);
	}
}
