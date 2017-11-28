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
		//System.out.print("What would you like your username to be for this chat room?\n>>");
		//username = scanner.nextLine();
		//System.out.println("Your username is: " + username);
		while (true) {
			System.out.print(">>");
			String input = scanner.nextLine();
			writer.println(input);
			writer.flush();
		}
	}

}