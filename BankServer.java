import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BankServer {
    private static final int PORT = 9999;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bank";

    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "HA1256ri#";
    private static Connection connection;

    public static void main(String[] args) {
        try {
            // Establish database connection
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Connected to the database.");

            // Start the server
            startServer();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Bank server started. Listening on port " + PORT);

            while (true) {
                // Accept incoming client connections
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                // Create a new thread to handle client's requests
                ClientHandler clientHandler = new ClientHandler(clientSocket, connection);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
