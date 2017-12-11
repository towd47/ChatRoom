import java.io.*;
import java.io.BufferedReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Socket_Host {

	static ArrayList<Socket> clientList = new ArrayList<Socket>();
	static ArrayList<ChatRoom> chatRooms = new ArrayList<ChatRoom>();

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

			} catch(IOException e) {
				e.printStackTrace();
			}

			// Creates a HostThread to manage each connection
			HostThread hostThread = new HostThread(conn); 
			hostThread.start();

			// Creates a Thread that waits for the hostThread to join as a sign the client disconnected
			new ClientConnectionWatcher(hostThread, conn).start();
			clientList.add(conn);
		}
	}
	
	public static ArrayList<Socket> getClients() {
		return clientList;
	}

	public static ArrayList<ChatRoom> getRooms() {
		return chatRooms;
	}

	public static void addChatRoom(ChatRoom room) {
		chatRooms.add(room);
	}

	public static boolean removeRoom(ChatRoom room) {
		return chatRooms.remove(room);
	}
}
