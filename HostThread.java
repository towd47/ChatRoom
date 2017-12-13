import java.io.*;
import java.net.*;
import java.util.ArrayList;

// Handles the client for joining and creating chat rooms
public class HostThread extends Thread {
	protected Socket socket;
    private PrintStream out;
    private BufferedReader brin;
    private String username;
    private Socket_Host host;

	public HostThread(Socket clientSocket, Socket_Host host) {
		this.socket = clientSocket;
        this.host = host;
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

        out.println(ANSI.BOLD + "Please enter your username:" + ANSI.RESET);

        String line;
        try {
            username = brin.readLine();
            out.println("* * * * * * * *");
            out.println(ANSI.BOLD + "Welcome to our chat server, " + username + ANSI.RESET);
            out.println("* * * * * * * *");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            out.println(ANSI.BOLD + "What would you like to do?" + ANSI.RESET);
            out.println(ANSI.UNDERLINE + "join\ncreate\nchange name\nquit" + ANSI.RESET);
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
                out.println(ANSI.BOLD + "Please enter your username:" + ANSI.RESET);
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
        ArrayList<ChatRoom> rooms = host.getRooms();
        boolean roomAvailable = false;
        String line = "";
        while (!roomAvailable) {
            roomAvailable = true;
            out.println(ANSI.BOLD + "Please enter a name for your chat room:" + ANSI.RESET);
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
        out.println(ANSI.BOLD + "Please enter a password or leave blank for no password:" + ANSI.RESET);
        password = getInput();
        ChatRoom newRoom;
        if (!password.equals("")) {
            newRoom = new ChatRoom(name, password);
        }
        else {
            newRoom = new ChatRoom(name);
        }
        host.addChatRoom(newRoom);
        joinRoom(newRoom);
        return true;
    }

    private void joinCommand() {
        ArrayList<ChatRoom> rooms = host.getRooms();
        if (rooms.size() == 0) {
            out.println("There are no available chatrooms at this time.");
            return;
        }
        out.println(ANSI.BOLD + "The available rooms are:" + ANSI.RESET);
        for (ChatRoom room: rooms) {
            out.println(ANSI.UNDERLINE + room.roomName + ANSI.RESET);
        }
        out.println(ANSI.BOLD + "Which room would you like to join?" + ANSI.RESET);
        String line = getInput();
        String passIn = "";
        for (ChatRoom room: rooms) {
            if (room.roomName.equalsIgnoreCase(line.trim())) {
            	if (room.hasPassword) {
            		out.println(ANSI.BOLD + "Please enter the password for this room." + ANSI.RESET);
            		passIn = getInput();
            		if (room.checkPassword(passIn)) {
                        joinRoom(room);
                        return;
            		}
                	else {
                		out.println("The password entered for this room is incorrect.");
                        return;
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
        HostChatThread chatThread = new HostChatThread(socket, room.roomName, username, host);
        chatThread.start();
        try {
            chatThread.join();
            room.removeMember(socket);
            if (room.getMembers().size() == 0) {
                host.removeRoom(room);
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