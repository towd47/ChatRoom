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
			System.err.println("IOException");
			return false;
		}

		new ClientInputThread(s_out).start();
		
		String response;
	    try {
			while ((response = s_in.readLine()) != null) 
			{
			    System.out.println( response );
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return true;
	}
	
}
