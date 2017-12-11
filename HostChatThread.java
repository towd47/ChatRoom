import java.io.*;
import java.net.*;
import java.util.ArrayList;

// Handles the Client while in a chat room
public class HostChatThread extends Thread {
	protected Socket socket;
    private String roomName;
    private String username;
    private PrintStream clientPrintStream;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_WHITE = "\u001B[37m";
    //Colors in array are as follows: RED, GREEN, YELLOW, BLUE, PURPLE, CYAN
    private final String[] colors = {"\u001B[31m", "\u001B[32m", "\u001B[33m", "\u001B[34m", "\u001B[35m", "\u001B[36m"};

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
            sendMessageToAllClients("User: '" +  username + "' has joined the room.");
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
                    int i = room.getUsers().indexOf(socket);

                    out.println(colors[(i)%6] + username + ": " + msg + ANSI_RESET);
                }
                break;
            }
        }
    }
}