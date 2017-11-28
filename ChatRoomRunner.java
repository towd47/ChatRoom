
public class ChatRoomRunner {
	
	public static void main(String[] args) {
		String host = "localhost";
		int port = 5000;
		if (args.length >= 1) {
			host = args[0];
		}
		if (args.length >= 2) {
			port = Integer.parseInt(args[1]);
		}
		Socket_Client client = new Socket_Client(host, port);

		if (!client.startClient()) {
			Socket_Host.socket_host();
		}
		
		return;
	}
}
