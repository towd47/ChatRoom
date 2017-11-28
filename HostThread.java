import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class HostThread extends Thread {
	protected Socket socket;
    PrintStream out;
    BufferedReader brin;

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


        out.println("Some commands you can use are:\njoin\ncreate\nquit");

        String line;
        while (true) {
            try {
                line = brin.readLine();
                out.println(line);
                runCommand(line);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
	}

    private void runCommand(String command) {
        switch (command) {
            case "join":
                joinRoom();
                break;
            case "create":
                createRoom();
                break;
            case "quit":
                break;
            default:
        }
    }

    private boolean createRoom() {
        ArrayList<ChatRoom> rooms = Socket_Host.getRooms();
        boolean roomAvailable = false;
        String line = "";
        while (!roomAvailable) {
            roomAvailable = true;
            out.println("Please enter a name for your chat room:");
            try {
                line = brin.readLine();
            }
            catch (IOException e) {
                return false;
            }
            for (ChatRoom room: rooms) {
                if (line == room.roomName) {
                    out.println("There is already a chat room with that name.");
                    roomAvailable = false;
                }
            }
        }
        String name = line;
        String password = "";
        out.println("Please enter a password or leave blank for no password:");
        try {
                line = brin.readLine();
        }
        catch (IOException e) {
            return false;
        }
        ChatRoom newRoom;
        if (line != "") {
            newRoom = new ChatRoom(name, line);
        }
        else {
            newRoom = new ChatRoom(name);
        }
        Socket_Host.addChatRoom(newRoom);
        HostChatThread chatThread = new HostChatThread(socket, name);
        chatThread.start();
        try {
            chatThread.join();
        }
        catch (InterruptedException e) {
            return false;
        }
        return true;
    }

    private boolean joinRoom() {
        ArrayList<ChatRoom> rooms = Socket_Host.getRooms();
        if (rooms.size() == 0) {
            out.println("There are no available chatrooms at this time.");
            return false;
        }
        out.println("The available rooms are:");
        for (ChatRoom room: rooms) {
            out.println(room.roomName);
        }
        String line = "";
        out.println("Which room would you like to join?");
        try {
            line = brin.readLine();
        }
        catch (IOException e) {
            return false;
        }
        for (ChatRoom room: rooms) {
            if (room.roomName.equals(line)) {
                out.println("Joining room " + room.roomName + ".");
                HostChatThread chatThread = new HostChatThread(socket, room.roomName);
                chatThread.start();
                try {
                    chatThread.join();
                }
                catch (InterruptedException e) {
                    return false;
                }
                return true;
            }
        }
        out.println("No chat room with the name " + line + " was found.");
        return false;
    }

}