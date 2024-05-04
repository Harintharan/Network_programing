// ClientHandler.java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private Connection connection;

    public ClientHandler(Socket clientSocket, Connection connection) {
        this.clientSocket = clientSocket;
        this.connection = connection;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        ) {
            // Handle client's requests
            String request;
            while ((request = in.readLine()) != null) {
                // Process client's request
                String response = processRequest(request);
                // Send response back to client
                out.println(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // Close client socket
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to process client's request
    private String processRequest(String request) {
        String[] tokens = request.split("\\s+");
        String command = tokens[0];
        String response = "";

        try {
            if (tokens.length < 2) {
                response = "Invalid request: Missing argument(s)";
            } else {
                switch (command) {
                    case "create_account":
                        if (tokens.length < 2) {
                            response = "Invalid request: Missing account number";
                        } else {
                            createAccount(tokens[1]);
                            response = "Account created successfully!";
                        }
                        break;
                    case "deposit":
                        if (tokens.length < 3) {
                            response = "Invalid request: Missing account number or amount";
                        } else {
                            deposit(tokens[1], Double.parseDouble(tokens[2]));
                            response = "Deposit successful!";
                        }
                        break;
                    case "withdraw":
                        if (tokens.length < 3) {
                            response = "Invalid request: Missing account number or amount";
                        } else {
                            withdraw(tokens[1], Double.parseDouble(tokens[2]));
                            response = "Withdrawal successful!";
                        }
                        break;
                    case "check_balance":
                        if (tokens.length < 2) {
                            response = "Invalid request: Missing account number";
                        } else {
                            double balance = checkBalance(tokens[1]);
                            response = "Balance: $" + balance;
                        }
                        break;
                    default:
                        response = "Invalid command!";
                }
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            response = "Error processing request!";
        }

        return response;
    }

    // Method to create account
    private void createAccount(String accountNumber) throws SQLException {
        String sql = "INSERT INTO accounts (account_number, balance) VALUES (?, 0)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNumber);
            statement.executeUpdate();
        }
    }

    // Method to deposit
    private void deposit(String accountNumber, double amount) throws SQLException {
        String sql = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, amount);
            statement.setString(2, accountNumber);
            statement.executeUpdate();
        }
    }

    // Method to withdraw
    private void withdraw(String accountNumber, double amount) throws SQLException {
        String sql = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, amount);
            statement.setString(2, accountNumber);
            statement.executeUpdate();
        }
    }

    // Method to check balance
    private double checkBalance(String accountNumber) throws SQLException {
        String sql = "SELECT balance FROM accounts WHERE account_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("balance");
            }
        }
        return 0.0; // Return 0 if account not found
    }
}
