import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class SimpleGUI {
    // Database connection URL
    static final String DB_URL = "jdbc:sqlite:C:/Users/umaye/IdeaProjects/IDea/DB/identifier.sqlite";

    public static void main(String[] args) {
        // Load the SQLite JDBC driver
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        // Create the main frame
        JFrame frame = new JFrame("Simple GUI Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 400);

        // Create a tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Create panel for Input tab
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(Color.BLACK); // Set background color to black

        // Create label "Input Your Information"
        JLabel infoLabel = new JLabel("Input Your Information");
        infoLabel.setForeground(Color.WHITE); // Set text color to white
        infoLabel.setFont(new Font(infoLabel.getFont().getName(), Font.BOLD, 20)); // Set font to bold and increase font size
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center align the text horizontally

        // Add label to the top using BorderLayout.NORTH
        inputPanel.add(infoLabel, BorderLayout.NORTH);

        // Create text field
        JTextField inputField = new JTextField();
        inputField.setPreferredSize(new Dimension(350, 50)); // Set preferred size
        inputField.setHorizontalAlignment(JTextField.CENTER); // Center align the text field
        inputField.setFont(new Font("Arial", Font.PLAIN, 18)); // Set font size and style

        // Add text field to the center using FlowLayout
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setBackground(Color.BLACK);
        centerPanel.add(inputField);
        inputPanel.add(centerPanel, BorderLayout.CENTER);

        // Create submit button
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 18)); // Set font size and style
        inputPanel.add(submitButton, BorderLayout.SOUTH); // Add button underneath text field

        // Add action listener to submit button
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get text from the input field
                String userInput = inputField.getText();

                // Insert data into SQLite database
                try (Connection conn = DriverManager.getConnection(DB_URL)) {
                    String sql = "INSERT INTO your_table_name (information) VALUES (?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, userInput);
                        pstmt.executeUpdate();
                    }
                    JOptionPane.showMessageDialog(frame, "Data inserted successfully into SQLite database.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Create panel for Data Log tab
        JPanel dataLogPanel = new JPanel(new BorderLayout());
        dataLogPanel.setBackground(Color.BLACK); // Set background color to black

        // Create button for importing data
        JButton importButton = new JButton("Import");
        importButton.setFont(new Font("Arial", Font.BOLD, 18)); // Set font size and style

        // Add action listener to import button
        importButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Retrieve and display all rows from the SQLite table
                try (Connection conn = DriverManager.getConnection(DB_URL)) {
                    String sql = "SELECT * FROM your_table_name";
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        ResultSet rs = pstmt.executeQuery();
                        StringBuilder tableContent = new StringBuilder();
                        tableContent.append("ID\tInformation\n");
                        while (rs.next()) {
                            int id = rs.getInt("id");
                            String information = rs.getString("information");
                            tableContent.append(id).append("\t").append(information).append("\n");
                        }
                        JOptionPane.showMessageDialog(frame, new JScrollPane(new JTextArea(tableContent.toString())), "Table Content", JOptionPane.PLAIN_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add import button to the center using FlowLayout
        JPanel importPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        importPanel.add(importButton);
        dataLogPanel.add(importPanel, BorderLayout.CENTER);

        // Add panels to the tabbed pane with tab titles
        tabbedPane.addTab("Input", inputPanel);
        tabbedPane.addTab("Data Log", dataLogPanel);

        // Add the tabbed pane to the frame
        frame.add(tabbedPane);

        // Set frame visibility
        frame.setVisible(true);
    }
}
