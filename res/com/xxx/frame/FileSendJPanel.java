package com.xxx.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.LineBorder;

import com.xxx.util.MyProtocol;

public class FileSendJPanel extends JPanel
{
	private String filePath;
	private String ip;
	private int port;
	private DatagramPacket pkg;
	private int fileSize;
	private int hasPassed;
    private DatagramPacket messagepkg;
    private JProgressBar jpb;
	
	public FileSendJPanel(String filePath, String ip, int port) throws Exception
	{
		super();
		this.filePath = filePath;
		this.ip = ip;
		this.port = port;
		this.fileSize = new BufferedInputStream(new FileInputStream(new File(filePath))).available();
		this.setLayout(null);
		this.setBackground(new Color(232, 212, 159));
		this.setPreferredSize(new Dimension(312, 50));
		
		JLabel tip = new JLabel("文件名：" + new File(filePath).getName(), JLabel.LEFT);
		tip.setFont(new Font("华文楷体", Font.BOLD, 15));
		this.add(tip);
		tip.setBounds(0, 0, 312, 25);
		
		jpb = new JProgressBar();
		jpb.setOrientation(JProgressBar.HORIZONTAL);
		jpb.setMinimum(0);
		jpb.setMaximum(100);
		jpb.setValue(0);
		jpb.setStringPainted(true);
		jpb.setBorder(new LineBorder(new Color(207, 93, 39), 1, true));
		jpb.setOpaque(false);
		this.add(jpb);
		jpb.setBounds(0, 25, 312, 25);
		
		new send().start();
	}
	
	class send extends Thread
	{
		public void run()
		{
			DatagramSocket send = null;
			try {
				DatagramSocket tempsc = new DatagramSocket();
				int port = tempsc.getLocalPort();
				tempsc.close();
				send = new DatagramSocket(port);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			BufferedInputStream bis = null;
			try {
				bis = new BufferedInputStream(  
				        new FileInputStream(new File(filePath)));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			byte[] buf = new byte[1024 * 10];
			byte[] msgbuf = new byte[1024];  
	        messagepkg = new DatagramPacket(msgbuf, msgbuf.length); 
			int len;
			try {
				while((len = bis.read(buf)) != -1)
				{
					pkg = new DatagramPacket(buf, len, 
							new InetSocketAddress(ip, port));
					send.send(pkg);
					hasPassed += pkg.getLength();
					jpb.setValue((int)(hasPassed * 1.0 / fileSize * 100));
					while(true)
					{
						send.receive(messagepkg);
						break;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			byte[] buf2 = MyProtocol.FILE_END.getBytes();
			DatagramPacket endpkg = new DatagramPacket(buf2, buf2.length, 
					new InetSocketAddress(ip, port));
			try {
				send.send(endpkg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				bis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			send.close();
		}
	}
}
