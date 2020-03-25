package system2;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

/**
 * 登录界面
 * 
 * @author LB
 * @create
 */
public class login extends JFrame implements ActionListener {
	JLabel user, password;
	JTextField username;
	JPasswordField passwordField;
	JButton loginButton;
	CardLayout cardLayout = new CardLayout();
	JPanel card;
	JPanel cardPanel;
	JTabbedPane jTabbedPane;
	int type = 1;
	Users users;
	static String str;
	static int b;

	public login() {
		init();
	}

	private void init() {
		setTitle("疫情信息管理系统");
		setLayout(new BorderLayout());
		user = new JLabel("用户名");
		password = new JLabel("密码");

		card = new JPanel(cardLayout);

		JPanel panel1 = new JPanel(new BorderLayout());

		username = new JTextField();
		passwordField = new JPasswordField();
		loginButton = new JButton("登录");
		loginButton.addActionListener(this);

		JPanel titlepanel = new JPanel(new FlowLayout());// 标题面板
		JLabel title = new JLabel("疫情信息管理系统");
		titlepanel.add(title);

		JPanel loginpanel = new JPanel();// 登录面板
		loginpanel.setLayout(null);

		user.setBounds(50, 20, 50, 20);
		password.setBounds(50, 60, 50, 20);
		username.setBounds(110, 20, 120, 20);
		passwordField.setBounds(110, 60, 120, 20);
		loginpanel.add(user);
		loginpanel.add(password);
		loginpanel.add(username);
		loginpanel.add(passwordField);

		panel1.add(titlepanel, BorderLayout.NORTH);
		panel1.add(loginpanel, BorderLayout.CENTER);
		panel1.add(loginButton, BorderLayout.SOUTH);

		card.add(panel1, "login");
		// card.add(cardPanel, "info");

		add(card);
		setBounds(600, 200, 900, 600);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	// 播放铃声
	static void playMusic() {// 背景音乐播放
		try {
			java.net.URL cb;
			File f = new File("src/images/Taylor+Swift+-+Ours.wav"); // 引号里面的是音乐文件所在的路径
			cb = f.toURL();
			AudioClip aau;
			aau = Applet.newAudioClip(cb);

			aau.play();
			aau.loop();// 循环播放
			System.out.println("可以播放");
			// 循环播放 aau.play()
			// 单曲 aau.stop()停止播放

		} catch (MalformedURLException e) {

			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new login();
		String z = JOptionPane.showInputDialog("闹钟:");
		Runnable runnable = new Runnable() {
			// 创建 run 方法
			public void run() {
				// 获取电脑时间
				java.util.Date utildate = new java.util.Date();
				str = DateFormat.getTimeInstance().format(utildate);
				System.out.println(str);
				System.out.println("Hello！");
				b = str.compareTo(z);
				if (b == 0) {
					System.out.println("1！");
					playMusic();
				}
			}
		};
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		// 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
		// 10：秒 1：秒
		// 第一次执行的时间为10秒，然后每隔一秒执行一次
		service.scheduleAtFixedRate(runnable, 10, 1, TimeUnit.SECONDS);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		boolean flag = false;// 用来标志用户是否正确

		if (e.getSource() == loginButton) {
			ArrayList<Users> list = new CheckUsers().getUsers();// 获得所有用户信息
			for (int i = 0; i < list.size(); i++) {// 遍历所有用户信息，以此来判断输入的信息是否正确
				users = list.get(i);
				String passwordStr = new String(passwordField.getPassword());
				if (username.getText().equals(users.getName()) && passwordStr.equals(users.getPassword())) {
					if (users.getType() == 1) {// 学生
						type = users.getType();
						JOptionPane.showMessageDialog(null, "欢迎登录(学生)", "疫情信息管理系统", JOptionPane.PLAIN_MESSAGE);
					}
					if (users.getType() == 5) {// 教职工
						type = users.getType();
						JOptionPane.showMessageDialog(null, "欢迎登录(教职工)", "疫情信息管理系统", JOptionPane.PLAIN_MESSAGE);
					}
					if (users.getType() == 2) {// 二级防疫部门
						type = users.getType();
						JOptionPane.showMessageDialog(null, "欢迎登录(二级防疫部门)", "疫情信息管理系统", JOptionPane.PLAIN_MESSAGE);
					}
					if (users.getType() == 3) {// 二级防疫部门负责人
						type = users.getType();
						System.out.println(type);
						JOptionPane.showMessageDialog(null, "欢迎登录(二级防疫部门负责人)", "疫情信息管理系统", JOptionPane.PLAIN_MESSAGE);
					}
					if (users.getType() == 4) {// 防疫办负责人
						type = users.getType();
						System.out.println(type);
						JOptionPane.showMessageDialog(null, "欢迎登录(防控办)", "疫情信息管理系统", JOptionPane.PLAIN_MESSAGE);
					}
					flag = true;
					break;// 如果信息正确就退出遍历，提高效率
				}
			}
			if (!flag) {// 信息不正确，重新输入
				JOptionPane.showMessageDialog(null, "请输入正确的用户名或密码", "警告", JOptionPane.WARNING_MESSAGE);
				username.setText("");
				passwordField.setText("");
			} else {
				// 当输入的信息正确时，就开始加载选项卡界面，并把选项卡界面加入到卡片布局器中
				DormitoryInfo dormitoryInfo = new DormitoryInfo(users, type);// 宿舍信息
				cardPanel = new JPanel();
				jTabbedPane = new JTabbedPane(JTabbedPane.LEFT);
				jTabbedPane.add("宿舍信息", dormitoryInfo);
				cardPanel.add(jTabbedPane);
				card.add(cardPanel, "info");
				cardLayout.show(card, "info");// 输入信息正确就显示操作界面，否则重新输入正确信息
			}
		}
	}
}
