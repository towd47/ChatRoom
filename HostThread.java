import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class HostThread extends Thread {
	protected Socket socket;

	public HostThread(Socket clientSocket) {
		this.socket = clientSocket;
	}

	public void run() {
		InputStream in = null;
        BufferedReader brin = null;
        PrintStream out = null;
        
        try {
            in = socket.getInputStream();
            brin = new BufferedReader(new InputStreamReader(in));

        } catch (IOException e) {
            return;
        }

        String line;

        while (true) {
            try {
                line = brin.readLine();
                for (Socket s: Socket_Host.getClients()) {
                	out = new PrintStream(s.getOutputStream());
                	out.println(line);
                }

            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
	}
}