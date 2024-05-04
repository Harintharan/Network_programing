// BankClient.java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BankClient {
    private static final String SERVER_IP = "localhost"; // Server IP address
    private static final int SERVER_PORT = 9999; // Server port number
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public BankClient() {
        try {
            // Connect to the server
            socket = new Socket(SERVER_IP, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String sendRequest(String request) {
        out.println(request);
        try {
            // Receive response from the server
            String response = in.readLine();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return "Error processing request!";
        }
    }

    public void close() {
        try {
            socket.close();
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
