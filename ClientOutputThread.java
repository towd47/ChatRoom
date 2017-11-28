import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}

}