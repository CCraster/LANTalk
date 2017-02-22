package com.xxx.frame;

import java.awt.*;
import java.awt.event.*;
import java.util.Date;

import javax.swing.*;

import com.xxx.*;
import com.xxx.info.*;
import com.xxx.util.*;

//��½�õĶԻ���
public class LoginFrame extends JFrame
{
	public JLabel tip;
	public JTextField userField = new JTextField("XXX" , 20);
	public JComboBox iconList = new JComboBox(
		new String[]{"������", "�����", "������", "������", "������", "����Ů", "�ɰ�Ů", "�ֲ�Ů","����Ů", "ѧ��Ů"});
	private JButton loginBn = new JButton("��½");
	private JButton cancel = new JButton("ȡ��");
	private JLabel showIcon = new JLabel(new ImageIcon("/src/login_image/������.jpg"), SwingConstants.CENTER);
	//�����������
	private LanTalk mainFrame;
	//����ͨ�ŵĹ���ʵ��
	public static ComUtil comUtil;
	//�ƶ�����ʹ��
	private Point pressPoint;
	private boolean isDragging = false;
	//��������ʹ��
	private JButton minimum;
	private JButton close;
	//�����������ڳ�ʼ���ĵ�½�Ի���
	public LoginFrame(LanTalk mainFrame , String msg)
	{
		this.mainFrame = mainFrame;
		setBG();
		//��java���ڷ��Ϊwindows���
		try
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch(Exception ex)
		{
			ex.printStackTrace(); 
	    }
		//����JPanel�����
		JPanel bgPanel = new JPanel();
		bgPanel.setLayout(null);
		bgPanel.setOpaque(false);
		add(bgPanel);
		//��½���汱��
		tip = new JLabel(msg, SwingConstants.CENTER);
		tip.setFont(new Font("������κ" , Font.BOLD , 20));
		bgPanel.add(tip);
		tip.setBounds(15, 80, 300, 100);
		//��½�����в�
		JPanel loginPanel = new JPanel(new BorderLayout());
		loginPanel.setOpaque(false);
		loginPanel.add(getPanel("�û���" , userField), BorderLayout.NORTH);
		loginPanel.add(getPanel("ͷ  ��" , iconList));
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
		updateIcon("������");
		bgPanel.add(showIcon);
		showIcon.setBounds(220, 160, 90, 90);
		//��½�����ϲ�
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
		///////////////////////�����С���ͽ���ͼ��//////////////////////////
		minimum = new JButton(new ImageIcon("src/operation_icon/min_normal.png"));
		minimum.setRolloverIcon(new ImageIcon("src/operation_icon/min_highlight.png"));
		minimum.setPressedIcon(new ImageIcon("src/operation_icon/min_down.png"));
		minimum.setFocusPainted(false);
		minimum.setContentAreaFilled(false);
		minimum.setToolTipText("��С��");
		close = new JButton(new ImageIcon("src/operation_icon/close_normal.png"));
		close.setRolloverIcon(new ImageIcon("src/operation_icon/close_highlight.png"));
		close.setPressedIcon(new ImageIcon("src/operation_icon/close_down.png"));
		close.setFocusPainted(false);
		close.setContentAreaFilled(false);
		close.setToolTipText("�ر�");
		bgPanel.add(minimum);
		bgPanel.add(close);
		minimum.setBounds(250, 10, 30, 20);
		close.setBounds(280, 10, 30, 20);
		minimum.addMouseListener(new OperationListener());
		close.addMouseListener(new OperationListener());
		///////////////////////JFrame����//////////////////////////////
		this.addMouseListener(  //������굥�����ͷ�
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
		this.addMouseMotionListener(  //��������ƶ�
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
	//���ô��ڱ���
	public void setBG()
	{
		((JPanel)this.getContentPane()).setOpaque(false);
		ImageIcon img = new ImageIcon("src/login_image/loginbg.jpg");
		JLabel background = new JLabel(img);
		this.getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
		background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
	} 
	//���߷������÷�����һ���ַ����������ϳ�JPanel����
	private JPanel getPanel(String name , JComponent jf)
	{
		JPanel jp = new JPanel();
		jp.add(new JLabel(name + "��"));
		jp.add(jf);
		jp.setOpaque(false);
		jp.setPreferredSize(new Dimension(130, 45));
		return jp;
	}
	//�÷������ڸı��½�������������ʾ��Ϣ
	public void setTipMsg(String tip)
	{
		this.tip.setText(tip);
	}
	//�÷������ڸ�����ʾͷ���JLabelͼ��
	public void updateIcon(String name)
	{
		showIcon.setIcon(new ImageIcon("src/login_image/" + name + ".jpg"));
	}
	//���ڲ�����ǩ�ļ�����
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
	//����һ���¼�������
	class MyActionListener implements ActionListener
	{
		private LoginFrame loginFrame;
		public MyActionListener(LoginFrame loginFrame)
		{
			this.loginFrame = loginFrame;
		}
		//����굥���¼�����ʱ
		public void actionPerformed(ActionEvent evt)
		{
			try
			{
				//��ʼ������ͨ����
				mainFrame.setTitle(userField.getText());  //Ϊ���������������ʾ����������JPanel
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
				//���ñ������ʹ����
				mainFrame.setSelfUser(mainFrame.getUser(1));
				JLabel showUser = new JLabel(" " + mainFrame.getSelfUser().getName(),
						new ImageIcon("src/icon/" + mainFrame.getSelfUser().getIcon() + ".jpg"), JLabel.LEFT);
				mainFrame.add(showUser);
				showUser.setFont(new Font("���Ŀ���" , Font.BOLD , 20));
				showUser.setBounds(8, 3, 200, 50);
				comUtil.broadCast(loginMsg);
				//������ʱ���㲥������Ϣ
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
				loginFrame.setTipMsg("��ȷ�ϳ������ö˿ڿ��У�������������");
			}
		}
	}
}
