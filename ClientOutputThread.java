import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

// Prints out all messages in clients terminal that are recieved from the server
public class ClientOutputThread extends Thread{
	BufferedReader s_in;
	BufferedReader s2;

	public ClientOutputThread(BufferedReader reader) {
		this.s_in = reader;
		s2 = new BufferedReader(new InputStreamReader(System.in));
	}

	public void run() {
		String response;
	    try {
			while ((response = s_in.readLine()) != null) 
			{
			    System.out.println( response );
			    System.out.print("\b");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}

}