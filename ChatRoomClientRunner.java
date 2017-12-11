public class ChatRoomClientRunner {
	
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

		if (!client.startClient()) { // Attempts to start client session
			System.out.println("Could not connect to the server. Now exiting.");
		}
		
		return;
	}
}
