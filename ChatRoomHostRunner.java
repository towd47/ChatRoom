public class ChatRoomHostRunner {
	
	public static void main(String[] args) {
		int port = 5000;
		if (args.length >= 1) {
			port = Integer.parseInt(args[0]);
		}

		Socket_Host server = new Socket_Host(port);
		server.run();
		
		return;
	}
}
