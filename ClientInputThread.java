import java.io.PrintWriter;
import java.util.Scanner;

// Sends all clientside input to the host socket
public class ClientInputThread extends Thread{
	PrintWriter writer;
	String username;

	public ClientInputThread(PrintWriter writer) {
		this.writer = writer;
	}

	public void run() {
		Scanner scanner = new Scanner(System.in);
		while (!Thread.interrupted()) {
			String input = scanner.nextLine();
			writer.println(input);
			writer.flush();
		}
	}
}