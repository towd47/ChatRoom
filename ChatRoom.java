import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatRoom {
	private ArrayList<Socket> members;
	private String password;
	public String roomName;
	public boolean hasPassword;
	public ArrayList<Socket> users;

	public ChatRoom(String roomName) {
		this.roomName = roomName;
		this.password = "";
		this.hasPassword = false;
		members = new ArrayList<Socket>();
		users = new ArrayList<Socket>();
	}

	public ChatRoom(String roomName, String password) {
		this.roomName = roomName;
		this.password = password;
		this.hasPassword = true;
		members = new ArrayList<Socket>();
		users = new ArrayList<Socket>();
	}

	public void addMember(Socket member) {
		members.add(member);
		users.add(member);
	}

	public void removeMember(Socket member) {
		members.remove(member);
	}

	public ArrayList<Socket> getMembers() {
		return members;
	}
	
	public ArrayList<Socket> getUsers() {
		return users;
	}
	
	public boolean checkPassword(String passIn) {
		if(passIn.equals(password)) {
			return true;
		}
		return false;
	}
}