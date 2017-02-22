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
	//����һ��JList����
	private JList friendsList = new JList(listModel);
	//����һ�����ڸ�ʽ�����ڵĸ�ʽ��
	public static DateFormat formatter = DateFormat.getDateTimeInstance(); 
	//�����¼�����UserInfo����
	private UserInfo selfUser;
	//��������ʹ��
	private JButton minimum;
	private JButton close;
	//�ƶ�����ʹ��
	private Point pressPoint;
	private boolean isDragging = false;	
	//ϵͳ������ͼ��
	public static TrayIcon trayicon;

	//����������������
	public static void main(String[] args) 
	{
		LanTalk lc = new LanTalk();
		new LoginFrame(lc , "�������ǳƲ�ѡ��ͷ��");
	}
	
	public LanTalk()
	{
		//��java���ڷ��Ϊwindows���
		try
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch(Exception ex)
		{
			ex.printStackTrace(); 
	    }
		//���ø�JListʹ��ImageCellRenderer��Ϊ��Ԫ�������
		friendsList.setCellRenderer(new ImageCellRenderer()); 
		listModel.addElement(new UserInfo("������" , "������" ,
				LanTalk.formatter.format(new Date()), null , -2000));
		friendsList.addMouseListener(new SelectUserListener());
		
		//����JPanel�����
		setBG();SystemTrayInitial();
		JPanel bgPanel = new JPanel();
		bgPanel.setLayout(null);
		bgPanel.setOpaque(false);
		this.setContentPane(bgPanel);
		///////////////////////�����沼��//////////////////////////
		friendsList.setOpaque(false);
		JScrollPane jsp = new JScrollPane(friendsList);
		jsp.setOpaque(false);
		jsp.getViewport().setOpaque(false);
		jsp.setBorder(new LineBorder(new Color(207, 93, 39), 1, true));
		bgPanel.add(jsp);
		jsp.setBounds(8, 55, 224, 400);
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
		minimum.setBounds(175, 10, 30, 20);
		close.setBounds(205, 10, 30, 20);
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
		setSize(240,480);
		setLocationRelativeTo(null);
		setResizable(false);
	}
	//���ô��ڱ���
	public void setBG()
	{
		((JPanel)this.getContentPane()).setOpaque(false);
		ImageIcon img = new ImageIcon("src/main/main_bg.png");
		JLabel background = new JLabel(img);
		this.getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
		background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
	}
	//��ȡ�������ʹ����
	public UserInfo getSelfUser()
	{
		return this.selfUser;
	}
	//���ñ������ʹ����
	public void setSelfUser(UserInfo selfUser)
	{
		this.selfUser = selfUser;
	}
	//���û��б�������û�
	public void addUser(UserInfo user)
	{
		listModel.addElement(user);
	}
	//���û��б���ɾ���û�
	public void removeUser(int pos)
	{
		listModel.removeElementAt(pos);
	}
	//���ݵ�ַ����ѯ�û�
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
	//�������������������������������Ƕ�ListModel�İ�װ������������������
	//��ȡ�����촰�ڵ��û�����
	public int getUserNum()
	{
		return listModel.size();
	}
	//��ȡָ��λ�õ��û�
	public UserInfo getUser(int pos)
	{
		return (UserInfo)listModel.elementAt(pos);
	}
	public LanTalk getObj()
	{
		return this;
	}
	//���ڲ�����ǩ�ļ�����
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
	//ʵ��JList�ϵ����˫��������
	class SelectUserListener extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			//������Ļ�����������2
			if (e.getClickCount() >= 2)
			{
				//ȡ�����˫��ʱѡ�е��б���
				UserInfo user = (UserInfo)friendsList.getSelectedValue();
				if(user.equals(selfUser))
					JOptionPane.showMessageDialog(null, 
							"���ܺ��Լ����죡����"
							, "������Ϣ", JOptionPane.WARNING_MESSAGE);
				else
				{
					//������б����Ӧ�û��Ľ�̸����Ϊnull
					if (user.getChatFrame() == null)
					{
						//Ϊ���û�����һ����̸���ڣ����ø��û����øô���
						ChatFrame frame = new ChatFrame(getSelfUser(), user);
						user.setChatFrame(frame);
					}
					//������û��Ĵ���û����ʾ�����ø��û��Ĵ�����ʾ����
					if (!user.getChatFrame().isShowing())
					{
						user.getChatFrame().setVisible(true);
					}
				}
			}
		}
	}
	/**
	 * �����������ݱ����÷���������������Ϣ�õ������ߣ�
	 * ������Ϣ��ʾ������Ի����С�
	 * @param packet ��Ҫ��������ݱ�
	 * @param single ����Ϣ�Ƿ�Ϊ˽����Ϣ
	 * @throws Exception 
	 */
	public void processMsg(DatagramPacket packet , boolean single) throws Exception
	{
		//��ȡ�÷��͸����ݱ���SocketAddress
		InetSocketAddress srcAddress = (InetSocketAddress)packet.getSocketAddress();
		UserInfo srcUser = getUserBySocketAddress(srcAddress);
		if (!srcUser.equals(this.selfUser))
		{
			//ȷ����Ϣ��Ҫ��ʾ���ĸ��û���Ӧ�����ϡ�
			UserInfo alertUser = single ? srcUser : getUser(0);
			//������û���Ӧ�Ĵ���Ϊ�գ���ʾ�ô���
			if (alertUser.getChatFrame() == null)
			{
				alertUser.setChatFrame(new ChatFrame(getSelfUser(), alertUser));
			}
			//��ʾ��ʾ��Ϣ
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
	//��ʼ������ϵͳ����
	private void SystemTrayInitial() 
	{ 	
		
		// ϵͳ����ʼ��
		if (!SystemTray.isSupported()) // �жϵ�ǰϵͳ�Ƿ�֧��ϵͳ��
			return;
		try {
			String title = "�������������";
			String company = "XXXС������";
			SystemTray sysTray = SystemTray.getSystemTray();
			Image image = Toolkit.getDefaultToolkit().getImage("src/program_icon/logo.png");// ϵͳ��ͼ��
			trayicon = new TrayIcon(image, title + "\n" + company, createMenu());
			trayicon.setImageAutoSize(true);
			trayicon.addActionListener(new SysTrayActionListener());
			sysTray.add(trayicon);
			trayicon.displayMessage(title, company, MessageType.INFO);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//ϵͳ���̹�������
	private PopupMenu createMenu() { // ����ϵͳ���˵��ķ���
		PopupMenu menu = new PopupMenu();
		MenuItem exitItem = new MenuItem("�˳�");
		exitItem.addActionListener(new ActionListener() { // ϵͳ���˳��¼�
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				});
		MenuItem openItem = new MenuItem("��");
		openItem.addActionListener(new ActionListener() {// ϵͳ���򿪲˵����¼�
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
	//ϵͳ���̼�����
	class SysTrayActionListener implements ActionListener {// ϵͳ��˫���¼�
		public void actionPerformed(ActionEvent e) {
			getObj().setVisible(true);
			getObj().toFront();
		}
	}
}
//�������ڸı�JList�б�����۵���
class ImageCellRenderer extends JPanel implements ListCellRenderer
{
	private ImageIcon icon;
	private String name;
	//������Ƶ�Ԫ��ʱ�ı���ɫ
	private Color background;
	//������Ƶ�Ԫ��ʱ��ǰ��ɫ
	private Color foreground;

	public Component getListCellRendererComponent(JList list, Object value, int index, 
	  boolean isSelected, boolean cellHasFocus)
	{
		UserInfo userInfo = (UserInfo)value;
		icon = new ImageIcon("src/icon/" + userInfo.getIcon() + ".jpg");
		name = userInfo.getName();
		background = isSelected ? new Color(246, 121, 42) : new Color(252, 235, 165);
		foreground = isSelected ? list.getSelectionForeground() : list.getForeground();
		//���ظ�JPanel������Ϊ��Ԫ�������
		return this;
	}
	//��дpaintComponent�������ı�JPanel�����
	public void paintComponent(Graphics g)
	{	
		super.paintComponent(g);
		super.setOpaque(false);
		int imageWidth = icon.getImage().getWidth(null);
		int imageHeight = icon.getImage().getHeight(null);
		g.setColor(background);
		g.fill3DRect(3, 3, getWidth() - 6, getHeight() - 3, true);
		g.setColor(foreground);
		//���ƺ���ͼ��
		g.drawImage(icon.getImage() , (int)(getWidth() * 0.15) , 10 , null);
		g.setFont(new Font("���Ŀ���" , Font.BOLD , 18));
		//���ƺ����û���
		g.drawString(name, imageWidth + (int)(getWidth() * 0.15) + 10, imageHeight / 2 + 17 );
	}
	//ͨ���÷��������ø�ImageCellRenderer����Ѵ�С
	public Dimension getPreferredSize()
	{  
		return new Dimension(60, 55);
	}
}
