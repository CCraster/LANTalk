package com.xxx.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class RecordJWindow extends JWindow
{
	private ChatFrame owner;
	private JTextPane recordPane = new JTextPane();
	private JButton deleteRecord, saveRecord;
	private JFileChooser fileChooser = new JFileChooser(new File("."));
	private int a = 365;
	private int b = 512;
	
//	public static void main(String[] args)
//	{
//		RecordJWindow p = new RecordJWindow(null);
//		p.setVisible(true);
//	}
	
	public RecordJWindow(ChatFrame owner)
	{
		super(owner);
		this.owner = owner;
		try
		{
			init();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void init()
	{
		this.setSize(a, b);
		JPanel p = new JPanel();
		p.setLayout(null);
		p.setOpaque(false);
		this.setBackground(new Color(232, 212, 159));
		this.setContentPane(p);
		
		JScrollPane jsp = new JScrollPane(recordPane);
		jsp.setBorder(new LineBorder(new Color(207, 93, 39), 1, true));
		p.add(jsp);
		jsp.setOpaque(false);
		jsp.getViewport().setBackground(new Color(241, 227, 190));;
		jsp.setBounds(0, 0, a, 465);
		recordPane.setOpaque(false);
		recordPane.setEditable(false);
		
		ButtonListener listener = new ButtonListener();
		deleteRecord = new JButton(new ImageIcon("src/record_image/delete_record_normal.png"));
		deleteRecord.setRolloverIcon(new ImageIcon("src/record_image/delete_record_focus.png"));
		deleteRecord.setFocusPainted(false);
		deleteRecord.setContentAreaFilled(false);
		deleteRecord.setToolTipText("删除消息记录");
		deleteRecord.addActionListener(listener);
		p.add(deleteRecord);
		deleteRecord.setBounds(200, 475, 70, 26);
		
		saveRecord = new JButton(new ImageIcon("src/record_image/save_record_normal.png"));
		saveRecord.setRolloverIcon(new ImageIcon("src/record_image/save_record_focus.png"));
		saveRecord.setFocusPainted(false);
		saveRecord.setContentAreaFilled(false);
		saveRecord.setToolTipText("导出消息记录");
		saveRecord.addActionListener(listener);
		p.add(saveRecord);
		saveRecord.setBounds(280, 475, 70, 26);
	}
	
	public JTextPane getRecordPane()
	{
		return this.recordPane;
	}
	
	public ChatFrame getOwner()
	{
		return this.owner;
	}
	
	@Override
	public void setVisible(boolean show)
	{
		if(show)
		{
			super.setVisible(show);
			new Thread(){  
				Point loc = getOwner().getLocation(); 
				int x = loc.x + 588;
				int start = 0 - 512;
	            public void run()
	            {
	            	for(; start < loc.y; start++)
	            	{
	            		getObj().setLocation(x, start);
	            	}
	            }
	        }.start();
		}
		else
		{
			Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
			int height = (int)screensize.getHeight();
			int x = this.getLocation().x;
			int y = this.getLocation().y;
			for(; y < height; y++)
			{
				this.setLocation(x, y);
			}
			super.setVisible(show);
			
		}
	}
	//定位表情框显示的位置
	public void determineAndSetLocation()
	{
		Point loc = owner.getLocationOnScreen();
		this.setLocation(loc.x + 10, loc.y + 337);
	}
	private JWindow getObj()
	{
		return this;
	}
	//保存聊天信息
	private void save() throws FileNotFoundException
	{
		if(fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			save(fileChooser.getSelectedFile());
		}
	}
	private void save(File file) throws FileNotFoundException
	{
		try
		{
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
		byte[] b = recordPane.getText().getBytes();
		out.write(b, 0, b.length);
		out.close();
		}
		catch(IOException iex)
		{
			iex.printStackTrace();
		}
	}
	
	class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == deleteRecord)
			{
				int choose = JOptionPane.showConfirmDialog(getObj(), 
						"您确定要删除消息记录吗？\n删除后将无法找回！！！", "删除消息记录", JOptionPane.YES_NO_OPTION);
				if(choose == JOptionPane.YES_OPTION)
				{
					getRecordPane().setText("");
				}
			}
			else if(e.getSource() == saveRecord)
			{
				try {
					save();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
}
