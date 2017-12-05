import java.util.Scanner;

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

		if (!client.startClient()) { // Attempts to start client session
			System.out.println("Would you like to host a server? (y/n)");
			Scanner scanner = new Scanner(System.in);
			String input = scanner.nextLine();
			if (input.equals("y")) {
				System.out.println("Now starting new server on port " + port + ".");
				Socket_Host.socket_host(); // Starts Server if you didnt connect to one
			}
		}
		
		return;
	}
}
