import java.io.PrintWriter;
import java.util.Scanner;

public class ClientInputThread extends Thread{
	PrintWriter writer;
	String username;

	public ClientInputThread(PrintWriter writer) {
		this.writer = writer;
	}

	public void run() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("What would you like your username to be for this chat room?");
		username = scanner.nextLine();
		while (true) {
			String input = scanner.nextLine();
			if (input.charAt(0) == '/') {
				
			}
			else {
				writer.println(username + ": " + input);
				writer.flush();
			}
		}
	}

}