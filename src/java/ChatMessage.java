package java;

import java.io.*;

public class ChatMessage implements Serializable {

	protected static final long serialVersionUID = 1112122200L;

	
	static final int START = 0, MESSAGE = 1, LOGOUT = 2, STOP = 3, NUMBER =4, WIN = 5;
	private int type;
	private String message;
	
	
	ChatMessage(int type, String message) {
		this.type = type;
		this.message = message;
	}
	
	int getType() {
		return type;
	}
	String getMessage() {
		return message;
	}
}

