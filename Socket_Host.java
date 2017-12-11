import java.io.*;
import java.io.BufferedReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Socket_Host {

	private ArrayList<Socket> clientList = new ArrayList<Socket>();
	private ArrayList<ChatRoom> chatRooms = new ArrayList<ChatRoom>();

	private int port;

	public Socket_Host(int port) {
		this.port = port;
	}

	public void run() {
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
			HostThread hostThread = new HostThread(conn, this); 
			hostThread.start();

			// Creates a Thread that waits for the hostThread to join as a sign the client disconnected
			new ClientConnectionWatcher(hostThread, conn).start();
			clientList.add(conn);
		}
	}
	
	public ArrayList<Socket> getClients() {
		return clientList;
	}

	public ArrayList<ChatRoom> getRooms() {
		return chatRooms;
	}

	public void addChatRoom(ChatRoom room) {
		chatRooms.add(room);
	}

	public boolean removeRoom(ChatRoom room) {
		return chatRooms.remove(room);
	}
}
