package com.bn.sn;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Constant 
{
	final static int SET_BRANCH=2;//此页面为设置界面的分支
	final static int MAIN_BRANCH=1;//此页面为主界面的分支
	final static int MAIN=0;//此页面为主界面的分支	
	final static int SAVEBELL=3;//此页面为设置铃声界面
	final static int DATEPICKER_DIALOG=10;//选择日期对话框的id
	//铃声数组
	final static int[] SONGID={R.raw.kanong,R.raw.kanongjita,R.raw.kanongpanio,R.raw.kanongyuanban,R.raw.musicbox,R.raw.moning,R.raw.sky,R.raw.girlflower,R.raw.kawayi,R.raw.couldthisbelove,R.raw.fatherbyhand,R.raw.whenidream};
	final static String[] BELLNAME={"卡农清音","卡农吉他版","卡农钢琴版","卡农原声版","八音盒","清晨","天空之城","女人花","可爱童音","Could Be Love","Father By Tby Hand","When I Dream"};//显示的铃声名称
	//==========================
	public  static int currYear;		  
	public static int currMonth;  
	public static int currDay;
	public  static int currYear1;		  
	public static int currMonth1;  
	public static int currDay1;
	public final static int DATE_DIALOG=0;
	public static int leftpadding=5; //距离屏幕上方的距离
	public static int toppadding=5;//距离屏幕坐标的距离
	public static int  Scheduleprev_x; //获取x的坐标              
	public static int  Scheduleprev_y;//获取y的坐标
	public static boolean flag=true;//日试图是否显示现在日期的标志位。
	public static boolean flag1=true;//周试图是否显示现在日期的标志位
	public static boolean flag2=false;//不给日期对话框添加监听。if true 则添加监听。
	public static final int TIME_DIALOG = 0;
	public static final int DATE_DIALOG1 = 1;
	protected static final int MORE_EDIT = 13;
	protected static final int ADD_CONTACT = 12;
	public static final int DIALOG_ADD_TYPE = 11;
	public static final int TIME_ALERT = 10;
	protected static final int ADD_MUSIC = 14;
	public static int  Scheduleprev_xdown; //获取x的坐标              
	public static int  Scheduleprev_ydown;//获取y的坐标    
	public static String weekday[]=new String[10];
	public static int weekYears[]=new int[10]; // 存取年月日。
	public static int weekMonths[]=new int[10];
	public static int weekDays[]=new int[10];
	public static int monthweekday=1;//如果等于1 则由1月视图转换到得  搜索。如果为2则为周视图转换到得搜索。如果为3则为日试图。	
	//==========================
	
	//将date转化为string
	public static String DateToString(Date date){
		String string=null;
		//格式化日期
		SimpleDateFormat sdf=new SimpleDateFormat("yy-MM-dd HH:mm");
		string=sdf.format(date);
		return string;	
	}
	//将字符串转回日期
	public static Date StringToDate(String dateStr)
	{
		Date date=null;
		//转换格式
		SimpleDateFormat sdf=new SimpleDateFormat("yy-MM-dd HH:mm");
		try
		{
			date=sdf.parse(dateStr);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return date;
	}
}
