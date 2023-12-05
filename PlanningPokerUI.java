import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import info.clearthought.layout.TableLayout;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PlanningPokerUI {
    private JFrame frame;
    private JTextField nameField = new JTextField(20);
    private DefaultTableModel tableModel = new DefaultTableModel();
    private JTable dataTable = new JTable(tableModel);
    private JButton revealButton = new JButton("Reveal");
    private JButton resetButton = new JButton("Reset");
    private JButton choiceButton;

    private PlanningPokerFunctionality functionality = new PlanningPokerFunctionality();

    public PlanningPokerUI() {
        frame = new JFrame("Planning Poker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the main panels
        JPanel panel1 = inputsPanel(); // Top panel with user inputs
        JPanel panel2 = tablePanel(); // Middle panel with data table
        JPanel panel3 = resetRevealPanel(); // Bottom panel with Reset and Reveal buttons

        revealButton.setEnabled(false);// Initially disabling the "Reveal" button

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(panel1, BorderLayout.NORTH);
        frame.getContentPane().add(panel2, BorderLayout.CENTER);
        frame.getContentPane().add(panel3, BorderLayout.SOUTH);
        frame.setSize(1400, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        functionality.startFlagPollingThread(dataTable);// Starting the flag polling thread to manage data visibility

        // Adding window listener to handle actions on closing the application
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                functionality.stopFlagPollingThread();
            }
        });

    }

    private JPanel inputsPanel() {
        JPanel panel = new JPanel();

        double size[][] = {
                { TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED, TableLayout.PREFERRED,
                        TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED,
                        TableLayout.PREFERRED },
                { TableLayout.PREFERRED }
        };

        TableLayout layout = new TableLayout(size);
        layout.setHGap(5);

        panel.setLayout(layout);

        panel.add(new JLabel("Enter your name:"), "0, 0");
        panel.add(nameField, "1, 0");

        nameField.addActionListener(e -> {
            String enteredName = nameField.getText().trim();

            if (enteredName.matches("^[a-zA-Z]+$")) {
                // Valid name: contains only letters
                for (Component component : panel.getComponents()) {
                    if (component instanceof JButton) {
                        component.setEnabled(true);
                    }
                }
                nameField.setEnabled(false);
            } else {
                // Invalid name: contains numbers or other characters
                JOptionPane.showMessageDialog(frame, "Please enter a valid name with only letters.", "Invalid Name",
                        JOptionPane.ERROR_MESSAGE);
                nameField.setText(""); // Clear the invalid input
            }
        });

        int[] options = { 1, 2, 3, 5, 8, 13, 20 };

        ActionListener voteListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add functionality for choice button
                String vote = e.getActionCommand();
                String name = nameField.getText();
                revealButton.setEnabled(true);
                functionality.inputToFile(name, vote);
            }
        };

        for (int i = 0; i < options.length; i++) {
            choiceButton = new JButton(String.valueOf(options[i]));
            choiceButton.setEnabled(false);
            choiceButton.setActionCommand(String.valueOf(options[i]));
            choiceButton.addActionListener(voteListener); // Use the same listener for all choice buttons
            panel.add(choiceButton, (i + 2) + ", 0");

        }

        // Setting borders and returning the panel
        Border border = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        panel.setBorder(border);

        return panel;
    }

    private JPanel tablePanel() {
        JPanel panel = new JPanel();

        double size[][] = {
                { TableLayout.FILL },
                { TableLayout.FILL }
        };

        TableLayout layout = new TableLayout(size);
        // Set up layout and components for the data table
        panel.setLayout(layout);

        tableModel.addColumn("Name");
        tableModel.addColumn("Choice");
        dataTable.setVisible(false);

        JScrollPane tableScrollPane = new JScrollPane(dataTable);
        panel.add(tableScrollPane, "0,0");

        return panel;
    }

    // Panel for Reset and Reveal buttons
    private JPanel resetRevealPanel() {
        JPanel panel = new JPanel();

        // Set up layout and components for the button panel
        panel.add(revealButton, "0,0");
        panel.add(resetButton, "1,0");

        // Create the reset button
        resetButton.addActionListener(e -> functionality.deleteAllFilesAndFlags());

        // Create the Reveal button
        revealButton.addActionListener(e -> functionality.createRevealFlag());

        return panel;
    }
}