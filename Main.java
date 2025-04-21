import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

class BankAccountManagementSystem extends JFrame {
    private static final String FILE_NAME = "bank_account.dat";
    private BankAccount account;

    private final JTextField depositField;
    private final JTextField withdrawField;
    private final JTextArea outputArea;

    public BankAccountManagementSystem() {
        setTitle("Bank Account Management System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        account = loadAccount();
        if (account == null) {
            String name = JOptionPane.showInputDialog(this, "Enter the account holder's name:");
            account = new BankAccount(name);
        }

        depositField = new JTextField(10);
        withdrawField = new JTextField(10);
        outputArea = new JTextArea(10, 30);
        outputArea.setEditable(false);

        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        JButton checkBalanceButton = new JButton("Check Balance");
        JButton exitButton = new JButton("Exit");

        depositButton.addActionListener(new DepositListener());
        withdrawButton.addActionListener(new WithdrawListener());
        checkBalanceButton.addActionListener(new CheckBalanceListener());
        exitButton.addActionListener(e -> exitApplication());

        add(new JLabel("Deposit Amount:"));
        add(depositField);
        add(depositButton);

        add(new JLabel("Withdraw Amount:"));
        add(withdrawField);
        add(withdrawButton);

        add(checkBalanceButton);
        add(new JScrollPane(outputArea));
        add(exitButton);
    }

    private class DepositListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                double amount = Double.parseDouble(depositField.getText());
                account.deposit(amount);
                saveAccount(account);
                outputArea.append("Successfully deposited: " + amount + "\n");
                depositField.setText("");
            } catch (NumberFormatException ex) {
                outputArea.append("Invalid deposit amount!\n");
            }
        }
    }

    private class WithdrawListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                double amount = Double.parseDouble(withdrawField.getText());
                account.withdraw(amount);
                saveAccount(account);
                outputArea.append("Successfully withdrew: " + amount + "\n");
                withdrawField.setText("");
            } catch (NumberFormatException ex) {
                outputArea.append("Invalid withdrawal amount!\n");
            }
        }
    }

    private class CheckBalanceListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            outputArea.append("Current balance: " + account.getBalance() + "\n");
        }
    }

    private void exitApplication() {
        saveAccount(account);
        System.exit(0);
    }

    private void saveAccount(BankAccount account) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(account);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving account data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private BankAccount loadAccount() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (BankAccount) ois.readObject();
        } catch (FileNotFoundException e) {
            return null; // No previous account exists
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error loading account data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BankAccountManagementSystem frame = new BankAccountManagementSystem();
            frame.setVisible(true);
        });
    }
}

