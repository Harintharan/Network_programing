// BankApplication.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BankApplication extends JFrame implements ActionListener {
    private BankClient bankClient;
    private JTextArea outputArea;

    public BankApplication() {
        // Set up the frame
        setTitle("Bank Application");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create bank client instance
        bankClient = new BankClient();

        // Create buttons for options
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1));
        JButton createAccountButton = new JButton("Create Account");
        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        JButton checkBalanceButton = new JButton("Check Balance");
        createAccountButton.addActionListener(this);
        depositButton.addActionListener(this);
        withdrawButton.addActionListener(this);
        checkBalanceButton.addActionListener(this);
        buttonPanel.add(createAccountButton);
        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawButton);
        buttonPanel.add(checkBalanceButton);

        // Create output area
        outputArea = new JTextArea(10, 30);
        outputArea.setEditable(false);

        // Add components to the frame
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(buttonPanel, BorderLayout.WEST);
        contentPane.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        String request = "";
        switch (command) {
            case "Create Account":
                String accountNumber = JOptionPane.showInputDialog("Enter account number:");
                request = "create_account " + accountNumber;
                break;
            case "Deposit":
                String depositInfo = JOptionPane.showInputDialog("Enter account number and amount separated by space:");
                String[] depositTokens = depositInfo.split("\\s+");
                request = "deposit " + depositTokens[0] + " " + depositTokens[1];
                break;
            case "Withdraw":
                String withdrawInfo = JOptionPane.showInputDialog("Enter account number and amount separated by space:");
                String[] withdrawTokens = withdrawInfo.split("\\s+");
                request = "withdraw " + withdrawTokens[0] + " " + withdrawTokens[1];
                break;
            case "Check Balance":
                String accountNum = JOptionPane.showInputDialog("Enter account number:");
                request = "check_balance " + accountNum;
                break;
        }
        String response = bankClient.sendRequest(request);
        outputArea.append(response + "\n");
    }

    public static void main(String[] args) {
        // Create and show the GUI
        SwingUtilities.invokeLater(() -> new BankApplication());
    }
}
