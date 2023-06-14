package windows;

import java.awt.*;//布局
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.*;//组件

public class Windows extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	// GUI
	JButton addKing, addWorker, addGuard;
	JPanel panel, panelShow, panelNorth, panelAdd;
	JPanel kingPanel, babyPanel, workerPanel, guardPanel;
	JLabel allLabel, kingLabel, workerLabel, guardLabel, babyLabel;// 蜜蜂标签
	JLabel honeyLabel;// 蜂蜜标签
	JTextArea eventRecord;
	JPopupMenu popupMenu;
	JMenuItem menuItem[] = new JMenuItem[2];	
	
	// data
	int allNum, kingNum, workerNum, guardNum, babyNum;// 各蜜蜂数量
	int honeyNum;// 蜂蜜
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
		popupMenu = new JPopupMenu("弹出式菜单");
		menuItem[0] = new JMenuItem("帮助");
		menuItem[1] = new JMenuItem("退出");
		for(int i=0;i<2;i++) {
			popupMenu.add(menuItem[i]);			
		}
        panel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                //获取鼠标点击的位置
            	if (e.getButton() == MouseEvent.BUTTON3) {
            		popupMenu.show(panel, e.getX(), e.getY());
            		System.out.println("position:   " + e.getX() +"  "+ e.getY());
            	}      	
            }
        });
        menuItem[0].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	System.out.println("点击了帮助");
            }
        });
        menuItem[1].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	System.out.println("点击了退出");
            	
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
		eventRecord = new JTextArea("一个王国诞生了--带着三个蜂卵。\n");
		eventRecord.setFont(new Font("仿宋", Font.BOLD, 20));
		eventRecord.setEditable(false);// 不可更改
		eventRecord.setLineWrap(true);// 自动换行
//		eventRecord.setWrapStyleWord(true);// 激活断行不断字功能

		JScrollPane scrollPane = new JScrollPane();// 创建滚动条面板
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);// 设置滚动条自动出现
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);//设置滚动条总是出现
//		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setViewportView(eventRecord);// （这是关键！不是用add）把text1组件放到滚动面板里
//		panelEvent.add(eventRecord);
		panel.add(panelShow);
		panel.add(scrollPane);
		panel.add(panelAdd);
		
		addKing = new JButton("+蜂王");
		addWorker = new JButton("+工蜂");
		addGuard = new JButton("+卫士");

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
		allLabel = new JLabel("所有个数：" + allNum);
		kingLabel = new JLabel("蜂王个数：" + kingNum);
		workerLabel = new JLabel("工蜂个数：" + workerNum);
		guardLabel = new JLabel("卫士个数：" + guardNum);
		babyLabel = new JLabel("蜂卵个数：" + babyNum);
		honeyLabel = new JLabel("剩余蜂蜜：" + honeyNum);
		panelNorth.add(allLabel);
		panelNorth.add(kingLabel);
		panelNorth.add(workerLabel);
		panelNorth.add(guardLabel);
		panelNorth.add(babyLabel);
		panelNorth.add(honeyLabel);
		add(panelNorth, BorderLayout.NORTH);
		createPanelShow();
		startGameTime = System.currentTimeMillis();
		//创建弹出式菜单
		createPopupMenu();
		
		// 创建各个线程
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
		allLabel.setText("所有个数：" + allNum);
	}

	class CreateBabyThread extends Thread {// 通过实时的蜂王数量，控制蜜蜂产卵速度
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
					allLabel.setText("所有个数：" + allNum);
					if (babyNum == 40) {
						babyLabel.setText("蜂卵个数：" + babyNum + "(max)");
					} else {
						babyLabel.setText("蜂卵个数：" + babyNum);
					}
					refreshPanelShow();
				}
			}
		}
	}

	class CreateSourceThread extends Thread {// 通过工蜂的数量，实时增加产蜜速率
		public void run() {
			while (!endFlag) {
				validate();// 更新一下数据
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
					honeyLabel.setText("剩余蜂蜜：" + honeyNum);
					refreshPanelShow();
				}
			}
		}
	}
	class UseSourceThread extends Thread {// 通过各蜜蜂的数量，实时减少蜂蜜，如果使用完毕，则蜜蜂逐渐死亡
		public void run() {
			while (!endFlag) {
				validate();// 更新一下数据
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
						eventRecord.append("新的一天开始了（大家吃了" + honeyConsumption + "点蜂蜜）\r\n");
						honeyNum -= honeyConsumption;
					} else {
						eventRecord.append("新的一天开始了（大家吃不饱--部分蜜蜂饿死了）\r\n");
						honeyNum = 0;
						deleteBeeNum(1);
						divideGrid(babyPanel, babyNum);
						divideGrid(workerPanel, workerNum);
						divideGrid(guardPanel, guardNum);
					}
					honeyLabel.setText("剩余蜂蜜：" + honeyNum);
					validate();
					allLabel.setText("所有个数：" + allNum);
					eventRecord.setCaretPosition(eventRecord.getText().length());// 及时更新显示的内容
					if(allNum == 0) {
						endFlag = true;
					}
				}
			}
		}
	}
	class InvasionThread extends Thread {// 通过蜂蜜，适当增大危机事件发生的概率
		public void run() {
			while (!endFlag) {
				validate();// 更新一下数据
				if (honeyNum > 50) {
					if (honeyNum < 100) {// 人类窃取蜂蜜，严重程度：1级
						try {
							int time = (int) (Math.random() * 8000 + 2000);
							Thread.sleep(time);
						} catch (Exception e) {
						}
						stealHoney(1);

					} else if (honeyNum < 200) {// 人类窃取蜂蜜（2级） 或者 胡蜂攻击（1级）
						try {
							int time = (int) (Math.random() * 10000 + 5000);
							Thread.sleep(time);
						} catch (Exception e) {
						}
						// 通过随机奇偶性来决定是 人类还是 胡蜂
						int random = (int) (Math.random() * 10);
						if (random % 2 == 0) {
							stealHoney(2);
						} else {
							eventRecord.append("小规模胡蜂侵略...\n");
							attackBee(1);
						}
						divideGrid(workerPanel, workerNum);
						divideGrid(guardPanel, guardNum);

					} else { // 人类窃取蜂蜜 或者 大规模胡蜂攻击
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
							eventRecord.append("大规模胡蜂侵略...\n");
							attackBee(2);
						}
						refreshPanelShow();
					}
					honeyLabel.setText("剩余蜂蜜：" + honeyNum);
				}
				try {
					Thread.sleep(15);
				} catch (Exception e) {
				}
			}

		}
	}

	class JudgeEndThread extends Thread {// 判断是否该结束游戏
		public void run() {
			while (true) {
				if (endFlag == true) {
					eventRecord.append("游戏结束！");
					if(honeyNum == 500) {
						eventRecord.append("蜂蜜数量达到500,王国鼎盛！");
					}
					break;
				} else {
					try {
						Thread.sleep(500);
					} catch (Exception e) {
					}
				}
				repaint();//重画
			}
			endGameTime = System.currentTimeMillis();
			eventRecord.append("这个王国一共延续了" + ((endGameTime - startGameTime) / 1000) + "秒。");
		}
	}

	void stealHoney(int level) {// 通过不同的等级模拟人类窃取蜂蜜
		if (level == 1) {
			int steal = (int) (Math.random() * 25 + 3 - guardNum);
			int die = (int) (Math.random() * 8 - 2);
			if (steal > 0) {
				eventRecord.append("趁蜜蜂出去采蜜时，一些人类窃取了" + steal + "点蜂蜜\n");
				honeyNum -= steal;
			} else {
				eventRecord.append("由于保护得当，人类无法窃取蜂蜜\n");
			}
			if (die > 0) {
				if (die > guardNum) {
					die = guardNum;
				}
				guardNum -= die;
				guardLabel.setText("卫士个数：" + guardNum);
				eventRecord.append("为了驱赶人类，" + die + "只卫士壮烈牺牲了。\n");
			}
		} else if (level == 2) {
			int steal = (int) (Math.random() * 100 + 1 - guardNum * 2);
			int die = (int) (Math.random() * guardNum / 10 - 2);
			if (steal > 0) {
				eventRecord.append("趁蜜蜂出去采蜜时，较多人类窃取了" + steal + "点蜂蜜\n");
				honeyNum -= steal;
			} else {
				eventRecord.append("由于保护得当，人类无法窃取蜂蜜\n");
			}
			if (die > 0) {
				guardNum -= die;
				guardLabel.setText("卫士个数：" + guardNum);
				eventRecord.append("为了驱赶人类，" + die + "只卫士壮烈牺牲了。\n");
			}
		}
		eventRecord.setCaretPosition(eventRecord.getText().length());// 及时更新显示的内容
	}

	void attackBee(int level) {// 通过不同的等级模拟胡蜂攻击蜜蜂
		int num = 0;
		if (level == 1) {
			num = (int) (Math.random() * 4 + 1);
		} else if (level == 2) {
			num = (int) (Math.random() * 6 + 2);
		}
		eventRecord.append("胡蜂个数：" + num + "\n");
		eventRecord.setCaretPosition(eventRecord.getText().length());// 及时更新显示的内容

		while (num > 0) {
			if (guardNum > 0) {// 打斗
				int die1 = (int) (Math.random() * num + 1);
				int die2 = (int) (Math.random() * guardNum + 1);
				if (guardNum - die1 > 0) {
					guardNum -= die1;
					guardLabel.setText("卫士个数：" + guardNum);
					eventRecord.append("与胡蜂的战斗中，" + die1 + "只卫士壮烈牺牲了。\n");
				} else {
					guardNum = 0;
					guardLabel.setText("卫士个数：" + guardNum);
					eventRecord.append("与胡蜂的战斗中，所有只卫士壮烈牺牲了。\n");
				}
				if (num - die2 > 0) {
					num -= die2;
					eventRecord.append(die2 + "只胡蜂死了，还剩下 " + num + "只胡蜂\n");
				} else {
					num = 0;
					eventRecord.append("所有胡蜂都死了。\n");
				}
				try {
					Thread.sleep(100);
				} catch (Exception e) {
				}
			} else {// 食用蜜蜂
				eventRecord.append("胡蜂开始吃其他蜜蜂了...（请尽快孵化卫士反击!!!）\n");
				try {
					deleteBeeNum(2);
					Thread.sleep(3000);
				} catch (Exception e) {
				}
				if (allNum == 0) {
					endFlag = true;
					eventRecord.append("\n游戏结束\n");
				}
			}
		}
		eventRecord.append("胡蜂被消灭\n");
	}

	void deleteBeeNum(int kind) {
		/*
		 * @param kind:减少蜜蜂数量的情况： kind == 1 ： 资源不足 kind == 2 ： 外敌入侵
		 */
		if (guardNum > 0) {
			guardNum--;
			allNum--;
			allLabel.setText("所有个数：" + allNum);
			guardLabel.setText("卫士个数：" + guardNum);
		} else {
			if (kind == 1) {
				if (kingNum > 0) {
					int random = (int) Math.random() * kingNum + 1;
					kingNum -= random;
					allNum -= random;
					allLabel.setText("所有个数：" + allNum);
					kingLabel.setText("蜂王个数：" + kingNum);
				} else if (workerNum > 0) {
					int random = (int) Math.random() * kingNum + 1;
					workerNum -= random;
					allNum -= random;
					allLabel.setText("所有个数：" + allNum);
					workerLabel.setText("工蜂个数：" + workerNum);
				}
			} else if (kind == 2) {
				if (workerNum > 0) {
					int random = (int) Math.random() * kingNum + 1;
					workerNum -= random;
					allNum -= random;
					allLabel.setText("所有个数：" + allNum);
					workerLabel.setText("工蜂个数：" + workerNum);
				} else if (kingNum > 0) {
					int random = (int) Math.random() * kingNum + 1;
					kingNum -= random;
					allNum -= random;
					allLabel.setText("所有个数：" + allNum);
					kingLabel.setText("蜂王个数：" + kingNum);
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
					kingLabel.setText("蜂王个数：" + kingNum + "(max)");
				} else {
					kingLabel.setText("蜂王个数：" + kingNum);
				}

			} else if (e.getSource() == addWorker) {
				workerNum++;
				babyNum--;
				workerLabel.setText("工蜂个数：" + workerNum);
				divideGrid(workerPanel, workerNum);
				divideGrid(babyPanel, babyNum);
			} else if (e.getSource() == addGuard) {
				guardNum++;
				babyNum--;
				guardLabel.setText("卫士个数：" + guardNum);
				divideGrid(guardPanel, guardNum);
				divideGrid(babyPanel, babyNum);
			}
			validate();

			babyLabel.setText("蜂卵个数：" + babyNum);

		}
	}

	public void divideGrid(JPanel panel, int count) {// 通过总数划分界面的行和列。
		panel.removeAll();
		validate();
		allLabel.setText("所有个数：" + allNum);
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
		if (count == 1) { // 特判 1张照片情况
			column = 1;
			row = 1;
		} else {
			for (row = count - 1; row > 1; row--) {
				if (row == count / 2) {
					continue;
				}
				if (count % row == 0) {// 若该数可拆分为2及以上的大数相乘
					column = count / row;
					break;
				}
			}
			if (row == 1) {// 若该数是质数，则 取 接近 其开平方根 的数
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
		ImageIcon image;// 暂时存储图片的变量
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
		int width = (int) (panel.getWidth() / column);// 这里设置图片的大小和窗口的大小相等
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
