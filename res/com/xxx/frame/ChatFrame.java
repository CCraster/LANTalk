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


//���彻̸�ĶԻ���
public class ChatFrame extends JFrame
{
	//������Ϣ��
	private JTextPane showMsgPane = new JTextPane();
	//����������
	private JTextPane inputMsgPane = new JTextPane();
	//��������ť
	private JButton jbt_font, jbt_face, jbt_shake, jbt_file, jbt_record;
	//����������Ϣ�İ�ť
	private JButton sendBn;
	private JButton clearBn;
	//�ý�̸���ڶ�Ӧ���û�
	public UserInfo self;
	private UserInfo user;
	//�ƶ�����ʹ��
	private Point pressPoint;
	private boolean isDragging = false;
	//��������ʹ��
	private JButton minimum;
	private JButton close;
	//���ߴ���
	private PicsJWindow expression;
	private FontJWindow font;
	private RecordJWindow record;
	//�����ַ���
	private String sendMsg;
	//�����ļ���·��
	private String sendFilePath;
	
	//�����������ڳ�ʼ����̸�Ի���Ľ���
	public ChatFrame(UserInfo self, UserInfo user)
	{
		this.self = self;
		this.user = user;
		setBG();
		//����JPanel�����
		JPanel bgPanel = new JPanel();
		bgPanel.setLayout(null);
		bgPanel.setOpaque(false);
		add(bgPanel);
		///////////////////////��������//////////////////////////
		//�����С���ͽ���ͼ��
		JLabel showUser = new JLabel(" " + user.getName(),
				new ImageIcon("src/icon/" + user.getIcon() + ".jpg"), JLabel.LEFT);
		bgPanel.add(showUser);
		showUser.setFont(new Font("���Ŀ���" , Font.BOLD , 20));
		showUser.setBounds(8, 5, 200, 50);
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
		minimum.setBounds(515, 15, 30, 20);
		close.setBounds(545, 15, 30, 20);
		minimum.addMouseListener(new OperationListener());
		close.addMouseListener(new OperationListener());
		////////////////////////�����������/////////////////////////
		//��ʾ��Ϣ�õ�JTextPane����
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
		
		//������
		ToolListener toollistener = new ToolListener();
		font = new FontJWindow(this);
		expression = new PicsJWindow(this);
		record = new RecordJWindow(this);
		
		jbt_font = new JButton(new ImageIcon("src/chatFrame_image/font_normal.png"));
		jbt_font.setRolloverIcon(new ImageIcon("src/chatFrame_image/font_focus.png"));
		jbt_font.setPressedIcon(new ImageIcon("src/chatFrame_image/font_press.png"));
		jbt_font.setFocusPainted(false);
		jbt_font.setContentAreaFilled(false);
		jbt_font.setToolTipText("����ѡ�񹤾���");
		jbt_font.addActionListener(toollistener);
		bgPanel.add(jbt_font);
		jbt_font.setBounds(10, 374, 28, 28);
		jbt_face = new JButton(new ImageIcon("src/chatFrame_image/face_normal.png"));
		jbt_face.setRolloverIcon(new ImageIcon("src/chatFrame_image/face_focus.png"));
		jbt_face.setPressedIcon(new ImageIcon("src/chatFrame_image/face_press.png"));
		jbt_face.setFocusPainted(false);
		jbt_face.setContentAreaFilled(false);
		jbt_face.setToolTipText("ѡ�����");
		jbt_face.addActionListener(toollistener);
		bgPanel.add(jbt_face);
		jbt_face.setBounds(39, 374, 28, 28);
		jbt_shake = new JButton(new ImageIcon("src/chatFrame_image/shake_normal.png"));
		jbt_shake.setRolloverIcon(new ImageIcon("src/chatFrame_image/shake_focus.png"));
		jbt_shake.setPressedIcon(new ImageIcon("src/chatFrame_image/shake_press.png"));
		jbt_shake.setFocusPainted(false);
		jbt_shake.setContentAreaFilled(false);
		jbt_shake.setToolTipText("����ѷ��ʹ��ڶ���");
		jbt_shake.addActionListener(toollistener);
		bgPanel.add(jbt_shake);
		jbt_shake.setBounds(68, 374, 28, 28);
		jbt_file = new JButton(new ImageIcon("src/chatFrame_image/file_normal.png"));
		jbt_file.setRolloverIcon(new ImageIcon("src/chatFrame_image/file_focus.png"));
		jbt_file.setPressedIcon(new ImageIcon("src/chatFrame_image/file_press.png"));
		jbt_file.setFocusPainted(false);
		jbt_file.setContentAreaFilled(false);
		jbt_file.setToolTipText("�����ļ�");
		jbt_file.addActionListener(toollistener);
		bgPanel.add(jbt_file);
		jbt_file.setBounds(97, 374, 28, 28);
		jbt_record = new JButton(new ImageIcon("src/chatFrame_image/record_normal.png"));
		jbt_record.setRolloverIcon(new ImageIcon("src/chatFrame_image/record_focus.png"));
		jbt_record.setPressedIcon(new ImageIcon("src/chatFrame_image/record_press.png"));
		jbt_record.setFocusPainted(false);
		jbt_record.setContentAreaFilled(false);
		jbt_record.setToolTipText("��Ϣ��¼");
		jbt_record.addActionListener(toollistener);
		bgPanel.add(jbt_record);
		jbt_record.setBounds(285, 374, 90, 28);
		
		//�ײ����������ť
		ButtonListener buttonListener = new ButtonListener();
		sendBn = new JButton(new ImageIcon("src/chatFrame_image/send_normal.png"));
		sendBn.setRolloverIcon(new ImageIcon("src/chatFrame_image/send_focus.png"));
		sendBn.setFocusPainted(false);
		sendBn.setContentAreaFilled(false);
		sendBn.setToolTipText("����");
		sendBn.addActionListener(buttonListener);
		bgPanel.add(sendBn);
		sendBn.setBounds(135, 477, 48, 28);
		
		clearBn = new JButton(new ImageIcon("src/chatFrame_image/clear_normal.png"));
		clearBn.setRolloverIcon(new ImageIcon("src/chatFrame_image/clear_focus.png"));
		clearBn.setFocusPainted(false);
		clearBn.setContentAreaFilled(false);
		clearBn.setToolTipText("���");
		clearBn.addActionListener(buttonListener);
		bgPanel.add(clearBn);
		clearBn.setBounds(200, 477, 48, 28);
		////////////////////////�ұ������û���Ϣ��ʾ����/////////////////////////
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
		userInf.setText(" �û��ǳƣ�\n    " + user.getName() + "\n �û�IP��\n    "
				+ user.getAddress() + "\n �û���½ʱ�䣺\n    " + user.getloginTime());
		bgPanel.add(userInf);
		userInf.setEditable(false);
		userInf.setOpaque(false);
		userInf.setFont(new Font("���Ŀ���", Font.BOLD, 15));
		userInf.setBounds(395, 270, 180, 230);
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
				if(record.isVisible())  //������Ϣ���������촰���ƶ�
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
	//���ô��ڱ���
	public void setBG()
	{
		((JPanel)this.getContentPane()).setOpaque(false);
		ImageIcon img = new ImageIcon("src/chatFrame_image/chat_bg.png");
		JLabel background = new JLabel(img);
		this.getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
		background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
	}
	//��������ʱʹ��
	public UserInfo getSelf()
	{
		return this.self;
	}
	public ChatFrame getObj()
	{
		return this;
	}
	//��������ʱʹ��
	public JTextPane getInputPane()
	{
		return this.inputMsgPane;
	}
	//���ڶ�������
	public void Shake() throws BadLocationException
	{
		if(this.isVisible() == false)
			this.setVisible(true);
		String msg = "��" + user.getName() + "��������������һ�����ڶ�����\n\n";
		StyledDocument doc = showMsgPane.getStyledDocument();
		SimpleAttributeSet attrSet = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attrSet, "����");
		StyleConstants.setFontSize(attrSet, 14);
		StyleConstants.setBold(attrSet, true);
		StyleConstants.setBackground(attrSet, new Color(255, 255, 255));
		StyleConstants.setForeground(attrSet, Color.red);
		doc.insertString(doc.getLength(), "\n     ", null);
		doc.insertString(doc.getLength(), msg, attrSet);
		showMsgPane.setCaretPosition(doc.getLength());
		
		StyledDocument record_doc = record.getRecordPane().getStyledDocument();//��Ϣ��¼���
		record_doc.insertString(record_doc.getLength(), "\n     ", null);
		record_doc.insertString(record_doc.getLength(), msg, attrSet);
		record.getRecordPane().setCaretPosition(record_doc.getLength());
		
		//���ڶ���ʵ��
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
	//�����ļ���Ϣ
	public void ReceiveFile(String Msg) throws Exception
	{
		if(!getObj().isVisible())
		{
			getObj().setVisible(true);
		}
		String[] fileInfo = Msg.substring(2, Msg.length() - 2).split(MyProtocol.FILE);
		
		String msg = "��" + user.getName() + "��������������һ���ļ���\n\n";
		StyledDocument doc = showMsgPane.getStyledDocument();
		SimpleAttributeSet attrSet = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attrSet, "����");
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
			
			StyledDocument record_doc = record.getRecordPane().getStyledDocument();//��Ϣ��¼���
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
			//�����Ƿ�����ļ�
			int receive = JOptionPane.showConfirmDialog(getObj(), 
					msg + "��Ҫ����ô��", user.getName() + "���������ļ�", JOptionPane.YES_NO_OPTION);
			String choose = "";
			if(receive == JOptionPane.YES_OPTION)
			{
				choose = "������ͬ����ա�" + user.getName() + "���������͵��ļ���\n\n";
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
					JFrame frame = new JFrame("��������" + user.getName() + "���ļ�");
					frame.add(re);
					frame.setVisible(true);
					frame.pack();
					frame.setResizable(false);
					frame.setLocationRelativeTo(this);
				}
			}
			else
			{
				choose = "�������ܾ����ա�" + user.getName() + "���������͵��ļ���\n\n";
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
			
			record_doc = record.getRecordPane().getStyledDocument();//��Ϣ��¼���
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
				msg2 = "��" + user.getName() + "���ܾ��˽��������͵��ļ���";
				JOptionPane.showMessageDialog(null, 
						msg2, "�ܽ��ļ�", JOptionPane.INFORMATION_MESSAGE);
			}
			else
			{
				msg2 = "��" + user.getName() + "��ͬ���˽��������͵��ļ���\n\n";
				FileSendJPanel se = new FileSendJPanel(sendFilePath, ((InetSocketAddress)user.getAddress()).getHostName(),
						Integer.parseInt(fileInfo[0]));
				JFrame frame = new JFrame("��" + user.getName() + "�����ļ�");
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
			
			StyledDocument record_doc = record.getRecordPane().getStyledDocument();//��Ϣ��¼���
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
	//����Ϣ��ʾ�������û���Ϣ��Ϣ
	public void AddTipMsg(String msg, boolean self) throws BadLocationException
	{
		StyledDocument doc = showMsgPane.getStyledDocument();
		SimpleAttributeSet attrSet = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attrSet, "����");
		StyleConstants.setFontSize(attrSet, 14);
		if(self == true)
			StyleConstants.setForeground(attrSet, new Color(0, 128, 64));
		else
			StyleConstants.setForeground(attrSet, new Color(0, 0, 255));
		doc.insertString(doc.getLength(), msg, attrSet);
		showMsgPane.setCaretPosition(doc.getLength());
		
		StyledDocument record_doc = record.getRecordPane().getStyledDocument();//��Ϣ��¼���
		record_doc.insertString(record_doc.getLength(), msg, attrSet);
		record.getRecordPane().setCaretPosition(record_doc.getLength());
	}
	//����Ϣ��ʾ�������û���Ϣ
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
		
		StyledDocument record_doc = record.getRecordPane().getStyledDocument();//��Ϣ��¼���
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
				
				//��Ϣ��¼���
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
	//����������Ϣ
	public String BuildMsg()
	{	
		String SendMsg = "";
		ArrayList<PicsIcon> list = new ArrayList<PicsIcon>();  
		StyledDocument doc = inputMsgPane.getStyledDocument();
		//��ȡ����
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
        //��Ϣ�����װ
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
        //��װ��Ϣ
        Color color = getSelf().getColor();
        String FontStr = getSelf().getFont() + MyProtocol.FONT + color.getRed() 
        		+ MyProtocol.FONT + color.getGreen() + MyProtocol.FONT + color.getBlue();
        SendMsg = textStr + MyProtocol.P_SPLIT + FontStr + MyProtocol.P_SPLIT + picsStr;
        inputMsgPane.setText("");  //��������
        
        if(textStr.equals("") && picsStr.equals(""))
        	return "";
        else
        	return SendMsg;
	}
	//�������
	public void Insertpic(PicsIcon pic)
	{
		inputMsgPane.insertIcon(pic);
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
				getObj().setVisible(false);
			}
		}
	}
	//������������
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
							"Ⱥ��ʱ���ܷ��ʹ��ڶ���������"
							, "��Ϣ����", JOptionPane.WARNING_MESSAGE);
				else
				{
					String msg = "��������" + user.getName() + "�� ������һ�����ڶ�����\n\n";
					StyledDocument doc = showMsgPane.getStyledDocument();
					SimpleAttributeSet attrSet = new SimpleAttributeSet();
					StyleConstants.setFontFamily(attrSet, "����");
					StyleConstants.setFontSize(attrSet, 14);
					StyleConstants.setBold(attrSet, true);
					StyleConstants.setBackground(attrSet, new Color(255, 255, 255));
					StyleConstants.setForeground(attrSet, Color.red);
					try {
						doc.insertString(doc.getLength(), "\n     ", null);
						doc.insertString(doc.getLength(), msg, attrSet);
						//��Ϣ��¼���
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
							"��Ǹ��������ʱ���ṩȺ��ʱ�����ļ��Ĺ��ܣ�"
							, "��Ϣ����", JOptionPane.WARNING_MESSAGE);
				}
				else
				{
					JFileChooser fileChooser = new JFileChooser(new File("."));
					fileChooser.showOpenDialog(getObj());
					String filePath = fileChooser.getSelectedFile().getAbsolutePath();
					if(!filePath.equals(""))
					{
						sendFilePath = filePath;
						String msg = "����" + user.getName() + "��������һ���ļ���\n\n";
						StyledDocument doc = showMsgPane.getStyledDocument();
						SimpleAttributeSet attrSet = new SimpleAttributeSet();
						StyleConstants.setFontFamily(attrSet, "����");
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
						
						StyledDocument record_doc = record.getRecordPane().getStyledDocument();//��Ϣ��¼���
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
						//�����������ļ�����Ϣ�ļ�
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
	//�ײ���ť������
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
							"���ܷ��Ϳ���Ϣ������"
							, "��Ϣ����", JOptionPane.WARNING_MESSAGE);
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
					//������Ϣ
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
