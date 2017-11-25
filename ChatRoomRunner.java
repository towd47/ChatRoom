
public class ChatRoomRunner {
	
	public static void main(String[] args) {
		if (!Socket_Client.socket_client()) {
			Socket_Host.socket_host();
		}
	}
}
