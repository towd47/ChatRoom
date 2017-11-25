import java.io.PrintWriter;
import java.util.Scanner;

public class ClientInput extends Thread{

	public static void run(PrintWriter writer) {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			String input = scanner.nextLine();
			if (input.charAt(0) == '/') {
				
			}
			else {
				writer.println(input);
				writer.flush();
			}
		}
	}

}