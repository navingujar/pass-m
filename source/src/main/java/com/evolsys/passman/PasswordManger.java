import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class PasswordManager extends JFrame {
    private static final String FILE_NAME = "passwords.txt";
    private HashMap<String, String> passwordMap;
    private JTextField accountField;
    private JPasswordField passwordField;
    private JTextArea displayArea;

    public PasswordManager() {
        passwordMap = new HashMap<>();
        loadPasswords();
    }

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

    public void addPassword(String account, String password) {
        passwordMap.put(account, password);
        savePasswords();
    }

    public String getPassword(String account) {
        return passwordMap.get(account);
    }

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
