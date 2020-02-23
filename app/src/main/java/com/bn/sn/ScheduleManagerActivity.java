package com.bn.sn;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.bn.calendar.BNCalendar;
import com.bn.calendar.BehindBlankActionListener;
import com.bn.calendar.CaActionListener;
import com.bn.calendar.ControlCenterActionListener;
import com.bn.calendar.FrontBlankActionListener;
import com.bn.calendar.NextActionLinstener;
import com.bn.calendar.PrevActionLinstener;



import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Handler;
import android.provider.Contacts.People;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;

public class ScheduleManagerActivity extends Activity 
{
	//=======================tongBegin======================
	Calendar calendar=Calendar.getInstance();	 //给日试图使用。
	Calendar calendar1=Calendar.getInstance(); //月视图使用
	Calendar calendar2=Calendar.getInstance(); //给日试图  周视图初始化日期。
	Calendar calendar3=Calendar.getInstance();
	final ArrayList <LinearLayout> savelinearlayout=new ArrayList <LinearLayout>() ;
	//==========================end=================
	AlarmManager alarmManager;//闹钟管理对象声明
	Date curDate=new Date(System.currentTimeMillis());//记录当前选中的日期时间
	BNCalendar bnc;	//日历控件的引用声明
	com.bn.calendar.BNCalendar selectbnc;//新建编辑中选择日期用
	private boolean isSelect=false;//标志是否为新建中的对话框，true为是
	private int curr;//标志当前正在显示页面的级别
	private LoginDialog ld;//登录对话框
	Dialog dateDialog;//修改年月日的对话框
	boolean hasPwd=false;//是否设有密码的标志
	private String pwd;//密码
	private SharedPreferences spwd;//取得存密码的sharedpreferences
	private Calendar todayCal;//存储系统当前时间
	//声明日历下方显示月日及添加按钮的对象
	private TextView showDateTV;
	private GridView gv;//用于盛放抽屉内容的view
	private ImageView hi;//抽屉拉手图片
	private SlidingDrawer sliding;//抽屉引用
	//抽屉内容中图片的id数组
	private int[] icons={R.drawable.filenew,R.drawable.selecticon,R.drawable.systemicon,
					R.drawable.dayviewicon,R.drawable.weekviewicon,R.drawable.monthviewicon,
					R.drawable.helpericon,R.drawable.messagicon,R.drawable.exiticon};
	//抽屉内容图片下面的文字内容
	private String[] textSliding={"新建日程","查询日程","系统设置","日视图","周视图","月视图","小助手","软件信息","退出"};	
	private SharedPreferences bell;//取得当前铃声id的sharedpreferences
	MediaPlayer mp;//试听使用的播放器
	//===============================guang=============
	DBUtil db=new DBUtil();
	Constant cs;
	Dialog changetimedialog;
	ArrayList<String> al=new ArrayList<String>();
	ArrayList<String> editcontent=new ArrayList<String>();
	String result=null;//短信
	String[] defultType=new String[]{"学习","工作","生活"};//软件的三个默认类型
	protected static final int DIALOG_DELETE_TYPE = 0;
	protected static final int DIALOG_ADD_TYPE = 1;
	protected static final int TIME_DIALOG = 2;
	protected static final int TIME_ALERT = 3;
	protected static final int FUJIAN_LIST = 4;
	protected static final int PICTURE_SELECT = 5;
	protected static final int ADD_MESSAGE = 6;
	protected static final int ADD_LOCATION=7;
	protected static final int UPDATE_TIME=0;//更新录音时间的消息编号
	protected static final int PICK_CONTACT = 0;	
	File myFile ;//用于存放音轨的文件	
	//初始化提前通知
	 boolean[] mulFlags=new boolean[]{true,false,false,false,false,false,false};
	 String[] items = null;
     public int id=2;
     public int editid=0;
	 public String title=null;
	 public int content=0;//内容的ID
	 public String selectedtype=null;
	 public String person=null;
	 public String place=null;
	 public String   alertstyle=null;	 
	 public int alerttime=0;
	 String alertone=null;
	 String alerttwo=null;
	 String alertthree=null;
	 String alertfour=null;
	 String alertfive=null;
	 String alertsix=null;
	 String alertseven=null;
	 Dialog dialogSetRange;
	 private String temp=null;
	 public String nowtime=null;
	 public int  type=0;
	 Date currdate;	
	 static ArrayList<String> alType=new ArrayList<String>();//存储所有日程类型的arraylist
	 protected int postion;
	 protected Bundle savedInstanceState;
	 public String text=null;
	 protected String soundpath=null;
	 protected String picpath=null;	
	//============录制声音=============
	    Handler hd;//消息处理器
	    boolean recordFlag=false;//录制中标志
		boolean isplay=false;//标志是否在播放中
		MediaRecorder myMediaRecorder;//媒体录音机
		int countSecond=0;//录制的秒数
		//musicpath; //临时纪录声音路径
		String musicpath=null;//每次记录新的声音路径
	 
      //======================获取图片
	 public static final int NONE = 0; 
     public static final int PHOTOHRAPH = 1;// 拍照  
     public static final int PHOTOZOOM = 2; // 缩放  
     public static final int PHOTORESOULT = 3;// 结果  
 
     public static final String IMAGE_UNSPECIFIED = "image/*"; 
     ImageView imageView ; 
     Button photosave;
     Button photocancle;
     Bitmap photo;
     private PhotoDialog PhotoDialog;
	 private Button xiangce;
	 private Button paizhao;
	 private static int recodes=0;
	 Handler hdd=new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				switch(msg.what)
				{
					case 0:
					begin();
					break;
				}
			}
		};
    //==========================
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏 
      //程序开始
        //注：模式为私有不允许其他程序访问
        requestWindowFeature(Window.FEATURE_NO_TITLE);//无标题
	    goToWelcomeView();      //开始界面。
      //================================
    }
    public void begin()
    {
		SharedPreferences mainid=this.getSharedPreferences("mainid",Context.MODE_PRIVATE);
		int id=mainid.getInt("mainid", 1);
		if(id==1) //判断显示哪个主界面界面
	       {
	    	   gotoMonthView();
	       }
	       else if(id==2)
	       {
	        	createWeek(); 
	       }else
	       {
	    	    createDay();
	       }    
		//获取sharedpreferences是否存有密码，若有的话则显示登录界面若没有则直接显示
    	spwd=this.getSharedPreferences("storepwd",Context.MODE_PRIVATE);
    	pwd=spwd.getString("pwd", null);
    	if(pwd!=null)
    	{
    		hasPwd=true;//改变标志位设有密码
	    	//生成一个登录对话框
			ld=new LoginDialog(this,pwd);
			ld.show();
    	}	
    	//================================
    	Constant.currYear=calendar2.get(Calendar.YEAR);
        Constant.currMonth=calendar2.get(Calendar.MONTH);
        Constant.currDay=calendar2.get(Calendar.DAY_OF_MONTH);
        Constant.currYear1=calendar2.get(Calendar.YEAR);
        Constant.currMonth1=calendar2.get(Calendar.MONTH);
        Constant.currDay1=calendar2.get(Calendar.DAY_OF_MONTH);
    }
    public void goToWelcomeView()
    {

    	MySurfaceView mview=new MySurfaceView(this);
    	getWindow().setFlags//全屏
    	(
    			WindowManager.LayoutParams.FLAG_FULLSCREEN, 
    			WindowManager.LayoutParams.FLAG_FULLSCREEN
    	);
    	setContentView(mview);  
    }
    //==========================日视图begin===================
    public void createDay() //创建日视图
    {   	curr=Constant.MAIN;    	
        	setContentView(R.layout.main);   
        	recodes=0;
        	 // 给日期添加监听。
        	showSlidingDrawer();
        	Constant.monthweekday=3;
     	BnScheduleManagerView bmv=(BnScheduleManagerView)findViewById(R.id.BnScheduleManagerView01);
        	bmv.addActionListener(       			
         new BnScheduleViewListener()
         {
 			@Override
 			public void doAction(Calendar cd) {	
 				 calendar.set(cd.get(Calendar.YEAR),cd.get(Calendar.MONTH),cd.get(Calendar.DAY_OF_MONTH));					 
 				 if(Constant.flag2==true)
 				 {
 					showDialog(Constant.DATEPICKER_DIALOG);		
 				 }
 				CreatDayListView();			 	 		
 			} 
          }
        );  
        CreatDayListView();        
 }
    public void CreatDayListView()
    {     
		        ExpandableListView exList=(ExpandableListView)findViewById(R.id.exlist01);
		      //设置组前面的图标
				exList.setGroupIndicator(this.getResources().getDrawable(R.drawable.gourpexpandle));
	            List<String> groupArray = new ArrayList<String>();  
		        groupArray.add("上午"); //添加的组类名字、  
	            groupArray.add("下午");  
	            groupArray.add("晚上"); 	            
	            final List<List<String>> childArray = new ArrayList<List<String>>();  
	            List<String> tempArray01 = new ArrayList<String>();  	           
	            Date date=new Date(calendar.getTimeInMillis());    //  将calendar转换成date。 	            
	    
	           Date date1=date;   
	           date1.setHours(0); 
	           date1.setMinutes(0);
	           date1.setSeconds(0);	           
	           final  ArrayList<String> moring=(ArrayList<String>) DBUtil.getContent(this,date1);         // 早上
	           for(int i=0;i<moring.size();i++)
	  		   {
	        	  String ch=moring.get(i).split("#")[1];	
	  			  tempArray01.add(ch);
	  		    }
               Date date3=date;   
	            date3.setHours(12); 
	            date3.setMinutes(0);
	            date3.setSeconds(0); 	     	                                                            
	     	   List<String> tempArray02 = new ArrayList<String>();  // 下午 	      	   
	     	   final ArrayList<String> afternoon=(ArrayList<String>) DBUtil.getContent(this,date3);         // 早上
	           for(int i=0;i<afternoon.size();i++)
	  		    {
	        	    String ch=afternoon.get(i).split("#")[1];	
	  			    tempArray02.add(ch);	  			
	  		    } 	     	    
	     	    Date date2=date;   
	            date2.setHours(19); 
	            date2.setMinutes(0);
	            date2.setSeconds(0);	     	                                          
		        List<String> tempArray03 = new ArrayList<String>();   // 晚上  	 		    		        
		       final  ArrayList<String> night=(ArrayList<String>) DBUtil.getContent(this,date2);         // 早上
	           for(int i=0;i<night.size();i++)
	  		   {
	        	    String ch=night.get(i).split("#")[1];	
	  			    tempArray03.add(ch);
	  		    }	
               childArray.add(tempArray01);  
		        childArray.add(tempArray02);  
		        childArray.add(tempArray03);
		        ExpandableAdapterER adapter = new ExpandableAdapterER(this,groupArray,childArray);    
		        exList.setAdapter(adapter);   		
		       //添加点击子项监听 		
    	        exList.setOnChildClickListener(
				new OnChildClickListener()
				{
					@Override
					public boolean onChildClick(ExpandableListView parent,
							View v, int groupPosition, int childPosition,
							long id) { 						
						String idd;
						if(groupPosition==0)
						{
							 idd=moring.get(childPosition).split("#")[0]; //获取选中日程的相应id号。 							
						}else if(groupPosition==1)			
						{
							 idd=afternoon.get(childPosition).split("#")[0];
						}else
						{
							 idd=night.get(childPosition).split("#")[0];
						}
						//调用对话框
						int id1=Integer.parseInt(idd);				
						Date date=new Date(calendar.getTimeInMillis()); //将日期传送给  传送给新建 。	
					    
					 	FileEditDialog edit=new FileEditDialog(ScheduleManagerActivity.this,id1,date,2);					
						edit.show();
						return true;
					}
				}
		);
   }   
    //==========================日视图end===================    
    //===========================周视图begin======================
    public void createWeek()//创建周视图
    {
    	  curr=Constant.MAIN;
          setContentView(R.layout.week); 
          recodes=0;
          showSlidingDrawer();         
          Constant.monthweekday=2;
          BnWeekView bwv=(BnWeekView)findViewById(R.id.BnWeekView01);
          final TextView tv=(TextView)this.findViewById(R.id.TextView01);
          int m=calendar1.get(Calendar.MONTH)+1;
    	  tv.setText("      "+m+"月 "+calendar1.get(Calendar.DAY_OF_MONTH)+"日");   
    	  bwv.DateDialogListener(
    	  new DateDialogListener()
    	  {
			@Override
			public void dialogListener(Calendar ca) {
				
			  calendar.set(ca.get(Calendar.YEAR),ca.get(Calendar.MONTH),ca.get(Calendar.DAY_OF_MONTH));
			  calendar1.set(ca.get(Calendar.YEAR),ca.get(Calendar.MONTH),ca.get(Calendar.DAY_OF_MONTH));
         	  int c=ca.get(Calendar.MONTH)+1;
			  tv.setText("      "+c+"月 "+ca.get(Calendar.DAY_OF_MONTH)+"日");			 
			  showDialog(Constant.DATEPICKER_DIALOG);	
			}   		  
    	  }    	  
    	  );
    	  bwv.addActionListener(       			
          new BnBeWeekViewListener()
          {
			@Override
			public void doAction(int x, int y, int z) {
			 tv.setText("      "+y+"月 "+z+"日");	
			 int m=y-1;
			 calendar1.set(x,m,z);	
			 weekListView();
			}  
         }
       );     	
       ImageView  ii=(ImageView)this.findViewById(R.id.ImageView01);		
		ii.setImageDrawable(getResources().getDrawable(R.drawable.add));
        ii.setOnClickListener(
        new OnClickListener()
        {
			@Override
			public void onClick(View v) {
				Date date=new Date(calendar1.getTimeInMillis());
				newschedule(date);//添加新建
        }
        }
        );              
        weekListView();
    }
    public void weekListView()
    {
 	     Date date=new Date(calendar1.getTimeInMillis()); 
 	     Date date1=date;   
          date1.setHours(0); 
          date1.setMinutes(0);
          date1.setSeconds(0);	
          final  ArrayList<String> weekcontent=(ArrayList<String>) DBUtil.getweekContent(this,date1);       
                 
          ListView lv=(ListView)this.findViewById(R.id.ListView01);                
          BaseAdapter ba=new BaseAdapter()
 	        {
 				@Override
 				public int getCount() {
 					return weekcontent.size();//总共个选项
 				}
 				@Override
 				public Object getItem(int arg0) { return null; }
 				@Override
 				public long getItemId(int arg0) { return 0; }
 				@Override
 				public View getView(int arg0, View arg1, ViewGroup arg2) {		
 					TextView tv=new TextView(ScheduleManagerActivity.this);
 					LinearLayout ll=new LinearLayout(ScheduleManagerActivity.this);
 					
 				    tv.setText(weekcontent.get(arg0).split("#")[1]);//设置内容 	
 				    tv.setTextColor(R.color.black);
 				    tv.setTextSize(15);
 					ll.setPadding(5,5,5,5);//设置四周留白					
 				    tv.setGravity(Gravity.LEFT);
 				    ll.addView(tv);
 				    return ll;
 				}        	
 	        };     
 	        lv.setAdapter(ba);//为ListView设置内容适配器         
 	        //设置选项选中的监听器  
 	        final Date date2=new Date(calendar1.getTimeInMillis()); 
 	   
 	        lv.setOnItemClickListener(
 	                new OnItemClickListener()
 	                {
 	     			@Override
 	     			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
 	     					long arg3) {//重写选项被单击事件的处理方法     				   	 		    	 			  
 	     	 			  String id1=weekcontent.get(arg2).split("#")[0];
 	     	 			  int id2=Integer.parseInt(id1);  	     	 			
 	     	 			  FileEditDialog fedd=new FileEditDialog(ScheduleManagerActivity.this,id2,date2,3);	// 弹出对话框   			    	              
 	     	              fedd.show();     			     			
 	     			}        	   
 	              }
 	         );    
    }
    //===========================周视图end========================
    //========================查询begin=====================
    public void bnsearch()// 搜索日程界面
    {   	
    	curr=Constant.MAIN_BRANCH;
    	setContentView(R.layout.search); 	 
    	ImageView iv=(ImageView)this.findViewById(R.id.ImageView01);	
    	iv.setImageDrawable(getResources().getDrawable(R.drawable.back));
    	TextView tv=(TextView)this.findViewById(R.id.TextView01);
    	tv.setTextColor(0x99009999);
    	tv.setTextSize(18);
    	iv.setOnClickListener(
    	 new OnClickListener()
    	 {
    		@Override
    		public void onClick(View v) {			
    			if(Constant.monthweekday==1)
    			{
    				gotoMonthView();
           		     // 返回到月视图
    			}else if(Constant.monthweekday==2)
    			{
    				 createWeek();// 返回到周视图
    				
    				
    			}else 
    			{
    				   createDay();//返回到日试图
    			}	 	
    		}
    	 }
    	);   
    	
   	ImageView iv2=(ImageView)this.findViewById(R.id.ImageView03);
    	iv2.setImageDrawable(getResources().getDrawable(R.drawable.icon_search));
    	//对搜索图片添加监听。    	
    	iv2.setOnClickListener(
    	 new OnClickListener()
    	 {
			@Override
			public void onClick(View v) {				
				 searchmatch();	
			}    	 
    	 }   	
    	);		
    	ListView lvv=(ListView)this.findViewById(R.id.ListView02);   	
    	final ArrayList<String> al=DBUtil.gettype(this); //查询类型
    	BaseAdapter ba=new BaseAdapter()
	        {
				@Override
				public int getCount() {
					return al.size();//总共个选项
				}
				@Override
				public Object getItem(int arg0) { return null; }
				@Override
				public long getItemId(int arg0) { return 0; }
				@Override
				public View getView(int arg0, View arg1, ViewGroup arg2) {		
				LinearLayout ll=new LinearLayout(ScheduleManagerActivity.this);
				savelinearlayout.add(ll);
				ll.setOrientation(LinearLayout.HORIZONTAL);		
				ImageView iv=new ImageView(ScheduleManagerActivity.this);
				iv.setImageDrawable(getResources().getDrawable(R.drawable.tool_almanac_query_icon));
				ll.addView(iv);
     			TextView tv=new TextView(ScheduleManagerActivity.this);	 			
			    tv.setText(al.get(arg0).split("#")[1]);//设置内容 	
			    tv.setTextSize(12);
     			tv.setTextColor(R.color.black); 		    
				ll.setPadding(0,5,5,5);//设置四周留白					
			    ll.addView(tv);
			    return ll;
				}        	
	        };     
	        lvv.setAdapter(ba);//为ListView设置内容适配器         
	        //设置选项选中的监听器  
	        lvv.setOnItemClickListener(
	                new OnItemClickListener()
	                {
	     			@Override
	     			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	     			long arg3) {//重写选项被单击事件的处理方法  
	     		
	     				final int t=Integer.parseInt(al.get(arg2).split("#")[0]);
	     				final String id2=al.get(arg2).split("#")[1];
	     				final LinearLayout ll=(LinearLayout)arg1;	     				
	     				 if(t==(-1))
	     				{	
							searchallcontent();		
							selectbackground(ll);	

	     				}else
	     				{
	     					searchtypecontent(t);		
	     					selectbackground(ll);			
	     			 }     	 			 	     	 			  
	     			}        	   
	              }
	         ); 
    }
    public void selectbackground(LinearLayout ll)
    {
        for(int i=0;i<savelinearlayout.size();i++)
        {
       	 if( ll==savelinearlayout.get(i))
       	 {
       		 ll.setBackgroundResource(R.drawable.bgg);
       	 }else
       	 {
       		 savelinearlayout.get(i).setBackgroundColor(0x000000);
       	 }        	 
        }  	
    }
    public void searchallcontent() // 查询所有日程。
    {	 
    	final Date date=new Date();
 	    final ArrayList<String> all=DBUtil.gettypeContent(ScheduleManagerActivity.this, -1);
 	
         ListView lv=(ListView)this.findViewById(R.id.ListView01);                
         BaseAdapter ba=new BaseAdapter()
	        {
				@Override
				public int getCount() {
					return all.size();//总共个选项
				}
				@Override
				public Object getItem(int arg0) { return null; }
				@Override
				public long getItemId(int arg0) { return 0; }
				@Override
				public View getView(int arg0, View arg1, ViewGroup arg2) {		
					TextView tv=new TextView(ScheduleManagerActivity.this);
					LinearLayout ll=new LinearLayout(ScheduleManagerActivity.this);
				    tv.setText(all.get(arg0).split("#")[1]);//设置内容 	
				    tv.setTextColor(R.color.black); 				 
					ll.setPadding(5,5,5,5);//设置四周留白					
				    tv.setGravity(Gravity.LEFT);
				    ll.addView(tv);
				    return ll;
				}        	
	        };     
	        lv.setAdapter(ba);//为ListView设置内容适配器         
	        //设置选项选中的监听器  
	        lv.setOnItemClickListener(
	                new OnItemClickListener()
	                {
	     			@Override
	     			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	     					long arg3) {//重写选项被单击事件的处理方法     				   	 		    	 			  
	     	 			  String id1=all.get(arg2).split("#")[0];
	     	 			  int id2=Integer.parseInt(id1);  	     	 			
	     	 			  FileEditDialog fedd=new FileEditDialog(ScheduleManagerActivity.this,id2,date,4);	// 弹出对话框   			    	              
	     	              fedd.show();     			     			
	     			}        	   
	              }
	         );     
    }
    public void searchtypecontent(final int typeid) //查询分类的日程
    {	   
       final Date date=new Date();
 	   final ArrayList<String> all=DBUtil.gettypeContent(ScheduleManagerActivity.this,typeid);
         ListView lv=(ListView)this.findViewById(R.id.ListView01);                
         BaseAdapter ba=new BaseAdapter()
	        {
				@Override
				public int getCount() {
					return all.size();//总共个选项
				}
				@Override
				public Object getItem(int arg0) { return null; }
				@Override
				public long getItemId(int arg0) { return 0; }
				@Override
				public View getView(int arg0, View arg1, ViewGroup arg2) {		
					TextView tv=new TextView(ScheduleManagerActivity.this);
					LinearLayout ll=new LinearLayout(ScheduleManagerActivity.this);
				    tv.setText(all.get(arg0).split("#")[1]);//设置内容 	
				    tv.setTextColor(R.color.black); 				 
					ll.setPadding(5,5,5,5);//设置四周留白					
				    tv.setGravity(Gravity.LEFT);
				    ll.addView(tv);
				    return ll;
				}        	
	        };     
	        lv.setAdapter(ba);//为ListView设置内容适配器         
	        //设置选项选中的监听器  
	        lv.setOnItemClickListener(
	                new OnItemClickListener()
	                {
	     			@Override
	     			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	     					long arg3) {//重写选项被单击事件的处理方法     				   	 		    	 			  
	     	 			  String id1=all.get(arg2).split("#")[0];
	     	 			  int id2=Integer.parseInt(id1);  	     	 			
	     	 			  FileEditDialog fedd=new FileEditDialog(ScheduleManagerActivity.this,id2,date,4);	// 弹出对话框   			    	              
	     	              fedd.show();     			     			
	     			}        	   
	              }
	         );  
   
    }    
    public void searchmatch()
    {
 	       EditText et=(EditText)this.findViewById(R.id.EditText02); 
 	       final Date date=new Date();
 	       if(et.getText().toString().length()!=0)
 	       {
            final ArrayList<String> al=DBUtil.searchContent(this, et.getText()+"");
            if(al.size()==0)
            {
     		   Toast.makeText(this, "对不起，没有相关内容！！！", Toast.LENGTH_SHORT).show();
            }
            ListView lv=(ListView)this.findViewById(R.id.ListView01);                
            BaseAdapter ba=new BaseAdapter()
   	        {
   				@Override
   				public int getCount() {
   					return al.size();//总共个选项
   				}
   				@Override
   				public Object getItem(int arg0) { return null; }
   				@Override
   				public long getItemId(int arg0) { return 0; }
   				@Override
   				public View getView(int arg0, View arg1, ViewGroup arg2) {		
   					TextView tv=new TextView(ScheduleManagerActivity.this);
   					LinearLayout ll=new LinearLayout(ScheduleManagerActivity.this);
   				    tv.setText(al.get(arg0).split("#")[1]);//设置内容 	
   				    tv.setTextColor(R.color.black); 				 
   					ll.setPadding(5,5,5,5);//设置四周留白					
   				    tv.setGravity(Gravity.LEFT);
   				    ll.addView(tv);
   				    return ll;
   				}        	
   	        };     
   	        lv.setAdapter(ba);//为ListView设置内容适配器         
   	        //设置选项选中的监听器  
   	        lv.setOnItemClickListener(
   	                new OnItemClickListener()
   	                {
   	     			@Override
   	     			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
   	     					long arg3) {//重写选项被单击事件的处理方法     				   	 		    	 			  
   	     	 			  String id1=al.get(arg2).split("#")[0];
   	     	 			  int id2=Integer.parseInt(id1);  	     	 			
   	     	 			  FileEditDialog fedd=new FileEditDialog(ScheduleManagerActivity.this,id2,date,4);	// 弹出对话框   			    	              
   	     	              fedd.show();     			     			
   	     			}        	   
   	              }
   	         );  		
 		}else
 		{
 			Toast.makeText(this, "请输入内容！！！", Toast.LENGTH_LONG).show();
 		}
    }
    //========================查询end=======================    
    //==========================设置闹钟方法begin==========================
    //设置闹钟方法
    public void setAlerm(Date date,int eventid)
    {
    	Toast.makeText(this, "设置闹铃"+Constant.DateToString(date), Toast.LENGTH_LONG).show();
    	Log.d("minDate", Constant.DateToString(date));
    	Log.d("eventid",eventid+"=============eventid============");
    	//获取闹钟管理的对象
    	alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    	//找对应的接受者
    	Intent intent=new Intent(ScheduleManagerActivity.this,AlermReceiver.class);
    	intent.putExtra("eventid", eventid);//将id值附加到intent中
    	Log.d("eventid",intent.getIntExtra("eventid", -1)+"=============getIntExtra============");
    	PendingIntent pendingIntent=PendingIntent.getBroadcast(ScheduleManagerActivity.this,0, intent,PendingIntent.FLAG_CANCEL_CURRENT);
    	//设置闹钟 
    	alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent); 
    }
    //取消闹钟方法
    public void cancelAlerm()
    {
    	//获取闹钟管理的对象
    	alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    	//找对应的接受者
    	Intent intent=new Intent(ScheduleManagerActivity.this,AlermReceiver.class);
    	PendingIntent pendingIntent=PendingIntent.getBroadcast(ScheduleManagerActivity.this,0, intent, 0);
    	//取消闹钟
    	alarmManager.cancel(pendingIntent);
    	//Toast.makeText(this, "闹钟已取消", Toast.LENGTH_SHORT).show();
    }
    //更新闹铃方法
    public void setAlarm()
    {
    	//取消之前的闹钟，重新查找设置
		cancelAlerm();
		Map<Date, Integer> map=DBUtil.findnearlyTime(ScheduleManagerActivity.this);
		for(Date d:map.keySet())
		{//遍历键值Date
			if(d!=null)
			{
				Log.d("minDate", d.toLocaleString());
				setAlerm(d,map.get(d));//设置闹钟测试
			}
			else
			{
				Log.d("minDate", "not found");
			}
		}			
    }
  
    int selectedIndex =0;//记录list的选中项，默认第一个
    BaseAdapter bellsBA;//显示铃声listview的适配器
	private addmusicDialog addmusicDialog;
    //设置闹钟铃声
    public void setBell()
    {
    	curr=Constant.SAVEBELL;//设置页面的分支
    	setContentView(R.layout.setbells);//转到设置铃声界面	
    	//取得当前铃声的id
    	bell=this.getSharedPreferences("bell",Context.MODE_PRIVATE);
    	selectedIndex=bell.getInt("bell", 0);//得到当前铃声id 若没有默认为第一个   	
    	mp=MediaPlayer.create(ScheduleManagerActivity.this,Constant.SONGID[selectedIndex]);
    	//创建列表的适配器
    	bellsBA=new BaseAdapter()
    	{
			@Override
			public int getCount() {
				return Constant.SONGID.length;
			}
			@Override
			public Object getItem(int arg0) {
				return null;
			}
			@Override
			public long getItemId(int position) {
				return position;
			}
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				LinearLayout ll=new LinearLayout(ScheduleManagerActivity.this);
				TextView tvname=new TextView(ScheduleManagerActivity.this);
				tvname.setText(Constant.BELLNAME[position]);//设置显示名称
				tvname.setTextSize(20);
				if(position%2!=0)//若不是双数设置不同的背景颜色
				{
					ll.setBackgroundDrawable(getResources().getDrawable(R.drawable.bell_bg));
				}		
				tvname.setTextColor(R.color.black);
				ll.addView(tvname);
				if(selectedIndex==position)//若是当前正在使用的铃声，则在后面显示一个对号
				{
					ImageView ivselect=new ImageView(ScheduleManagerActivity.this);
					ivselect.setImageDrawable(getResources().getDrawable(R.drawable.button_ok));//设置图片
					
					ll.addView(ivselect);
				}
				return ll;
			}		
    	};
    	//给listview添加适配器
    	ListView bellsList=(ListView)findViewById(R.id.bellslist01);
    	bellsList.setAdapter(bellsBA);
    	//设置listview的项被点击的事件监听
    	bellsList.setOnItemClickListener(
    	           new OnItemClickListener()
    	           {
    				@Override
    				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
    						long arg3) {//重写选项被单击事件的处理方法
    					//=================点击不同项可以试听begin===============
    					//如果以前有播放器则释放
    					 if(mp!=null){mp.release();}
    					 //创建新的播放器
    					 mp=MediaPlayer.create(ScheduleManagerActivity.this,Constant.SONGID[arg2]);
    					 mp.setOnCompletionListener(//歌曲播放结束事件的监听器
    							 new OnCompletionListener()
    								{
    									@Override
    									public void onCompletion(MediaPlayer arg0) 
    									{//歌曲播放结束停止播放并更新界面状态
    										arg0.stop();
    									}			
    								}				 
    					 );	
    				    try 
    					 {
    						mp.start();
    					 } catch (Exception e1) 
    					 {					
    						e1.printStackTrace();
    					 }	
    					//=================点击不同项可以试听end===============
    					 //改变选中的项
    					 selectedIndex=arg2;
    					 //通知更新列表数据
    					 bellsBA.notifyDataSetChanged();    				 
    				}        	   
    	           }
    	        );
    	//得到返回按钮的引用
		Button backButton=(Button)findViewById(R.id.backbutton);
		//给返回按钮添加监听
		backButton.setOnClickListener(
				new OnClickListener()
				{
					@Override
					public void onClick(View v) {
						//停止播放
						mp.stop();
						goBack(curr);//返回
					}
					
				}
		);
    }
    //==========================设置闹钟方法end==========================   
    //===========================月视图==========================
    public void gotoMonthView()
    {
    	curr=Constant.MAIN;//主界面
    	Constant.monthweekday=1;
    	//初始化布局文件为monthviewshow.xml
		setContentView(R.layout.monthviewshow);	
		recodes=0;
		//初始化抽屉控件
		showSlidingDrawer();
		//得到日月显示的TextView对象的值
		setTextView(curDate);//设置显示
		//得到日历控件的引用
		bnc=(com.bn.calendar.BNCalendar)findViewById(R.id.calendar01);
		//给日历控件和下面的文本显示赋初值
		bnc.setDate(curDate);//设置日历时间
		bnc.setLeftPanning(10);//设置日历绘画左边留白
		
		//给日历添加监听,改变下面显示的日期
		bnc.addCaActionListener
        (
        	new CaActionListener()
        	{
				@Override
				public void doAction(Date d) {
					setTextView(d);
					setExpandable();//更新列表的值
				}
        	}
        );
		//给向前按钮添加监听
		bnc.addPrevActionLinstener(
        	new PrevActionLinstener()
        	{
				@Override
				public void doAction(Date preDate) {
					//============打印===========
					Log.d("pre", "=========111===========");
					Log.d("date",preDate.getYear()+1900+"-"+(preDate.getMonth()+1)+"-"+preDate.getDate());
					setTextView(preDate);
					setExpandable();//更新列表的值
				}
        		
        	}
        );
        //给向后按钮添加监听
		bnc.addNextActionLinstener(
        		new NextActionLinstener()
        		{
					@Override
					public void doAction(Date nextDate) {
						setTextView(nextDate);
						setExpandable();//更新列表的值
					}	
        		}
        );  
        //给点击前面空白位置添加监听
		bnc.addFrontBlankActionListener(
        		new FrontBlankActionListener()
        		{
					@Override
					public void doAction(Date preDate) {
						setTextView(preDate);
						setExpandable();//更新列表的值
					}
        			
        		}
        );
        //给点击日历后面空白位置添加监听
		bnc.addBehindBlankActionListener(
        		new BehindBlankActionListener()
        		{
					@Override
					public void doAction(Date nextDate) {
						setTextView(nextDate);
						setExpandable();//更新列表的值
					}
        			
        		}
        );
		
		//给中间显示年月部分添加监听
		bnc.addControlCenterActionListener(
        		new ControlCenterActionListener()
        		{
					@Override
					public void doAction(Date date) 
					{
						curDate=date;	
						//显示选择日期对话框
						showDialog(Constant.DATEPICKER_DIALOG);	
					}
        			
        		}
        );
		//获得回今天的按钮引用
		Button backtoday=(Button)findViewById(R.id.gobacktoday);
		//添加监听
		backtoday.setOnClickListener(
				new OnClickListener()
				{
					@Override
					public void onClick(View v) {
						//设置当前日期为今天
						curDate=new Date(System.currentTimeMillis());
						bnc.setDate(curDate);//设置日历时间
						setTextView(curDate);//设置显示
						setExpandable();//更新列表的值
					}
					
				}
		);
		//设置列表的值
		setExpandable();
		
		//得到添加日程引用
		Button addButton=(Button)findViewById(R.id.addbutton01);
		//给添加日程添加监听
		addButton.setOnClickListener(
				new OnClickListener()
				{
					@Override
					public void onClick(View v) {
						
						newschedule(curDate);
					}
					
				}
		);

		
    }
    //用于设置expandablelistview的值方法
    public void setExpandable()
    {
    	//得到expandablelist控件的引用
		ExpandableListView exList=(ExpandableListView)findViewById(R.id.exlist01);
		//设置组前面的图标
		exList.setGroupIndicator(this.getResources().getDrawable(R.drawable.gourpexpandle));
		//分组名称以(id#类型名)的形式给出，以方便再查询
		List<String> groupName=DBUtil.getTypeList(this);
		//各分组的子项
		final List<List<String>> childName=new ArrayList<List<String>>();
		//从数据空中分别查询不同分类内的日程记录
		for(int i=0;i<groupName.size();i++)
		{
			List<String> ch=DBUtil.getScheduleTitile(this,groupName.get(i).split("#")[0],curDate);
			//添加到列表中
			childName.add(ch);
		}
		//给其添加适配器
		exList.setAdapter(new ExpandableAdapter(this,groupName,childName));
		//添加点击子项监听
		exList.setOnChildClickListener(
				new OnChildClickListener()
				{
					@Override
					public boolean onChildClick(ExpandableListView parent,
							View v, int groupPosition, int childPosition,
							long id) {
						//通过组号和自己的编号 得到相应日程的id号
						String[] eventstr=childName.get(groupPosition).get(childPosition).split("#");
						//调用对话框
						EditDialog edit=new EditDialog(ScheduleManagerActivity.this,eventstr[0]);
						edit.setTitle(eventstr[1]);
						edit.show();
						return true;
					}
				}
		);
    }
    
  //更新日历下方的时间显示为选择的日期并计算距离当前时间为多少
	public void setTextView(Date d)
	{
		curDate=d;
		//更新日期后判断回今天按钮是否显示
		Button backtoday=(Button)findViewById(R.id.gobacktoday);
		//看当前日期是否为今天
		Date today=new Date(System.currentTimeMillis());
		if(today.getYear()==curDate.getYear()&&today.getMonth()==curDate.getMonth()&&
				today.getDate()==curDate.getDate())
		{//若是今天则设置为不可见
			backtoday.setVisibility(View.INVISIBLE);
		}
		else
		{//若不为今天则设置为可见
			backtoday.setVisibility(View.VISIBLE);
		}
		
		//得到日月显示的TextView对象引用
		showDateTV=(TextView)findViewById(R.id.monthday01);	
		String dis=getDistance(curDate);//得到距离今天多少天的字符串
		String timeString=(curDate.getMonth()+1)+"月"+curDate.getDate()+"日 "+dis;
		showDateTV.setText(timeString);
	}
	//此方法用于计算说给日期距离当前时间的天数及是前多少天还是后多少天的字符串
	public String getDistance(Date d)
	{
		todayCal=Calendar.getInstance();
		todayCal.setTimeInMillis(System.currentTimeMillis());//设置为系统当前时间
		Calendar selectedCal=Calendar.getInstance();
		selectedCal.setTime(d);//被选中的日期
		String disStr;
		long today=todayCal.getTimeInMillis();
		long select=selectedCal.getTimeInMillis();
		long dislong=(today-select)/1000/60/60/24;//转换成天
		if(dislong<0||dislong==0)
		{//若小于0说明选中日期在后面
			//若是今天则标志位今天
			if(selectedCal.get(Calendar.YEAR)==todayCal.get(Calendar.YEAR)&&
					selectedCal.get(Calendar.MONTH)==todayCal.get(Calendar.MONTH)&&
					selectedCal.get(Calendar.DAY_OF_MONTH)==todayCal.get(Calendar.DAY_OF_MONTH))
			 {
				disStr="今天";
			 }
			else if(dislong>=0)
			{
				disStr="后1天";
			}
			else
			{
				dislong=Math.round(dislong-1);//?????
				disStr="后"+Math.abs(dislong)+"天";
			}
			
		}
		//在前面或当天
		else 
		{
				disStr="前"+dislong+"天";
		}	
		
		return disStr;
	}
	
	//==============================设置密码==========================
	public void gotoSetPwd()
	{
		curr=Constant.SET_BRANCH;//为设置页面的子页面
		//转到设置密码的页面
		setContentView(R.layout.setpassword);
		setOnBackListener();//返回按钮添加监听
		//得到设置开启关闭密码的按钮引用
		final ToggleButton tb=(ToggleButton)findViewById(R.id.toggle01);
		//得到密码输入框的引用
		final EditText etpwd=(EditText)findViewById(R.id.setpwd01);
		//得到确定密码的输入框引用
		final EditText etrepwd=(EditText)findViewById(R.id.setpwd02);
		//初始按钮及文本框状态为关闭和不可用
		tb.setChecked(false);
		etpwd.setEnabled(false);
		etrepwd.setEnabled(false);
		//给ToggleButton添加监听
		tb.setOnCheckedChangeListener(
				new OnCheckedChangeListener()
				{
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if(isChecked)
						{//若处于选中状态则说明用户要开启密码，设置文本框可用
							etpwd.setEnabled(true);
							etrepwd.setEnabled(true);
						}
						else
						{//若没有选中则说明用户不需输入密码，设文本框为不可用
							//将已输入内容清除
							etpwd.setText("");
							etrepwd.setText("");
							//设为不可选
							etpwd.setEnabled(false);
							etrepwd.setEnabled(false);
						}
					}
					
				}
		);
		//给确定按钮添加监听
		Button okButton=(Button)findViewById(R.id.okbutton);
		okButton.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if(tb.isChecked())
						{//若按钮是选中状态说明用户输入密码了，进行判断
							//获得用户输入的数据
							String inpwd=etpwd.getText().toString().trim();
							String reinpwd=etrepwd.getText().toString().trim();
							if(inpwd.equals("")||reinpwd.equals(""))
							{//若内容为全部输入则提醒
								Toast.makeText(ScheduleManagerActivity.this,
										R.string.remind, Toast.LENGTH_SHORT).show();
							}
							else if(!inpwd.equals(reinpwd))
							{//两次密码不一致
								Toast.makeText(ScheduleManagerActivity.this,
										"两次密码不一致", Toast.LENGTH_SHORT).show();
								//清除输入的内容
								etpwd.setText("");
								etrepwd.setText("");
								etpwd.requestFocus();
							}
							else
							{
								//若检验合格将密码存入sharedpreference
								SharedPreferences.Editor editor=spwd.edit();//得到一个edit
						        editor.putString("pwd",inpwd);//将输入的密码存入
						        editor.commit();//提交
						        hasPwd=true;//改变标志位为 有密码
								Toast.makeText(ScheduleManagerActivity.this,
										R.string.successset, Toast.LENGTH_SHORT).show();
								//返回设置页面
								toSetView();
							}
						}
						else
						{
							Toast.makeText(ScheduleManagerActivity.this,
									R.string.successset, Toast.LENGTH_SHORT).show();
							//返回设置页面
							toSetView();
						}
					}
				}
		);
		
	}
	//==============================重新设置密码或取消密码==========================
	public void gotoCancelorResetPwd()
	{
		curr=Constant.SET_BRANCH;//为设置页面的子页面
		//转到设置密码的页面
		setContentView(R.layout.cancleorresetpwd);
		setOnBackListener();
		//==========================标题改动===============
		TextView tvtitle=(TextView)findViewById(R.id.tvtitle);
		tvtitle.setText(R.string.resetpwd);//初始化标题为修改密码
		
		//得到设置开启关闭密码的按钮引用
		final ToggleButton tb=(ToggleButton)findViewById(R.id.toggle01);
		//得到原密码输入框的引用
		final EditText prepwd=(EditText)findViewById(R.id.previouspwd01);
		//得到密码输入框的引用
		final EditText etpwd=(EditText)findViewById(R.id.setpwd01);
		//得到确定密码的输入框引用
		final EditText etrepwd=(EditText)findViewById(R.id.setpwd02);
		//两个文本引用
		final TextView tv01=(TextView)findViewById(R.id.pwdtext01);
		final TextView tv02=(TextView)findViewById(R.id.pwdtext02);
		//初始按钮及文本框状态为开启可用可用于修改密码，输入原密码及新的密码
		tb.setChecked(true);
		prepwd.setEnabled(true);
		etpwd.setEnabled(true);
		etrepwd.setEnabled(true);
		//给ToggleButton添加监听
		tb.setOnCheckedChangeListener(
				new OnCheckedChangeListener()
				{
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if(isChecked)
						{//若处于选中状态则说明用户要重写设置密码，设置文本框可用
							etpwd.setVisibility(View.VISIBLE);
							etrepwd.setVisibility(View.VISIBLE);
							tv01.setVisibility(View.VISIBLE);
							tv02.setVisibility(View.VISIBLE);
							TextView tvtitle=(TextView)findViewById(R.id.tvtitle);
							tvtitle.setText(R.string.resetpwd);//修改标题为修改密码
						}
						else
						{//若没有选中则说明用户不需输入密码，向取消密码，则让其输入原密码即可
							//将已输入内容清除
							etpwd.setText("");
							etrepwd.setText("");
							//设为不可见
							etpwd.setVisibility(View.INVISIBLE);
							etrepwd.setVisibility(View.INVISIBLE);
							tv01.setVisibility(View.INVISIBLE);
							tv02.setVisibility(View.INVISIBLE);
							TextView tvtitle=(TextView)findViewById(R.id.tvtitle);
							tvtitle.setText(R.string.canclepwd);//修改标题为取消密码

						}
					}
					
				}
		);
		//给确定按钮添加监听
		Button okButton=(Button)findViewById(R.id.okbutton);
		okButton.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if(tb.isChecked())
						{//若按钮是选中状态说明用户输入密码了，进行判断
							//获得用户输入的数据
							String preinpwd=prepwd.getText().toString().trim();//输入的原密码
							String inpwd=etpwd.getText().toString().trim();//新密码
							String reinpwd=etrepwd.getText().toString().trim();//确认密码
							//更新密码存储的值
							pwd=spwd.getString("pwd", null);
							//先判断输入的原密码是否正确
							if(!preinpwd.equals(pwd))
							{//若不正确则提醒并清除原密码中输入内容
								prepwd.setText("");
								prepwd.requestFocus();//获取焦点
								Toast.makeText(ScheduleManagerActivity.this,
										"原密码输入不正确", Toast.LENGTH_SHORT).show();
							}
							else if(inpwd.equals("")||reinpwd.equals(""))
							{//若内容为全部输入则提醒
								Toast.makeText(ScheduleManagerActivity.this,
										R.string.remind, Toast.LENGTH_SHORT).show();
							}
							else if(!inpwd.equals(reinpwd))
							{//两次密码不一致
								//清除输入，并提示
								etpwd.setText("");
								etrepwd.setText("");
								etpwd.requestFocus();
								Toast.makeText(ScheduleManagerActivity.this,
										"两次密码不一致", Toast.LENGTH_SHORT).show();
							}
							else
							{
								//将新密码存入sharedpreffernce
								SharedPreferences.Editor editor=spwd.edit();//得到一个edit
						        editor.putString("pwd",inpwd);//将输入的密码存入
						        editor.commit();//提交
								Toast.makeText(ScheduleManagerActivity.this,
										R.string.successset, Toast.LENGTH_SHORT).show();
								//返回设置页面
								toSetView();
							}
						}
						else
						{//若为不选中，则说明用户想取消密码，验证原密码是否正确
							String pripwd=prepwd.getText().toString().trim();
							//更新密码存储的值
							pwd=spwd.getString("pwd", null);
							if(pripwd.equals(pwd))
							{//若正确则清除密码存储
								SharedPreferences.Editor editor=spwd.edit();//得到一个edit
						        editor.remove("pwd");//将输入的密码移除
						        editor.commit();//提交
						        hasPwd=false;//改变标志位为没有密码
						        Toast.makeText(ScheduleManagerActivity.this,
										R.string.successset, Toast.LENGTH_SHORT).show();
						      //返回设置页面
								toSetView();
							}
							else
							{//若输入不正确，提示
								prepwd.setText("");//清除输入
								Toast.makeText(ScheduleManagerActivity.this,
										"原密码输入不正确", Toast.LENGTH_SHORT).show();
							}
							
							
						}
					}
				}
		);

	}
	//================================跳转到设置页面=======================
	public void toSetView()
	{
		curr=Constant.MAIN_BRANCH;//为主页面的子页面
		setContentView(R.layout.set);
		setOnBackListener();
		//再次后去密码看有没有
		pwd=spwd.getString("pwd", null);
		if(pwd!=null)
    	{
    		hasPwd=true;//改变标志位设有密码
    	}
		else
		{
			hasPwd=false;
		}
		//获得设置密码按钮的引用，并添加监听
		Button setpwdButton=(Button)findViewById(R.id.setpwdbutton);
		setpwdButton.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if(hasPwd)
						{//若设置有密码则跳转到修改和取消密码界面
							gotoCancelorResetPwd();
						}
						else
						{//若没有密码则跳转到设置密码页面
							gotoSetPwd();
						}
					}
				}
		);
		//设置铃声按钮引用，添加监听
		Button setbellButton=(Button)findViewById(R.id.setbellsbutton);
		setbellButton.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						//转到设置铃声界面
						setBell();
					}
				}
		);
	}
	//=========================关于页面======================
	public void goAbout()
	{
		curr=Constant.MAIN_BRANCH;
		setContentView(R.layout.about);
		setOnBackListener();//返回按钮添加监听
	}
    //===========================判断返回方法==================
	public void goBack(int flag)
	{
		switch(flag)
		{
		case Constant.MAIN:			 
			 if(recodes==1)
			 {
	     		   Toast.makeText(this, "再按一次退出程序！", Toast.LENGTH_SHORT).show();

			 }else
			 {				 				
				 saveMain();//保存退出界面
			     System.exit(0);//若是主页面则退出
			     recodes=0;
			 }break;
		case Constant.SET_BRANCH:toSetView();break;//若是设置的子页则返回设置页面
		case Constant.MAIN_BRANCH:
			if(Constant.monthweekday==1) //判断显示哪个主界面界面
	       {
	    	   gotoMonthView();
	       }
	       else if(Constant.monthweekday==2)
	       {
	        	 createWeek(); 
	       }else
	       {
	    	     createDay();
	       }            
		   break;//若是设置的子页则返回主页面
		case Constant.SAVEBELL:
			//若是保存铃声的界面则将选择的铃声id存储
			SharedPreferences.Editor editor=bell.edit();//得到一个edit
	        editor.putInt("bell", selectedIndex);//将铃声id存入
	        editor.commit();//提交
	        //返回设置页面
	        toSetView();
			break;
		}
	}
	//===========================给返回按钮添加监听=================
	public void setOnBackListener()
	{
		//得到返回按钮的引用
		Button backButton=(Button)findViewById(R.id.backbutton);
		//给返回按钮添加监听
		backButton.setOnClickListener(
				new OnClickListener()
				{
					@Override
					public void onClick(View v) {
						goBack(curr);
					}
					
				}
		);
	}
	//===========================显示抽屉方法===================
	public void showSlidingDrawer()
	{
		//得到抽屉控件，抽屉的拉手及抽屉内容的GridView的引用
		sliding=(SlidingDrawer)findViewById(R.id.sliding01);
		gv=(GridView)findViewById(R.id.contentShow);
		hi=(ImageView)findViewById(R.id.handleImage);
		//创建一个GridAdaper适配器给gv添加
		GridAdapter ga=new GridAdapter(this,icons,textSliding);
		//给gridview添加适配器
		gv.setAdapter(ga);
		//给抽屉添加打开的监听
		sliding.setOnDrawerOpenListener(
			new SlidingDrawer.OnDrawerOpenListener() {	
				@Override
				public void onDrawerOpened() {
					hi.setImageResource(R.drawable.close);	
					//设为主页面分支，按返回键关闭抽屉
					curr=Constant.MAIN_BRANCH;
				}
			}
		);
		//给关闭抽屉添加监听
		sliding.setOnDrawerCloseListener(
				new SlidingDrawer.OnDrawerCloseListener() {	
					@Override
					public void onDrawerClosed() {
						hi.setImageResource(R.drawable.open);	
					}
				}
		);	
	}
	//===============================显示对话框==========================
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog=null;
		switch(id)
		{
		case Constant.ADD_MUSIC:
			addmusicDialog=new addmusicDialog(ScheduleManagerActivity.this);//创建选择时间对话框
			dialog=addmusicDialog;
			break;
		case Constant.DATEPICKER_DIALOG://若选择的是选择时间对话框
			dateDialog=new ChangeDateDialog(ScheduleManagerActivity.this);//创建选择时间对话框
			dialog=dateDialog;	
			break;
		//===============================================
		case DIALOG_ADD_TYPE:
			AlertDialog.Builder b=new AlertDialog.Builder(this);
			b.setTitle("添加类型");
			b.setIcon(android.R.drawable.ic_dialog_info);
			final EditText text=new EditText(this);
			b.setView(text);
		    	 b.setPositiveButton("确定", new DialogInterface.OnClickListener()
        		{  //获取view里面的值
					@Override
					public void onClick(DialogInterface dialog, int which) {
				          temp=text.getText().toString();
						//获取上面view的值并且赋给temp添加到类型里面去
						db.insertType(ScheduleManagerActivity.this, temp);
						//Toast.makeText(getApplicationContext(),temp ,Toast.LENGTH_LONG).show();
					}      			
        		});
		    	
	    	b.setNegativeButton("取消", null);
	    	
			dialogSetRange=b.create();
			  	dialog=dialogSetRange;	
		break;
		
		//设置提醒时间         
		case TIME_DIALOG:
			dateDialog=new ChangeTimeDialog(ScheduleManagerActivity.this);//创建选择时间对话框
			dialog=dateDialog;	
			break;
		case TIME_ALERT:
			  android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
			  builder.setIcon(R.drawable.dd);
			  builder.setTitle("选择重复提醒方式");//默认是正点提醒
		      builder.setMultiChoiceItems(
		    		  R.array.cftx, 
		    		  mulFlags,
		    		  new DialogInterface.OnMultiChoiceClickListener() {
		            
		            @Override
		            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
		                mulFlags[which]=isChecked;
		            } 
		               
		        });
		      
		      builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
		            
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
			               //对mulFlags进行扫描，确定time  1-7
			            	SimpleDateFormat sdf1=new SimpleDateFormat("yy-MM-dd HH:mm");
			            	if(mulFlags[0]){
		            			alertone=nowtime;
								}else{
								alertone=null;
									}
			            		if(mulFlags[1]){
			            			try {
										Date date1=sdf1.parse(nowtime);//将String转换为Date
										int  min=date1.getMinutes()-5;//使得分钟数减五
										date1.setMinutes(min);
										alerttwo=sdf1.format(date1);//将Date转换为String
									} catch (ParseException e) {
										e.printStackTrace();
									}
									
								}
	                              if(mulFlags[2]){
	                            	  try {
											Date tempdate=sdf1.parse(nowtime);//将String转换为Date
											int  min=tempdate.getMinutes()-10;//使得分钟数减10
											tempdate.setMinutes(min);
											alertthree=sdf1.format(tempdate);//将Date转换为String
										} catch (ParseException e) {
											e.printStackTrace();
										}
										
	                           }
	                             if(mulFlags[3]){
	                            	 try {
											Date tempdate=sdf1.parse(nowtime);//将String转换为Date
											int  min=tempdate.getMinutes()-30;//使得分钟数减30
											tempdate.setMinutes(min);//将
											alertfour=sdf1.format(tempdate);//将Date转换为String
										} catch (ParseException e) {
											e.printStackTrace();
									  }
										//Toast.makeText(getApplicationContext(), alertthree, Toast.LENGTH_LONG).show();
	                             }
	                             if(mulFlags[4]){
	                            	 try {
											Date tempdate=sdf1.parse(nowtime);//将String转换为Date
											int hour=tempdate.getHours()-1;//将时间减小一个小时
											tempdate.setHours(hour);
											alertfive=sdf1.format(tempdate);//将Date转换为String
										} catch (ParseException e) {
											e.printStackTrace();
										 }
										
	                            }
	                             if(mulFlags[5]){
	                            	 try {Date tempdate=sdf1.parse(nowtime);//将String转换为Date
											int day=tempdate.getDate()-1;//将时间减小一天
											tempdate.setDate(day);
											alertsix=sdf1.format(tempdate);//将Date转换为String
										} catch (ParseException e) {
											e.printStackTrace();
										}
	                            	
	                             }
	                             if(mulFlags[6]){
	                            	 try {Date tempdate=sdf1.parse(nowtime);//将String转换为Date
											int week=tempdate.getDate()-7;//将时间减小一周
											tempdate.setDate(week);
											alertseven=sdf1.format(tempdate);//将Date转换为String
										} catch (ParseException e) {
											e.printStackTrace();
										}
	                            	
	                             }

		            }
		        });
		        dialog = builder.create();
		        break;
		case FUJIAN_LIST:
			android.app.AlertDialog.Builder builderlist = new AlertDialog.Builder(this);
			builderlist.setIcon(R.drawable.fujian);
			builderlist.setTitle("");
			builderlist.setItems(R.array.fujianlist, new DialogInterface.OnClickListener() {
	            
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                
	            	String tempstr=getResources().getStringArray(R.array.fujianlist)[which];
	            		             
	            	switch(which){
	            	case 0://跳转添加联系人   返回String  person
	            		showDialog(Constant.ADD_CONTACT);
	            		break;
	            	case 1://跳转添加短信
	            		showDialog(ADD_MESSAGE);
	            		break;
	            	case 2://跳转添加图片    返回图片的路径
	            		showDialog(PICTURE_SELECT);
	            		break;
	            	case 3://跳转添加声音     返回音频存放的位置
	            		showDialog(Constant.ADD_MUSIC);
	            		break;
	            	case 4://添加地点
	            		showDialog(ADD_LOCATION);
	            		break;
	            	}
	            	
	            }
	        });
	        dialog = builderlist.create();

			break;
			
		case PICTURE_SELECT:
			PhotoDialog=new PhotoDialog(ScheduleManagerActivity.this);//创建选择对话框
			dialog=PhotoDialog;
			/*android.app.AlertDialog.Builder builderpicture = new AlertDialog.Builder(this);
			builderpicture.setTitle("选择图片");
			//通过渲染xml文件，得到一个视图（View），再拿到这个View里面的Gallery  
			LayoutInflater li = LayoutInflater.from(ScheduleManagerActivity.this);  
			View imageChooseView = li.inflate(R.layout.gallery, null); 
			Gallery gallery = (Gallery)imageChooseView.findViewById(R.id.Gallery);  
			BaseAdapter ba=new BaseAdapter()
	        {
				@Override
				public int getCount() {
					return imageIDs.length;
				}

				@Override
				public Object getItem(int arg0) {			    
					return null;
				}

				@Override
				public long getItemId(int arg0) {
					return 0;
				}

				@Override
				public View getView(int  position, View convertView, ViewGroup parent) {
					ImageView iv = new ImageView(ScheduleManagerActivity.this);
					iv.setImageResource(imageIDs[ position]);
				postion=imageIDs[ position];
					iv.setScaleType(ImageView.ScaleType.FIT_XY);
					iv.setLayoutParams(new Gallery.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
					return iv;
				}        	
	        };
	        gallery.setAdapter(ba);
			builderpicture.setView(imageChooseView);
			builderpicture.setPositiveButton("确定",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), "您选择了"+postion, Toast.LENGTH_LONG).show();
				}
			});
			builderpicture.setNegativeButton("取消",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					
				}
			});
			dialog = builderpicture.create();*/
			break;
        case ADD_MESSAGE://添加短信  			
			android.app.AlertDialog.Builder buildermessage = new AlertDialog.Builder(this);
			buildermessage.setTitle("添加短信");
			LayoutInflater messageli = LayoutInflater.from(ScheduleManagerActivity.this);  
			ListView messagelistview=new ListView(this);
			messagelistview.setCacheColorHint(Color.BLACK);
			Uri SMS_CONTENT = Uri.parse("content://sms/"); 
	        Cursor cursor = ScheduleManagerActivity.this.getContentResolver().query(SMS_CONTENT,null,null,null,null);
	        while(cursor.moveToNext()){
	        	//cursor.getString(cursor.getColumnIndex("address")).toString()+":"+地址
	           result=cursor.getString(cursor.getColumnIndex("body")).toString();
	            al.add(result);
	            }			
		    ArrayAdapter<String> aa;//将Arraylist的数据加载到listview里面就能显示短信了
			aa = new ArrayAdapter<String>(ScheduleManagerActivity.this, android.R.layout.simple_expandable_list_item_1,al);
	        messagelistview.setAdapter(aa);
	        messagelistview.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> arg0, View messagelistview,
						int arg2, long arg3) {
		          //获取我点击的短信内容
					TextView tvn=(TextView)messagelistview;
					String testtext=tvn.getText().toString();
					EditText  temptext=(EditText)findViewById(R.id.etxjrcNote);
					
					temptext.setText(testtext);	
				}});
			buildermessage.setView(messagelistview);
			
			buildermessage.setPositiveButton("确定",null);
			buildermessage.setNegativeButton("取消", null);
			
			dialog=buildermessage.create();
			cursor.close();
			break;
			//添加地点
		case ADD_LOCATION:
			AlertDialog.Builder builderlocation=new AlertDialog.Builder(this);
			builderlocation.setTitle("添加地点");
			builderlocation.setIcon(android.R.drawable.ic_dialog_info);	
			final EditText textlocation=new EditText(this);
			builderlocation.setView(textlocation);
			builderlocation.setPositiveButton("确定", new DialogInterface.OnClickListener()
        		{  //获取view里面的值
					@Override
					public void onClick(DialogInterface dialog, int which) {
		           temp=textlocation.getText().toString();
				   TextView locationtextview=(TextView)findViewById(R.id.locationtextview01);
				   locationtextview.setText(temp);
				   ImageView  ivw=(ImageView)findViewById(R.id.ImageViewmap01);
				   ivw.setBackgroundDrawable(getResources().getDrawable(R.drawable.ballon));
				   ivw.setOnClickListener(
					new OnClickListener()
					{

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

						}
						
					}			   
				   );    				
					}      			
        		});		    	
			builderlocation.setNegativeButton("取消", null);
	    	TextView locationtextview=(TextView)findViewById(R.id.locationtextview01);
			locationtextview.setText(temp);
			dialogSetRange=builderlocation.create();
			dialog=dialogSetRange;	
			break;
		case Constant.MORE_EDIT:
			AlertDialog.Builder builderdeletetype=new AlertDialog.Builder(this);
			builderdeletetype.setTitle("点击删除类型");
			builderdeletetype.setIcon(android.R.drawable.ic_dialog_info);
			ListView deletelistview=new ListView(this);
			ArrayList<String> deletetype=new ArrayList<String>();
			deletetype=(ArrayList<String>) db.getTypeList(getApplicationContext());
			ArrayAdapter<String> deletetypeaa;
			deletetypeaa = new ArrayAdapter<String>(ScheduleManagerActivity.this, android.R.layout.simple_expandable_list_item_1,deletetype);
			
			deletelistview.setAdapter(deletetypeaa);
			deletelistview.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> arg0, View deletelistview,
						int arg2, long arg3) {
					TextView tvn=(TextView)deletelistview;
					String deletetypestr=tvn.getText().toString();
					String[] tempdelete=deletetypestr.split("#");
					String deletetype=tempdelete[1].toString();
					db.deleteType(ScheduleManagerActivity.this, deletetype);
					
				}});
			
			builderdeletetype.setView(deletelistview);
			dialog=builderdeletetype.create();
			
			break;
		case Constant.ADD_CONTACT:
			android.app.AlertDialog.Builder buildercontact = new AlertDialog.Builder(this);
			
			buildercontact.setTitle("添加联系人");
			buildercontact.setIcon(android.R.drawable.ic_dialog_info);
			ListView  contactlist=new ListView(this);
			////
			Cursor cursorcontact = getContentResolver().query(People.CONTENT_URI, null, null, null, null);
	        startManagingCursor(cursorcontact);
	         
	        ListAdapter listAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_expandable_list_item_1, 
	        		cursorcontact,
	                new String[]{People.NAME}, 
	                new int[]{android.R.id.text1});
	         
	        contactlist.setAdapter(listAdapter);
	        contactlist.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> arg0, View contactlist,
						int arg2, long arg3) {
					TextView tvn=(TextView)contactlist;
					StringBuilder sb=new StringBuilder();
					
					String testperson=tvn.getText().toString();
					sb.append(testperson);
					TextView contactview=(TextView)findViewById(R.id.contacttextview01);
					contactview.setText(testperson);
				}});	     
			buildercontact.setView(contactlist);
			buildercontact.setPositiveButton("确定", null);
			buildercontact.setNegativeButton("取消", null);
			
			dialog=buildercontact.create();
			break;
		}
		return dialog;
	}
	@Override
	protected void onPrepareDialog(int id, final Dialog dialog) {
		switch(id)
		{
		
		case Constant.ADD_MUSIC:
			final TextView showtime =(TextView)dialog.findViewById(R.id.showtime);
			Button start =(Button)dialog.findViewById(R.id.startbt);
			Button stop  =(Button)dialog.findViewById(R.id.endbt);
			//隐藏播放和删除的按钮
			LinearLayout ll=(LinearLayout)dialog.findViewById(R.id.showmusiclnear);
			ll.setVisibility(View.INVISIBLE);
			Button soundsave=(Button)dialog.findViewById(R.id.soundsave);
			soundsave.setEnabled(false);
			hd=new Handler() 
	        {
	        	@Override
	        	public void handleMessage(Message msg)
	        	{
	        		//调用父类处理
	        		super.handleMessage(msg);
	        		//根据消息what编号的不同，执行不同的业务逻辑
	        		switch(msg.what)
	        		{
	        		   //将消息中的内容提取出来显示在Toast中
	        		   case UPDATE_TIME:
	        			   //获取消息中的数据
	        			   Bundle b=msg.getData();
	        			   //获取内容字符串 
	        			   String msgStr=b.getString("msg");
	        			   //设置字符串到显示录音时长的文本框中
	        			   showtime.setText(msgStr);	        		   
	        		   break;
	        		}
	        	}
	        };   
	        
	        start.setOnClickListener(new OnClickListener() {
				//
				@Override
				public void onClick(View v) {
					//检测是否有内存卡
					if(!Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
					{//若没有插闪存卡则报错
						Toast.makeText(getApplicationContext(), "请检测内存卡", Toast.LENGTH_SHORT).show();
						return;
					}
					try
					{
						//判断是否在录制中
						if(recordFlag)
						{//若正在录音则提示，并不做任何操作
							Toast.makeText(getApplicationContext(), "请结束本次录音后再开始新的录音", Toast.LENGTH_LONG).show();
							return;
						}
						//若没有录音则修改标志位为录制中
						recordFlag=true;
						//设置开始录音
						Toast.makeText(getApplicationContext(), "开始录音", Toast.LENGTH_LONG).show();
						//创建临时文件
						myFile = File.createTempFile
						(
								"myAudio",  //基本文件名
								".amr",     //后缀
								Environment.getExternalStorageDirectory() //目录路径,从外面来
						);
						//创建录音机对象
						myMediaRecorder = new MediaRecorder();
						//设置输入设备为麦克风
						myMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
						//设置输出格式为默认的amr格式
						myMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
						//设置音频编码器为默认的编码器
						myMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
						//设置输出文件的路径
						myMediaRecorder.setOutputFile(myFile.getAbsolutePath());
						//给路径字符串赋值
						musicpath=myFile.getAbsolutePath();
						Log.d("addmusic", musicpath);
						 
						//准备录音
						myMediaRecorder.prepare();
						//开始录音
						myMediaRecorder.start(); 
						//设置录音中标记为true
						recordFlag=true;
						//计时器清0
						countSecond=0;
						//启动一个线程进行计时
						new Thread()
						{
							public void run()
							{
								while(recordFlag)
								{
									//计时器加一
									countSecond++;
									//调用方法设置新时长
									setTime();
									//休息1000ms
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							}

							/*private void setTime() {
								// TODO Auto-generated method stub
								//计算分钟和秒
						    	int second=countSecond%60;
						    	int minute=countSecond/60;      	
						    	//创建内容字符串
						    	String msgStr=(minute>9?minute+"m:":("0"+minute+"m:"))+(second>9?second+"s":"0"+second+"s");
								//创建消息数据Bundle
								Bundle b=new Bundle();
								//将内容字符串放进数据Bundle中
								b.putString("msg", msgStr);
								//创建消息对象
								Message msg=new Message();
								//设置数据Bundle到消息中
								msg.setData(b);
								//设置消息的what值
								msg.what=UPDATE_TIME;
								//发送消息
								hd.sendMessage(msg);
								
							}*/
						}.start();	
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					//设置开始录音
					Toast.makeText(getApplicationContext(), "开始录音", Toast.LENGTH_LONG).show();
				}
			});
	        //给停止按钮添加监听
	        
	        stop.setOnClickListener(
					new OnClickListener()
					{
						@Override
						public void onClick(View v) {
							if(myFile != null&&myMediaRecorder!=null)
							{
								//停止录音
								myMediaRecorder.stop();
								//释放录音机对象
								myMediaRecorder.release();
								//将录音机对象引用设置为null
								myMediaRecorder = null;	
								//同时养显示文件名及操作的按钮可见
								LinearLayout ll=(LinearLayout)dialog.findViewById(R.id.showmusiclnear);
								ll.setVisibility(View.VISIBLE);
								//确定按钮可用
								Button soundsave=(Button)dialog.findViewById(R.id.soundsave);
								soundsave.setEnabled(true);
								//得到文本控件，添加音频名称
								TextView tvname=(TextView)dialog.findViewById(R.id.musicnametv);
								tvname.setText(musicpath.split("/")[3]);
							}
							//设置录音中标记为false
							recordFlag=false;
						}
						
					});
	        
	      //试听按钮添加监听
			Button play=(Button)dialog.findViewById(R.id.playbutton);
			play.setOnClickListener(
					new OnClickListener()
					{
						@Override
						public void onClick(View v) {
							if(isplay&&mp!=null)
							{//若正在播放则停止
								mp.stop();
								isplay=false;
								//换图片为播放
								Button playbutton=(Button)v;
								playbutton.setBackgroundResource(R.drawable.play);
								return;
							}
							//若没有播放中 则开始播放
							isplay=true;//改变标志位为播放中
							//换图片为暂停
							Button playbutton=(Button)v;
							playbutton.setBackgroundResource(R.drawable.stop);
							if(musicpath != null)//若文件不为空则播放
							{
								//如果以前有播放器则释放
		    					 if(mp!=null){mp.release();}
		    					 //创建新的播放器
		    					 mp=new MediaPlayer();
		    					 mp.setOnCompletionListener(//歌曲播放结束事件的监听器
		    							 new OnCompletionListener()
		    								{
		    									@Override
		    									public void onCompletion(MediaPlayer arg0) 
		    									{//歌曲播放结束停止播放并更新界面状态
		    										arg0.stop();
		    										//设置图标为播放
		    										Button play=(Button)dialog.findViewById(R.id.playbutton);
		    										play.setBackgroundResource(R.drawable.play);
		    									}			
		    								}				 
		    					 );	
		    				    try 
		    					 {
		    				    	//设置播放歌曲的路径
		    						mp.setDataSource(musicpath);
		    						//进行播放前的准备工作，new方式创建的MediaPlayer一定需要prepare
		    						//否则报错
		    						mp.prepare();
		    						mp.start();
		    					 } catch (Exception e1) 
		    					 {					
		    						e1.printStackTrace();
		    					 }	
							}
						}	
					});
			//给删除按钮添加监听
			Button delete=(Button)dialog.findViewById(R.id.deletebutton);
			delete.setOnClickListener(
					new OnClickListener()
					{
						@Override
						public void onClick(View v) {
							//制定文件路径并删除
							File pathMusicPath = new File(musicpath);
							pathMusicPath.delete();
							//同时养显示文件名及操作的按钮不可见
							LinearLayout ll=(LinearLayout)dialog.findViewById(R.id.showmusiclnear);
							ll.setVisibility(View.INVISIBLE);
							//清除计算
							countSecond=0;
							setTime();
							Toast.makeText(getApplicationContext(), "录音已删除", Toast.LENGTH_LONG).show();

						}	
					});
			//给确定按钮添加监听
			 soundsave=(Button)dialog.findViewById(R.id.soundsave);
			soundsave.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (musicpath.toString().length()!=0){
						soundpath=musicpath.toString();
						Toast.makeText(getApplicationContext(), "成功添加声音", Toast.LENGTH_LONG).show();
					}else {
						soundpath=null;
					}
					Button playsoundbt=(Button)findViewById(R.id.playsound);
					playsoundbt.setVisibility(View.VISIBLE);
					playsoundbt.setOnClickListener(new OnClickListener() {
		    			
		    			@Override
		    			public void onClick(View v) {
		    				
		    					try 
		    				 {
		    			    	//设置播放歌曲的路径
		    				    mp=new	MediaPlayer();
		    					mp.setDataSource(soundpath);
		    					//进行播放前的准备工作，new方式创建的MediaPlayer一定需要prepare
		    					//否则报错
		    					mp.prepare();
		    					mp.start();
		    				 } catch (Exception e1) 
		    				 {					
		    					 Toast.makeText(getApplicationContext(), "声音初始化错误"+e1.toString(), Toast.LENGTH_LONG).show();
		    				 }
		    				
		    				
		    				
		    			}
		    		});
					dialog.cancel();
					
				}
			});
			//给取消按钮添加监听
			Button soundcancle=(Button)dialog.findViewById(R.id.soundcancle);
			soundcancle.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					dialog.cancel();
				}
			});
	        
			
			break;
		
		case  PICTURE_SELECT:
			imageView=(ImageView)dialog.findViewById(R.id.imageID);
			xiangce = (Button) dialog.findViewById(R.id.btn_01); //查看相册
	        paizhao = (Button) dialog.findViewById(R.id.btn_02); //调取系统相机拍照
	        photosave=(Button)dialog.findViewById(R.id.photosave);
	        photocancle=(Button)dialog.findViewById(R.id.photocancle);	
	        xiangce.setOnClickListener(new OnClickListener() {
	        
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_PICK, null); 
	                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED); 
	                startActivityForResult(intent, PHOTOZOOM);
	                Toast.makeText(getApplicationContext(), "xiangce", Toast.LENGTH_LONG).show();
					
				}
			});
	       paizhao.setOnClickListener(new OnClickListener()
	        {
	            @Override 
	            public void onClick(View v)
	            {
	                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
	                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg"))); 
	                startActivityForResult(intent, PHOTOHRAPH);
	            }
	        }); 
	       photosave.setOnClickListener(new View.OnClickListener() {
			
			  @Override
			  public void onClick(View v) {
				ImageView showphoto=(ImageView)findViewById(R.id.showphoto);
				showphoto.setImageBitmap(photo);
			    dialog.cancel();
			  }
		   });
	       photocancle.setOnClickListener(new View.OnClickListener() {
				
				  @Override
				  public void onClick(View v) {
					  dialog.cancel();
					
				  }
			   });
			break;
		case Constant.DATEPICKER_DIALOG://若是显示时间控件
			if(Constant.monthweekday==1)
			{
				if(isSelect)
				{
					Date d=selectbnc.getDate();
					calendar.set(d.getYear()+1900,d.getMonth(),d.getDate());
				}
				else
				{
					Date d=bnc.getDate();
					calendar.set(d.getYear()+1900,d.getMonth(),d.getDate());
				}
			}			
			updateChange(dialog);	    
			ImageButton yearadd=(ImageButton)dialog.findViewById(R.id.yearadd);//加年份
			    yearadd.setOnClickListener(
						new View.OnClickListener() {
							@Override
							public void onClick(View v) {
							int year=(calendar.get(Calendar.YEAR)+1);
						    calendar.set(year, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)); //加年份
							updateChange(dialog);//更新显示值
							}
						}
				);
				//减年份
				ImageButton yearminus=(ImageButton)dialog.findViewById(R.id.yearminus);//加年份
				yearminus.setOnClickListener(
						new View.OnClickListener() {
							@Override
							public void onClick(View v) {
							int year=(calendar.get(Calendar.YEAR)-1);
							calendar.set(year, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));	//减年份
								updateChange(dialog);//更新显示值
							}
						}
				);
				//月份加
				ImageButton monthadd=(ImageButton)dialog.findViewById(R.id.monthadd);//加年份
				monthadd.setOnClickListener(
						new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								 if( calendar.get(Calendar.MONTH)<11)
								   {
									   
									   if(calendar.get(Calendar.DAY_OF_MONTH)==31)//如果为31天的话 加月份为下个月的最后一天、
									   {
										   int month=calendar.get(Calendar.MONTH)+1;
									        calendar.set(calendar.get(Calendar.YEAR),month,getMonthDays(calendar.get(Calendar.YEAR),(calendar.get(Calendar.MONTH)+1)));//加月份		   
									   }else
									   {
										   int month=calendar.get(Calendar.MONTH)+1;
									        calendar.set(calendar.get(Calendar.YEAR),month, calendar.get(Calendar.DAY_OF_MONTH));//加月份
									   }					   
								   }else
								   {
									   int year=calendar.get(Calendar.YEAR)+1;
								        calendar.set(year,0, calendar.get(Calendar.DAY_OF_MONTH));//加年份
								   }
								    updateChange(dialog);//更新显示值
									}
								}
				);
				//月减
				ImageButton monthminus=(ImageButton)dialog.findViewById(R.id.monthminus);
				monthminus.setOnClickListener(
						new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								 if( calendar.get(Calendar.MONTH)>0)
								   {
									 if(calendar.get(Calendar.DAY_OF_MONTH)==31||calendar.get(Calendar.DAY_OF_MONTH)==30)//如果为31天的话 减月份为下个月的最后一天、
									   {
										   int month=calendar.get(Calendar.MONTH)-1;
									        calendar.set(calendar.get(Calendar.YEAR),month,getMonthDays(calendar.get(Calendar.YEAR),(calendar.get(Calendar.MONTH)-1)));//加月份		   
									   }else
									   {
										   int month=(calendar.get(Calendar.MONTH)-1);
									        calendar.set(calendar.get(Calendar.YEAR),month, calendar.get(Calendar.DAY_OF_MONTH));
									   }
									  
								   }else
								   {
									   int year=calendar.get(Calendar.YEAR)-1;
								        calendar.set(year, 11, calendar.get(Calendar.DAY_OF_MONTH));
								   }						  
								updateChange(dialog);//更新显示值
							}
						}
				);
				//日期加
				ImageButton dayaddd=(ImageButton)dialog.findViewById(R.id.dayadd);
				dayaddd.setOnClickListener(
						new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								 if( calendar.get(Calendar.DAY_OF_MONTH)<getMonthDays(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH))  )
								   {
									   int day=calendar.get(Calendar.DAY_OF_MONTH)+1;
								        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),day);
								   }else if(calendar.get(Calendar.DAY_OF_MONTH)==getMonthDays(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)))
								   {
									   if(calendar.get(Calendar.MONTH)==11) //换年
										 {
										   int year=calendar.get(Calendar.YEAR)+1;
									        calendar.set(year,0,1);						 
										 }else                          //换月份
										 {    
											 int month=calendar.get(Calendar.MONTH)+1;
											 calendar.set(calendar.get(Calendar.YEAR),month,1);												 
										 }
									   
								   //     calendar.set(calendar.get(Calendar.YEAR)-1, 11, calendar.get(Calendar.DAY_OF_MONTH));
								   }//加日期
								updateChange(dialog);//更新显示值
							}
						}
				);
				//日期减
				ImageButton dayminuss=(ImageButton)dialog.findViewById(R.id.dayminus);
				dayminuss.setOnClickListener(
						new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								
								if((calendar.get(Calendar.DAY_OF_MONTH)-1)>0)// 获得天数。
								{
									int day= calendar.get(Calendar.DAY_OF_MONTH)-1;
							        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),day);             
								}else if((calendar.get(Calendar.DAY_OF_MONTH)-1)==0)//切换月份
								{	
									if((calendar.get(Calendar.DAY_OF_MONTH)-1)==-1) //如果为1月1号。则更换为上一年。
									{     
										int year=calendar.get(Calendar.YEAR)-1;
								        calendar.set(year,11, 31); 
								     }else       //换月份
									{						
								    	int month=calendar.get(Calendar.MONTH)-1;
								        int getMonthDays=getMonthDays(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)-1);							
								        calendar.set(calendar.get(Calendar.YEAR),month,getMonthDays);						
									} 	
								}
								updateChange(dialog); 	//更新显示值
							 }		
						}         
				);		
				//确定按钮添加监听
				Button okbuttonn=(Button)dialog.findViewById(R.id.dateok);
				okbuttonn.setOnClickListener(
						new View.OnClickListener() {
							@Override
							public void onClick(View v) {       													
							if(isSelect)
							{//若是新建编辑中调用
								Date date=new Date(calendar.getTimeInMillis());
								selectbnc.setDate(date);//设置日历时间
							}
							else
							{
								if(Constant.monthweekday==1)
				    			{
									curDate.setYear(calendar.get(Calendar.YEAR)-1900);
								    curDate.setMonth(calendar.get(Calendar.MONTH));
								    curDate.setDate(calendar.get(Calendar.DAY_OF_MONTH)); 
								    gotoMonthView();
				           		                                  // 返回到月视图
				    			}else if(Constant.monthweekday==2)
				    			{
							        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));						
				    			     weekdate();// 返回到周视图	    				
				    			}else if(Constant.monthweekday==3)
				    			{
							        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));						
				    				daydate();//日试图
				    			}	
								calendar3.setTime(curDate);
							}
						     dialog.cancel();
							}	
					 }
				);
				//取消按钮添加监听
				Button cancelbuttonn=(Button)dialog.findViewById(R.id.datecancel);//加年份
				cancelbuttonn.setOnClickListener(
						new View.OnClickListener() {
							@Override
							public void onClick(View v) {
		       					dialog.cancel();
							}
						}
				);
				break; 	
				//设置提醒时间         
		case TIME_DIALOG:	
				isSelect=true;//改变标志为新建编辑中的日期选择
				updateChangeq(dialog);
				//设置日历时间
			  selectbnc=(com.bn.calendar.BNCalendar)dialog.findViewById(R.id.calendar01);
			  selectbnc.setDate(new Date(calendar3.getTimeInMillis()));//设置日历时间
			  //设置中间显示年月的部分添加监听
			  selectbnc.addControlCenterActionListener(
		        		new ControlCenterActionListener()
		        		{
							@Override
							public void doAction(Date date) 
							{
								curDate=date;	
								//显示选择日期对话框
								showDialog(Constant.DATEPICKER_DIALOG);	
							}
		        			
		        		}
		        );
			  //小时加
			  ImageButton addhour=(ImageButton)dialog.findViewById(R.id.addhour);  
			   addhour.setOnClickListener(
						new View.OnClickListener() {
							@Override
							public void onClick(View v) {
							
						     if(calendar3.get(Calendar.HOUR_OF_DAY)<23)
						     {
						       int hour=calendar3.get(Calendar.HOUR_OF_DAY)+1;
						 
						       calendar3.set(calendar3.get(Calendar.YEAR),calendar3.get(Calendar.MONTH),calendar3.get(Calendar.DAY_OF_MONTH),hour, calendar3.get(Calendar.MINUTE));
						     }else
						     {
						    	 calendar3.set(calendar3.get(Calendar.YEAR),calendar3.get(Calendar.MONTH),calendar3.get(Calendar.DAY_OF_MONTH),0, calendar3.get(Calendar.MINUTE));
						     }
						     updateChangeq(dialog);}
							
						}
				);
			  ImageButton addmin=(ImageButton)dialog.findViewById(R.id.addmin);
			   addmin.setOnClickListener(
						new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								 if(calendar3.get(Calendar.MINUTE)<59)
							     {
							       int min=calendar3.get(Calendar.MINUTE)+1;						 
							       calendar3.set(calendar3.get(Calendar.YEAR),calendar3.get(Calendar.MONTH),calendar3.get(Calendar.DAY_OF_MONTH),calendar3.get(Calendar.HOUR_OF_DAY), min);
							     }else
							     {
							    	 calendar3.set(calendar3.get(Calendar.YEAR),calendar3.get(Calendar.MONTH),calendar3.get(Calendar.DAY_OF_MONTH),calendar3.get(Calendar.HOUR_OF_DAY), 0);
							     }						
								 updateChangeq(dialog);}
						}
				);
			   //小时减
			  ImageButton plushour=(ImageButton)dialog.findViewById(R.id.plushour);
			  plushour.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					 if(calendar3.get(Calendar.HOUR_OF_DAY)>1)
				     {
				         int hour=calendar3.get(Calendar.HOUR_OF_DAY)-1;
				 
				         calendar3.set(calendar3.get(Calendar.YEAR),calendar3.get(Calendar.MONTH),calendar3.get(Calendar.DAY_OF_MONTH),hour, calendar3.get(Calendar.MINUTE));
				     }else
				     {
				    	 calendar3.set(calendar3.get(Calendar.YEAR),calendar3.get(Calendar.MONTH),calendar3.get(Calendar.DAY_OF_MONTH),0, calendar3.get(Calendar.MINUTE));
				     }					
					 updateChangeq(dialog);}
			});
			  ImageButton plusmin=(ImageButton)dialog.findViewById(R.id.plusmin);
			    plusmin.setOnClickListener(new View.OnClickListener() {				
					@Override
					public void onClick(View v) {
						if(calendar3.get(Calendar.MINUTE)>0)
					     {
					       int min=calendar3.get(Calendar.MINUTE)-1;						 
					         calendar3.set(calendar3.get(Calendar.YEAR),calendar3.get(Calendar.MONTH),calendar3.get(Calendar.DAY_OF_MONTH),calendar3.get(Calendar.HOUR_OF_DAY), min);
					     }else
					     {
					    	 calendar3.set(calendar3.get(Calendar.YEAR),calendar3.get(Calendar.MONTH),calendar3.get(Calendar.DAY_OF_MONTH),calendar3.get(Calendar.HOUR_OF_DAY), 0);
					     }							
						updateChangeq(dialog);}
				});
		         //确定按钮添加监听
				Button okkbuttonn=(Button)dialog.findViewById(R.id.dateok);//加年份
				okkbuttonn.setOnClickListener(
						new View.OnClickListener() {
							@Override
							public void onClick(View v) {									
							///提交之后的工作。
							Date selectDate=selectbnc.getDate();
							//赋给calendar3
							calendar3.set(Calendar.YEAR, selectDate.getYear()+1900);
							calendar3.set(Calendar.MONTH,selectDate.getMonth());
							calendar3.set(Calendar.DAY_OF_MONTH,selectDate.getDate());
							TextView temp=(TextView)findViewById(R.id.tvnewscheduleDate);
							Date date=new Date(calendar3.getTimeInMillis());
							nowtime=Constant.DateToString(date);
							temp.setText(nowtime);
							alertone=nowtime;
							isSelect=false;//改变标志为主页中的日期选择
							dialog.cancel();	
								
					}
				   }
				);
				//取消按钮添加监听
				Button canncelbuttonn=(Button)dialog.findViewById(R.id.datecancel);//加年份
				canncelbuttonn.setOnClickListener(
						new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								isSelect=false;//改变标志为主页中的日期选择
		       					dialog.cancel();
							}
						}
				); 
				break; 	
		}
	}
	protected void setTime() {
		// TODO Auto-generated method stub
		//计算分钟和秒
    	int second=countSecond%60;
    	int minute=countSecond/60;      	
    	//创建内容字符串
    	String msgStr=(minute>9?minute+"m:":("0"+minute+"m:"))+(second>9?second+"s":"0"+second+"s");
		//创建消息数据Bundle
		Bundle b=new Bundle();
		//将内容字符串放进数据Bundle中
		b.putString("msg", msgStr);
		//创建消息对象
		Message msg=new Message();
		//设置数据Bundle到消息中
		msg.setData(b);
		//设置消息的what值
		msg.what=UPDATE_TIME;
		//发送消息
		hd.sendMessage(msg);
	}
	public void daydate()       //日视图的对话框确定按钮。
	{
		BnScheduleManagerView bmv=(com.bn.sn.BnScheduleManagerView)findViewById(R.id.BnScheduleManagerView01);  //如果被封装成一个控件。那么不能再用构造器创建对象。
	    int year=calendar.get(Calendar.YEAR);
	    int month=calendar.get(Calendar.MONTH);
	    int day=calendar.get(Calendar.DAY_OF_MONTH);    
		bmv.setTime(year, month, day);		
		if(Constant.monthweekday==1) //判断显示哪个主界面界面
	       {
	    	   gotoMonthView();
	       }
	       else if(Constant.monthweekday==2)
	       {
	        	 createWeek(); 
	       }else
	       {
	    	     createDay();
	       }           
		
	}
	public void weekdate()
	{
		BnWeekView bmv=(BnWeekView)findViewById(R.id.BnWeekView01);  //如果被封装成一个控件。那么不能再用构造器创建对象。
	    int year=calendar.get(Calendar.YEAR);
	    int month=calendar.get(Calendar.MONTH);
	    int day=calendar.get(Calendar.DAY_OF_MONTH);      				
		bmv.setTime(year, month, day);
		if(Constant.monthweekday==1) //判断显示哪个主界面界面
	     {
	     	   gotoMonthView();
	     }
	    else if(Constant.monthweekday==2)
	     {
	        	 createWeek(); 
	     }else
	     {
	    	     createDay();
	     }     
	}
	//=============================显示选择时间对话框所用方法begin===========================	
	public void updateChangeq(Dialog dialog)
	{		
		EditText edithour=(EditText)dialog.findViewById(R.id.showhour);
	    EditText editmin=(EditText)dialog.findViewById(R.id.showmin); 
	    int hour=calendar3.get(Calendar.HOUR_OF_DAY);
	    int min=calendar3.get(Calendar.MINUTE);
		edithour.setText(hour>9?hour+"":"0"+hour);
		editmin.setText(min>9?min+"":"0"+min);	 
	}
	public void updateChange(Dialog dialog)
	{
		//得到最上面显示当前日期情况的文本框控件引用
		TextView showDate=(TextView)dialog.findViewById(R.id.showdatetv);
		//设置值
		showDate.setText(getStrdate(calendar));
		//分别拿到日月年的文本框进行设置
		EditText etYear=(EditText)dialog.findViewById(R.id.edityear);
		EditText etMonth=(EditText)dialog.findViewById(R.id.editmonth);
		EditText etDate=(EditText)dialog.findViewById(R.id.editday);
		etYear.setText((calendar.get(Calendar.YEAR))+"");		
	    etMonth.setText((calendar.get(Calendar.MONTH)+1)>9?(calendar.get(Calendar.MONTH)+1)+"":"0"+(calendar.get(Calendar.MONTH)+1));
		etDate.setText(calendar.get(Calendar.DAY_OF_MONTH)>9?calendar.get(Calendar.DAY_OF_MONTH)+"":"0"+calendar.get(Calendar.DAY_OF_MONTH));
	}	
	public String getStrdate(Calendar d)
	{
		StringBuffer datestr=new StringBuffer();
		datestr.append(d.get(Calendar.YEAR));
		datestr.append("年");
		datestr.append((d.get(Calendar.MONTH)+1)>9?(d.get(Calendar.MONTH)+1):"0"+(d.get(Calendar.MONTH)+1));
		datestr.append("月");
		datestr.append(d.get(Calendar.DAY_OF_MONTH)>9?d.get(Calendar.DAY_OF_MONTH):"0"+d.get(Calendar.DAY_OF_MONTH));
		datestr.append("日");
		datestr.append("星期");
		switch(d.get(Calendar.DAY_OF_WEEK))
		{				
		case 1:datestr.append("日");break;
		case 2:datestr.append("一");break;
		case 3:datestr.append("二");break;
		case 4:datestr.append("三");break;
		case 5:datestr.append("四");break;
		case 6:datestr.append("五");break;
		case 7:datestr.append("六");break;
		}
		Log.d("d", d.get(Calendar.DAY_OF_WEEK)+"");
		return datestr.toString();
	}
	//定义方法获得当前月的天数
	public static int getMonthDays(int year, int month) //计算每个月的天数
	{
		month++;
		switch (month)
		{
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
			{
				return 31;
			}
			case 4:
			case 6:
			case 9:
			case 11:
			{
				return 30;
			}
			case 2:
			{
				if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0))
					return 29;
				else
					return 28;
			}
		}
		return 0;
	}
	//=============================显示选择时间对话框所用方法end===========================
	//onKeyDown方法
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
    	//按下手机返回按钮时
    	
    	if(keyCode==4){
    		recodes++;
    		goBack(curr);
    		return true;
    	}
    	return false;
	}
    //保存退出界面
    public void saveMain()
    {
    	SharedPreferences mainid=this.getSharedPreferences("mainid",Context.MODE_PRIVATE);
    	SharedPreferences.Editor editor=mainid.edit();//得到一个edit
        editor.putInt("mainid",Constant.monthweekday);//将输入的密码存入
        editor.commit();//提交
    }
    //===============================
  //============初始化新建日程的界面==================
	public void newschedule(Date date) 
	{
		curr=Constant.MAIN_BRANCH;
		this.currdate=date ;//接收参数显示相应的日期，此处显示的是默认的日期
		//添加日期的id号码  唯一的标识符   
		final int tempid = db.geteventID(ScheduleManagerActivity.this,1);
		id=tempid;
		setContentView(R.layout.newschedule);
		//将地点和联系人设为空
		TextView locationtextview1=(TextView)findViewById(R.id.locationtextview01);
		locationtextview1.setText("");
		
		//============设置标题============
		TextView newOredit=(TextView)findViewById(R.id.title);
		newOredit.setText("日程新建");
		
		//=============初始化类型==============
	    db.getstype(this);
		Spinner sp=(Spinner)findViewById(R.id.typespinner);//显示项目
		BaseAdapter ba=new BaseAdapter(){      //为Spinner准备内容适配器
        	@Override
			public int getCount() {
				return alType.size();
			}
        	@Override
			public Object getItem(int position) {
				return alType.get(position);
			}
        	@Override
			public long getItemId(int position) {
				return position;
			}
        	@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				LinearLayout ll=new LinearLayout(ScheduleManagerActivity.this);
				ll.setOrientation(LinearLayout.HORIZONTAL);	
				TextView tv=new TextView(ScheduleManagerActivity.this);
				
				tv.setText(alType.get(position));
				selectedtype=alType.get(position);//新建日程的类型、
				type=position;
				tv.setTextSize(20);
				tv.setTextColor(R.color.black);
				return tv;
			}
        };
        
        sp.setAdapter(ba);
        //=========为类型添加按钮添加监听=============
        ImageView   btstype=(ImageView)findViewById(R.id.btstype);
        btstype.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				showDialog(DIALOG_ADD_TYPE);
				
				}
		});
        //========为类型删除添加监听
        ImageView  delete=(ImageView)findViewById(R.id.deletdtype);
        delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				showDialog(Constant.MORE_EDIT);
				
				}
		});
        alertone=nowtime;
       //=============设置日程显示的日期和时间   Start==================
		
		TextView tv=(TextView)findViewById(R.id.tvnewscheduleDate);
		/*date.setHours(8);//初始化日程时间
		date.setMinutes(0);*/
		nowtime=cs.DateToString(currdate);
		tv.setText(nowtime);
		
		
        ImageView dateimageview=(ImageView)findViewById(R.id.dateimageview);
        dateimageview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//调取系统的时间设置对话框并且返回时间
				showDialog(TIME_DIALOG);
				
			}
		});
        
      //=============设置日程显示的日期和时间   End==================
        
      //=============为闹钟重复提醒添加监听========================
        ImageView alertimageview=(ImageView)findViewById(R.id.alertimageview);
        alertimageview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(TIME_ALERT);				
			}
		});
     
        EditText titletext=(EditText)findViewById(R.id.newscheduletitleedit);
       
        //=======为附件那添加监听,添加联系人，短信，图片，声音等
        ImageView fujian=(ImageView)findViewById(R.id.fujianimageview);
        fujian.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				showDialog(FUJIAN_LIST);
			}
		});
        //==========禁止显示显示提醒的内容
        TextView showalarm=(TextView)findViewById(R.id.showalarm);
        showalarm.setVisibility(View.GONE);
        //==========禁止显示播放按钮
        Button soundplay=(Button)findViewById(R.id.playsound);
        soundplay.setVisibility(View.GONE);
        //======为保存按钮添加监听
        ImageButton btcancle=(ImageButton)findViewById(R.id.btcancle);
        btcancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goBack(curr);
	    		
				
			}
		});
        
        ImageButton  btsave=(ImageButton)findViewById(R.id.btsave);
        btsave.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
			   //db.geteventID(getApplicationContext(), 1);
				//显示标题
				EditText titletext=(EditText)findViewById(R.id.newscheduletitleedit);
				String title=titletext.getText().toString();//type是根据类型获取到的
				//显示联系人
				TextView contact=(TextView)findViewById(R.id.contacttextview01);
				contact.setText(person);				
				//添加日程的类型ID    根据给的类型查找其ID
				//type=db.gettypeID(getApplicationContext(), selectedtype);				
				//添加日程的联系人
				TextView  contacttext=(TextView)findViewById(R.id.contacttextview01);
				person=contacttext.getText().toString();				
				//添加日程的地点
				TextView  loction=(TextView)findViewById(R.id.locationtextview01);
				place=loction.getText().toString();				
				//添加日程的正文
				EditText contenttext=(EditText)findViewById(R.id.etxjrcNote);
				text=contenttext.getText().toString();				
				Date nowDate=new Date(System.currentTimeMillis());
				String nowDateStr=cs.DateToString(nowDate);
				if (title.length()!=0)
				{					
				         db.insertcontent(getApplicationContext(), id, text, picpath, soundpath);
				         db.insertalert(ScheduleManagerActivity.this, id, nowtime, alertone,alerttwo ,alertthree ,alertfour ,alertfive ,alertseven ,alertsix );
			             db.insetschedule(ScheduleManagerActivity.this, id, title, id, type, person,place, alertstyle);
				         db.inserttimetable(getApplicationContext(),id,id);						
				}
				else{
					Toast.makeText(getApplicationContext(), "请输入标题", Toast.LENGTH_LONG).show();
					return;
				}
				ImageButton  btsave=(ImageButton)findViewById(R.id.btsave);
				btsave.setVisibility(View.VISIBLE);
				btsave.setEnabled(false);
				setAlarm();//更新闹铃设置
				if(Constant.monthweekday==1) //判断显示哪个主界面界面
			       {
			    	   gotoMonthView();
			       }
			       else if(Constant.monthweekday==2)
			       {
			        	 createWeek(); 
			       }else
			       {
			    	     createDay();
			       }            
				
				
			}			
		});
	 }	
	public String addContact() {
		String contactperson=null;
	    setContentView(R.layout.addcontacts);
	    Button btnClick=(Button)findViewById(R.id.btnClick);
	    btnClick.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
               
                intent.setType("vnd.android.cursor.item/phone");
                startActivityForResult(intent, PICK_CONTACT);
				
		}
		});
		Button addok=(Button)findViewById(R.id.addok);
		addok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText cantacts=(EditText)findViewById(R.id.show);
				person=cantacts.getText().toString();
				setContentView(R.layout.newschedule);
				EditText titletext=(EditText)findViewById(R.id.newscheduletitleedit);
				TextView contact=(TextView)findViewById(R.id.contacttextview01);
				contact.setText(person);
							}
		});
		
		Button addcancle=(Button)findViewById(R.id.addcancle);
		addcancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setContentView(R.layout.newschedule);
			}
		});
		return contactperson;
	}
	
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
		if (resultCode == NONE) 
            return; 
        // 拍照  
        if (requestCode == PHOTOHRAPH) { 
            //设置文件保存路径这里放在跟目录下  
           File picture = new File(Environment.getExternalStorageDirectory() + "/temp.jpg");
           picpath="/mnt/sdcard/temp.jpg";
           //Toast.makeText(getApplicationContext(), picpath, Toast.LENGTH_LONG).show();
            startPhotoZoom(Uri.fromFile(picture)); 
        } 
         
        if (data == null) 
            return; 
         
        // 读取相册缩放图片  
        if (requestCode == PHOTOZOOM) { 
            startPhotoZoom(data.getData()); 
        } 
        // 处理结果  
        if (requestCode == PHOTORESOULT) { 
            Bundle extras = data.getExtras(); 
            if (extras != null) { 
            	 photo = extras.getParcelable("data");
            	 
            	 //==========start======
            	 String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(System.currentTimeMillis()));//设置文件名
                 File file=new File(Environment.getExternalStorageDirectory()+"/" + fileName + ".png");//创建新的文件用于存储图像
                 try {
 				    file.createNewFile();//创建新的文件
 				  } catch (IOException e1) {
 						e1.printStackTrace();
 					}
 				 try {
 					BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
 					photo.compress(Bitmap.CompressFormat.JPEG, 80, bos);
 					picpath=Environment.getExternalStorageDirectory()+"/" + fileName + ".png";//存储图像的路径
 					bos.flush();//将输入流强行输出并且关闭
 					bos.close();
 				} catch (FileNotFoundException e) {
 					e.printStackTrace();
 				} catch (IOException e) {
 					e.printStackTrace();
 				}
 				//==============end================
                ByteArrayOutputStream stream = new ByteArrayOutputStream(); 
                photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0 - 100)压缩文件  
                imageView.setImageBitmap(photo); 
                
            } 
 
        } 
   
        
     //=====================end=====================
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
        case PICK_CONTACT:
            if (resultCode == Activity.RESULT_OK)
            {  String  result = "";
                Uri contactData = data.getData();// 获取返回的数据
                Cursor cursor = managedQuery(contactData, null, null, null, null);// 查询联系人信息
                // 如果查询到指定的联系人
                if (cursor.moveToFirst())
                {
                    String contactID = cursor.getString(cursor
                            .getColumnIndex(ContactsContract.Contacts._ID));
                    // 获取联系人的姓名
                    String name = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                   
                    result=result+name;
                    // String phoneNumber = "此联系人未输入电话号码";
                    // 根据联系人查询该联系人的详细信息
                    Cursor phones = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                    + "=" + contactID, null, null);
                  //关闭游标
                    phones.close();
                    EditText show = (EditText)findViewById(R.id.show);
                    show.setText(result);
               }
                cursor.close();
            }
            break;
        }

    }

	//=========================编辑页面begin==============================
	//初始化编辑日程的界面
	public void editschedule( int inid) 
	{  //contentid  和eventid和alerttime的id是相同的
		curr=Constant.MAIN_BRANCH;
		setContentView(R.layout.newschedule);
		String tempstr=null;
		int	contentid=0;
		String edittypestr=null;
		//=====初始化eventtable的内容  id   title  type （id） person place
		ArrayList<String> editevent=new ArrayList<String>();
		editevent=db.getevent(getApplicationContext(), inid);
		Iterator it2=editevent.iterator();
		if (it2.hasNext()){
				tempstr=it2.next().toString();
		}
		String[] temparray=tempstr.split("#");
		final int  eventid =Integer.parseInt(temparray[0]) ;   //inid====eventid
		
		String titile = temparray[1].toString();
		contentid=Integer.parseInt(temparray[2]);
		final int typeid = Integer.parseInt(temparray[3]);
		person=temparray[4].toString();
		place=temparray[5].toString();
		String alerttytle=temparray[6].toString();
		//============初始化联系人
		TextView  contacttext=(TextView)findViewById(R.id.contacttextview01);
		contacttext.setText(person);
		//地图监听
		
		if ((place.length())==0){
			ImageView  ivw=(ImageView)findViewById(R.id.ImageViewmap01);
			ivw.setEnabled(false);
		}else{
			
			ImageView  ivw=(ImageView)findViewById(R.id.ImageViewmap01);
			   ivw.setBackgroundDrawable(getResources().getDrawable(R.drawable.ballon));
			   ivw.setOnClickListener(
				new OnClickListener()
				{

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

					}
					
				}			   
			   );
		}
		  
		
		//初始化content的title   picpath  soundpath    
		ArrayList<String> editcontent=new ArrayList<String>();
		editcontent=db.getcontent(getApplicationContext(), inid);//将5改成contentid
		Iterator it1=editcontent.iterator();
		 
		if (it1.hasNext()){
			tempstr=it1.next().toString();
		
		}
		  temparray=tempstr.split("#");
		 contentid = Integer.parseInt(temparray[0]);//ontent的id
	    text=temparray[1];
		picpath=temparray[2];
	    soundpath=temparray[3];
	    ImageView shophoto=(ImageView)findViewById(R.id.showphoto);
	    //=============初始化内容
	    
	    EditText contenttext=(EditText)findViewById(R.id.etxjrcNote);
	    contenttext.setText(text.toString());
	    //===========初始化图片和声音=====
	    try {
            URL url = new URL("file:"+picpath);//异常
            URLConnection conn = url.openConnection();
            conn.connect();
            InputStream in = conn.getInputStream();
            Bitmap map = BitmapFactory.decodeStream(in);
            shophoto.setImageBitmap(map);
          } catch (Exception e) {
           
          }
	    //======================为播放声音的按钮添加监听
          
        Button playsound=(Button)findViewById(R.id.playsound);
        if((soundpath.length()-4)==0){
        	playsound.setVisibility(View.GONE);
        }else{
        	playsound.setOnClickListener(new OnClickListener() {
    			
    			@Override
    			public void onClick(View v) {
    				
    					try 
    				 {
    			    	//设置播放歌曲的路径
    				    mp=new	MediaPlayer();
    					mp.setDataSource(soundpath);
    					//进行播放前的准备工作，new方式创建的MediaPlayer一定需要prepare
    					//否则报错
    					mp.prepare();
    					mp.start();
    				 } catch (Exception e1) 
    				 {					
    					 Toast.makeText(getApplicationContext(), "声音初始化错误"+e1.toString(), Toast.LENGTH_LONG).show();
    				 }
    				
    				
    				
    			}
    		});
        }
	    
	    //初始化typetable的type
	    ArrayList<String> edittype=new ArrayList<String>();
	    edittype=db.gettype(getApplicationContext(), typeid);
	    it1=edittype.iterator();
	    if (it1.hasNext()){
			tempstr=it1.next().toString();
		}
	    temparray=tempstr.split("#");
	    final int selectedtypeid = Integer.parseInt(temparray[0]);
	    edittypestr=temparray[1].toString();
	    
	    //获取alerttable的相关内容
	    ArrayList<String> editimes=new ArrayList<String>();
	    editimes=db.gettimes(getApplicationContext(), inid);
	    it1=editimes.iterator();
	    String[] tenptimes=new String[editimes.size()];
	   
	    String temptimes = null;
		if (it1.hasNext()){
	    	temptimes=it1.next().toString();
		}
	    tenptimes=temptimes.split("#");
	    
	    alerttime=Integer.parseInt(tenptimes[0]);
	    
	    nowtime=tenptimes[1].toString();
	    /*currdate=new Date(System.currentTimeMillis());//重写方法将String类型的转换为date类型的
	    
	    nowtime=DateToString(currdate);*/
	    StringBuilder sb=new  StringBuilder() ;
	    sb.append("提醒:");
	    currdate=cs.StringToDate(nowtime);
	    alertone=tenptimes[2].toString();
	      if ((alertone.length()-4)!=0){
	    	  sb.append("正点  ");  
	      }
	    alerttwo=tenptimes[3].toString();
	      if ((alerttwo.length()-4)!=0){
	    	  sb.append("5分");  
	       }
	    alertthree=tenptimes[4].toString();
	       if ((alertthree.length()-4)!=0){
	    	  sb.append("10分");  
	        }
	    alertfour=tenptimes[5].toString();
	       if ((alertfour.length()-4)!=0){
	    	  sb.append("30 分");  
	        }
	    alertfive=tenptimes[6].toString();
	       if ((alertfive.length()-4)!=0){
	    	  sb.append("一小时 ");  
	        }
	    alertsix=tenptimes[7].toString();
	        if ((alertsix.length()-4)!=0){
	    	  sb.append("一天 ");  
	        }
	    alertseven=tenptimes[8].toString();
	    if ((alertseven.length()-4)!=0){
	    	  sb.append("一周 ");  
	        }
	    //初始化标题栏
	    TextView newOredit=(TextView)findViewById(R.id.title);
		newOredit.setText("编辑日程");
		//============初始化闹钟的提醒内容
		
		  TextView showalarm=(TextView)findViewById(R.id.showalarm);
		  showalarm.setText(sb.toString());
		  //showalarm  
	    
		//初始化类型及添加删除按钮
		//=============初始化类型==============
		 db.getstype(this);
		    
			Spinner sp=(Spinner)findViewById(R.id.typespinner);//显示项目
			BaseAdapter ba=new BaseAdapter(){      //为Spinner准备内容适配器
	        	@Override
				public int getCount() {
					return alType.size();
				}
	        	@Override
				public Object getItem(int selectedtypeid) {
					return alType.get(selectedtypeid);
				}
	        	@Override
				public long getItemId(int position) {
	        		type=position;
					return 0;
				}
	        	@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					LinearLayout ll=new LinearLayout(ScheduleManagerActivity.this);
					ll.setOrientation(LinearLayout.HORIZONTAL);	
					TextView tv=new TextView(ScheduleManagerActivity.this);
					
					tv.setText(alType.get(position));
					selectedtype=alType.get(position);//新建日程的类型
					tv.setTextSize(20);
					tv.setTextColor(R.color.black);
					return tv;
				}
	        };      
	        sp.setAdapter(ba);sp.
	        setSelection(typeid);

        //=========为类型添加按钮添加监听=============
        ImageView   btstype=(ImageView)findViewById(R.id.btstype);
        btstype.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				showDialog(DIALOG_ADD_TYPE);
				
				}
		});
        //========为类型删除添加监听
        ImageView  delete=(ImageView)findViewById(R.id.deletdtype);
        delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				showDialog(DIALOG_DELETE_TYPE);
				
				}
		});
	    
        
        //初始化日期及闹铃提醒，闹钟   
        TextView tv=(TextView)findViewById(R.id.tvnewscheduleDate);
		tv.setText(nowtime);
		
		ImageView dateimageview=(ImageView)findViewById(R.id.dateimageview);
        dateimageview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//调取系统的时间设置对话框并且返回时间
				showDialog(TIME_DIALOG);
				
			}
		});
        
        
		ImageView alertimageview=(ImageView)findViewById(R.id.alertimageview);
        alertimageview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(TIME_ALERT);
				
			}
		});
        ////////////////////
        //========title======
        EditText titletext=(EditText)findViewById(R.id.newscheduletitleedit);
        titletext.setText(titile);
        //=======为附件那添加监听,添加联系人，短信，图片，声音等
        ImageView fujian=(ImageView)findViewById(R.id.fujianimageview);
        fujian.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				showDialog(FUJIAN_LIST);
			}
		});
          contacttext=(TextView)findViewById(R.id.contacttextview01);
        contacttext.setText(person);
        TextView  loction=(TextView)findViewById(R.id.locationtextview01);
        loction.setText(place);
        //======为保存按钮添加监听
        ImageButton btcancle=(ImageButton)findViewById(R.id.btcancle);
        btcancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goBack(curr);
	    		
				
			}
		});
        ImageButton  btsave=(ImageButton)findViewById(R.id.btsave);
        btsave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			  
				EditText titletext=(EditText)findViewById(R.id.newscheduletitleedit);
				String title=titletext.getText().toString();//type是根据类型获取到的
				//显示联系人
				/*TextView contact=(TextView)findViewById(R.id.contacttextview01);
				contact.setText(person);*/
				
				//添加日程的类型ID    根据给的类型查找其ID
				//type=db.gettypeID(getApplicationContext(), selectedtype);
				
				//添加日程的联系人
				TextView  contacttext=(TextView)findViewById(R.id.contacttextview01);
				person=contacttext.getText().toString();
				
				//添加日程的地点
				TextView  loction=(TextView)findViewById(R.id.locationtextview01);
				place=loction.getText().toString();
				
				//添加日程的正文
				EditText contenttext=(EditText)findViewById(R.id.etxjrcNote);
				text=contenttext.getText().toString();
				
				Date nowDate2=new Date(System.currentTimeMillis());
				String nowDateStr2=cs.DateToString(nowDate2);
				if(title.length()!=0){
					db.updatealert(ScheduleManagerActivity.this, eventid, nowtime, alertone,alerttwo ,alertthree ,alertfour,alertfive ,alertsix,alertseven  );
				    db.updateschedule(ScheduleManagerActivity.this, eventid, title, eventid, type, person,place, alertstyle);
				    db.updatecontent(getApplicationContext(),eventid, text, picpath, soundpath);
						
						
						
				}else {
					return;
				}
				
				ImageButton  btsave=(ImageButton)findViewById(R.id.btsave);
				btsave.setVisibility(View.VISIBLE);
				btsave.setEnabled(false);
				setAlarm();//更新闹铃设置
				if(Constant.monthweekday==1) //判断显示哪个主界面界面
			       {
			    	   gotoMonthView();
			       }
			       else if(Constant.monthweekday==2)
			       {
			        	 createWeek(); 
			       }else
			       {
			    	     createDay();
			       }            
				 //db.upcontent(getApplicationContext(), eventid, text, picpath, soundpath);
				 
			}

			
		});

	   }
	//==============================编辑页面end========================
	public void startPhotoZoom(Uri uri)
    {
        Intent intent = new Intent("com.android.camera.action.CROP"); 
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED); 
        intent.putExtra("crop", "true"); 
        // aspectX aspectY 是宽高的比例  
        intent.putExtra("aspectX",1); 
        intent.putExtra("aspectY",1);
        // outputX outputY 是裁剪图片宽高  
        intent.putExtra("outputX",100); 
        intent.putExtra("outputY",100); 
        intent.putExtra("return-data", true); 
        startActivityForResult(intent, PHOTORESOULT); 
    } 
	
}