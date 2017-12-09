import java.io.*;
import java.net.*;
import java.util.ArrayList;

// Handles the client for joining and creating chat rooms
public class HostThread extends Thread {
	protected Socket socket;
    PrintStream out;
    BufferedReader brin;
    private String username;

	public HostThread(Socket clientSocket) {
		this.socket = clientSocket;
	}

	public void run() {
		InputStream in = null;
        
        try {
            in = socket.getInputStream();
            brin = new BufferedReader(new InputStreamReader(in));
            out = new PrintStream(socket.getOutputStream());

        } catch (IOException e) {
            return;
        }

        out.println("Please enter your username:");

        String line;
        try {
            username = brin.readLine();
            out.println("Your username is: " + username);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            out.println("What would you like to do?\njoin\ncreate\nchange name\nquit");
            line = getInput();
            if (line == null || !runCommand(line.trim())) {
                return;
            }
        }
	}

    private boolean runCommand(String command) { // Handles input from the client
        switch (command) {
            case "join":
                joinCommand();
                break;
            case "create":
                createRoom();
                break;
            case "change name": 
                out.println("Please enter your username:");
                String line = getInput();
                if (line != null && line.length() > 0) {
                    username = line;
                    out.println("Your username is: " + username);
                }
                break;
            case "quit":
                try {
                    socket.close();
                    return false;
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            default:
        }
        return true;
    }

    private boolean createRoom() {
        ArrayList<ChatRoom> rooms = Socket_Host.getRooms();
        boolean roomAvailable = false;
        String line = "";
        while (!roomAvailable) {
            roomAvailable = true;
            out.println("Please enter a name for your chat room:");
            line = getInput();
            for (ChatRoom room: rooms) {
                if (line.equalsIgnoreCase(room.roomName)) {
                    out.println("There is already a chat room with that name.");
                    roomAvailable = false;
                }
            }
        }
        String name = line;
        String password = "";
        out.println("Please enter a password or leave blank for no password:");
        line = getInput();
        ChatRoom newRoom;
        if (line != "") {
            newRoom = new ChatRoom(name, line);
        }
        else {
            newRoom = new ChatRoom(name);
        }
        Socket_Host.addChatRoom(newRoom);
        joinRoom(newRoom);
        return true;
    }

    private void joinCommand() {
        ArrayList<ChatRoom> rooms = Socket_Host.getRooms();
        if (rooms.size() == 0) {
            out.println("There are no available chatrooms at this time.");
            return;
        }
        out.println("The available rooms are:");
        for (ChatRoom room: rooms) {
            out.println(room.roomName);
        }
        out.println("Which room would you like to join?");
        String line = getInput();
        for (ChatRoom room: rooms) {
            if (room.roomName.equals(line.trim())) {
                joinRoom(room);
                return;
            }
        }
        out.println("No chat room with the name '" + line + "' was found.");
    }

    private void joinRoom(ChatRoom room) {
        room.addMember(socket);
        out.println("Joining room: " + room.roomName + ".");
        HostChatThread chatThread = new HostChatThread(socket, room.roomName, username);
        chatThread.start();
        try {
            chatThread.join();
            room.removeMember(socket);
            if (room.getMembers().size() == 0) {
                Socket_Host.removeRoom(room);
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String getInput() {
        try {
            String line = brin.readLine();
            return line;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}