package com.xxx;

import java.util.*;
import java.awt.*;
import java.net.*;
import java.text.*;
import java.awt.TrayIcon.MessageType;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.*;
import javax.swing.text.BadLocationException;

import com.xxx.info.UserInfo;
import com.xxx.util.MyProtocol;
import com.xxx.frame.*;

public class LanTalk extends JFrame
{
	private DefaultListModel listModel = new DefaultListModel();
	//定义一个JList对象
	private JList friendsList = new JList(listModel);
	//定义一个用于格式化日期的格式器
	public static DateFormat formatter = DateFormat.getDateTimeInstance(); 
	//定义记录本身的UserInfo对象
	private UserInfo selfUser;
	//操作窗口使用
	private JButton minimum;
	private JButton close;
	//移动窗口使用
	private Point pressPoint;
	private boolean isDragging = false;	
	//系统栏隐藏图标
	public static TrayIcon trayicon;

	//主方法，程序的入口
	public static void main(String[] args) 
	{
		LanTalk lc = new LanTalk();
		new LoginFrame(lc , "请输入昵称并选择头像");
	}
	
	public LanTalk()
	{
		//改java窗口风格为windows风格
		try
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch(Exception ex)
		{
			ex.printStackTrace(); 
	    }
		//设置该JList使用ImageCellRenderer作为单元格绘制器
		friendsList.setCellRenderer(new ImageCellRenderer()); 
		listModel.addElement(new UserInfo("所有人" , "所有人" ,
				LanTalk.formatter.format(new Date()), null , -2000));
		friendsList.addMouseListener(new SelectUserListener());
		
		//定义JPanel放组件
		setBG();SystemTrayInitial();
		JPanel bgPanel = new JPanel();
		bgPanel.setLayout(null);
		bgPanel.setOpaque(false);
		this.setContentPane(bgPanel);
		///////////////////////主界面布局//////////////////////////
		friendsList.setOpaque(false);
		JScrollPane jsp = new JScrollPane(friendsList);
		jsp.setOpaque(false);
		jsp.getViewport().setOpaque(false);
		jsp.setBorder(new LineBorder(new Color(207, 93, 39), 1, true));
		bgPanel.add(jsp);
		jsp.setBounds(8, 55, 224, 400);
		///////////////////////添加最小化和结束图标//////////////////////////
		minimum = new JButton(new ImageIcon("src/operation_icon/min_normal.png"));
		minimum.setRolloverIcon(new ImageIcon("src/operation_icon/min_highlight.png"));
		minimum.setPressedIcon(new ImageIcon("src/operation_icon/min_down.png"));
		minimum.setFocusPainted(false);
		minimum.setContentAreaFilled(false);
		minimum.setToolTipText("最小化");
		close = new JButton(new ImageIcon("src/operation_icon/close_normal.png"));
		close.setRolloverIcon(new ImageIcon("src/operation_icon/close_highlight.png"));
		close.setPressedIcon(new ImageIcon("src/operation_icon/close_down.png"));
		close.setFocusPainted(false);
		close.setContentAreaFilled(false);
		close.setToolTipText("关闭");
		bgPanel.add(minimum);
		bgPanel.add(close);
		minimum.setBounds(175, 10, 30, 20);
		close.setBounds(205, 10, 30, 20);
		minimum.addMouseListener(new OperationListener());
		close.addMouseListener(new OperationListener());
		///////////////////////JFrame设置//////////////////////////////
		this.addMouseListener(  //监听鼠标单击和释放
		new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				isDragging = true;
				pressPoint = e.getPoint();
			}
			public void mouseReleased(MouseEvent e) 
			{
			    isDragging = false;
			}
		});
		this.addMouseMotionListener(  //监听鼠标移动
		new MouseMotionAdapter()
		{
			public void mouseDragged(MouseEvent e)
			{
				Point point = e.getPoint();
				Point locationPoint = getLocation();
				int x = locationPoint.x + point.x - pressPoint.x;
				int y = locationPoint.y + point.y - pressPoint.y;
				setLocation(x, y);
			}
		});
		setIconImage(new ImageIcon("src/program_icon/logo.png").getImage());
		setUndecorated(true);
		setSize(240,480);
		setLocationRelativeTo(null);
		setResizable(false);
	}
	//设置窗口背景
	public void setBG()
	{
		((JPanel)this.getContentPane()).setOpaque(false);
		ImageIcon img = new ImageIcon("src/main/main_bg.png");
		JLabel background = new JLabel(img);
		this.getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
		background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
	}
	//获取本程序的使用者
	public UserInfo getSelfUser()
	{
		return this.selfUser;
	}
	//设置本程序的使用者
	public void setSelfUser(UserInfo selfUser)
	{
		this.selfUser = selfUser;
	}
	//向用户列表中添加用户
	public void addUser(UserInfo user)
	{
		listModel.addElement(user);
	}
	//从用户列表中删除用户
	public void removeUser(int pos)
	{
		listModel.removeElementAt(pos);
	}
	//根据地址来查询用户
	public UserInfo getUserBySocketAddress(SocketAddress address)
	{
		for (int i = 1 ; i < getUserNum() ; i++)
		{
			UserInfo user = getUser(i);
			if (user.getAddress() != null && 
				user.getAddress().equals(address))
			{
				return user;
			}
		}
		return null;
	}
	//—————————下面两个方法是对ListModel的包装—————————
	//获取该聊天窗口的用户数量
	public int getUserNum()
	{
		return listModel.size();
	}
	//获取指定位置的用户
	public UserInfo getUser(int pos)
	{
		return (UserInfo)listModel.elementAt(pos);
	}
	public LanTalk getObj()
	{
		return this;
	}
	//窗口操作标签的监听类
	class OperationListener extends MouseAdapter
	{
		//@override
		public void mouseClicked(MouseEvent e)
		{
			if(e.getSource() == minimum)
			{
				getObj().setExtendedState(JFrame.ICONIFIED);
			}
			else if(e.getSource() == close)
			{
				getObj().setVisible(false);
			}
		}
	}
	//实现JList上的鼠标双击监听器
	class SelectUserListener extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			//如果鼠标的击键次数大于2
			if (e.getClickCount() >= 2)
			{
				//取出鼠标双击时选中的列表项
				UserInfo user = (UserInfo)friendsList.getSelectedValue();
				if(user.equals(selfUser))
					JOptionPane.showMessageDialog(null, 
							"不能和自己聊天！！！"
							, "警告消息", JOptionPane.WARNING_MESSAGE);
				else
				{
					//如果该列表项对应用户的交谈窗口为null
					if (user.getChatFrame() == null)
					{
						//为该用户创建一个交谈窗口，并让该用户引用该窗口
						ChatFrame frame = new ChatFrame(getSelfUser(), user);
						user.setChatFrame(frame);
					}
					//如果该用户的窗口没有显示，则让该用户的窗口显示出来
					if (!user.getChatFrame().isShowing())
					{
						user.getChatFrame().setVisible(true);
					}
				}
			}
		}
	}
	/**
	 * 处理网络数据报，该方法将根据聊天信息得到聊天者，
	 * 并将信息显示在聊天对话框中。
	 * @param packet 需要处理的数据报
	 * @param single 该信息是否为私聊信息
	 * @throws Exception 
	 */
	public void processMsg(DatagramPacket packet , boolean single) throws Exception
	{
		//获取该发送该数据报的SocketAddress
		InetSocketAddress srcAddress = (InetSocketAddress)packet.getSocketAddress();
		UserInfo srcUser = getUserBySocketAddress(srcAddress);
		if (!srcUser.equals(this.selfUser))
		{
			//确定消息将要显示到哪个用户对应窗口上。
			UserInfo alertUser = single ? srcUser : getUser(0);
			//如果该用户对应的窗口为空，显示该窗口
			if (alertUser.getChatFrame() == null)
			{
				alertUser.setChatFrame(new ChatFrame(getSelfUser(), alertUser));
			}
			//显示提示信息
			String Msg = new String(packet.getData() , 0 , packet.getLength());
			if(Msg.startsWith(MyProtocol.SHAKE) && Msg.endsWith(MyProtocol.SHAKE))
				alertUser.getChatFrame().Shake();
			else if(Msg.startsWith(MyProtocol.FILE) && Msg.endsWith(MyProtocol.FILE))
				alertUser.getChatFrame().ReceiveFile(Msg);
			else
			{
				alertUser.getChatFrame().AddTipMsg(srcUser.getName() + " " + formatter.format(new Date()) + "\n", false);
				alertUser.getChatFrame().AddUserMsg(Msg);
			}
			if (!alertUser.getChatFrame().isShowing())
			{
				alertUser.getChatFrame().setVisible(true);
			}
		}
	}
	//初始化程序系统托盘
	private void SystemTrayInitial() 
	{ 	
		
		// 系统栏初始化
		if (!SystemTray.isSupported()) // 判断当前系统是否支持系统栏
			return;
		try {
			String title = "局域网聊天软件";
			String company = "XXX小组制作";
			SystemTray sysTray = SystemTray.getSystemTray();
			Image image = Toolkit.getDefaultToolkit().getImage("src/program_icon/logo.png");// 系统栏图标
			trayicon = new TrayIcon(image, title + "\n" + company, createMenu());
			trayicon.setImageAutoSize(true);
			trayicon.addActionListener(new SysTrayActionListener());
			sysTray.add(trayicon);
			trayicon.displayMessage(title, company, MessageType.INFO);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//系统托盘功能设置
	private PopupMenu createMenu() { // 创建系统栏菜单的方法
		PopupMenu menu = new PopupMenu();
		MenuItem exitItem = new MenuItem("退出");
		exitItem.addActionListener(new ActionListener() { // 系统栏退出事件
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				});
		MenuItem openItem = new MenuItem("打开");
		openItem.addActionListener(new ActionListener() {// 系统栏打开菜单项事件
					public void actionPerformed(ActionEvent e) {
						if (!getObj().isVisible()) {
							getObj().setVisible(true);
							getObj().toFront();
						} else
						{
							getObj().toFront();
						}
					}
				});
		menu.add(openItem);
		menu.addSeparator();
		menu.add(exitItem);
		return menu;
	}
	//系统托盘监听器
	class SysTrayActionListener implements ActionListener {// 系统栏双击事件
		public void actionPerformed(ActionEvent e) {
			getObj().setVisible(true);
			getObj().toFront();
		}
	}
}
//定义用于改变JList列表项外观的类
class ImageCellRenderer extends JPanel implements ListCellRenderer
{
	private ImageIcon icon;
	private String name;
	//定义绘制单元格时的背景色
	private Color background;
	//定义绘制单元格时的前景色
	private Color foreground;

	public Component getListCellRendererComponent(JList list, Object value, int index, 
	  boolean isSelected, boolean cellHasFocus)
	{
		UserInfo userInfo = (UserInfo)value;
		icon = new ImageIcon("src/icon/" + userInfo.getIcon() + ".jpg");
		name = userInfo.getName();
		background = isSelected ? new Color(246, 121, 42) : new Color(252, 235, 165);
		foreground = isSelected ? list.getSelectionForeground() : list.getForeground();
		//返回该JPanel对象作为单元格绘制器
		return this;
	}
	//重写paintComponent方法，改变JPanel的外观
	public void paintComponent(Graphics g)
	{	
		super.paintComponent(g);
		super.setOpaque(false);
		int imageWidth = icon.getImage().getWidth(null);
		int imageHeight = icon.getImage().getHeight(null);
		g.setColor(background);
		g.fill3DRect(3, 3, getWidth() - 6, getHeight() - 3, true);
		g.setColor(foreground);
		//绘制好友图标
		g.drawImage(icon.getImage() , (int)(getWidth() * 0.15) , 10 , null);
		g.setFont(new Font("华文楷体" , Font.BOLD , 18));
		//绘制好友用户名
		g.drawString(name, imageWidth + (int)(getWidth() * 0.15) + 10, imageHeight / 2 + 17 );
	}
	//通过该方法来设置该ImageCellRenderer的最佳大小
	public Dimension getPreferredSize()
	{  
		return new Dimension(60, 55);
	}
}
