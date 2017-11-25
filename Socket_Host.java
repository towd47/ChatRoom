import java.io.*;
import java.io.BufferedReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Socket_Host {
	
	public static void socket_host() {
		ServerSocket socket = null;
		Socket conn = null;
		PrintStream out = null;
		BufferedReader in = null;
		String message = null;
		int port = 5000;
		
		try {
			socket = new ServerSocket(port, 10);
			
			System.out.println("Server opened on port " + port + ". Waiting for connection...");
			
			conn = socket.accept();
			
			System.out.println("Connection received from " + conn.getInetAddress().getHostName() + " : " + conn.getPort());
			
			out = new PrintStream(conn.getOutputStream());
			out.flush();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			out.println("Welcome to our chat server.");
			out.flush();
		}
		
		catch(IOException e) {
			System.err.println("IOException");
		}
		
		try
        {
            in.close();
            out.close();
            socket.close();
        }
         
        catch(IOException ioException)
        {
            System.err.println("Unable to close. IOexception");
        }
	}
	
}
