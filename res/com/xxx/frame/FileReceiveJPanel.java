package com.xxx.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.LineBorder;

import com.xxx.util.MyProtocol;

public class FileReceiveJPanel extends JPanel
{
	private String fileName;
	private String savePath;
	private int fileSize;
	private int port;
	private int serverPort;
	private int hasPassed;
    private DatagramPacket messagepkg;
    private JProgressBar jpb;
    
	public FileReceiveJPanel(String fileName, String savePath, int fileSize, int port) throws Exception
	{
		this.fileName = fileName;
		this.savePath = savePath;
		this.fileSize = fileSize;
		this.port = port;
		this.hasPassed = 0;
		this.setLayout(null);
		this.setBackground(new Color(232, 212, 159));
		this.setPreferredSize(new Dimension(312, 50));
		
		JLabel tip = new JLabel("文件名：" + fileName, JLabel.LEFT);
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
		
		new receive().start();
	}
	
	class receive extends Thread
	{
		public void run() 
		{
			DatagramSocket receive = null;
			try {
				receive = new DatagramSocket(port);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			BufferedOutputStream bos = null;
			try {
				bos = new BufferedOutputStream(  
				        new FileOutputStream(new File(savePath)));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			byte[] buf = new byte[1024 * 10];
			DatagramPacket pkg = new DatagramPacket(buf, buf.length);
			byte[] msgbuf = new byte[1024];
			msgbuf = "ok".getBytes();
			while(true)
			{
				try {
					receive.receive(pkg);
					hasPassed += pkg.getLength();
					jpb.setValue((int)(hasPassed * 1.0 / fileSize * 100));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(new String(pkg.getData(), 0, pkg.getLength()).equals(MyProtocol.FILE_END))
				{
					try {
						bos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					receive.close();
					break;
				}
				messagepkg = new DatagramPacket(msgbuf, msgbuf.length, (InetSocketAddress)pkg.getSocketAddress());
				try {
					receive.send(messagepkg);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					bos.write(pkg.getData(), 0, pkg.getLength());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					bos.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				bos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			receive.close();
		}
	}
}
