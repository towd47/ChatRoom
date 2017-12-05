import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class HostChatThread extends Thread {
	protected Socket socket;
    private String roomName;
    private String username;
    private PrintStream clientPrintStream;

	public HostChatThread(Socket clientSocket, String roomName, String username) {
		this.socket = clientSocket;
        this.roomName = roomName;
        this.username = username;
        try {
            clientPrintStream = new PrintStream(socket.getOutputStream());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
	}

	public void run() {
		InputStream in = null;
        BufferedReader brin = null;
        
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
                if (line == null || line.length() == 0) {

                } else if (line.startsWith("/")) {
                    if (!commandHandler(line)) {
                        return;
                    }
                } else {
                    sendMessageToAllClients(line);
                }

            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
	}

    // Returns false if the user used quit to leave the chat room
    // Otherwise returns true
    private boolean commandHandler(String input) {
        String[] args = input.split(" ");
        String command = args[0];
        if (command.length() == 1) {
            return true;
        }
        switch (command.substring(1)) {
            case "quit":
                clientPrintStream.println("Leaving ChatRoom: " + roomName + ".");
                return false;
            default:
                clientPrintStream.println("Command " + command + " not found.");
            case "help":
                clientPrintStream.println("The available commands are:\n/quit\n/help");
                break;
        }
        return true;
    }

    private void sendMessageToAllClients(String msg) throws IOException {
        PrintStream out = null;
        for (ChatRoom room: Socket_Host.getRooms()) {
            if (room.roomName.equals(this.roomName)) {
                 for (Socket s: room.getMembers()) {
                    out = new PrintStream(s.getOutputStream());
                    out.println(username + ": " + msg);
                }
                break;
            }
        }
    }
}