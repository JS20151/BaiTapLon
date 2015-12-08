
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
	private static int uniqueId;
	private ArrayList<ClientThread> al;
	private ServerGUI sg;
	private int port,k=0;
	private boolean keepGoing;
	private int[] num = new int[100];
	private boolean playing = false;

	public Server(int port, ServerGUI sg) {
		this.sg = sg;
		this.port = port;
		al = new ArrayList<ClientThread>();
	}

	public void random() {
		ArrayList<Integer> array = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			array.add(i);
		}
		Collections.shuffle(array);
		for(int i=0;i<100;i++){
			num[i]= array.get(i);
		}
	}

	public void start() {
		keepGoing = true;
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			while (keepGoing) {
				display("Server sÄƒÌƒn saÌ€ng taÌ£i port: " + port + ".");
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
				display(e.getMessage());
			}
		} catch (IOException e) {
			String msg = e.getMessage();
			display(msg);
		}
	}

	@SuppressWarnings("resource")
	protected void stop() {
		keepGoing = false;
		try {
			new Socket("localhost", port);
		} catch (Exception e) {
			display(e.getMessage());
		}
	}

	private void display(String msg) {
		String ms = "" + msg;
			sg.appendEvent(ms + "\n");
	}

	private synchronized void broadcast(ChatMessage message) {

		String messageLf = " " + message.getMessage() + "\n";


		for (int i = al.size(); --i >= 0;) {
			ClientThread ct = al.get(i);

			if (!ct.writeMsg(new ChatMessage(message.getType(), messageLf, message.getNumber()))) {
				al.remove(i);
				display("ChÃ¢Ì�m dÆ°Ì�t kÃªÌ�t nÃ´Ì�i vÆ¡Ì�i " + ct.username);
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
				display(username + " Ä‘aÌƒ tham gia");
			} catch (IOException e) {
				display(e.getMessage());
				return;
			}

			catch (ClassNotFoundException e) {
				display(e.getMessage());
			}

		}

		public void run() {

			boolean keepGoing = true;
			while (keepGoing) {

				try {
					cm = (ChatMessage) sInput.readObject();
				} catch (IOException e) {
					display(e.getMessage());
					break;
				} catch (ClassNotFoundException e2) {
					break;
				}

				String message = cm.getMessage();

				switch (cm.getType()) {
				
				case ChatMessage.LOGIN:
					broadcast(new ChatMessage(ChatMessage.MESSAGE, "ChaÌ€o mÆ°Ì€ng "+username + " Ä‘aÌƒ tham gia" + message, 0));
					break;
				case ChatMessage.MESSAGE:
					broadcast(new ChatMessage(ChatMessage.MESSAGE, username + ": " + message, 0));
					break;
				case ChatMessage.LOGOUT:
					broadcast(new ChatMessage(ChatMessage.MESSAGE, username + " Ä‘aÌƒ thoaÌ�t khoÌ‰i troÌ€ chÆ¡i", 0));
					keepGoing = false;
					break;
				case ChatMessage.START:
					if (!playing) {
						broadcast(new ChatMessage(ChatMessage.START, "", 0));
						playing = true;
					}
					random();
					break;
				case ChatMessage.NUMBER:
					broadcast(new ChatMessage(ChatMessage.NUMBER, "", num[k]));
					broadcast(new ChatMessage(ChatMessage.MESSAGE, "SÃ´Ì� " + num[k], 0));
					k++;
					break;
				case ChatMessage.BINGO:
					k=0;
					playing = false;
					broadcast(new ChatMessage(ChatMessage.MESSAGE, username + " BINGO", 0));
					broadcast(new ChatMessage(ChatMessage.BINGO, "", 0));
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
				display(e.getMessage());
			}
			;
			try {
				if (socket != null)
					socket.close();
			} catch (Exception e) {
				display(e.getMessage());
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
				display(e.getMessage());
			}
			return true;
		}
	}
}
