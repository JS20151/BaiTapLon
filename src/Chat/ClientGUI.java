package Chat;


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

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static JButton button[][];
	int[][] x = new int[5][5];
	private JTextArea save;
	JLabel labelTime;
	JLabel labelPoint;
	private JLabel labelName, labelPort;
	private JTextField txtChat, txtName, txtPort;
	JButton btnLogin, btnLogout, btnStart;
	static int point = 0;//
	int timeRemain = 20;//
	int[] numberSelected = new int[25]; //
	static int[] numberSelectedX = new int[25]; //
	static int[] numberSelectedY = new int[25]; //
	static int h = 0; //
	static int groupMax = 0;
	private int defaultPort;
	private String defaultHost;
	private boolean connected;
	private Client client;
	public boolean startGame;

	ClientGUI(String host, int port) {
		super("Chat Client");
		defaultPort = port;
		defaultHost = host;
		JPanel game = new JPanel(new GridLayout(5, 5));
		int k = 0;
		ArrayList<Integer> array = new ArrayList<>();
		for (int i = 0; i < 25; i++) {
			array.add(i + 1);
		}
		Collections.shuffle(array);
		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 5; j++) {
				x[i][j] = array.get(k);
				k++;
			}
		button = new JButton[5][5];
		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 5; j++) {
				button[i][j] = new JButton(x[i][j] + "");
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
		labelTime = new JLabel("Time: " + timeRemain);
		labelPoint = new JLabel("Point: " + point);
		timeAndPoint.add(labelPoint);
		timeAndPoint.add(labelTime);
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
		JPanel loginLogout = new JPanel(new GridLayout(1, 3));
		btnLogin = new JButton("LOGIN");
		btnLogin.addActionListener(this);
		btnLogout = new JButton("LOGOUT");
		btnLogout.addActionListener(this);
		btnLogout.setEnabled(false);
		btnStart = new JButton("START");
		btnStart.addActionListener(this);
		loginLogout.add(btnLogin);
		loginLogout.add(btnLogout);
		loginLogout.add(btnStart);
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
		for (int i = 0; i < 25; i++) {
			array.add(i + 1);
		}
		Collections.shuffle(array);
		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 5; j++) {
				x[i][j] = array.get(k);
				k++;
			}
		point = 0;
		labelPoint.setText("Point: " + point);
		int h = 0;
		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 5; j++) {
				Integer num = x[i][j];
				button[i][j].setText(num.toString());
				button[i][j].setEnabled(true);
				numberSelected[h] = 26;
				numberSelectedX[h] = 26;
				numberSelectedY[h] = 26;
				groupMax = 0;
				h++;
			}

	}

	public void GameDisable() {
		point = 0;
		labelPoint.setText("Point: " + point);
		int k = 0;
		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 5; j++) {
				button[i][j].setEnabled(false);
				numberSelected[k] = 26;
				numberSelectedX[k] = 26;
				numberSelectedY[k] = 26;
				groupMax = 0;
				k++;
			}

	}

	public void ButtonSelected(String x) {
		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 5; j++) {
				if (button[i][j].getText().equals(x)) {
					button[i][j].setEnabled(false);
					numberSelected[h] = Integer.parseInt(x);
					numberSelectedX[h] = i;
					numberSelectedY[h] = j;
					point = UpdatePoint(numberSelectedX[h], numberSelectedY[h]);
					h++;
					timeRemain = 20;
					labelPoint.setText("Point: " + point);
					if (point == 5)
						Stop();
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

	public static void Stop() {
		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 5; j++) {
				button[i][j].setEnabled(false);
			}
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
			for (int i = 0; i < 5; i++)
				for (int j = 0; j < 5; j++) {
					if (e.getSource() == button[i][j]) {
						ButtonSelected(button[i][j].getText());
						client.sendMessage(new ChatMessage(ChatMessage.NUMBER, button[i][j].getText()));
						if(point == 5)
						client.sendMessage(new ChatMessage(ChatMessage.WIN, ""));
					}
					

				}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o == btnStart) {
			client.sendMessage(new ChatMessage(ChatMessage.START, ""));
			return;
		}
		if (o == btnLogout) {
			client.sendMessage(new ChatMessage(ChatMessage.LOGOUT, ""));
			btnLogout.setEnabled(false);
			btnLogin.setEnabled(true);
			txtName.setEditable(true);
			txtPort.setEnabled(true);
			client.disconnect();
			GameDisable();
			connected = false;
			return;
		}
		if (connected) {
			client.sendMessage(new ChatMessage(ChatMessage.MESSAGE, txtChat.getText()));
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
			txtChat.setText("");
			connected = true;
			txtChat.addActionListener(this);
			btnLogin.setEnabled(false);
			btnLogout.setEnabled(true);
			txtName.setEditable(false);
			txtPort.setEnabled(false);
		}
	}
}
