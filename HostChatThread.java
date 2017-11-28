import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class HostChatThread extends Thread {
	protected Socket socket;
    private String roomName;

	public HostChatThread(Socket clientSocket, String roomName) {
		this.socket = clientSocket;
        this.roomName = roomName;
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
                if (line == null) {
                    return;
                }
                for (ChatRoom room: Socket_Host.getRooms()) {
                    if (room.roomName.equals(this.roomName)) {
                        //for (Socket s: room.getMembers()) {
                        for (Socket s: Socket_Host.getClients()) {
                        	out = new PrintStream(s.getOutputStream());
                    	    out.println(line);
                        }
                        break;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
	}
}