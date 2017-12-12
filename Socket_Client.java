import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Socket_Client {
	String host;
	int port;

	public Socket_Client(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	//Connects the client with the server socket
	public boolean startClient() {
		Socket socket = null;

		PrintWriter s_out = null;
		BufferedReader s_in = null;
		
		try {
			socket = new Socket(host, port);
			System.out.println("Connected");
			
			s_out = new PrintWriter(socket.getOutputStream(), true);
			s_in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
		
		catch(IOException e) {
			System.err.println("Unable to find existing server to connect to.");
			return false;
		}

		// Starts two threads, one for handeling all client input, and one for output
		ClientInputThread inputThread = new ClientInputThread(s_out);
		inputThread.start();
		ClientOutputThread outputThread = new ClientOutputThread(s_in);
		outputThread.start();

		try {
			outputThread.join();
		}
		catch (InterruptedException e) {

		}
		inputThread.interrupt();
		System.out.print("Connection to server lost, press any key to exit.");
		
		try {
			socket.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.print(ANSI_RESET);
	    return true;
	}
	
}
