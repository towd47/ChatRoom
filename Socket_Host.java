import java.io.*;
import java.io.BufferedReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Socket_Host {

	static ArrayList<Socket> clientList = new ArrayList<Socket>();

	public static void socket_host() {
		ServerSocket socket = null;
		Socket conn = null;
		PrintStream out = null;
		int port = 5000;

		
		try {
			socket = new ServerSocket(port, 10);
			
			System.out.println("Server opened on port " + port + ". Waiting for connections...");
			
		}
		
		catch(IOException e) {
			System.err.println("IOException");
		}
		
		while(true) {
			try {
				conn = socket.accept();
				System.out.println("Connection received from " + conn.getInetAddress().getHostName() + " : " + conn.getPort());
				out = new PrintStream(conn.getOutputStream());
				out.flush();
				out.println("Welcome to our chat server.");
				out.flush();

			} catch(IOException e) {
				e.printStackTrace();
			}

			new HostThread(conn).start();
			clientList.add(conn);
		}
	}
	
	public static ArrayList<Socket> getClients() {
		return clientList;
	}
}
