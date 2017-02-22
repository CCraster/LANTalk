package com.xxx.info;

import java.awt.Color;
import java.awt.Font;
import java.net.*;
import java.util.Date;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.xxx.LanTalk;
import com.xxx.frame.ChatFrame;
import com.xxx.util.MyProtocol;

public class UserInfo
{
	//���û���ͼ��
	private String icon;
	//���û�������
	private String name;
	//���û��ĵ�½ʱ��
	private String loginTime;
	//���û���MulitcastSocket���ڵ�IP�Ͷ˿�
	private SocketAddress address;
	//���û�ʧȥ��ϵ�Ĵ���
	private int lost;
	//�û�����������
	private Font userFont= new Font("Monospaced", Font.PLAIN, 12);;
	private Color fontColor;
	//���û���Ӧ�Ľ�̸����
	private ChatFrame chatFrame;
	
	public UserInfo(){}

	public UserInfo(String icon , String name ,String loginTime, int lost)
	{
		this.icon = icon;
		this.name = name;
		this.loginTime = loginTime;
		this.lost = lost;
		this.userFont = new Font("Monospaced", Font.PLAIN, 12);
		this.fontColor = Color.black;
	}
	
	public UserInfo(String icon , String name ,String loginTime
			,SocketAddress address , int lost)
	{
		this.icon = icon;
		this.name = name;
		this.loginTime = loginTime;
		this.address = address;
		this.lost = lost;
		this.userFont = new Font("Monospaced", Font.PLAIN, 12);
		this.fontColor = Color.black;
	}

	public void setIcon(String icon)
	{
		this.icon = icon;
	}
	public String getIcon()
	{
		 return this.icon;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	public String getName()
	{
		 return this.name;
	}
	
	public String getloginTime()
	{
		 return this.loginTime;
	}

	public void setAddress(SocketAddress address)
	{
		this.address = address;
	}
	public SocketAddress getAddress()
	{
		 return this.address;
	}

	public void setLost(int lost)
	{
		this.lost = lost;
	}
	public int getLost()
	{
		 return this.lost;
	}
	
	public void setFont(Font userFont)
	{
		this.userFont = userFont;
	}
	public String getFont()
	{
		String font = "";
		font += this.userFont.getFontName() + MyProtocol.FONT
				+ this.userFont.getStyle() + MyProtocol.FONT;
		font += userFont.getSize();
		
		return font;
	}
	
	public void setColor(Color color)
	{
		this.fontColor = color;
	}
	public Color getColor()
	{
		return this.fontColor;
	}
	
	public void setChatFrame(ChatFrame chatFrame)
	{
		this.chatFrame = chatFrame;
	}
	public ChatFrame getChatFrame()
	{
		return this.chatFrame;
	}

	//ʹ��address��Ϊ���û��ı�ʶ
	public int hashCode()
	{
		return address.hashCode();
	}
	public boolean equals(Object obj)
	{
		if (obj != null && 
			obj.getClass() == UserInfo.class)
		{
			return ((UserInfo)obj).getAddress().equals(this.address);
		}
		return false;
	}
}