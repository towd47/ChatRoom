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

        out.println(HostChatThread.ANSI_BOLD + "Please enter your username:" + HostChatThread.ANSI_RESET);

        String line;
        try {
            username = brin.readLine();
            out.println("* * * * * * * *");
            out.println(HostChatThread.ANSI_BOLD + "Welcome to our chat server, " + username + HostChatThread.ANSI_RESET);
            out.println("* * * * * * * *");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            out.println(HostChatThread.ANSI_BOLD + "What would you like to do?" + HostChatThread.ANSI_RESET);
            out.println(HostChatThread.ANSI_UNDERLINE + "join\ncreate\nchange name\nquit" + HostChatThread.ANSI_RESET);
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
                out.println(HostChatThread.ANSI_BOLD + "Please enter your username:" + HostChatThread.ANSI_RESET);
                String line = getInput();
                if (line != null && line.length() > 0) {
                    username = line;
                    out.println("Your username has been updated to: " + username);
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
            out.println(HostChatThread.ANSI_BOLD + "Please enter a name for your chat room:" + HostChatThread.ANSI_RESET);
            line = getInput();
            if (line == "" || line == null) {
                return false;
            }
            for (ChatRoom room: rooms) {
                if (line.equalsIgnoreCase(room.roomName)) {
                    out.println("There is already a chat room with the name: " + line + ".");
                    roomAvailable = false;
                }
            }
        }
        String name = line;
        String password = "";
        out.println(HostChatThread.ANSI_BOLD + "Please enter a password or leave blank for no password:" + HostChatThread.ANSI_RESET);
        password = getInput();
        ChatRoom newRoom;
        if (!password.equals("")) {
        	System.out.println("Test");
            newRoom = new ChatRoom(name, password);
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
        out.println(HostChatThread.ANSI_BOLD + "The available rooms are:" + HostChatThread.ANSI_RESET);
        for (ChatRoom room: rooms) {
            out.println(HostChatThread.ANSI_UNDERLINE + room.roomName + HostChatThread.ANSI_RESET);
        }
        out.println(HostChatThread.ANSI_BOLD + "Which room would you like to join?" + HostChatThread.ANSI_RESET);
        String line = getInput();
        String passIn = "";
        for (ChatRoom room: rooms) {
            if (room.roomName.equalsIgnoreCase(line.trim())) {
            	if (room.hasPassword) {
            		out.println(HostChatThread.ANSI_BOLD + "Please enter the password for this room." + HostChatThread.ANSI_RESET);
            		passIn = getInput();
            		if (room.checkPassword(passIn)) {
                        joinRoom(room);
                        return;
            		}
                	else {
                		out.println("The password entered for this room is incorrect.");
                	}
            	}
            	else {
                    joinRoom(room);
                    return;
                }
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