// Peter Idestam-Almquist, 2021-03-07.
// Server, multi-threaded, accepting several simultaneous clients.

package pack;

import java.net.Socket;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import java.util.concurrent.ConcurrentHashMap;

class ChatServer implements Runnable {
	private final static int PORT = 8000;
	private final static int MAX_CLIENTS = 5;
	private final static Executor executor = Executors.newFixedThreadPool(MAX_CLIENTS);
	private final static ConcurrentHashMap<Socket, String> map = new ConcurrentHashMap<>();
	
	private final Socket clientSocket;
	private String clientName = "";
	
	private ChatServer(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
	
	public void run() {
		SocketAddress remoteSocketAddress = clientSocket.getRemoteSocketAddress();
		SocketAddress localSocketAddress = clientSocket.getLocalSocketAddress();
		System.out.println("Accepted client " + remoteSocketAddress 
			+ " (" + localSocketAddress + ").");
	
		PrintWriter socketWriter = null;
		BufferedReader socketReader = null;
		try {
			socketWriter = new PrintWriter(clientSocket.getOutputStream(), true);                   
            socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			String threadInfo = " (" + Thread.currentThread().getName() + ").";
            String inputLine = socketReader.readLine();
			System.out.println("Received: \"" + inputLine + "\" from " 
				+ remoteSocketAddress + threadInfo);
				
			// First message is client name.
			clientName = inputLine;

			map.put(clientSocket, clientName);
			
            while (inputLine != null) {
				// socketWriter.println("client name: " + clientName + " input: " + inputLine);
				System.out.println("Sent: \"" + inputLine + "\" to " 
					+ clientName + " " + remoteSocketAddress + clientName + "\n");

				for (ConcurrentHashMap.Entry<Socket, String> entry : map.entrySet()) {


					new PrintWriter(entry.getKey().getOutputStream(), true).println( clientName + " : " + inputLine);
				}

				inputLine = socketReader.readLine();
				System.out.println("Received: \"" + inputLine + "\" from " 
					+ clientName + " " + remoteSocketAddress + threadInfo);
            }
			System.out.println("Closing connection " + remoteSocketAddress 
				+ " (" + localSocketAddress + ").");
		}
		catch (Exception exception) {
            System.out.println(exception);
        }
		finally {
			try {
				if (socketWriter != null)
					socketWriter.close();
				if (socketReader != null)
					socketReader.close();
				if (clientSocket != null)
					clientSocket.close();
			}
			catch (Exception exception) {
				System.out.println(exception);
			}
		}
	}

    public static void main(String[] args) {
		System.out.println("ChatServer started.");

		ServerSocket serverSocket = null;
		Socket clientSocket = null;
		try {
            serverSocket = new ServerSocket(PORT);
			SocketAddress serverSocketAddress = serverSocket.getLocalSocketAddress();
			System.out.println("Listening (" + serverSocketAddress + ").");
            
			while (true) {
				clientSocket = serverSocket.accept();     
				executor.execute(new ChatServer(clientSocket));
			}
        } 
		catch (Exception exception) {
            System.out.println(exception);
        }
		finally {
			try {
				if (serverSocket != null)
					serverSocket.close();
			}
			catch (Exception exception) {
				System.out.println(exception);
			}
		}
    }
}
