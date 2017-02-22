package com.xxx.frame;

import java.awt.*;
import java.awt.event.*;
import java.util.Date;

import javax.swing.*;

import com.xxx.*;
import com.xxx.info.*;
import com.xxx.util.*;

//登陆用的对话框
public class LoginFrame extends JFrame
{
	public JLabel tip;
	public JTextField userField = new JTextField("XXX" , 20);
	public JComboBox iconList = new JComboBox(
		new String[]{"卖萌男", "猥琐男", "音乐男", "自恋男", "自信男", "暴力女", "可爱女", "恐怖女","卖萌女", "学渣女"});
	private JButton loginBn = new JButton("登陆");
	private JButton cancel = new JButton("取消");
	private JLabel showIcon = new JLabel(new ImageIcon("/src/login_image/卖萌男.jpg"), SwingConstants.CENTER);
	//聊天的主界面
	private LanTalk mainFrame;
	//聊天通信的工具实例
	public static ComUtil comUtil;
	//移动窗口使用
	private Point pressPoint;
	private boolean isDragging = false;
	//操作窗口使用
	private JButton minimum;
	private JButton close;
	//构造器，用于初始化的登陆对话框
	public LoginFrame(LanTalk mainFrame , String msg)
	{
		this.mainFrame = mainFrame;
		setBG();
		//改java窗口风格为windows风格
		try
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch(Exception ex)
		{
			ex.printStackTrace(); 
	    }
		//定义JPanel放组件
		JPanel bgPanel = new JPanel();
		bgPanel.setLayout(null);
		bgPanel.setOpaque(false);
		add(bgPanel);
		//登陆界面北部
		tip = new JLabel(msg, SwingConstants.CENTER);
		tip.setFont(new Font("华文新魏" , Font.BOLD , 20));
		bgPanel.add(tip);
		tip.setBounds(15, 80, 300, 100);
		//登陆界面中部
		JPanel loginPanel = new JPanel(new BorderLayout());
		loginPanel.setOpaque(false);
		loginPanel.add(getPanel("用户名" , userField), BorderLayout.NORTH);
		loginPanel.add(getPanel("头  像" , iconList));
		loginPanel.setPreferredSize(new Dimension(130, 50));
		iconList.setPreferredSize(new Dimension(130, 20));
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.add(loginPanel);
		iconList.addActionListener(
		new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				updateIcon((String)iconList.getSelectedItem());
			}
		});
		centerPanel.setPreferredSize(new Dimension(200, 80));
		centerPanel.setOpaque(false);
		bgPanel.add(centerPanel);
		centerPanel.setBounds(-10, 165, 280, 100);
		updateIcon("卖萌男");
		bgPanel.add(showIcon);
		showIcon.setBounds(220, 160, 90, 90);
		//登陆界面南部
		JPanel bp = new JPanel();
		loginBn.addActionListener(new MyActionListener(this));
		cancel.addActionListener(
		new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		bp.add(loginBn); 
		bp.add(cancel);
		bp.setOpaque(false);
		bp.setPreferredSize(new Dimension(300, 50));
		bgPanel.add(bp);
		bp.setBounds(55, 255, 200, 100);
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
		minimum.setBounds(250, 10, 30, 20);
		close.setBounds(280, 10, 30, 20);
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
		setSize(321, 321);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	//设置窗口背景
	public void setBG()
	{
		((JPanel)this.getContentPane()).setOpaque(false);
		ImageIcon img = new ImageIcon("src/login_image/loginbg.jpg");
		JLabel background = new JLabel(img);
		this.getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
		background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
	} 
	//工具方法，该方法将一个字符串和组件组合成JPanel对象
	private JPanel getPanel(String name , JComponent jf)
	{
		JPanel jp = new JPanel();
		jp.add(new JLabel(name + "："));
		jp.add(jf);
		jp.setOpaque(false);
		jp.setPreferredSize(new Dimension(130, 45));
		return jp;
	}
	//该方法用于改变登陆窗口最上面的提示信息
	public void setTipMsg(String tip)
	{
		this.tip.setText(tip);
	}
	//该方法用于更新显示头像的JLabel图标
	public void updateIcon(String name)
	{
		showIcon.setIcon(new ImageIcon("src/login_image/" + name + ".jpg"));
	}
	//窗口操作标签的监听类
	class OperationListener extends MouseAdapter
	{
		//@override
		public void mouseClicked(MouseEvent e)
		{
			if(e.getSource() == minimum)
			{
				setExtendedState(JFrame.ICONIFIED);
			}
			else if(e.getSource() == close)
			{
				System.exit(0);
			}
		}
	}
	//定义一个事件监听器
	class MyActionListener implements ActionListener
	{
		private LoginFrame loginFrame;
		public MyActionListener(LoginFrame loginFrame)
		{
			this.loginFrame = loginFrame;
		}
		//当鼠标单击事件发生时
		public void actionPerformed(ActionEvent evt)
		{
			try
			{
				//初始化聊天通信类
				mainFrame.setTitle(userField.getText());  //为聊天主程序添加显示在线人数的JPanel
				ShowOnline so = new ShowOnline(mainFrame);
				mainFrame.add(so);
				so.setOpaque(false);
				so.setBounds(8, 455, 224, 24);
				comUtil = new ComUtil(mainFrame);
				String loginTime = LanTalk.formatter.format(new Date());
				final String loginMsg = MyProtocol.PRESENCE + userField.getText()
					+ MyProtocol.SPLITTER + iconList.getSelectedItem()
					+ MyProtocol.SPLITTER + loginTime
					+ MyProtocol.PRESENCE;
				mainFrame.addUser(new UserInfo(iconList.getSelectedItem() + "", userField.getText(),
						loginTime, 0));
				//设置本程序的使用者
				mainFrame.setSelfUser(mainFrame.getUser(1));
				JLabel showUser = new JLabel(" " + mainFrame.getSelfUser().getName(),
						new ImageIcon("src/icon/" + mainFrame.getSelfUser().getIcon() + ".jpg"), JLabel.LEFT);
				mainFrame.add(showUser);
				showUser.setFont(new Font("华文楷体" , Font.BOLD , 20));
				showUser.setBounds(8, 3, 200, 50);
				comUtil.broadCast(loginMsg);
				//启动定时器广播在线信息
				Timer timer = new Timer(1000 * 1, 
					new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						comUtil.broadCast(loginMsg);
					}
				});
				timer.start();
				loginFrame.setVisible(false);
				mainFrame.setVisible(true);
			}
			catch (Exception ex)
			{ex.printStackTrace();
				loginFrame.setTipMsg("请确认程序所用端口空闲，且网络正常！");
			}
		}
	}
}
