package Code;


import java.net.*;
import java.io.*;
import java.util.*;

public class Client {

	private ObjectInputStream sInput;
	private ObjectOutputStream sOutput;
	private Socket socket;

	private ClientGUI cg;

	private String server, username;
	private int port;

	Client(String server, int port, String username, ClientGUI cg) {
		this.server = server;
		this.port = port;
		this.username = username;
		this.cg = cg;
	}

	public boolean start() {
		try {
			socket = new Socket(server, port);
		} catch (Exception ec) {
			display(ec.getMessage());
			return false;
		}

		//String msg = "ChaÌ€o mÆ°Ì€ng " + username + " tham gia phoÌ€ng chat";
		//display(msg);

		try {
			sInput = new ObjectInputStream(socket.getInputStream());
			sOutput = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException eIO) {
			display(eIO.getMessage());
			return false;
		}

		new ListenFromServer().start();
		;
		try {
			sOutput.writeObject(username);
		} catch (IOException eIO) {
			display(eIO.getMessage());
			return false;
		}
		return true;
	}

	private void display(String msg) {
		cg.append(msg + "\n");
	}

	void sendMessage(ChatMessage msg) {
		try {
			sOutput.writeObject(msg);
		} catch (IOException e) {
			display(e.getMessage());
		}
	}

	void disconnect() {
		try {
			if (sInput != null)
				sInput.close();
		} catch (Exception e) {
		}
		try {
			if (sOutput != null)
				sOutput.close();
		} catch (Exception e) {
		}
		try {
			if (socket != null)
				socket.close();
		} catch (Exception e) {
		}
	}

	public static void main(String[] args) {

	}

	class ListenFromServer extends Thread {

		public void run() {
			while (true) {
				try {
					ChatMessage msg = (ChatMessage) sInput.readObject();
					if (msg.getType() == ChatMessage.MESSAGE) {
						cg.append(msg.getMessage());
					}
					if (msg.getType() == ChatMessage.START) {
						cg.btnNumber.setEnabled(true);
						cg.GameEnable();
					}
					if (msg.getType() == ChatMessage.BINGO)
						cg.btnNumber.setEnabled(false);
					if (msg.getType() == ChatMessage.NUMBER) {
						cg.ButtonSelected(msg.getNumber());
					}
				} catch (IOException e) {
					display("Ä�aÌƒ ngÄƒÌ�t kÃªÌ�t nÃ´Ì�i");
					break;
				} catch (ClassNotFoundException e2) {
				}
			}
		}
	}
}

