package com.xxx.util;

public interface MyProtocol
{
	String PRESENCE = "@@"; 	 //在线信息广播
	String SPLITTER = "&&"; 	 //登陆信息分割
	String FONT = "~~";  		 //字体分割
	String SENDFONT = "$$";      //发送用户的字体设置
	String EXP_SPLIT = "!!";     //表情分割
	String EXP_DETAIL = "%%";    //表情定位分割
	String P_SPLIT = "##";		 //发送区域分隔符
	String SHAKE = "@#";		 //震动信息标志
	String FILE = "@&";			 //文件标志
	String FILE_REFUSE = "NOSEND"; //拒绝接收文件
	String FILE_END = "FILE_END"; //文件结束标志
}


