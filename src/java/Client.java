package java;


import java.net.*;
import java.io.*;
import java.util.*;

public class Client  {

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
		} 
		catch(Exception ec) {
			display("Error connectiong to server:" + ec);
			return false;
		}
		
		String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
		display(msg);

		try
		{
			sInput  = new ObjectInputStream(socket.getInputStream());
			sOutput = new ObjectOutputStream(socket.getOutputStream());
		}
		catch (IOException eIO) {
			display("Exception creating new Input/output Streams: " + eIO);
			return false;
		}

		new ListenFromServer().start();;
		try
		{
			sOutput.writeObject(username);
		}
		catch (IOException eIO) {
			display("Exception doing login : " + eIO);
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
		}
		catch(IOException e) {
			display("Exception writing to server: " + e);
		}
	}

	
	void disconnect() {
		try { 
			if(sInput != null) sInput.close();
		}
		catch(Exception e) {} 
		try {
			if(sOutput != null) sOutput.close();
		}
		catch(Exception e) {} 
        try{
			if(socket != null) socket.close();
		}
		catch(Exception e) {} 	
	}

	public static void main(String[] args) {
		
	}


	class ListenFromServer extends Thread {

		public void run() {
			while(true) {
				try {
					ChatMessage msg = (ChatMessage) sInput.readObject();
					if (msg.getType() == ChatMessage.MESSAGE){
						cg.append(msg.getMessage());
					}
					if (msg.getType() == ChatMessage.START)
						cg.GameEnable();
					if (msg.getType() == ChatMessage.STOP)
						cg.GameDisable();
					if (msg.getType() == ChatMessage.NUMBER){
						cg.ButtonSelected(msg.getNumber());
					}
				}
				catch(IOException e) {
					display("Server has close the connection: " + e); 	
					break;
				}
				catch(ClassNotFoundException e2) {
				}
			}
		}
	}
}

