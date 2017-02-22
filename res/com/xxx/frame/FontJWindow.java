package com.xxx.frame;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.StyleConstants;

import com.xxx.util.MyProtocol;

public class FontJWindow extends JWindow
{
	private ChatFrame owner;
	private JComboBox jcbFontName, jcbFontSize;
	private JButton jbBold, jbItalic, jbPlain, jbColor;
	private String[] fontName;
	private Integer[] fontSize = {9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20
							, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30};
	
	public FontJWindow(ChatFrame owner)
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
	
	public void init() throws Exception
	{
		this.setSize(365, 30);
		JPanel p = new JPanel();
		p.setLayout(null);
		p.setOpaque(false);
		this.setContentPane(p);
		
		FontListener fontListener = new FontListener();
		GraphicsEnvironment getFont = GraphicsEnvironment.getLocalGraphicsEnvironment();
		fontName = getFont.getAvailableFontFamilyNames();
		jcbFontName = new JComboBox(fontName);
		jcbFontName.setSelectedItem("仿宋");
		jcbFontName.addActionListener(fontListener);
		p.add(jcbFontName);
		jcbFontName.setBounds(3, 3, 100, 26);
		
		jcbFontSize = new JComboBox(fontSize);
		jcbFontSize.setSelectedItem(12);
		jcbFontSize.addActionListener(fontListener);
		p.add(jcbFontSize);
		jcbFontSize.setBounds(108, 3, 50, 26);
		
		jbBold = new JButton(new ImageIcon("src/font_image/bold_normal.png"));
		jbBold.setRolloverIcon(new ImageIcon("src/font_image/bold_focus.png"));
		jbBold.setFocusPainted(false);
		jbBold.setContentAreaFilled(false);
		jbBold.setToolTipText("加粗");
		jbBold.addActionListener(fontListener);
		p.add(jbBold);
		jbBold.setBounds(163, 3, 26, 26);
		
		jbItalic = new JButton(new ImageIcon("src/font_image/italic_normal.png"));
		jbItalic.setRolloverIcon(new ImageIcon("src/font_image/italic_focus.png"));
		jbItalic.setFocusPainted(false);
		jbItalic.setContentAreaFilled(false);
		jbItalic.setToolTipText("斜体");
		jbItalic.addActionListener(fontListener);
		p.add(jbItalic);
		jbItalic.setBounds(190, 3, 26, 26);
		
		jbPlain = new JButton(new ImageIcon("src/font_image/plain_normal.png"));
		jbPlain.setRolloverIcon(new ImageIcon("src/font_image/plain_focus.png"));
		jbPlain.setFocusPainted(false);
		jbPlain.setContentAreaFilled(false);
		jbPlain.setToolTipText("普通字体");
		jbPlain.addActionListener(fontListener);
		p.add(jbPlain);
		jbPlain.setBounds(217, 3, 26, 26);
		
		jbColor = new JButton(new ImageIcon("src/font_image/color_normal.png"));
		jbColor.setRolloverIcon(new ImageIcon("src/font_image/color_focus.png"));
		jbColor.setFocusPainted(false);
		jbColor.setContentAreaFilled(false);
		jbColor.setToolTipText("颜色");
		jbColor.addActionListener(fontListener);
		p.add(jbColor);
		jbColor.setBounds(244, 3, 26, 26);
	}
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
		this.setLocation(loc.x + 10, loc.y + 337);
	}
	private JWindow getObj()
	{
		return this;
	}
	
	class FontListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == jcbFontName)
			{
				String fontName = (String)jcbFontName.getSelectedItem();
				String[] userFont = (owner.getSelf().getFont()).split(MyProtocol.FONT);
				Font newFont = new Font(fontName, Integer.parseInt(userFont[1]), Integer.parseInt(userFont[2]));
				owner.getSelf().setFont(newFont);
				owner.getInputPane().setFont(newFont);
			}
			else if(e.getSource() == jcbFontSize)
			{
				int fontSize = (Integer)jcbFontSize.getSelectedItem();
				String[] userFont = (owner.getSelf().getFont()).split(MyProtocol.FONT);
				Font newFont = new Font(userFont[0], Integer.parseInt(userFont[1]), fontSize);
				owner.getSelf().setFont(newFont);
				owner.getInputPane().setFont(newFont);
			}
			else if(e.getSource() == jbBold)
			{
				String[] userFont = (owner.getSelf().getFont()).split(MyProtocol.FONT);
				Font newFont = null;
				if(jbBold.getIcon().toString().equals("src/font_image/bold_normal.png"))
				{
					newFont = new Font(userFont[0], Font.BOLD, Integer.parseInt(userFont[2]));
					jbBold.setIcon(new ImageIcon("src/font_image/bold_down.png"));
					jbItalic.setIcon(new ImageIcon("src/font_image/italic_normal.png"));
					jbPlain.setIcon(new ImageIcon("src/font_image/plain_normal.png"));
				}
				else
				{
					newFont = new Font(userFont[0], Font.PLAIN, Integer.parseInt(userFont[2]));
					jbBold.setIcon(new ImageIcon("src/font_image/bold_normal.png"));
				}
				owner.getSelf().setFont(newFont);
				owner.getInputPane().setFont(newFont);
			}
			else if(e.getSource() == jbItalic)
			{
				String[] userFont = (owner.getSelf().getFont()).split(MyProtocol.FONT);
				Font newFont = null;
				if(jbItalic.getIcon().toString().equals("src/font_image/italic_normal.png"))
				{
					newFont = new Font(userFont[0], Font.ITALIC, Integer.parseInt(userFont[2]));
					jbBold.setIcon(new ImageIcon("src/font_image/bold_normal.png"));
					jbItalic.setIcon(new ImageIcon("src/font_image/italic_down.png"));
					jbPlain.setIcon(new ImageIcon("src/font_image/plain_normal.png"));
				}
				else
				{
					newFont = new Font(userFont[0], Font.PLAIN, Integer.parseInt(userFont[2]));
					jbItalic.setIcon(new ImageIcon("src/font_image/italic_normal.png"));
				}
				owner.getSelf().setFont(newFont);
				owner.getInputPane().setFont(newFont);
			}
			else if(e.getSource() == jbPlain)
			{
				String[] userFont = (owner.getSelf().getFont()).split(MyProtocol.FONT);
				Font newFont = null;
				if(jbPlain.getIcon().toString().equals("src/font_image/plain_normal.png"))
				{
					newFont = new Font(userFont[0], Font.PLAIN, Integer.parseInt(userFont[2]));
					jbBold.setIcon(new ImageIcon("src/font_image/bold_normal.png"));
					jbItalic.setIcon(new ImageIcon("src/font_image/italic_normal.png"));
					jbPlain.setIcon(new ImageIcon("src/font_image/plain_down.png"));
				}
				else
				{
					newFont = new Font(userFont[0], Font.PLAIN, Integer.parseInt(userFont[2]));
					jbPlain.setIcon(new ImageIcon("src/font_image/plain_normal.png"));
				}
				owner.getSelf().setFont(newFont);
				owner.getInputPane().setFont(newFont);
			}
			else if(e.getSource() == jbColor)
			{
				Color color = JColorChooser.showDialog(getObj(), "请选择一种颜色", owner.getSelf().getColor());
				if(color != null)
				{
					owner.getSelf().setColor(color);
					owner.getInputPane().setForeground(color);
				}
			}
		}
	}
}
