package com.xxx.info;

import java.net.URL;
import javax.swing.ImageIcon;

/*
 * �Լ���д�ļ̳�ImageIcon���࣬���ڱ����ʹ��
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
