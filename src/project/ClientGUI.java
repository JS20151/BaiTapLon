package project;


import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientGUI extends JFrame implements ActionListener {


	private static final long serialVersionUID = 1L;
	static JButton button[][];
	int[][] x = new int[10][10];
	private JTextArea save;
	JLabel labelTime;
	JLabel labelPoint;
	private JLabel labelName, labelPort;
	private JTextField txtChat, txtName, txtPort;
	JButton btnLogin, btnLogout, btnStart, btnNumber;
	static int point = 0;
	int[] numberSelected = new int[100]; 
	static int[] numberSelectedX = new int[100]; 
	static int[] numberSelectedY = new int[100]; 
	static int h = 0;
	static int groupMax = 0;
	private int defaultPort;
	private String defaultHost;
	private boolean connected;
	private static Client client;
	public boolean startGame;

	ClientGUI(String host, int port) {
		super("Chat Client");
		defaultPort = port;
		defaultHost = host;
		JPanel game = new JPanel(new GridLayout(10, 10));
		int k = 0;
		ArrayList<Integer> array = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			array.add(i + 1);
		}
		Collections.shuffle(array);
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++) {
				x[i][j] = array.get(k);
				k++;
			}
		button = new JButton[10][10];
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++) {
				button[i][j] = new JButton(" ");
				game.add(button[i][j]);
				button[i][j].setEnabled(false);
				button[i][j].addActionListener(new BtnGameListener());
			}
		add(game, BorderLayout.NORTH);

		save = new JTextArea("Welcome to the Chat room\n", 80, 50);
		JPanel chatRoom = new JPanel(new GridLayout(1, 1));
		chatRoom.add(new JScrollPane(save));
		save.setEditable(false);
		add(chatRoom, BorderLayout.CENTER);

		JPanel textChat = new JPanel(new GridLayout(4, 1));
		JPanel timeAndPoint = new JPanel(new GridLayout(1, 2));
		labelPoint = new JLabel("Point: " + point);
		timeAndPoint.add(labelPoint);
		textChat.add(timeAndPoint);
		JPanel namePort = new JPanel(new GridLayout(1, 4));
		labelName = new JLabel("Player Name");
		txtName = new JTextField("Player");
		labelPort = new JLabel("Port");
		txtPort = new JTextField("1500");
		namePort.add(labelName);
		namePort.add(txtName);
		namePort.add(labelPort);
		namePort.add(txtPort);
		textChat.add(namePort);
		txtChat = new JTextField("");
		textChat.add(txtChat);
		JPanel loginLogout = new JPanel(new GridLayout(1, 4));
		btnLogin = new JButton("LOGIN");
		btnLogin.addActionListener(this);
		btnLogout = new JButton("LOGOUT");
		btnLogout.addActionListener(this);
		btnLogout.setEnabled(false);
		btnStart = new JButton("START");
		btnStart.addActionListener(this);
		btnStart.setEnabled(false);
		btnNumber = new JButton("NUMBER");
		btnNumber.setEnabled(false);
		btnNumber.addActionListener(this);
		loginLogout.add(btnLogin);
		loginLogout.add(btnLogout);
		loginLogout.add(btnStart);
		loginLogout.add(btnNumber);
		textChat.add(loginLogout);
		add(textChat, BorderLayout.SOUTH);

		//
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600, 600);
		setVisible(true);
		txtChat.requestFocus();
	}

	public void GameEnable() {
		int k = 0;
		ArrayList<Integer> array = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			array.add(i + 1);
		}
		Collections.shuffle(array);
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++) {
				x[i][j] = array.get(k);
				k++;
			}
		point = 0;
		labelPoint.setText("Point: " + point);
		int h = 0;
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++) {
				Integer num = x[i][j];
				button[i][j].setText(num.toString());
				button[i][j].setEnabled(true);
				numberSelected[h] = 101;
				numberSelectedX[h] = 101;
				numberSelectedY[h] = 101;
				groupMax = 0;
				h++;
			}

	}

	public void GameDisable() {
		point = 0;
		labelPoint.setText("Point: " + point);
		int k = 0;
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++) {
				button[i][j].setEnabled(false);
				numberSelected[k] = 101;
				numberSelectedX[k] = 101;
				numberSelectedY[k] = 101;
				groupMax = 0;
				k++;
			}

	}

	public void ButtonSelected(int x) {
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++) {
				if (Integer.parseInt(button[i][j].getText())==x) {
					button[i][j].setEnabled(false);
					numberSelected[h] = x;
					numberSelectedX[h] = i;
					numberSelectedY[h] = j;
					point = UpdatePoint(numberSelectedX[h], numberSelectedY[h]);
					h++;
					labelPoint.setText("Point: " + point);
					if (point == 5){
						bingo();
						h=0;
					}					
				}
			}
	}

	public static int UpdatePoint(int x, int y) {
		int groupX = 0;
		int groupY = 0;
		for (int a = 0; a < h; a++) {
			if ((x == numberSelectedX[a]) && (Math.abs(y - numberSelectedY[a]) == 1)) {
				groupX = 2;
				for (int b = 0; b < h; b++) {
					if (((x == numberSelectedX[b]) && (Math.abs(y - numberSelectedY[b]) == 1) && (b != a))
							|| ((numberSelectedX[a] == numberSelectedX[b])
									&& (Math.abs(numberSelectedY[a] - numberSelectedY[b]) == 1))) {
						groupX = 3;
						for (int c = 0; c < h; c++) {
							if (((x == numberSelectedX[c]) && (Math.abs(y - numberSelectedY[c]) == 1) && (c != a)
									&& (c != b))
									|| ((numberSelectedX[c] == numberSelectedX[a])
											&& (Math.abs(numberSelectedY[c] - numberSelectedY[a]) == 1) && (c != b))
									|| ((numberSelectedX[c] == numberSelectedX[b])
											&& (Math.abs(numberSelectedY[c] - numberSelectedY[b]) == 1) && (c != a))) {
								groupX = 4;
								for (int d = 0; d < h; d++) {
									if (((x == numberSelectedX[d]) && (Math.abs(y - numberSelectedY[d]) == 1)
											&& (d != a) && (d != b) && (d != c))
											|| ((numberSelectedX[d] == numberSelectedX[a])
													&& (Math.abs(numberSelectedY[d] - numberSelectedY[a]) == 1)
													&& (d != b)
													&& (d != c))
											|| ((numberSelectedX[d] == numberSelectedX[b])
													&& (Math.abs(numberSelectedY[d] - numberSelectedY[b]) == 1)
													&& (d != a) && (d != c))
											|| ((numberSelectedX[d] == numberSelectedX[c])
													&& (Math.abs(numberSelectedY[d] - numberSelectedY[c]) == 1)
													&& (d != b) && (d != a)))
										groupX = 5;
								}
							}
						}
					}
				}
			}
		}

		for (int a = 0; a < h; a++) {
			if ((y == numberSelectedY[a]) && (Math.abs(x - numberSelectedX[a]) == 1)) {
				groupY = 2;
				for (int b = 0; b < h; b++) {
					if (((y == numberSelectedY[b]) && (Math.abs(x - numberSelectedX[b]) == 1) && (b != a))
							|| ((numberSelectedY[a] == numberSelectedY[b])
									&& (Math.abs(numberSelectedX[a] - numberSelectedX[b]) == 1))) {
						groupY = 3;
						for (int c = 0; c < h; c++) {
							if (((y == numberSelectedY[c]) && (Math.abs(x - numberSelectedX[c]) == 1) && (c != a)
									&& (c != b))
									|| ((numberSelectedY[c] == numberSelectedY[a])
											&& (Math.abs(numberSelectedX[c] - numberSelectedX[a]) == 1) && (c != b))
									|| ((numberSelectedY[c] == numberSelectedY[b])
											&& (Math.abs(numberSelectedX[c] - numberSelectedX[b]) == 1) && (c != a))) {
								groupY = 4;
								for (int d = 0; d < h; d++) {
									if (((y == numberSelectedY[d]) && (Math.abs(x - numberSelectedX[d]) == 1)
											&& (d != a) && (d != b) && (d != c))
											|| ((numberSelectedY[d] == numberSelectedY[a])
													&& (Math.abs(numberSelectedX[d] - numberSelectedX[a]) == 1)
													&& (d != b)
													&& (d != c))
											|| ((numberSelectedY[d] == numberSelectedY[b])
													&& (Math.abs(numberSelectedX[d] - numberSelectedX[b]) == 1)
													&& (d != a) && (d != c))
											|| ((numberSelectedY[d] == numberSelectedY[c])
													&& (Math.abs(numberSelectedX[d] - numberSelectedX[c]) == 1)
													&& (d != b) && (d != a)))
										groupY = 5;
								}
							}
						}
					}
				}
			}
		}

		if (((groupX > groupY) ? groupX : groupY) > groupMax)
			groupMax = ((groupX > groupY) ? groupX : groupY);

		return groupMax;
	}

	public static void bingo() {
		client.sendMessage(new ChatMessage(ChatMessage.BINGO, "", 0));
	}

	void append(String str) {
		save.append(str);
		save.setCaretPosition(save.getText().length() - 1);
	}

	public static void main(String[] args) {
		new ClientGUI("localhost", 1500);

	}

	class BtnGameListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o == btnStart) {
			client.sendMessage(new ChatMessage(ChatMessage.START, "", 0));
			return;
		}
		if (o == btnLogout) {
			client.sendMessage(new ChatMessage(ChatMessage.LOGOUT, "", 0));
			btnLogout.setEnabled(false);
			btnLogin.setEnabled(true);
			btnStart.setEnabled(false);
			btnNumber.setEnabled(false);
			txtName.setEditable(true);
			txtPort.setEnabled(true);
			client.disconnect();
			GameDisable();
			connected = false;
			return;
		}
		if (o==btnNumber) {
			client.sendMessage(new ChatMessage(ChatMessage.NUMBER, "", 0));
			return;
		}
		
		if (connected) {
			client.sendMessage(new ChatMessage(ChatMessage.MESSAGE, txtChat.getText(), 0));
			txtChat.setText("");
			return;
		}
		if (o == btnLogin) {
			String username = txtName.getText().trim();
			if (username.length() == 0)
				return;
			String portNumber = txtPort.getText().trim();
			if (portNumber.length() == 0)
				return;
			int port = 0;
			port = Integer.parseInt(portNumber);
			client = new Client("localhost", port, username, this);
			if (!client.start())
				return;
			client.sendMessage(new ChatMessage(ChatMessage.LOGIN, "", 0));
			txtChat.setText("");
			connected = true;
			txtChat.addActionListener(this);
			btnLogin.setEnabled(false);
			btnLogout.setEnabled(true);
			btnStart.setEnabled(true);
			txtName.setEditable(false);
			txtPort.setEnabled(false);
		}
	}
}
