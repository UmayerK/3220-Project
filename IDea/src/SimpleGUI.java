import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * A simple GUI application that demonstrates the use of JDBC to insert and retrieve data from a SQLite database.
 * It features a main window with two tabs: one for user input and another for displaying data from the database.
 */
public class SimpleGUI {
    /** Database connection URL. */
    static final String DB_URL = "jdbc:sqlite:C:/Users/umaye/IdeaProjects/IDea/DB/identifier.sqlite";

    /**
     * The main method that sets up the GUI and database connection.
     * @param args Command line arguments (not used).
     */
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

        // Panel for Input tab
        createInputPanel(frame, tabbedPane);

        // Panel for Data Log tab
        createDataLogPanel(frame, tabbedPane);

        // Add the tabbed pane to the frame and set visibility
        frame.add(tabbedPane);
        frame.setVisible(true);
    }

    /**
     * Creates the input panel with a text field and a submit button. User input is inserted into a SQLite database.
     * @param frame The main application frame for displaying dialog messages.
     * @param tabbedPane The tabbed pane to which the input panel is added.
     */
    private static void createInputPanel(JFrame frame, JTabbedPane tabbedPane) {
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(Color.BLACK); // Set background color to black

        // Label for input
        JLabel infoLabel = new JLabel("Input Your Information");
        infoLabel.setForeground(Color.WHITE); // Set text color to white
        infoLabel.setFont(new Font(infoLabel.getFont().getName(), Font.BOLD, 20)); // Set font
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center align

        inputPanel.add(infoLabel, BorderLayout.NORTH);

        // Text field for input
        JTextField inputField = new JTextField();
        inputField.setPreferredSize(new Dimension(350, 50)); // Set preferred size
        inputField.setHorizontalAlignment(JTextField.CENTER); // Center align
        inputField.setFont(new Font("Arial", Font.PLAIN, 18)); // Set font

        // Panel for text field
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setBackground(Color.BLACK);
        centerPanel.add(inputField);
        inputPanel.add(centerPanel, BorderLayout.CENTER);

        // Submit button for input
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 18)); // Set font
        inputPanel.add(submitButton, BorderLayout.SOUTH); // Add button

        // Action listener for submit button
        submitButton.addActionListener(e -> submitAction(frame, inputField));

        // Add input panel to tabbed pane
        tabbedPane.addTab("Input", inputPanel);
    }

    /**
     * Action performed by submit button: inserts user input into database and shows a success or error message.
     * @param frame The main application frame for displaying dialog messages.
     * @param inputField The text field containing user input.
     */
    private static void submitAction(JFrame frame, JTextField inputField) {
        String userInput = inputField.getText();

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

    /**
     * Creates the data log panel with a button for importing data from the SQLite database and displaying it.
     * @param frame The main application frame for displaying dialog messages.
     * @param tabbedPane The tabbed pane to which the data log panel is added.
     */
    private static void createDataLogPanel(JFrame frame, JTabbedPane tabbedPane) {
        JPanel dataLogPanel = new JPanel(new BorderLayout());
        dataLogPanel.setBackground(Color.BLACK); // Set background color

        JButton importButton = new JButton("Import");
        importButton.setFont(new Font("Arial", Font.BOLD, 18)); // Set font

        // Action listener for import button
        importButton.addActionListener(e -> importAction(frame));

        // Panel for import button
        JPanel importPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        importPanel.add(importButton);
        dataLogPanel.add(importPanel, BorderLayout.CENTER);

        // Add data log panel to tabbed pane
        tabbedPane.addTab("Data Log", dataLogPanel);
    }

    /**
     * Action performed by import button: retrieves data from database and displays it in a dialog.
     * @param frame The main application frame for displaying the dialog.
     */
    private static void importAction(JFrame frame) {
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
}
