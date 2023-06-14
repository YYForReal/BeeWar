package windows;

import java.awt.*;//����
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.*;//���

public class Windows extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	// GUI
	JButton addKing, addWorker, addGuard;
	JPanel panel, panelShow, panelNorth, panelAdd;
	JPanel kingPanel, babyPanel, workerPanel, guardPanel;
	JLabel allLabel, kingLabel, workerLabel, guardLabel, babyLabel;// �۷��ǩ
	JLabel honeyLabel;// ���۱�ǩ
	JTextArea eventRecord;
	JPopupMenu popupMenu;
	JMenuItem menuItem[] = new JMenuItem[2];	
	
	// data
	int allNum, kingNum, workerNum, guardNum, babyNum;// ���۷�����
	int honeyNum;// ����
	boolean endFlag = false;
	long startGameTime, endGameTime;

	public Windows() {
		allNum = 3;
		kingNum = 0;
		workerNum = 0;
		guardNum = 0;
		babyNum = 3;
		createAndShowGUI();
	}

	public void createPopupMenu() {
		popupMenu = new JPopupMenu("����ʽ�˵�");
		menuItem[0] = new JMenuItem("����");
		menuItem[1] = new JMenuItem("�˳�");
		for(int i=0;i<2;i++) {
			popupMenu.add(menuItem[i]);			
		}
        panel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                //��ȡ�������λ��
            	if (e.getButton() == MouseEvent.BUTTON3) {
            		popupMenu.show(panel, e.getX(), e.getY());
            		System.out.println("position:   " + e.getX() +"  "+ e.getY());
            	}      	
            }
        });
        menuItem[0].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	System.out.println("����˰���");
            }
        });
        menuItem[1].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	System.out.println("������˳�");
            	
            }
        });
	}
	public void createAndShowGUI() {
		setTitle("BeeWar");
		setVisible(true);
		setBounds(60, 60, 800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new JPanel();
		panel.setLayout(new GridLayout(3, 1));
		add(panel, BorderLayout.CENTER);
		panelAdd = new JPanel();
		panelAdd.setLayout(new GridLayout(1, 3));
		panelShow = new JPanel();
		panelShow.setLayout(new GridLayout(1, 4));
		panelShow.setSize(800, 40);
		eventRecord = new JTextArea("һ������������--�����������ѡ�\n");
		eventRecord.setFont(new Font("����", Font.BOLD, 20));
		eventRecord.setEditable(false);// ���ɸ���
		eventRecord.setLineWrap(true);// �Զ�����
//		eventRecord.setWrapStyleWord(true);// ������в����ֹ���

		JScrollPane scrollPane = new JScrollPane();// �������������
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);// ���ù������Զ�����
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);//���ù��������ǳ���
//		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setViewportView(eventRecord);// �����ǹؼ���������add����text1����ŵ����������
//		panelEvent.add(eventRecord);
		panel.add(panelShow);
		panel.add(scrollPane);
		panel.add(panelAdd);
		
		addKing = new JButton("+����");
		addWorker = new JButton("+����");
		addGuard = new JButton("+��ʿ");

		addKing.addActionListener(this);
		addWorker.addActionListener(this);
		addGuard.addActionListener(this);

		panelAdd.add(addKing);
		panelAdd.add(addWorker);
		panelAdd.add(addGuard);

		panelNorth = new JPanel();
		panelNorth.setLayout(new GridLayout(2, 3));
		panelNorth.setBackground(Color.cyan);
		// allLabel,kingLabel,workerLabel,guardLabel,babyLabel
		allLabel = new JLabel("���и�����" + allNum);
		kingLabel = new JLabel("����������" + kingNum);
		workerLabel = new JLabel("���������" + workerNum);
		guardLabel = new JLabel("��ʿ������" + guardNum);
		babyLabel = new JLabel("���Ѹ�����" + babyNum);
		honeyLabel = new JLabel("ʣ����ۣ�" + honeyNum);
		panelNorth.add(allLabel);
		panelNorth.add(kingLabel);
		panelNorth.add(workerLabel);
		panelNorth.add(guardLabel);
		panelNorth.add(babyLabel);
		panelNorth.add(honeyLabel);
		add(panelNorth, BorderLayout.NORTH);
		createPanelShow();
		startGameTime = System.currentTimeMillis();
		//��������ʽ�˵�
		createPopupMenu();
		
		// ���������߳�
		CreateBabyThread cbt = new CreateBabyThread();
		cbt.start();
		CreateSourceThread cst = new CreateSourceThread();
		cst.start();
		UseSourceThread ust = new UseSourceThread();
		ust.start();
		InvasionThread it = new InvasionThread();
		it.start();
		JudgeEndThread jet = new JudgeEndThread();
		jet.start();
	}

	public void createPanelShow() {
		kingPanel = new JPanel();
		babyPanel = new JPanel();
		workerPanel = new JPanel();
		guardPanel = new JPanel();
		kingPanel.setSize(new Dimension(200, 160));
		babyPanel.setSize(new Dimension(200, 160));
		workerPanel.setSize(new Dimension(200, 160));
		guardPanel.setSize(new Dimension(200, 160));

		kingPanel.setName("kingPanel");
		babyPanel.setName("babyPanel");
		workerPanel.setName("workerPanel");
		guardPanel.setName("guardPanel");
		refreshPanelShow();
		panelShow.add(kingPanel);
		panelShow.add(babyPanel);
		panelShow.add(workerPanel);
		panelShow.add(guardPanel);

	}
	public void refreshPanelShow() {
		divideGrid(kingPanel, kingNum);
		divideGrid(babyPanel, babyNum);
		divideGrid(workerPanel, workerNum);
		divideGrid(guardPanel, guardNum);
		validate();
		allLabel.setText("���и�����" + allNum);
	}

	class CreateBabyThread extends Thread {// ͨ��ʵʱ�ķ��������������۷�����ٶ�
		public void run() {
			while (!endFlag) {
				validate();
				if (kingNum > 0 && babyNum < 40) {
					long startTime = System.currentTimeMillis();
					long endTime = System.currentTimeMillis();
					while (kingNum != 0 && endTime - startTime < 1000 * (10 / kingNum)) {
						endTime = System.currentTimeMillis();
						try {
							Thread.sleep(15);
						} catch (Exception e) {
						}
					}
					allNum++;
					babyNum++;
					allLabel.setText("���и�����" + allNum);
					if (babyNum == 40) {
						babyLabel.setText("���Ѹ�����" + babyNum + "(max)");
					} else {
						babyLabel.setText("���Ѹ�����" + babyNum);
					}
					refreshPanelShow();
				}
			}
		}
	}

	class CreateSourceThread extends Thread {// ͨ�������������ʵʱ���Ӳ�������
		public void run() {
			while (!endFlag) {
				validate();// ����һ������
				if (workerNum > 0) {
					long startTime = System.currentTimeMillis();
					long endTime = System.currentTimeMillis();
					while (workerNum != 0 && endTime - startTime < 1000 / workerNum) {
						endTime = System.currentTimeMillis();
						try {
							Thread.sleep(15);
						} catch (Exception e) {
						}
					}
					honeyNum++;
					honeyLabel.setText("ʣ����ۣ�" + honeyNum);
					refreshPanelShow();
				}
			}
		}
	}
	class UseSourceThread extends Thread {// ͨ�����۷��������ʵʱ���ٷ��ۣ����ʹ����ϣ����۷�������
		public void run() {
			while (!endFlag) {
				validate();// ����һ������
				if ((allNum - babyNum) > 0) {
					long startTime = System.currentTimeMillis();
					long endTime = System.currentTimeMillis();
					while (endTime - startTime < 10000) {
						endTime = System.currentTimeMillis();
						try {
							Thread.sleep(15);
						} catch (Exception e) {
						}
					}
					int honeyConsumption = kingNum * 5 + workerNum * 1 + guardNum * 2;
					if (honeyNum > honeyConsumption) {
						eventRecord.append("�µ�һ�쿪ʼ�ˣ���ҳ���" + honeyConsumption + "����ۣ�\r\n");
						honeyNum -= honeyConsumption;
					} else {
						eventRecord.append("�µ�һ�쿪ʼ�ˣ���ҳԲ���--�����۷�����ˣ�\r\n");
						honeyNum = 0;
						deleteBeeNum(1);
						divideGrid(babyPanel, babyNum);
						divideGrid(workerPanel, workerNum);
						divideGrid(guardPanel, guardNum);
					}
					honeyLabel.setText("ʣ����ۣ�" + honeyNum);
					validate();
					allLabel.setText("���и�����" + allNum);
					eventRecord.setCaretPosition(eventRecord.getText().length());// ��ʱ������ʾ������
					if(allNum == 0) {
						endFlag = true;
					}
				}
			}
		}
	}
	class InvasionThread extends Thread {// ͨ�����ۣ��ʵ�����Σ���¼������ĸ���
		public void run() {
			while (!endFlag) {
				validate();// ����һ������
				if (honeyNum > 50) {
					if (honeyNum < 100) {// ������ȡ���ۣ����س̶ȣ�1��
						try {
							int time = (int) (Math.random() * 8000 + 2000);
							Thread.sleep(time);
						} catch (Exception e) {
						}
						stealHoney(1);

					} else if (honeyNum < 200) {// ������ȡ���ۣ�2���� ���� ���乥����1����
						try {
							int time = (int) (Math.random() * 10000 + 5000);
							Thread.sleep(time);
						} catch (Exception e) {
						}
						// ͨ�������ż���������� ���໹�� ����
						int random = (int) (Math.random() * 10);
						if (random % 2 == 0) {
							stealHoney(2);
						} else {
							eventRecord.append("С��ģ��������...\n");
							attackBee(1);
						}
						divideGrid(workerPanel, workerNum);
						divideGrid(guardPanel, guardNum);

					} else { // ������ȡ���� ���� ���ģ���乥��
						try {
							int time = (int) (Math.random() * 10000 + 6000);
							Thread.sleep(time);
						} catch (Exception e) {
						}
						//
						int random = (int) (Math.random() * 10);
						if (random % 2 == 0) {
							stealHoney(2);
						} else {
							eventRecord.append("���ģ��������...\n");
							attackBee(2);
						}
						refreshPanelShow();
					}
					honeyLabel.setText("ʣ����ۣ�" + honeyNum);
				}
				try {
					Thread.sleep(15);
				} catch (Exception e) {
				}
			}

		}
	}

	class JudgeEndThread extends Thread {// �ж��Ƿ�ý�����Ϸ
		public void run() {
			while (true) {
				if (endFlag == true) {
					eventRecord.append("��Ϸ������");
					if(honeyNum == 500) {
						eventRecord.append("���������ﵽ500,������ʢ��");
					}
					break;
				} else {
					try {
						Thread.sleep(500);
					} catch (Exception e) {
					}
				}
				repaint();//�ػ�
			}
			endGameTime = System.currentTimeMillis();
			eventRecord.append("�������һ��������" + ((endGameTime - startGameTime) / 1000) + "�롣");
		}
	}

	void stealHoney(int level) {// ͨ����ͬ�ĵȼ�ģ��������ȡ����
		if (level == 1) {
			int steal = (int) (Math.random() * 25 + 3 - guardNum);
			int die = (int) (Math.random() * 8 - 2);
			if (steal > 0) {
				eventRecord.append("���۷��ȥ����ʱ��һЩ������ȡ��" + steal + "�����\n");
				honeyNum -= steal;
			} else {
				eventRecord.append("���ڱ����õ��������޷���ȡ����\n");
			}
			if (die > 0) {
				if (die > guardNum) {
					die = guardNum;
				}
				guardNum -= die;
				guardLabel.setText("��ʿ������" + guardNum);
				eventRecord.append("Ϊ���������࣬" + die + "ֻ��ʿ׳�������ˡ�\n");
			}
		} else if (level == 2) {
			int steal = (int) (Math.random() * 100 + 1 - guardNum * 2);
			int die = (int) (Math.random() * guardNum / 10 - 2);
			if (steal > 0) {
				eventRecord.append("���۷��ȥ����ʱ���϶�������ȡ��" + steal + "�����\n");
				honeyNum -= steal;
			} else {
				eventRecord.append("���ڱ����õ��������޷���ȡ����\n");
			}
			if (die > 0) {
				guardNum -= die;
				guardLabel.setText("��ʿ������" + guardNum);
				eventRecord.append("Ϊ���������࣬" + die + "ֻ��ʿ׳�������ˡ�\n");
			}
		}
		eventRecord.setCaretPosition(eventRecord.getText().length());// ��ʱ������ʾ������
	}

	void attackBee(int level) {// ͨ����ͬ�ĵȼ�ģ����乥���۷�
		int num = 0;
		if (level == 1) {
			num = (int) (Math.random() * 4 + 1);
		} else if (level == 2) {
			num = (int) (Math.random() * 6 + 2);
		}
		eventRecord.append("���������" + num + "\n");
		eventRecord.setCaretPosition(eventRecord.getText().length());// ��ʱ������ʾ������

		while (num > 0) {
			if (guardNum > 0) {// ��
				int die1 = (int) (Math.random() * num + 1);
				int die2 = (int) (Math.random() * guardNum + 1);
				if (guardNum - die1 > 0) {
					guardNum -= die1;
					guardLabel.setText("��ʿ������" + guardNum);
					eventRecord.append("������ս���У�" + die1 + "ֻ��ʿ׳�������ˡ�\n");
				} else {
					guardNum = 0;
					guardLabel.setText("��ʿ������" + guardNum);
					eventRecord.append("������ս���У�����ֻ��ʿ׳�������ˡ�\n");
				}
				if (num - die2 > 0) {
					num -= die2;
					eventRecord.append(die2 + "ֻ�������ˣ���ʣ�� " + num + "ֻ����\n");
				} else {
					num = 0;
					eventRecord.append("���к��䶼���ˡ�\n");
				}
				try {
					Thread.sleep(100);
				} catch (Exception e) {
				}
			} else {// ʳ���۷�
				eventRecord.append("���俪ʼ�������۷���...���뾡�������ʿ����!!!��\n");
				try {
					deleteBeeNum(2);
					Thread.sleep(3000);
				} catch (Exception e) {
				}
				if (allNum == 0) {
					endFlag = true;
					eventRecord.append("\n��Ϸ����\n");
				}
			}
		}
		eventRecord.append("���䱻����\n");
	}

	void deleteBeeNum(int kind) {
		/*
		 * @param kind:�����۷������������ kind == 1 �� ��Դ���� kind == 2 �� �������
		 */
		if (guardNum > 0) {
			guardNum--;
			allNum--;
			allLabel.setText("���и�����" + allNum);
			guardLabel.setText("��ʿ������" + guardNum);
		} else {
			if (kind == 1) {
				if (kingNum > 0) {
					int random = (int) Math.random() * kingNum + 1;
					kingNum -= random;
					allNum -= random;
					allLabel.setText("���и�����" + allNum);
					kingLabel.setText("����������" + kingNum);
				} else if (workerNum > 0) {
					int random = (int) Math.random() * kingNum + 1;
					workerNum -= random;
					allNum -= random;
					allLabel.setText("���и�����" + allNum);
					workerLabel.setText("���������" + workerNum);
				}
			} else if (kind == 2) {
				if (workerNum > 0) {
					int random = (int) Math.random() * kingNum + 1;
					workerNum -= random;
					allNum -= random;
					allLabel.setText("���и�����" + allNum);
					workerLabel.setText("���������" + workerNum);
				} else if (kingNum > 0) {
					int random = (int) Math.random() * kingNum + 1;
					kingNum -= random;
					allNum -= random;
					allLabel.setText("���и�����" + allNum);
					kingLabel.setText("����������" + kingNum);
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int maxKing = 5;

		if (babyNum > 0) {
			if (e.getSource() == addKing && kingNum < maxKing) {
				kingNum++;
				babyNum--;
				divideGrid(kingPanel, kingNum);
				divideGrid(babyPanel, babyNum);
				validate();
				if (kingNum == maxKing) {
					kingLabel.setText("����������" + kingNum + "(max)");
				} else {
					kingLabel.setText("����������" + kingNum);
				}

			} else if (e.getSource() == addWorker) {
				workerNum++;
				babyNum--;
				workerLabel.setText("���������" + workerNum);
				divideGrid(workerPanel, workerNum);
				divideGrid(babyPanel, babyNum);
			} else if (e.getSource() == addGuard) {
				guardNum++;
				babyNum--;
				guardLabel.setText("��ʿ������" + guardNum);
				divideGrid(guardPanel, guardNum);
				divideGrid(babyPanel, babyNum);
			}
			validate();

			babyLabel.setText("���Ѹ�����" + babyNum);

		}
	}

	public void divideGrid(JPanel panel, int count) {// ͨ���������ֽ�����к��С�
		panel.removeAll();
		validate();
		allLabel.setText("���и�����" + allNum);
		if (count == 0) {
			String address = "D:/test/zero.jpg";
			File file = new File(address);
			ImageIcon image = new ImageIcon(file.getAbsolutePath());
			JLabel imageLabel = new JLabel();
			imageLabel.setIcon(image);
			image.setImage(image.getImage().getScaledInstance(panel.getWidth(), panel.getHeight(), Image.SCALE_SMOOTH));
			panel.add(imageLabel);
			return;
		}
		int column = 1, row;
		if (count == 1) { // ���� 1����Ƭ���
			column = 1;
			row = 1;
		} else {
			for (row = count - 1; row > 1; row--) {
				if (row == count / 2) {
					continue;
				}
				if (count % row == 0) {// �������ɲ��Ϊ2�����ϵĴ������
					column = count / row;
					break;
				}
			}
			if (row == 1) {// ���������������� ȡ �ӽ� �俪ƽ���� ����
				if (count >= 3) {
					column = (int) (count / Math.sqrt((double) count)) + 1;
					if (count % column == 0) {
						row = count / column;
					} else {
						row = count / column + 1;
					}
				} else {
					column = count;
				}
			}
		}
		panel.setLayout(new GridLayout(row, column));
		String address = "D:/test/";
		ImageIcon image;// ��ʱ�洢ͼƬ�ı���
		if (panel.getName().equals("kingPanel")) {
			address = address + "king.jpg";
		} else if (panel.getName().equals("babyPanel")) {
			address = address + "baby.jpg";
		} else if (panel.getName().equals("workerPanel")) {
			address = address + "worker.jpg";
		} else if (panel.getName().equals("guardPanel")) {
			address = address + "guard.jpg";
		}
		File file = new File(address);
		image = new ImageIcon(file.getAbsolutePath());
		JLabel imageLabel = new JLabel();
		imageLabel.setIcon(image);
		int width = (int) (panel.getWidth() / column);// ��������ͼƬ�Ĵ�С�ʹ��ڵĴ�С���
		int height = (int) (panel.getHeight() / row);
		image.setImage(image.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
//		image.setImage(image.getImage().getScaledInstance(panel.getWidth() / column ,panel.getHeight() / row, Image.SCALE_AREA_AVERAGING));
		for (int i = 0; i < count; i++) {
			JLabel temp = new JLabel();
			temp.setIcon(image);
			panel.add(temp);
		}
	}

	public static void main(String[] args) {
		new Windows();
	}

}
