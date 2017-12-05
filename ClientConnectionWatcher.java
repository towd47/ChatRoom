import java.io.*;
import java.net.*;

public class ClientConnectionWatcher extends Thread {
	private HostThread hostThread;
	private Socket socket;

	public ClientConnectionWatcher(HostThread hostThread, Socket socket) {
		this.hostThread = hostThread;
		this.socket = socket;
	}

	// Waits for a client to disconnect then prints a message to the server terminal
	public void run() {
		try {
			hostThread.join();
		}
		catch (InterruptedException e) {
			return;
		}
		System.out.println(socket.getInetAddress().getHostName() + " : " + socket.getPort() + " has disconnected.");
		return;
	}

}