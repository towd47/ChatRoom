import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

// Prints out all messages in clients terminal that are recieved from the server
public class ClientOutputThread extends Thread{
	BufferedReader s_in;

	public ClientOutputThread(BufferedReader reader) {
		this.s_in = reader;
	}

	public void run() {
		String response;
	    try {
			while ((response = s_in.readLine()) != null) 
			{
			    System.out.println( response );
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("ClientOutputThread closing");
		return;
	}

}