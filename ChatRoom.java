import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatRoom {
	private ArrayList<Socket> members;
	private String password;
	public String roomName;

	public ChatRoom(String roomName) {
		this.roomName = roomName;
		this.password = "";
		members = new ArrayList<Socket>();
	}

	public ChatRoom(String roomName, String password) {
		this.roomName = roomName;
		this.password = password;
		members = new ArrayList<Socket>();
	}

	public void addMember(Socket member) {
		members.add(member);
	}

	public void removeMember(Socket member) {
		members.remove(member);
	}

	public ArrayList<Socket> getMembers() {
		return members;
	}
}