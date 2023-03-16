import java.net.*;
import java.io.*;

public class testClient {
    
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int serverPort = 8080;
        
        try {
            Socket socket = new Socket(serverAddress, serverPort);
            System.out.println("Connected to server: " + socket.getInetAddress().getHostAddress());
            
            // Send a message to the server
            OutputStream output = socket.getOutputStream();
            String message = "Hello, server!";
            output.write(message.getBytes());
            
            // Read the server's response
            InputStream input = socket.getInputStream();
            byte[] buffer = new byte[10024];
            int bytesRead = input.read(buffer);
            String response = new String(buffer, 0, bytesRead);
            System.out.println("Server responded: " + response);
            
            // Close the socket when we're done with it
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
