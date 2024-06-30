
/**
 * EvolSys IT Consulting (2024)
 * Project: Password Manager
 * Author: IT-Dev@evolsys
 * Date: 2024-06-30
 * Version: 0.1.08
 * Status: In Build
 * The PasswordManager class is a GUI-based password manager application.
 * It allows users to add and retrieve passwords for different accounts.
 * Password requirements: 8-20 characters, at least one uppercase, 
 * one lowercase letter, one digit/special character.
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Scanner;


public class PasswordManager extends JFrame {
    private HashMap<String, String> passwordMap;
    private JTextField accountField;
    private JPasswordField passwordField;
    private JTextArea displayArea;

    /**
     * Constructs a PasswordManager object.
     * Initializes the passwordMap and loads existing passwords from the file.
     */
    public PasswordManager() {
        passwordMap = new HashMap<>();
        loadPasswords();
    }

    /**
     * Loads existing passwords from the file and populates the passwordMap.
     * If no existing password file is found, a new one will be created.
     */
    private void loadPasswords() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    passwordMap.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("No existing password file found. A new one will be created.");
        }
    }

    /**
     * Saves the passwords in the passwordMap to the file.
     */
    private void savePasswords() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String account : passwordMap.keySet()) {
                bw.write(account + "," + passwordMap.get(account));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new password to the passwordMap and saves it to the file.
     * 
     * @param account  the account name
     * @param password the password
     */
    public void addPassword(String account, String password) {
        passwordMap.put(account, password);
        savePasswords();
    }

    /**
     * Retrieves the password for the specified account from the passwordMap.
     * 
     * @param account the account name
     * @return the password for the account, or null if not found
     */
    public String getPassword(String account) {
        return passwordMap.get(account);
    }

    /**
     * Sets up the GUI components and event listeners.
     */
    private void setupGUI() {
        setTitle("Password Manager");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.add(new JLabel("Account:"));
        accountField = new JTextField();
        panel.add(accountField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton addButton = new JButton("Add Password");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPasswordFromGUI();
            }
        });
        panel.add(addButton);

        JButton retrieveButton = new JButton("Retrieve Password");
        retrieveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                retrievePasswordFromGUI();
            }
        });
        panel.add(retrieveButton);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Adds a password from the GUI input fields to the passwordMap.
     * Displays a success message or an error message if the input is invalid.
     */
    private void addPasswordFromGUI() {
        String account = accountField.getText();
        String password = new String(passwordField.getPassword());
        if (!account.isEmpty() && !password.isEmpty()) {
            addPassword(account, password);
            displayArea.append("Password added for account: " + account + "\n");
            accountField.setText("");
            passwordField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Please enter both account and password.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Retrieves a password from the passwordMap based on the account entered in the GUI.
     * Displays the password or an error message if the account is not found.
     */
    private void retrievePasswordFromGUI() {
        String account = accountField.getText();
        if (!account.isEmpty()) {
            String password = getPassword(account);
            if (password != null) {
                displayArea.append("Password for account " + account + ": " + password + "\n");
            } else {
                displayArea.append("No password found for account: " + account + "\n");
            }
            accountField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Please enter an account name.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Runs the console-based version of the password manager.
     * Allows users to add and retrieve passwords using the command line.
     */
    private void runConsole() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Password Manager:");
            System.out.println("1. Add new password");
            System.out.println("2. Retrieve password");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (option) {
                case 1:
                    System.out.print("Enter account name: ");
                    String account = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    addPassword(account, password);
                    System.out.println("Password added successfully.");
                    break;
                case 2:
                    System.out.print("Enter account name: ");
                    account = scanner.nextLine();
                    String retrievedPassword = getPassword(account);
                    if (retrievedPassword != null) {
                        System.out.println("Password for " + account + ": " + retrievedPassword);
                    } else {
                        System.out.println("No password found for that account.");
                    }
                    break;
                case 3:
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /**
     * The main method of the PasswordManager application.
     * Creates an instance of PasswordManager and either runs the GUI or console version based on the command line argument.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PasswordManager pm = new PasswordManager();
        if (args.length > 0 && args[0].equals("console")) {
            pm.runConsole();
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    pm.setupGUI();
                    pm.setVisible(true);
                }
            });
        }
    }
}
