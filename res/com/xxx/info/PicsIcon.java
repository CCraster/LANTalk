package com.xxx.info;

import java.net.URL;
import javax.swing.ImageIcon;

/*
 * 自己重写的继承ImageIcon的类，用于表情的使用
 */

public class PicsIcon extends ImageIcon
{
	private int num;
	
	public PicsIcon(String url, int num)
	{
		super(url);
		this.num = num;
	}
	
	public int getNum()
	{
		return num;
	}
	public void setNum(int num)
	{
		this.num = num;
	}
}
