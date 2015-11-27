package GameOff;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CreateJPanel extends JFrame implements ActionListener {
	JPanel panel1, panel2;
	static JButton button[][];
	static JLabel label1;
	static JLabel label2;
	static JLabel label3;
	JLabel label4;
	static int[][] x = new int[5][5];// Ma trÃ¢Ì£n chÆ°Ì�a 25 sÃ´Ì� hiÃªÌ‰n
										// thiÌ£ lÃªn
	// Button
	static int point = 0;// Ä�iÃªÌ‰m
	static int timeRemain = 20;// ThÆ¡Ì€i gian coÌ€n laÌ£i
	static int[] numberSelected = new int[25]; // 3 biÃªÌ�n chÆ°Ì�a sÃ´Ì�
												// Ä‘Æ°Æ¡Ì£c
	static int[] numberSelectedX = new int[25]; // choÌ£n vaÌ€ viÌ£ triÌ�
	static int[] numberSelectedY = new int[25]; // cuÌ‰a noÌ�
	static int h = 0; // h laÌ€ chiÌ‰ sÃ´Ì� cuÌ‰a maÌ‰ng numberSelected
	static int groupMax = 0;

	public CreateJPanel() {
		createJFrame();
	}

	// TaÌ£o giao diÃªÌ£n chiÌ�nh
	private void createJFrame() {
		setTitle("Java Project");
		setLayout(new GridLayout(2, 1));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createContent();
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void createContent() {
		// TaÌ£o Panel thÆ°Ì� nhÃ¢Ì�t
		panel1 = new JPanel(new GridLayout(2, 2));
		label1 = new JLabel("Point: " + point);
		label2 = new JLabel("Time: " + timeRemain);
		label3 = new JLabel("SÃ´Ì� lÃ¢Ì€n choÌ£n: ");
		label4 = new JLabel("SÃ´Ì� Ä‘Æ°Æ¡Ì£c choÌ£n: ");
		panel1.add(label2);
		//panel1.add(label3);
		panel1.add(label1);
		//panel1.add(label4);
		// TaÌ£o Panel thÆ°Ì� 2 gÃ´Ì€m 25 Button
		panel2 = new JPanel(new GridLayout(5, 5));
		int k = 0;// ChiÌ‰ sÃ´Ì� cuÌ‰a caÌ�c phÃ¢Ì€n tÆ°Ì€ trong Array
		ArrayList<Integer> array = new ArrayList<>();
		for (int i = 0; i < 25; i++) {
			array.add(i + 1);
		}
		Collections.shuffle(array);// Ä�aÌ‰o giaÌ� triÌ£ caÌ�c phÃ¢Ì€n tÆ°Ì‰
									// trong array
		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 5; j++) {
				x[i][j] = array.get(k);
				k++;
			}
		// TaÌ£o button vaÌ€ thÃªm vaÌ€o Panel 2
		button = new JButton[5][5];
		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 5; j++) {
				button[i][j] = new JButton(x[i][j] + "");
				panel2.add(button[i][j]);
				button[i][j].addActionListener(this);
			}
		add(panel1);
		add(panel2);
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
					label1.setText("Point: " + point);
					if (point == 5)
						Stop();
					
				}
			}
	}
	public static int UpdatePoint(int x, int y) {
		int groupX = 0;
		int groupY = 0;
		for (int a = 0; a < h; a++) {
			if ((x == numberSelectedX[a])
					&& (Math.abs(y - numberSelectedY[a]) == 1)) {
				groupX = 2;// haÌ€ng 2
				for (int b = 0; b < h; b++) {
					if (((x == numberSelectedX[b])
							&& (Math.abs(y - numberSelectedY[b]) == 1) && (b != a))
							|| ((numberSelectedX[a] == numberSelectedX[b]) && (Math
									.abs(numberSelectedY[a]
											- numberSelectedY[b]) == 1))) {
						groupX = 3;// haÌ€ng 3
						for (int c = 0; c < h; c++) {
							if (((x == numberSelectedX[c])
									&& (Math.abs(y - numberSelectedY[c]) == 1)
									&& (c != a) && (c != b))
									|| ((numberSelectedX[c] == numberSelectedX[a])
											&& (Math.abs(numberSelectedY[c]
													- numberSelectedY[a]) == 1) && (c != b))
									|| ((numberSelectedX[c] == numberSelectedX[b])
											&& (Math.abs(numberSelectedY[c]
													- numberSelectedY[b]) == 1) && (c != a))) {
								groupX = 4;// haÌ€ng 4
								for (int d = 0; d < h; d++) {
									if (((x == numberSelectedX[d])
											&& (Math.abs(y - numberSelectedY[d]) == 1)
											&& (d != a) && (d != b) && (d != c))
											|| ((numberSelectedX[d] == numberSelectedX[a])
													&& (Math.abs(numberSelectedY[d]
															- numberSelectedY[a]) == 1)
													&& (d != b) && (d != c))
											|| ((numberSelectedX[d] == numberSelectedX[b])
													&& (Math.abs(numberSelectedY[d]
															- numberSelectedY[b]) == 1)
													&& (d != a) && (d != c))
											|| ((numberSelectedX[d] == numberSelectedX[c])
													&& (Math.abs(numberSelectedY[d]
															- numberSelectedY[c]) == 1)
													&& (d != b) && (d != a)))
										groupX = 5;// haÌ€ng 5
								}
							}
						}
					}
				}
			}
		}

		for (int a = 0; a < h; a++) {
			if ((y == numberSelectedY[a])
					&& (Math.abs(x - numberSelectedX[a]) == 1)) {
				groupY = 2;// haÌ€ng 2
				for (int b = 0; b < h; b++) {
					if (((y == numberSelectedY[b])
							&& (Math.abs(x - numberSelectedX[b]) == 1) && (b != a))
							|| ((numberSelectedY[a] == numberSelectedY[b]) && (Math
									.abs(numberSelectedX[a]
											- numberSelectedX[b]) == 1))) {
						groupY = 3;// haÌ€ng 3
						for (int c = 0; c < h; c++) {
							if (((y == numberSelectedY[c])
									&& (Math.abs(x - numberSelectedX[c]) == 1)
									&& (c != a) && (c != b))
									|| ((numberSelectedY[c] == numberSelectedY[a])
											&& (Math.abs(numberSelectedX[c]
													- numberSelectedX[a]) == 1) && (c != b))
									|| ((numberSelectedY[c] == numberSelectedY[b])
											&& (Math.abs(numberSelectedX[c]
													- numberSelectedX[b]) == 1) && (c != a))) {
								groupY = 4;// haÌ€ng 4
								for (int d = 0; d < h; d++) {
									if (((y == numberSelectedY[d])
											&& (Math.abs(x - numberSelectedX[d]) == 1)
											&& (d != a) && (d != b) && (d != c))
											|| ((numberSelectedY[d] == numberSelectedY[a])
													&& (Math.abs(numberSelectedX[d]
															- numberSelectedX[a]) == 1)
													&& (d != b) && (d != c))
											|| ((numberSelectedY[d] == numberSelectedY[b])
													&& (Math.abs(numberSelectedX[d]
															- numberSelectedX[b]) == 1)
													&& (d != a) && (d != c))
											|| ((numberSelectedY[d] == numberSelectedY[c])
													&& (Math.abs(numberSelectedX[d]
															- numberSelectedX[c]) == 1)
													&& (d != b) && (d != a)))
										groupY = 5;// haÌ€ng 5
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
		timeRemain = 0;
	}

	public static void RanDom() {
		Integer random;
		int count = 0;
		Random rand = new Random();
		do {
			random = rand.nextInt(25);
			for (int i = 0; i < h; i++) {
				if (random != numberSelected[i]) {
					count++;
				}
			}
		} while (count != h);
		if (count == h) {
			for (int i = 0; i < 5; i++)
				for (int j = 0; j < 5; j++) {
					if (button[i][j].getText().equals(random.toString())) {
						button[i][j].setEnabled(false);
						numberSelectedX[h] = i;
						numberSelectedY[h] = j;
						point = UpdatePoint(numberSelectedX[h],
								numberSelectedY[h]);
						h++;
						timeRemain = 20;
						label1.setText("Point: " + point);
						if (groupMax == 5)
							Stop();
					}
				}
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 5; j++) {
				if (e.getSource() == button[i][j]) {
					ButtonSelected(button[i][j].getText());
				}

			}
	}

	public static void main(String[] args) throws InterruptedException {
		new CreateJPanel();
		// Ä�ÃªÌ�m ngÆ°Æ¡Ì£c thÆ¡Ì€i gian
		while (timeRemain > 0) {
			timeRemain--;
			Thread.sleep(1000);
			label2.setText("Time: " + timeRemain);
			if (timeRemain == 0)
				RanDom();
		}
	}

}