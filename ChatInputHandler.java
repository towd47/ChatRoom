

public class ChatInputHandler {
	private boolean isCommand;
	private String command;
	private String argsOrMessage;

	public InputHandler(String input) {
		if (input.charAt(0) == '/') {
			isCommand = true;
			String[] words = input.split(" ");
			command = words[0];
			argsOrMessage = "";
			for (int i = 1; i < input.length; i++) {
				argsOrMessage = argsOrMessage + input[i];
			}
		}
		else {
			isCommand = false;
			argsOrMessage = input;
		}
	}
}