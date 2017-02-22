package com.xxx.frame;

import javax.swing.*;
import javax.swing.text.BadLocationException;

import java.awt.*;
import java.awt.event.*;

import com.xxx.info.*;

public class PicsJWindow extends JWindow
{
	private JLabel[] expression = new JLabel[100];
	private ChatFrame owner;
	private GridLayout grid = new GridLayout(10, 10);
	
//	public static void main(String[] args)
//	{
//		PicsJWindow p = new PicsJWindow(null);
//		p.setVisible(true);
//	}
	
	public PicsJWindow(ChatFrame owner)
	{
		super(owner);
		this.owner = owner;
		try
		{
			init();
//			this.setAlwaysOnTop(true);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	private void init() throws Exception
	{
		this.setSize(300, 300);
		JPanel p = new JPanel(grid);
		p.setOpaque(false);
		this.setContentPane(p);
		for(int i = 0; i < 100; i++)
		{
			expression[i] = new JLabel(new PicsIcon("src/expression/" + i + ".gif", i));
			expression[i].setBorder(BorderFactory.
					createLineBorder(new Color(255, 255, 255), 1));
			expression[i].setToolTipText(i + "");
			expression[i].addMouseListener(
			new MouseAdapter()
			{
				@Override
				public void mouseClicked(MouseEvent e)
				{
					if(e.getButton() == 1)
					{
						PicsIcon pic = (PicsIcon)((JLabel)e.getSource()).getIcon();
						owner.Insertpic(pic);
						getObj().dispose();
					}
				}
				@Override
				public void mouseEntered(MouseEvent e)
				{
					((JLabel)e.getSource()).setBorder(BorderFactory.createLineBorder(Color.blue));
				}
				@Override 
				public void mouseExited(MouseEvent e)
				{
					((JLabel)e.getSource()).setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255), 1));
				}
			});
			p.add(expression[i]);
		}
	}
	//重写显示
	@Override
	public void setVisible(boolean show)
	{
		if(show)
		{
			determineAndSetLocation();
		}
		super.setVisible(show);
	}
	//定位表情框显示的位置
	public void determineAndSetLocation()
	{
		Point loc = owner.getLocationOnScreen();
		this.setLocation(loc.x - 110, loc.y + 70);
	}
	private JWindow getObj()
	{
		return this;
	}

}
