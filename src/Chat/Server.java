package Chat;


import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Server {
	private static int uniqueId;
	private ArrayList<ClientThread> al;
	private ServerGUI sg;
	private int port;
	private boolean keepGoing;

	public Server(int port, ServerGUI sg) {
		this.sg = sg;
		this.port = port;
		al = new ArrayList<ClientThread>();
	}

	public void start() {
		keepGoing = true;
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			while (keepGoing) {
				display("Server waiting for Clients on port " + port + ".");
				Socket socket = serverSocket.accept();
				if (!keepGoing)
					break;
				ClientThread t = new ClientThread(socket);
				al.add(t);
				t.start();
			}
			try {
				serverSocket.close();
				for (int i = 0; i < al.size(); ++i) {
					ClientThread tc = al.get(i);
					try {
						tc.sInput.close();
						tc.sOutput.close();
						tc.socket.close();
					} catch (IOException ioE) {
					}
				}
			} catch (Exception e) {
				display("Exception closing the server and clients: " + e);
			}
		} catch (IOException e) {
			String msg = " Exception on new ServerSocket: " + e + "\n";
			display(msg);
		}
	}

	@SuppressWarnings("resource")
	protected void stop() {
		keepGoing = false;
		try {
			new Socket("localhost", port);
		} catch (Exception e) {
		}
	}

	private void display(String msg) {
		String time = "" + msg;
		if (sg == null)
			System.out.println(time);
		else
			sg.appendEvent(time + "\n");
	}

	private synchronized void broadcast(ChatMessage message) {

		String messageLf = " " + message.getMessage() + "\n";

		sg.appendRoom(messageLf);

		for (int i = al.size(); --i >= 0;) {
			ClientThread ct = al.get(i);

			if (!ct.writeMsg(new ChatMessage(message.getType(), messageLf))) {
				al.remove(i);
				display("Disconnected Client " + ct.username + " removed from list.");
			}
		}
	}

	synchronized void remove(int id) {

		for (int i = 0; i < al.size(); ++i) {
			ClientThread ct = al.get(i);

			if (ct.id == id) {
				al.remove(i);
				return;
			}
		}
	}

	public static void main(String[] args) {

	}

	class ClientThread extends Thread {

		Socket socket;
		ObjectInputStream sInput;
		ObjectOutputStream sOutput;

		int id;

		String username;

		ChatMessage cm;

		ClientThread(Socket socket) {
			id = ++uniqueId;
			this.socket = socket;
			try {

				sOutput = new ObjectOutputStream(socket.getOutputStream());
				sInput = new ObjectInputStream(socket.getInputStream());

				username = (String) sInput.readObject();
				display(username + " just connected.");
			} catch (IOException e) {
				display("Exception creating new Input/output Streams: " + e);
				return;
			}

			catch (ClassNotFoundException e) {
			}

		}

		public void run() {

			boolean keepGoing = true;
			while (keepGoing) {

				try {
					cm = (ChatMessage) sInput.readObject();
				} catch (IOException e) {
					display(username + " Exception reading Streams: " + e);
					break;
				} catch (ClassNotFoundException e2) {
					break;
				}

				String message = cm.getMessage();

				switch (cm.getType()) {

				case ChatMessage.MESSAGE:
					broadcast(new ChatMessage(ChatMessage.MESSAGE, username + ": " + message));
					break;
				case ChatMessage.LOGOUT:
					broadcast(new ChatMessage(ChatMessage.STOP, ""));
					display(username + " disconnected with a LOGOUT message.");
					keepGoing = false;
					break;
				case ChatMessage.START:
					broadcast(new ChatMessage(ChatMessage.MESSAGE, "Player Connected:"));

					for (int i = 0; i < al.size(); ++i) {
						ClientThread ct = al.get(i);
						broadcast(new ChatMessage(ChatMessage.MESSAGE, (i + 1) + ": " + ct.username));
					}
					if (al.size() > 1)
						broadcast(new ChatMessage(ChatMessage.START, ""));
					break;
				case ChatMessage.NUMBER:
					broadcast(new ChatMessage(ChatMessage.NUMBER, message));
					broadcast(new ChatMessage(ChatMessage.MESSAGE, username + " selected number " + message));
					break;
				case ChatMessage.WIN:
					broadcast(new ChatMessage(ChatMessage.MESSAGE, username + " is winner"));
					break;

				}
			}

			remove(id);
			close();
		}

		private void close() {
			try {
				if (sOutput != null)
					sOutput.close();
			} catch (Exception e) {
			}
			try {
				if (sInput != null)
					sInput.close();
			} catch (Exception e) {
			}
			;
			try {
				if (socket != null)
					socket.close();
			} catch (Exception e) {
			}
		}

		private boolean writeMsg(ChatMessage msg) {

			if (!socket.isConnected()) {
				close();
				return false;
			}

			try {
				sOutput.writeObject(msg);
			} catch (IOException e) {
				display("Error sending message to " + username);
				display(e.toString());
			}
			return true;
		}
	}
}
