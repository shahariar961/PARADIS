import java.net.*;
import java.io.*;

public class testServer {
    
    private ServerSocket serverSocket;
    
    public testServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void start() {
        System.out.println("Server is listening on port " + serverSocket.getLocalPort());
        
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket.getInetAddress().getHostAddress());
                
                // Spawn a new thread to handle communication with the client
                Thread thread = new Thread(new SocketHandler(socket));
                thread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private class SocketHandler implements Runnable {
        
        private Socket socket;
        
        public SocketHandler(Socket socket) {
            this.socket = socket;
        }
        
        public void run() {
            try {
                // Use the socket's input and output streams to communicate with the client
                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();
                PrintWriter send = new PrintWriter(output,true);
                send.println("welcome");
                // TODO: Handle communication with the client here
                
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    // Close the socket when we're done with it
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static void main(String[] args) {
        testServer server = new testServer(8080);
        server.start();
    }
}
