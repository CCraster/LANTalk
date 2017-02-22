package com.xxx.frame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.xxx.*;

/*
 * 为聊天主程序添加显示在线人数功能的JPanel
 */

public class ShowOnline extends JPanel
{
	private String msg = "";
	private LanTalk lanTalk = null;
	private Color color = Color.red;
	private Timer timer = null;
	private int changepx;
	private int x;
	
	public ShowOnline(LanTalk lanTalk)
	{
		this.lanTalk = lanTalk;
		this.x = 0;
		this.changepx = 1;
		timer = new Timer(200, 
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						update();
					}
				});
		timer.start();
	}
	
	public void update()
	{
		repaint();
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.setColor(color);
		g.setFont(new Font("华文新魏" , Font.BOLD , 15));
		msg = "当前有 " + (lanTalk.getUserNum() - 1) + " 人在线";
		FontMetrics fm = g.getFontMetrics();
		int stringwidth = fm.stringWidth(msg);
		int stringascent = fm.getAscent();
		if(changepx == 1 && x + changepx + stringwidth > getWidth())
		{
			changepx = -1;
			x += changepx;
		}
		else
			x += changepx;
		if(changepx == -1 && x + changepx < 0)
		{
			changepx = 1;
			x += changepx;
		}
		else
			x += changepx;
		g.drawString(msg, x, getHeight() - stringascent / 2);
	}
	
	public Dimension getPreferredSize()
	{  
		return new Dimension(200, 20);
	}
}
