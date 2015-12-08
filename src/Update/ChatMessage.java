package Update;


import java.io.*;

public class ChatMessage implements Serializable {

	protected static final long serialVersionUID = 1112122200L;

	
	static final int START = 0, MESSAGE = 1, LOGOUT = 2, STOP = 3, NUMBER =4, BINGO = 5, LOGIN =6;
	private int type;
	private String message;
	private int number;
	
	
	ChatMessage(int type, String message,int number) {
		this.type = type;
		this.message = message;
		this.number = number;
	}
	
	int getType() {
		return type;
	}
	String getMessage() {
		return message;
	}
	int getNumber() {
		return number;
	}
}

