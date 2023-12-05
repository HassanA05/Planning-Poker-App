import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PlanningPokerFunctionality {
     //Enter your desired Directory below eg C:/PlanningPokerApp/NamesAndChoicesStorage
    private static final String BASEPATH = "";
    // Fields to manage flag status and file paths
    private volatile boolean revealFlag = false;
    private Thread flagPollingThread;


    // File paths for data storage and flag management
    File baseDirectory = new File(BASEPATH);
    File revealFilePath = new File(BASEPATH + "reveal.flg");

    // Method to start the flag polling thread
    public void startFlagPollingThread(JTable dataTable) {
        // Initiates a new thread to continuously check the reveal flag status and handle table visibility
        flagPollingThread = new Thread(() -> {
            while (true) {
                checkRevealFlag(); // Checks the status of the reveal flag
                handleTableVisibility(dataTable); // Manages the visibility of the table based on the flag status
                try {
                    Thread.sleep(1000); // Sleeps for a second before re checking
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        flagPollingThread.start(); // Starts the thread
    }

    // Method to check the status of the reveal flag
    private void checkRevealFlag() {
        // Checks if the reveal flag file exists, setting the revealFlag variable accordingly
        revealFlag = revealFilePath.exists();
    }

    // Method to stop the flag polling thread
    public void stopFlagPollingThread() {
        // Checks if the thread is running and interrupts it
        if (flagPollingThread != null && flagPollingThread.isAlive()) {
            flagPollingThread.interrupt();
        }
    }

    // Method to create the reveal flag file
    public void createRevealFlag() {
        // Attempts to create a reveal flag file and logs whether it was successful or not
        try {
            if (revealFilePath.createNewFile()) {
                System.out.println("Flag file created: reveal.flg");
            } else {
                System.out.println("Flag file already exists");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to delete all files and flags
    public void deleteAllFilesAndFlags() {
        // Deletes all files in the specified directory
        List<File> files = getArrayOfFiles();
        for (File file : files) {
            if (file.delete()) {
                System.out.println("Deleted file: " + file.getName());
            } else {
                System.err.println("Failed to delete file: " + file.getName());
            }
        }
    }

    // Method to handle the visibility of the data table
    private void handleTableVisibility(JTable dataTable) {
        // Checks the reveal flag status and manages the visibility of the data table accordingly
        if (dataTable != null) {
            checkRevealFlag(); // Checks the reveal flag status
            DefaultTableModel tableModel = (DefaultTableModel) dataTable.getModel(); // Obtains the table model

            SwingUtilities.invokeLater(() -> {
                if (!revealFlag && dataTable.isVisible()) { // Hides the table if the reveal flag is not set
                    tableModel.setRowCount(0); // Clears the table content
                    dataTable.setVisible(false); // Hides the table
                } else if (revealFlag && !dataTable.isVisible()) { // Shows the table if the reveal flag is set
                    populateAndDisplayTable(tableModel); // Populates and displays the table
                    dataTable.setVisible(true); // Shows the table
                    revealFlag = false; // Resets the flag to prevent continuous display
                }
            });
        }
    }

    // Method to populate and display the data table
    private void populateAndDisplayTable(DefaultTableModel tableModel) {
        // Reads files in the specified directory and adds their content to the table

        List<File> files = getArrayOfFiles();
        for (File file : files) {
            if (file.getName().endsWith(".txt")) {
                String fileName = file.getName();
                String name = fileName.substring(0, fileName.lastIndexOf("."));
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String choice = br.readLine();
                    tableModel.addRow(new Object[] { name, choice }); // Adds a row to the table
                } catch (IOException exception) {
                    System.err.println("Error reading file: " + exception.getMessage());
                }
            }
        }
    }

    private List<File> getArrayOfFiles() {

        List<File> filteredFiles = new ArrayList<File>();
        File[] listOfFiles = baseDirectory.listFiles();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    filteredFiles.add(file);
                }
            }
        }
        return filteredFiles;
    }

    // Method to save user input to a file
    public void inputToFile(String name, String choice) {
        // Saves the user's name and choice to a file
        File file = new File(BASEPATH + name + ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(choice);
            writer.close(); // Closes the writer
            System.out.println("Your Name: " + name + " and Choice: " + choice + " saved.");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
