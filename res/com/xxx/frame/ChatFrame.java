package com.xxx.frame;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;

import com.xxx.*;
import com.xxx.frame.LoginFrame.OperationListener;
import com.xxx.info.*;
import com.xxx.util.MyProtocol;


//定义交谈的对话框
public class ChatFrame extends JFrame
{
	//聊天信息区
	private JTextPane showMsgPane = new JTextPane();
	//聊天输入区
	private JTextPane inputMsgPane = new JTextPane();
	//工具栏按钮
	private JButton jbt_font, jbt_face, jbt_shake, jbt_file, jbt_record;
	//发送聊天信息的按钮
	private JButton sendBn;
	private JButton clearBn;
	//该交谈窗口对应的用户
	public UserInfo self;
	private UserInfo user;
	//移动窗口使用
	private Point pressPoint;
	private boolean isDragging = false;
	//操作窗口使用
	private JButton minimum;
	private JButton close;
	//工具窗口
	private PicsJWindow expression;
	private FontJWindow font;
	private RecordJWindow record;
	//发送字符串
	private String sendMsg;
	//发送文件的路径
	private String sendFilePath;
	
	//构造器，用于初始化交谈对话框的界面
	public ChatFrame(UserInfo self, UserInfo user)
	{
		this.self = self;
		this.user = user;
		setBG();
		//定义JPanel放组件
		JPanel bgPanel = new JPanel();
		bgPanel.setLayout(null);
		bgPanel.setOpaque(false);
		add(bgPanel);
		///////////////////////顶部区域//////////////////////////
		//添加最小化和结束图标
		JLabel showUser = new JLabel(" " + user.getName(),
				new ImageIcon("src/icon/" + user.getIcon() + ".jpg"), JLabel.LEFT);
		bgPanel.add(showUser);
		showUser.setFont(new Font("华文楷体" , Font.BOLD , 20));
		showUser.setBounds(8, 5, 200, 50);
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
		minimum.setBounds(515, 15, 30, 20);
		close.setBounds(545, 15, 30, 20);
		minimum.addMouseListener(new OperationListener());
		close.addMouseListener(new OperationListener());
		////////////////////////左边聊天区域/////////////////////////
		//显示信息用的JTextPane设置
		showMsgPane.setEditable(false);
		JScrollPane jsp1 = new JScrollPane(showMsgPane);
		jsp1.setBorder(new LineBorder(new Color(207, 93, 39), 1, true));
		jsp1.setOpaque(false);
		jsp1.getViewport().setOpaque(false);
		showMsgPane.setOpaque(false);
		bgPanel.add(jsp1);
		jsp1.setBounds(8,60,370,310);
		
		JScrollPane jsp2 = new JScrollPane(inputMsgPane);
		jsp2.setBorder(new LineBorder(new Color(207, 93, 39), 1, true));
		jsp2.setOpaque(false);
		jsp2.getViewport().setOpaque(false);
		inputMsgPane.setOpaque(false);
		bgPanel.add(jsp2);
		jsp2.setBounds(8,407,373,65);
		
		//工具栏
		ToolListener toollistener = new ToolListener();
		font = new FontJWindow(this);
		expression = new PicsJWindow(this);
		record = new RecordJWindow(this);
		
		jbt_font = new JButton(new ImageIcon("src/chatFrame_image/font_normal.png"));
		jbt_font.setRolloverIcon(new ImageIcon("src/chatFrame_image/font_focus.png"));
		jbt_font.setPressedIcon(new ImageIcon("src/chatFrame_image/font_press.png"));
		jbt_font.setFocusPainted(false);
		jbt_font.setContentAreaFilled(false);
		jbt_font.setToolTipText("字体选择工具栏");
		jbt_font.addActionListener(toollistener);
		bgPanel.add(jbt_font);
		jbt_font.setBounds(10, 374, 28, 28);
		jbt_face = new JButton(new ImageIcon("src/chatFrame_image/face_normal.png"));
		jbt_face.setRolloverIcon(new ImageIcon("src/chatFrame_image/face_focus.png"));
		jbt_face.setPressedIcon(new ImageIcon("src/chatFrame_image/face_press.png"));
		jbt_face.setFocusPainted(false);
		jbt_face.setContentAreaFilled(false);
		jbt_face.setToolTipText("选择表情");
		jbt_face.addActionListener(toollistener);
		bgPanel.add(jbt_face);
		jbt_face.setBounds(39, 374, 28, 28);
		jbt_shake = new JButton(new ImageIcon("src/chatFrame_image/shake_normal.png"));
		jbt_shake.setRolloverIcon(new ImageIcon("src/chatFrame_image/shake_focus.png"));
		jbt_shake.setPressedIcon(new ImageIcon("src/chatFrame_image/shake_press.png"));
		jbt_shake.setFocusPainted(false);
		jbt_shake.setContentAreaFilled(false);
		jbt_shake.setToolTipText("向好友发送窗口抖动");
		jbt_shake.addActionListener(toollistener);
		bgPanel.add(jbt_shake);
		jbt_shake.setBounds(68, 374, 28, 28);
		jbt_file = new JButton(new ImageIcon("src/chatFrame_image/file_normal.png"));
		jbt_file.setRolloverIcon(new ImageIcon("src/chatFrame_image/file_focus.png"));
		jbt_file.setPressedIcon(new ImageIcon("src/chatFrame_image/file_press.png"));
		jbt_file.setFocusPainted(false);
		jbt_file.setContentAreaFilled(false);
		jbt_file.setToolTipText("发送文件");
		jbt_file.addActionListener(toollistener);
		bgPanel.add(jbt_file);
		jbt_file.setBounds(97, 374, 28, 28);
		jbt_record = new JButton(new ImageIcon("src/chatFrame_image/record_normal.png"));
		jbt_record.setRolloverIcon(new ImageIcon("src/chatFrame_image/record_focus.png"));
		jbt_record.setPressedIcon(new ImageIcon("src/chatFrame_image/record_press.png"));
		jbt_record.setFocusPainted(false);
		jbt_record.setContentAreaFilled(false);
		jbt_record.setToolTipText("消息记录");
		jbt_record.addActionListener(toollistener);
		bgPanel.add(jbt_record);
		jbt_record.setBounds(285, 374, 90, 28);
		
		//底部发送清除按钮
		ButtonListener buttonListener = new ButtonListener();
		sendBn = new JButton(new ImageIcon("src/chatFrame_image/send_normal.png"));
		sendBn.setRolloverIcon(new ImageIcon("src/chatFrame_image/send_focus.png"));
		sendBn.setFocusPainted(false);
		sendBn.setContentAreaFilled(false);
		sendBn.setToolTipText("发送");
		sendBn.addActionListener(buttonListener);
		bgPanel.add(sendBn);
		sendBn.setBounds(135, 477, 48, 28);
		
		clearBn = new JButton(new ImageIcon("src/chatFrame_image/clear_normal.png"));
		clearBn.setRolloverIcon(new ImageIcon("src/chatFrame_image/clear_focus.png"));
		clearBn.setFocusPainted(false);
		clearBn.setContentAreaFilled(false);
		clearBn.setToolTipText("清除");
		clearBn.addActionListener(buttonListener);
		bgPanel.add(clearBn);
		clearBn.setBounds(200, 477, 48, 28);
		////////////////////////右边聊天用户信息显示区域/////////////////////////
		JLabel userProfile_jbl1 = new JLabel(new ImageIcon("src/chatFrame_image/inf_bg1.png"));
		bgPanel.add(userProfile_jbl1);
		userProfile_jbl1.setBounds(395, 60, 180, 25);
		JLabel userProfile_jbl2 = new JLabel(new ImageIcon("src/profile_image/" + user.getIcon() + ".jpg"));
		bgPanel.add(userProfile_jbl2);
		userProfile_jbl2.setBounds(395, 85, 180, 150);
		
		JLabel userInf_jbl1 = new JLabel(new ImageIcon("src/chatFrame_image/inf_bg2.png"));
		bgPanel.add(userInf_jbl1);
		userInf_jbl1.setBounds(395, 235, 180, 25);
		JTextArea userInf = new JTextArea();
		userInf.setText(" 用户昵称：\n    " + user.getName() + "\n 用户IP：\n    "
				+ user.getAddress() + "\n 用户登陆时间：\n    " + user.getloginTime());
		bgPanel.add(userInf);
		userInf.setEditable(false);
		userInf.setOpaque(false);
		userInf.setFont(new Font("华文楷体", Font.BOLD, 15));
		userInf.setBounds(395, 270, 180, 230);
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
				if(record.isVisible())  //聊天信息窗口随聊天窗口移动
				{
					record.setLocation(getObj().getLocation().x + 588, getObj().getLocation().y);
				}
				expression.setVisible(false);
				font.setVisible(false);
			}
		});
		setIconImage(new ImageIcon("src/icon/" + user.getIcon() + ".jpg").getImage());
		setUndecorated(true);
		setSize(588, 512);
		this.setLocationRelativeTo(null);
	}
	//设置窗口背景
	public void setBG()
	{
		((JPanel)this.getContentPane()).setOpaque(false);
		ImageIcon img = new ImageIcon("src/chatFrame_image/chat_bg.png");
		JLabel background = new JLabel(img);
		this.getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
		background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
	}
	//设置字体时使用
	public UserInfo getSelf()
	{
		return this.self;
	}
	public ChatFrame getObj()
	{
		return this;
	}
	//设置字体时使用
	public JTextPane getInputPane()
	{
		return this.inputMsgPane;
	}
	//窗口抖动代码
	public void Shake() throws BadLocationException
	{
		if(this.isVisible() == false)
			this.setVisible(true);
		String msg = "【" + user.getName() + "】向【您】发送了一个窗口抖动！\n\n";
		StyledDocument doc = showMsgPane.getStyledDocument();
		SimpleAttributeSet attrSet = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attrSet, "楷体");
		StyleConstants.setFontSize(attrSet, 14);
		StyleConstants.setBold(attrSet, true);
		StyleConstants.setBackground(attrSet, new Color(255, 255, 255));
		StyleConstants.setForeground(attrSet, Color.red);
		doc.insertString(doc.getLength(), "\n     ", null);
		doc.insertString(doc.getLength(), msg, attrSet);
		showMsgPane.setCaretPosition(doc.getLength());
		
		StyledDocument record_doc = record.getRecordPane().getStyledDocument();//信息记录面板
		record_doc.insertString(record_doc.getLength(), "\n     ", null);
		record_doc.insertString(record_doc.getLength(), msg, attrSet);
		record.getRecordPane().setCaretPosition(record_doc.getLength());
		
		//窗口抖动实现
		new Thread(){  
            long begin = System.currentTimeMillis();  
            long end = System.currentTimeMillis();  
            Point p = getObj().getLocationOnScreen();  
            public void run(){  
                int i = 1;  
                while((end-begin) < 500){  
                    getObj().setLocation(new Point((int)p.getX()-5*i,(int)p.getY()+5*i));  
                    end = System.currentTimeMillis();  
                    try {  
                        Thread.sleep(1);  
                        i=-i;  
                        getObj().setLocation(p);  
                    } catch (InterruptedException e) {  
                        e.printStackTrace();  
                    }  
                }  
            }  
        }.start();
	}
	//接收文件信息
	public void ReceiveFile(String Msg) throws Exception
	{
		if(!getObj().isVisible())
		{
			getObj().setVisible(true);
		}
		String[] fileInfo = Msg.substring(2, Msg.length() - 2).split(MyProtocol.FILE);
		
		String msg = "【" + user.getName() + "】向【您】发送了一个文件！\n\n";
		StyledDocument doc = showMsgPane.getStyledDocument();
		SimpleAttributeSet attrSet = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attrSet, "楷体");
		StyleConstants.setFontSize(attrSet, 14);
		StyleConstants.setBold(attrSet, true);
		StyleConstants.setBackground(attrSet, new Color(255, 255, 255));
		StyleConstants.setForeground(attrSet, Color.red);
		if(fileInfo.length == 2)
		{
			try {
				doc.insertString(doc.getLength(), "\n     ", null);
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				doc.insertString(doc.getLength(), msg, attrSet);
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			showMsgPane.setCaretPosition(doc.getLength());
			
			StyledDocument record_doc = record.getRecordPane().getStyledDocument();//信息记录面板
			try {
				record_doc.insertString(record_doc.getLength(), "\n     ", null);
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				record_doc.insertString(record_doc.getLength(), msg, attrSet);
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			record.getRecordPane().setCaretPosition(record_doc.getLength());
			//处理是否接收文件
			int receive = JOptionPane.showConfirmDialog(getObj(), 
					msg + "您要接收么？", user.getName() + "向您发送文件", JOptionPane.YES_NO_OPTION);
			String choose = "";
			if(receive == JOptionPane.YES_OPTION)
			{
				choose = "【您】同意接收【" + user.getName() + "】给您发送的文件！\n\n";
				JFileChooser fileSave = new JFileChooser(new File("."));
				FileReceiveJPanel re = null;
				if(fileSave.showSaveDialog(getObj()) == JFileChooser.APPROVE_OPTION);
				{
					DatagramSocket tempsc = new DatagramSocket();
					int port = tempsc.getLocalPort();
					tempsc.close();
					re = new FileReceiveJPanel(fileInfo[0], fileSave.getSelectedFile().getAbsolutePath()
							, Integer.parseInt(fileInfo[1]), port);
					LoginFrame.comUtil.sendSingle(MyProtocol.FILE + port + MyProtocol.FILE
							, (InetSocketAddress)user.getAddress());
					JFrame frame = new JFrame("接收来自" + user.getName() + "的文件");
					frame.add(re);
					frame.setVisible(true);
					frame.pack();
					frame.setResizable(false);
					frame.setLocationRelativeTo(this);
				}
			}
			else
			{
				choose = "【您】拒绝接收【" + user.getName() + "】给您发送的文件！\n\n";
				LoginFrame.comUtil.sendSingle(MyProtocol.FILE + MyProtocol.FILE_REFUSE + MyProtocol.FILE
						, (InetSocketAddress)user.getAddress());
			}
			try {
				doc.insertString(doc.getLength(), "\n     ", null);
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				doc.insertString(doc.getLength(), choose, attrSet);
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			showMsgPane.setCaretPosition(doc.getLength());
			
			record_doc = record.getRecordPane().getStyledDocument();//信息记录面板
			try {
				record_doc.insertString(record_doc.getLength(), "\n     ", null);
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				record_doc.insertString(record_doc.getLength(), choose, attrSet);
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			record.getRecordPane().setCaretPosition(record_doc.getLength());
		}
		else if(fileInfo.length == 1)
		{
			String msg2 = null;
			if(fileInfo[0].equals(MyProtocol.FILE_REFUSE))
			{
				msg2 = "【" + user.getName() + "】拒绝了接收您发送的文件！";
				JOptionPane.showMessageDialog(null, 
						msg2, "拒接文件", JOptionPane.INFORMATION_MESSAGE);
			}
			else
			{
				msg2 = "【" + user.getName() + "】同意了接收您发送的文件！\n\n";
				FileSendJPanel se = new FileSendJPanel(sendFilePath, ((InetSocketAddress)user.getAddress()).getHostName(),
						Integer.parseInt(fileInfo[0]));
				JFrame frame = new JFrame("向" + user.getName() + "发送文件");
				frame.add(se);
				frame.setVisible(true);
				frame.pack();
				frame.setResizable(false);
				frame.setLocationRelativeTo(this);
				
			}
			try {
				doc.insertString(doc.getLength(), "\n     ", null);
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				doc.insertString(doc.getLength(), msg2, attrSet);
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			showMsgPane.setCaretPosition(doc.getLength());
			
			StyledDocument record_doc = record.getRecordPane().getStyledDocument();//信息记录面板
			try {
				record_doc.insertString(record_doc.getLength(), "\n     ", null);
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				record_doc.insertString(record_doc.getLength(), msg2, attrSet);
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			record.getRecordPane().setCaretPosition(record_doc.getLength());
		}
		
	}
	//给消息显示面板添加用户信息消息
	public void AddTipMsg(String msg, boolean self) throws BadLocationException
	{
		StyledDocument doc = showMsgPane.getStyledDocument();
		SimpleAttributeSet attrSet = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attrSet, "楷体");
		StyleConstants.setFontSize(attrSet, 14);
		if(self == true)
			StyleConstants.setForeground(attrSet, new Color(0, 128, 64));
		else
			StyleConstants.setForeground(attrSet, new Color(0, 0, 255));
		doc.insertString(doc.getLength(), msg, attrSet);
		showMsgPane.setCaretPosition(doc.getLength());
		
		StyledDocument record_doc = record.getRecordPane().getStyledDocument();//信息记录面板
		record_doc.insertString(record_doc.getLength(), msg, attrSet);
		record.getRecordPane().setCaretPosition(record_doc.getLength());
	}
	//给消息显示面板添加用户消息
	public void AddUserMsg(String msg) throws BadLocationException
	{
		String[] msgSplit = msg.split(MyProtocol.P_SPLIT);
		
		String[] font = msgSplit[1].split(MyProtocol.FONT);
		SimpleAttributeSet attrSet = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attrSet, font[0]);
		if(Integer.parseInt(font[1]) == 1)
			StyleConstants.setBold(attrSet, true);
		else if(Integer.parseInt(font[1]) == 2)
			StyleConstants.setItalic(attrSet, true);
		StyleConstants.setFontSize(attrSet, Integer.parseInt(font[2]));
		StyleConstants.setForeground(attrSet, new Color(Integer.parseInt(font[3])
				, Integer.parseInt(font[4]), Integer.parseInt(font[5])));
		
		StyledDocument doc = showMsgPane.getStyledDocument();
		doc.insertString(doc.getLength(), "   " + msgSplit[0], attrSet);
		
		StyledDocument record_doc = record.getRecordPane().getStyledDocument();//信息记录面板
		record_doc.insertString(record_doc.getLength(), "   " + msgSplit[0], attrSet);
		
		if(msgSplit.length == 3)
		{
			String[] exp = msgSplit[2].split(MyProtocol.EXP_SPLIT);
			int strLength = msgSplit[0].length();
			for(int i = 0; i < exp.length; i++)
			{
				String[] temp = exp[i].split(MyProtocol.EXP_DETAIL);
				int pos = showMsgPane.getStyledDocument().getLength() - strLength + Integer.parseInt(temp[0]);
				showMsgPane.setCaretPosition(pos);
				showMsgPane.insertIcon(new ImageIcon("src/expression/" + Integer.parseInt(temp[1]) + ".gif"));
				
				//消息记录面板
				int pos2 = record.getRecordPane().getStyledDocument().getLength() - strLength + Integer.parseInt(temp[0]);
				record.getRecordPane().setCaretPosition(pos2);
				record.getRecordPane().insertIcon(new ImageIcon("src/expression/" + Integer.parseInt(temp[1]) + ".gif"));
			}
		}
		showMsgPane.getStyledDocument().insertString(showMsgPane.getStyledDocument().getLength(), "\n", null);
		showMsgPane.setCaretPosition(doc.getLength());
		
		record.getRecordPane().getStyledDocument().insertString(record.getRecordPane().getStyledDocument().getLength(), "\n", null);
		record.getRecordPane().setCaretPosition(record_doc.getLength());
	}
	//构建发送信息
	public String BuildMsg()
	{	
		String SendMsg = "";
		ArrayList<PicsIcon> list = new ArrayList<PicsIcon>();  
		StyledDocument doc = inputMsgPane.getStyledDocument();
		//获取表情
        for(int i=0; i<doc.getRootElements()[0].getElementCount(); i++)
        {
            Element root = doc.getRootElements()[0].getElement(i);
            for(int j=0;j<root.getElementCount();j++)
            {
                PicsIcon icon = (PicsIcon)StyleConstants.getIcon(root.getElement(j).getAttributes());  
                if(icon != null)
                {
                    list.add(icon);
                }
            }
        }
        //信息处理封装
        int charNum = 0;
        int picsNum = 0;
        String textStr = "";
        String picsStr = "";
        for(int i=0; i<this.inputMsgPane.getStyledDocument().getLength(); i++) 
        {
            if(this.inputMsgPane.getStyledDocument().getCharacterElement(i).getName().equals("icon")) 
            {
            	if(picsNum < list.size() - 1)
            	{
            		picsStr += charNum  + MyProtocol.EXP_DETAIL + list.get(picsNum).getNum() + MyProtocol.EXP_SPLIT;
            	}
            	else
            	{
            		picsStr += charNum  + MyProtocol.EXP_DETAIL + list.get(picsNum).getNum();
            	}
            	picsNum++;
            }
            else 
            {
                try 
                {  
                	charNum++;
                	textStr += this.inputMsgPane.getStyledDocument().getText(i,1);
                }
                catch (BadLocationException e) 
                {  
                    // TODO Auto-generated catch block  
                    e.printStackTrace();  
                }
            }  
        }
        //封装信息
        Color color = getSelf().getColor();
        String FontStr = getSelf().getFont() + MyProtocol.FONT + color.getRed() 
        		+ MyProtocol.FONT + color.getGreen() + MyProtocol.FONT + color.getBlue();
        SendMsg = textStr + MyProtocol.P_SPLIT + FontStr + MyProtocol.P_SPLIT + picsStr;
        inputMsgPane.setText("");  //输入框清除
        
        if(textStr.equals("") && picsStr.equals(""))
        	return "";
        else
        	return SendMsg;
	}
	//插入表情
	public void Insertpic(PicsIcon pic)
	{
		inputMsgPane.insertIcon(pic);
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
				getObj().setVisible(false);
			}
		}
	}
	//工具栏监听类
	class ToolListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == jbt_font)
			{
				if(font.isVisible())
				{
					font.setVisible(false);
				}
				else
				{
					font.setVisible(true);
				}
			}
			else if(e.getSource() == jbt_face)
			{
				if(expression.isVisible())
				{
					expression.setVisible(false);
				}
				else
				{
					expression.setVisible(true);
				}
			}
			else if(e.getSource() == jbt_shake)
			{
				InetSocketAddress dest = (InetSocketAddress)user.getAddress();
				if(dest == null)
					JOptionPane.showMessageDialog(null, 
							"群聊时不能发送窗口抖动！！！"
							, "消息警告", JOptionPane.WARNING_MESSAGE);
				else
				{
					String msg = "【您】向【" + user.getName() + "】 发送了一个窗口抖动！\n\n";
					StyledDocument doc = showMsgPane.getStyledDocument();
					SimpleAttributeSet attrSet = new SimpleAttributeSet();
					StyleConstants.setFontFamily(attrSet, "楷体");
					StyleConstants.setFontSize(attrSet, 14);
					StyleConstants.setBold(attrSet, true);
					StyleConstants.setBackground(attrSet, new Color(255, 255, 255));
					StyleConstants.setForeground(attrSet, Color.red);
					try {
						doc.insertString(doc.getLength(), "\n     ", null);
						doc.insertString(doc.getLength(), msg, attrSet);
						//消息记录添加
						StyledDocument record_doc = record.getRecordPane().getStyledDocument();
						record_doc.insertString(record_doc.getLength(), "\n     ", null);
						record_doc.insertString(record_doc.getLength(), msg, attrSet);
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					LoginFrame.comUtil.sendSingle(MyProtocol.SHAKE + MyProtocol.SHAKE, dest);
				}
			}
			else if(e.getSource() == jbt_record)
			{
				if(record.isVisible())
				{
					record.setVisible(false);
				}
				else
				{
					record.setVisible(true);
				}
			}
			else if(e.getSource() == jbt_file)
			{
				if(user.getAddress() == null)
				{
					JOptionPane.showMessageDialog(null, 
							"抱歉，程序暂时不提供群聊时发送文件的功能！"
							, "消息警告", JOptionPane.WARNING_MESSAGE);
				}
				else
				{
					JFileChooser fileChooser = new JFileChooser(new File("."));
					fileChooser.showOpenDialog(getObj());
					String filePath = fileChooser.getSelectedFile().getAbsolutePath();
					if(!filePath.equals(""))
					{
						sendFilePath = filePath;
						String msg = "您向【" + user.getName() + "】发送了一个文件！\n\n";
						StyledDocument doc = showMsgPane.getStyledDocument();
						SimpleAttributeSet attrSet = new SimpleAttributeSet();
						StyleConstants.setFontFamily(attrSet, "楷体");
						StyleConstants.setFontSize(attrSet, 14);
						StyleConstants.setBold(attrSet, true);
						StyleConstants.setBackground(attrSet, new Color(255, 255, 255));
						StyleConstants.setForeground(attrSet, Color.red);
						try {
							doc.insertString(doc.getLength(), "\n     ", null);
						} catch (BadLocationException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {
							doc.insertString(doc.getLength(), msg, attrSet);
						} catch (BadLocationException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						showMsgPane.setCaretPosition(doc.getLength());
						
						StyledDocument record_doc = record.getRecordPane().getStyledDocument();//信息记录面板
						try {
							record_doc.insertString(record_doc.getLength(), "\n     ", null);
						} catch (BadLocationException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {
							record_doc.insertString(record_doc.getLength(), msg, attrSet);
						} catch (BadLocationException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						record.getRecordPane().setCaretPosition(record_doc.getLength());
						//发送请求发送文件的信息文件
						BufferedInputStream bf = null;
						try {
							bf = new BufferedInputStream(new FileInputStream(new File(sendFilePath)));
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						String Msg = null;
						try {
							Msg = MyProtocol.FILE + new File(sendFilePath).getName()
									+ MyProtocol.FILE + bf.available() + MyProtocol.FILE;
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						LoginFrame.comUtil.sendSingle(Msg, (InetSocketAddress)user.getAddress());
					}
				}
			}
		}
	}
	//底部按钮监听类
	class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			InetSocketAddress dest = (InetSocketAddress)user.getAddress();
			
			if(e.getSource() == sendBn)
			{
				String Msg = BuildMsg();
				if(Msg.equals(""))
					JOptionPane.showMessageDialog(null, 
							"不能发送空消息！！！"
							, "消息警告", JOptionPane.WARNING_MESSAGE);
				else
				{
					try 
					{
						AddTipMsg(getSelf().getName() + " " + LanTalk.formatter.format(new Date()) + "\n", true);
					} 
					catch (BadLocationException e1) 
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try 
					{
						AddUserMsg(Msg);
					} 
					catch (BadLocationException e1) 
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					//发送信息
					if(dest == null)
					{
						LoginFrame.comUtil.broadCast(Msg);
					}
					else
					{
						LoginFrame.comUtil.sendSingle(Msg, dest);
					}
				}
			}
			else if(e.getSource() == clearBn)
			{
				showMsgPane.setText("");
			}
		}
	}
}
