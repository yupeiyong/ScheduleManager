package com.bn.sn;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;


public class DBUtil
{
	//============================所有处理类型数据库的方法start==============================
	public static SQLiteDatabase openDatabase(Context fa)
	{
		SQLiteDatabase sld=null;
		try
		{
			sld=SQLiteDatabase.openDatabase
				(
					"/data/data/com.bn.sn/myDb", 
					null, 
					SQLiteDatabase.OPEN_READWRITE|SQLiteDatabase.CREATE_IF_NECESSARY
				);		
			//1类别表id设为identity(1,1)从1开始每次自增长1
			String sql="create table if not exists typetable"+
						"(id Integer PRIMARY KEY,type char(20) not null);";
			sld.execSQL(sql);						
			//2内容表
			sql="create table if not exists contenttable(id int not null," +
					"text varchar(50),picpath varchar(50),soundpath  varchar(50),primary key(id));";
			sld.execSQL(sql);			
			//3提醒事件表
			sql="create table if not exists alerttable(alerttime int,nowtime char(20),alertone char(20),"+
			"alerttwo char(20),alertthree char(20),alertfour char(20), alertfive char(20),alertsix char(20),"
			+"alertseven char(20),primary key(alerttime));";
			sld.execSQL(sql);
			//4事件表
			sql="create table if not exists eventtable(id int not null,title char(20),"
				+"content int,type int,person varchar(30),place char(30),alertstyle char(20),"
				+"primary key(id),foreign key(content) references contenttable(id),"
				+"foreign key(type) references typetable(id));";
			sld.execSQL(sql);			
			//5时间表
			sql="create table if not exists timetable(id int not null,"+
			"eventid int,alerttime int,primary key(id),"+
			"foreign key(eventid) references eventtable(id),"+
			"foreign key(alerttime) references alerttable(alerttime));"; 
			sld.execSQL(sql);		
		}catch(Exception e)
		{
			Log.d("error", e.toString()+"=============open===============");
			Toast.makeText(fa, "数据库打开创建错误："+e.toString(), Toast.LENGTH_LONG).show();
		}
		return sld;
	}	
	//关闭数据库的方法
    public static void closeDatabase(SQLiteDatabase sld,Context fa)
    {
    	try
    	{
	    	sld.close();       		
    	}
		catch(Exception e)
		{
			Log.d("error", e.toString()+"=============close===============");
			Toast.makeText(fa, "数据库关闭失败："+e.toString(), Toast.LENGTH_LONG).show();
		}
    }
    //===================获得分类列表==================
    public static List<String> getTypeList(Context context)
    {
    	SQLiteDatabase sld=null;
    	List<String> typelist=new ArrayList<String>();
    	Cursor cur = null;
    	try
    	{
    		//打开数据库
        	sld=openDatabase(context);
        	//String test="delete from typetable";
    		//sld.execSQL(test);
        	//查询语句
        	String sql="select id,type from typetable";
        	cur=sld.rawQuery(sql, null);//执行语句得到结果集
        	while(cur.moveToNext())
        	{
        		StringBuffer cursb=new StringBuffer();
        		cursb.append(cur.getInt(0)+"#");//将一条记录整理成中间以#分隔的字符串
        		cursb.append(cur.getString(1));
        		//添加到列表中去
        		//打印
        		Log.d("type", cursb.toString()+"=");
        		typelist.add(cursb.toString());
        	}
        	
    	}
    	catch(Exception e)
    	{
    		Log.d("error", e.toString()+"===============type=============");
    		Toast.makeText(context, "数据库查询失败："+e.toString(), Toast.LENGTH_LONG).show();
    	}	
    	finally
    	{	
    		try
    		{
    			cur.close();//关闭结果集
            	closeDatabase(sld,context);//关闭数据库
    		}
    		catch(Exception e)
    		{
    			Log.d("error", e.toString()+"============type================");
    		}
    		
    	}
    	return typelist;
    }
    //根据说给类型id查找相应分类的日程标题
    public static List<String> getScheduleTitile(Context context,String stypeid,Date time)
    {
    	SQLiteDatabase sld=null;
    	List<String> titlelist=new ArrayList<String>();
    	Cursor cur = null;
    	//根据所给的类型id查询日程题目
    	//将传来的日期参数取此天的起点和终点
    	time.setHours(0);
    	time.setMinutes(0);
    	time.setSeconds(0);
    	String datebegin=Constant.DateToString(time);
    	Log.d("d1", datebegin);
    	//终点
    	time.setHours(23);
    	time.setMinutes(59);
    	time.setSeconds(59);
    	String dateend=Constant.DateToString(time);
    	Log.d("d2", dateend);
    	try
    	{
    		//打开数据库
        	sld=openDatabase(context);
        	//查询语句
        	String sql="select id,title from eventtable where type=?"
        			+ "and id in(" +
        			"select eventid from timetable where alerttime in(" +
        			"select alerttime from alerttable where nowtime >='"+datebegin+"' and nowtime <='"+dateend+"'))";
        		
        	cur=sld.rawQuery(sql,new String[]{stypeid});//执行语句得到结果集
        	while(cur.moveToNext())
        	{
        		StringBuffer cursb=new StringBuffer();
        		cursb.append(cur.getInt(0)+"#");//将一条记录整理成中间以#分隔的字符串
        		cursb.append(cur.getString(1));
        		//添加到列表中去
        		//打印
        		Log.d("title", cursb.toString()+"=");
        		titlelist.add(cursb.toString());
        	}
    	}
    	catch(Exception e)
    	{
    		Log.d("error", e.toString()+"=============title===============");
    		Toast.makeText(context, "数据库查询失败："+e.toString(), Toast.LENGTH_LONG).show();
    	}
    	finally
    	{
    		try
    		{
    			cur.close();//关闭结果集
            	closeDatabase(sld,context);//关闭数据库
    		}
    		catch(Exception e)
    		{
    			Log.d("error", e.toString()+"=============title2===============");
    		}
    	}
    	
    	return titlelist;
    }
    //删除相应id的日程及timetable和alerttable中的相关记录
    public static void deleteSchdule(Context context,String eventid)
    {
    	//先删除alerttable的记录再删除timetable的记录，最后删除eventtable中的记录
    	SQLiteDatabase sld=null;
    	Cursor cur = null;
    	try
    	{
    		//打开数据库
        	sld=openDatabase(context);
        	String sql="select id,alerttime from timetable where eventid=?";
        	cur=sld.rawQuery(sql,new String[]{eventid});//先通过timetable找到相应alerttable的id
        	while(cur.moveToNext())
        	{
        		//删除提醒表中的内容
        		sql="delete from alerttable where alerttime=?";
        		sld.execSQL(sql,new String[]{cur.getInt(1)+""});
        		//删除timetable中的相应内容
        		sql="delete from timetable where id=?";
        		sld.execSQL(sql,new String[]{cur.getInt(0)+""});
        	}
        	cur.close();
        	//再查询相应日程对应的内容表中的id
        	sql="select content from eventtable where id=?";
        	cur=sld.rawQuery(sql,new String[]{eventid});
        	//删除事件表的日程记录
        	sql="delete from eventtable where id=?";
        	sld.execSQL(sql,new String[]{eventid});
        	//删除内容表中的记录
        	sql="delete from contenttable where id=?";
        	sld.execSQL(sql,new String[]{eventid});
        	/*
        	if(cur.moveToNext())
        	{
        		sql="delete from contenttable where id=?";
        		sld.execSQL(sql,new String[]{cur.getInt(0)+""});
        		Toast.makeText(context, "删除成功", Toast.LENGTH_LONG).show();
        	}else{
        		Toast.makeText(context, "删除失败", Toast.LENGTH_LONG).show();
        	}*/
        	
    	}
    	catch(Exception e)
    	{
    		Log.d("error", e.toString()+"=============delete===============");
    		Toast.makeText(context, "删除失败："+e.toString(), Toast.LENGTH_LONG).show();
    	}
    	finally
    	{
    		try
    		{
    			cur.close();//关闭结果集
            	closeDatabase(sld,context);//关闭数据库
    		}
    		catch(Exception e)
    		{
    			Log.d("error", e.toString()+"=============delete2===============");
    		}
    	}
    }

    //查询数据库中未来最近的时间,及其对应的事件id
    public static Map<Date,Integer> findnearlyTime(Context context)
    {
    	Map<Date,Integer> map=new HashMap<Date,Integer>();//以键值对的形式将时间和事件id返回
    	Date date=null;
    	Integer eventid=null;//事件id
    	SQLiteDatabase sld=null;
    	Cursor cur = null;
    	try
    	{
    		//得到系统当前时间的字符串
    		Date nowtime=new Date(System.currentTimeMillis());
    		String nowstr=Constant.DateToString(nowtime);
    		//打开数据库
        	sld=openDatabase(context);
        	
        	String minDatestr=null;//用于存放查出的所有最小中最小的一个
        	//查询第一列时间的最小值
        	String sql="select min(alertone) from alerttable where alertone>'"+nowstr+"'";
        	cur=sld.rawQuery(sql,null);
        	if(cur.moveToNext())
        	{
        		if(cur.getString(0)!=null)
        		{//若最小值不为空则赋值非记录最小值的字符串
        			minDatestr=cur.getString(0);
        		}
        	}
        	cur.close();
        	//第二列时间的最小值
        	sql="select min(alerttwo) from alerttable where alerttwo>'"+nowstr+"'";
        	cur=sld.rawQuery(sql,null);
        	if(cur.moveToNext())
        	{
        		if(cur.getString(0)!=null&&minDatestr==null)
        		{//若最小值不为空且字符组没有被赋值则记录最小值的字符串
        			minDatestr=cur.getString(0);
        		}
        		else if(cur.getString(0)!=null&&cur.getString(0).compareTo(minDatestr)<0)
        		{//若所得日期字符串比已存的小则赋值
        			minDatestr=cur.getString(0);
        		}
        	}
        	cur.close();
        	//第三列时间的最小值
        	sql="select min(alertthree) from alerttable where alertthree>'"+nowstr+"'";
        	cur=sld.rawQuery(sql,null);
        	if(cur.moveToNext())
        	{
        		if(cur.getString(0)!=null&&minDatestr==null)
        		{//若最小值不为空且字符组没有被赋值则记录最小值的字符串
        			minDatestr=cur.getString(0);
        		}
        		else if(cur.getString(0)!=null&&cur.getString(0).compareTo(minDatestr)<0)
        		{//若所得日期字符串比已存的小则赋值
        			minDatestr=cur.getString(0);
        		}
        	}
        	cur.close();
        	//第四列时间的最小值
        	sql="select min(alertfour) from alerttable where alertfour>'"+nowstr+"'";
        	cur=sld.rawQuery(sql,null);
        	if(cur.moveToNext())
        	{
        		if(cur.getString(0)!=null&&minDatestr==null)
        		{//若最小值不为空且字符组没有被赋值则记录最小值的字符串
        			minDatestr=cur.getString(0);
        		}
        		else if(cur.getString(0)!=null&&cur.getString(0).compareTo(minDatestr)<0)
        		{//若所得日期字符串比已存的小则赋值
        			minDatestr=cur.getString(0);
        		}
        	}
        	cur.close();
        	//第五列时间的最小值
        	sql="select min(alertfive) from alerttable where alertfive>'"+nowstr+"'";
        	cur=sld.rawQuery(sql,null);
        	if(cur.moveToNext())
        	{
        		if(cur.getString(0)!=null&&minDatestr==null)
        		{//若最小值不为空且字符组没有被赋值则记录最小值的字符串
        			minDatestr=cur.getString(0);
        		}
        		else if(cur.getString(0)!=null&&cur.getString(0).compareTo(minDatestr)<0)
        		{//若所得日期字符串比已存的小则赋值
        			minDatestr=cur.getString(0);
        		}
        	}
        	cur.close();
        	//第六列时间的最小值
        	sql="select min(alertsix) from alerttable where alertsix>'"+nowstr+"'";
        	cur=sld.rawQuery(sql,null);
        	if(cur.moveToNext())
        	{
        		if(cur.getString(0)!=null&&minDatestr==null)
        		{//若最小值不为空且字符组没有被赋值则记录最小值的字符串
        			minDatestr=cur.getString(0);
        		}
        		else if(cur.getString(0)!=null&&cur.getString(0).compareTo(minDatestr)<0)
        		{//若所得日期字符串比已存的小则赋值
        			minDatestr=cur.getString(0);
        		}
        	}
        	cur.close();
        	//第七列时间的最小值
        	sql="select min(alertseven) from alerttable where alertseven>'"+nowstr+"'";
        	cur=sld.rawQuery(sql,null);
        	if(cur.moveToNext())
        	{
        		if(cur.getString(0)!=null&&minDatestr==null)
        		{//若最小值不为空且字符组没有被赋值则记录最小值的字符串
        			minDatestr=cur.getString(0);
        		}
        		else if(cur.getString(0)!=null&&cur.getString(0).compareTo(minDatestr)<0)
        		{//若所得日期字符串比已存的小则赋值
        			minDatestr=cur.getString(0);
        		}
        	}
        	cur.close();
        	//若查出了记录则找出其对应的事件id并将此字符串转换成Date类型
        	if(minDatestr!=null)
        	{
        		sql="select eventid from timetable where alerttime=(" +
        				"select alerttime from alerttable where alertone='"+minDatestr+"' or " +
        				"alerttwo='"+minDatestr+"' or alertthree='"+minDatestr+"' or " +
        				"alertfour='"+minDatestr+"' or alertfive='"+minDatestr+"' or " +
        				"alertsix='"+minDatestr+"' or alertseven='"+minDatestr+"')";
        		cur=sld.rawQuery(sql,null);
        		if(cur.moveToNext())
        		{
        			eventid=cur.getInt(0);//拿到事件id
        		}
        		date=Constant.StringToDate(minDatestr);
        		map.put(date, eventid);
        	}
    	}
    	catch(Exception e)
    	{
    		Log.d("error", e.toString()+"=============selectDate===============");
    		Toast.makeText(context, "查询日期失败："+e.toString(), Toast.LENGTH_LONG).show();
    	}
    	finally
    	{
    		try
    		{
    			cur.close();//关闭结果集
            	closeDatabase(sld,context);//关闭数据库
    		}
    		catch(Exception e)
    		{
    			Log.d("error", e.toString()+"=============selectDate===============");
    		}
    	}	
    	return map;
    }
    
    //通过事件的id查询其标题
    public static String getTitleById(Context context,int id)
    {
    	String title=null;
    	SQLiteDatabase sld=null;
    	Cursor cur = null;
    	try
    	{
    		//打开数据库
        	sld=openDatabase(context);
    		String sql="select title from eventtable where id="+id;
    		cur=sld.rawQuery(sql, null);
    		if(cur.moveToNext())
    		{
    			title=cur.getString(0);//取得标题
    		}
    	}
    	catch(Exception e)
    	{
    		Log.d("error", e.toString()+"=============selectTitleById===============");
    		Toast.makeText(context, "通过id查询标题失败："+e.toString(), Toast.LENGTH_LONG).show();
    	}
    	finally
    	{
    		try
    		{
    			cur.close();//关闭结果集
            	closeDatabase(sld,context);//关闭数据库
    		}
    		catch(Exception e)
    		{
    			Log.d("error", e.toString()+"=============selectTitleById===============");
    		}
    	}	
    	return title;
    }
    //===========================================
    public static ArrayList<String> getContent(Context ff,Date date)  //日视图查询
	{		
		 String sql=null;
		 SQLiteDatabase sld=null;
		  ArrayList<String> al=new ArrayList<String>();
		  al.clear();
		sld=openDatabase(ff);
		  try{	
			   if(date.getHours()==0) //上午
			   {				   
				   String begin=Constant.DateToString(date); 	
				 				   
				   date.setHours(12);
				   date.setMinutes(0);
				   date.setSeconds(0);
		           String end=Constant.DateToString(date);
				   sql="select id ,title from eventtable where id  in(select eventid from timetable where alerttime in (select alerttime from alerttable where nowtime>='"+begin+"'and nowtime <='"+end+"'))";
				   Cursor cur=sld.rawQuery(sql, null);
	                int i=0;  			       
	        	   while(cur.moveToNext())	        		 
		        	{   	   		        				        			
		        		al.add(cur.getInt(0)+"#"+cur.getString(1));		        				        		 		        		               
		                Log.d("ddddddddddshang wu", al.get(i)); i++;
		        	 }		
	        	  cur.close();
			 }else if(date.getHours()==12) // 下午
			 {
		 
				 
				 String begin=Constant.DateToString(date); 
			
				   date.setHours(19);
				   date.setMinutes(0);
				   date.setSeconds(0);
		           String end=Constant.DateToString(date);				          			
				   sql="select id ,title from eventtable where id in(select eventid from timetable where alerttime in (select alerttime from alerttable where nowtime>='"+begin+"'and nowtime <='"+end+"'))";
				   Cursor cur=sld.rawQuery(sql, null);
	              			       
	           			    int i=0;   
		        	while(cur.moveToNext())	        		
			        	{   	   		        					        			
			        		al.add(cur.getInt(0)+"#"+cur.getString(1));		
			        		Log.d("ddddddddddxia wu", al.get(i)); i++;
		        	 }
		        cur.close();
			 }
			  else if(date.getHours()==19) //说明是晚上。
			 {
				   
				   String begin=Constant.DateToString(date); 				  
				   date.setHours(23);
				   date.setMinutes(59);
				   date.setSeconds(59);
		           String end=Constant.DateToString(date);				       			
				   sql="select id ,title from eventtable where id in (select eventid from timetable where alerttime in (select alerttime from alerttable where nowtime>='"+begin+"'and nowtime <='"+end+"'))";
				   Cursor cur=sld.rawQuery(sql, null);
	               			        
		        	while(cur.moveToNext())	        		
			        	{   	   		        					        			
		        		   al.add(cur.getInt(0)+"#"+cur.getString(1));	
		        		   Log.d("dddddwanshang", "dddd");
			        	 }  
		        	cur.close();
			 }			   
			}catch(Exception e)
			{
				e.printStackTrace(); 
			}
	    	finally
	    	{	    
	    		
	    		try{closeDatabase(sld,ff);}catch(Exception e){e.printStackTrace();}
	    	}
		return al;
	}	

	public static ArrayList<String> getweekContent(Context ff,Date date) //周视图查询
	{
		SQLiteDatabase sld=null;
		 String sql=null;
		  ArrayList<String> al=new ArrayList<String>();
		  al.clear();
		  sld=openDatabase(ff);
		  try{							 				   
				   String begin=Constant.DateToString(date); 			   
				   date.setHours(23);
				   date.setMinutes(59);
				   date.setSeconds(59);
		           String end=Constant.DateToString(date);
				   sql="select id ,title from eventtable where id  in(select eventid from timetable where alerttime in (select alerttime from alerttable where nowtime>='"+begin+"'and nowtime <='"+end+"'))";
				   Cursor cur=sld.rawQuery(sql, null);
	       		       
	        	   while(cur.moveToNext())	        		
		        	{   	   		        				        			
		        		al.add(cur.getInt(0)+"#"+cur.getString(1));		        				        		 		        		                              
		        	 }		
	        	  cur.close();
			   }catch(Exception e)
			{
				e.printStackTrace(); 
			}
	    	finally
	    	{	    	    		
	    		try{closeDatabase(sld,ff);}catch(Exception e){e.printStackTrace();}
	    	}
		    return al;		
	}	
	public static ArrayList<String> gettype(Context ff) // 获取日程类型、
	{
		SQLiteDatabase sld=null;
		 String sql=null;
		  ArrayList<String> al=new ArrayList<String>();
		  al.clear();
		  sld=openDatabase(ff);
		  try{						 			
				   sql="select id ,type from typetable";
				   Cursor cur=sld.rawQuery(sql, null); 	
				   al.add((-1)+"#全部");
	        	   while(cur.moveToNext())	        		
		        	{   	   		        				        			
		        		al.add(cur.getInt(0)+"#"+cur.getString(1));		        				        		 		        		               		            
		        	 }		
	        	  cur.close();
		}catch(Exception e)
			{
				e.printStackTrace(); 
			}
	    	finally
	    	{	    	    		
	    		try{closeDatabase(sld,ff);}catch(Exception e){e.printStackTrace();}
	    	}
		    return al; 	  
	}
	public static ArrayList<String> gettypeContent(Context ff,int typeid) //获取相应类型的内容。
	{
		SQLiteDatabase sld=null;
		sld=openDatabase(ff);
		 String sql=null;
		  ArrayList<String> al=new ArrayList<String>();
		  al.clear();
		  try{					
	
		   if(typeid==(-1))
			  {
					  sql="select id ,title from eventtable";
					   Cursor cur=sld.rawQuery(sql, null); 			       
		        	   while(cur.moveToNext())	        		
			        	{   	   		        				        			
			        		al.add(cur.getInt(0)+"#"+cur.getString(1));		        				        		 		        		               		            
			        	}		
		        	  cur.close();
			  }else
			  {
				  sql="select id ,title from eventtable where type=(select id from typetable where id='"+typeid+"')" ;
				   Cursor cur=sld.rawQuery(sql, null); 			       
	        	   while(cur.moveToNext())	        		
		        	{   	   		        				        			
		        		al.add(cur.getInt(0)+"#"+cur.getString(1));		        				        		 		        		               		            
		        	 }		
	        	  cur.close();
			  }	   		   
 	}catch(Exception e)
			{
				e.printStackTrace(); 
			}
	    	finally
	    	{	    	    		
	    		try{closeDatabase(sld,ff);}catch(Exception e){e.printStackTrace();}
	    	}
		    return al; 	  
	}
	public static ArrayList<String> searchContent(Context fa,String edit) //通过输入文本框中的内容进行查询相关的信息
	{
		SQLiteDatabase sld=null;
		sld=openDatabase(fa);
		 ArrayList<String> al=new ArrayList<String>();
		 String sql=null;
		 al.clear();
    	try 	   	
    	{       
    		sql="select id ,title from eventtable where title like '%"+edit+"%'";  
    		Cursor cur=sld.rawQuery(sql, null);   
    		while(cur.moveToNext())	        		
        	{   	   		        				        			
        		 al.add(cur.getInt(0)+"#"+cur.getString(1));		        				        		 		        		               		            
        	}	
    		cur.close();
    	}
		catch(Exception e)
		{
			Toast.makeText(fa, "数据库错误 delete："+e.toString(), Toast.LENGTH_LONG).show();;
		}finally
    	{    		
    		try{closeDatabase(sld,fa);}catch(Exception e){e.printStackTrace();}
    	}
		return al;
	}	
	//==========================================================
	//===从数据库中读取eventid,使得传进来的id能有一个值，并且返回==========
    @SuppressWarnings("unused")
	public  int geteventID(Context context,int id){
    	SQLiteDatabase sld=null;
    	Cursor cursor=null;
    	try{
    		sld=openDatabase(context);
    		
    		String sql ="select max(id) from eventtable ";
    		cursor=sld.query("eventtable", null, null, null, null, null, "id");//执行语句得到结果集
    	    if (cursor.moveToLast())
    	    {   
    	    	id=cursor.getInt(0)+1; 
    	    }
    		//Toast.makeText(context, "id:"+id, Toast.LENGTH_LONG).show();
    		cursor.close();
    	}catch(Exception e){
    		Toast.makeText(context, "ID赋值失败"+e.toString(), Toast.LENGTH_LONG).show();
    	}
    	closeDatabase(sld,context);
		return id;
    	
    }	//==========.==从数据库中读取日程的类型=================
	public static void getstype(ScheduleManagerActivity smac){
		SQLiteDatabase sld=null;
		String sql;
		try
		{
			sld=openDatabase(smac);
			Cursor cursor=sld.query("typetable", null, null, null, null, null, "id");
			int count=cursor.getCount();
			if(count==0)//如果是第一次运行程序，自动创建3个缺省类型
			{
				for(int i=0;i<smac.defultType.length;i++)
				{
				 	sql="insert into typetable values("+i+",'"+smac.defultType[i]+"')";
					sld.execSQL(sql);
				}
				
				cursor=sld.query("typetable", null, null, null, null, null, "id");
				count=cursor.getCount();
			}
			smac.alType.clear();
			while(cursor.moveToNext())
			{
				smac.alType.add(cursor.getString(1));
			}
			sld.close();
			cursor.close();
		}catch(Exception e)
		{
			Toast.makeText(smac, "类型数据库打开创建错误："+e.toString(), Toast.LENGTH_LONG).show();
		}
	}
	//========================添加新的类型===============================
	public static boolean insertType(ScheduleManagerActivity smac,String newtype)//更新类型数据库
	{
		SQLiteDatabase sld=null;
		Cursor cursor=null;
		boolean cf=false;//false代表没有类型名称重复，true代表有重复
		try
		{
			sld=openDatabase(smac);
			cursor=sld.query("typetable", null, null, null, null, null, "id");
			smac.alType.clear();
			while(cursor.moveToNext())//存入新日程时，与数据库中已有的日程进行比较，如果重复，则标志位设为true
			{
				if(newtype.equals(cursor.getString(1)))
				{
					cf=true;
				}
				smac.alType.add(cursor.getString(1));
			}
			if(!cf)
			{ 
				smac.alType.add(newtype);
				String sql="delete from typetable";
				sld.execSQL(sql);
				for(int i=0;i<smac.alType.size();i++)
				{
					sql="insert into typetable values("+i+",'"+smac.alType.get(i)+"')";
					sld.execSQL(sql);
				}
			//	Toast.makeText(smac, "成功添加类型“"+newtype+"”。", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(smac, "类型名称重复！", Toast.LENGTH_LONG).show();
			}
		}
		catch(Exception e)
		{
			Toast.makeText(smac, "类型数据库更新错误："+e.toString(), Toast.LENGTH_LONG).show();
			return false;
		}
		finally
		{
			cursor.close();
			sld.close();
		}
		return true;
	}
	//================================删除类型============================
	public static void deleteType(ScheduleManagerActivity smac,String type)
	{    SQLiteDatabase sld=null;
	     Cursor cursor=null;
	     int tempid=0;
	if (type.equals("学习")||type.equals("工作")||type.equals("生活")){
		 Toast.makeText(smac, "禁止删除系统类型", Toast.LENGTH_LONG).show();
		 return;
		
    }else{
    	try
		{
			sld=SQLiteDatabase.openDatabase
				(
						"/data/data/com.bn.sn/myDb", 
					null, 
					SQLiteDatabase.OPEN_READWRITE				
				);
			//根据传过来的类型查找其id
			String sql="select id from typetable where type='"+type+"'";
			cursor=sld.rawQuery(sql, null);
			if (cursor.moveToNext()){
		    	 tempid = cursor.getInt(0);
		     }		
			sql="select id from eventtable where type="+tempid;
			cursor=sld.rawQuery(sql, null);
		    if (cursor.moveToNext()){//判断是否为空，不为空就跳出不删除该类型
		    	 Toast.makeText(smac, "有此类型日程,删除失败!", Toast.LENGTH_LONG).show();
		    	 return;
		     }
			sql="delete from typetable where type='"+type+"'";
			sld.execSQL(sql);
			Toast.makeText(smac, "成功删除类型", Toast.LENGTH_SHORT).show();		
		}
		catch(Exception e)
		{
			Toast.makeText(smac, "类型删除错误："+e.toString(), Toast.LENGTH_LONG).show();
		}
  	   finally
		{
			sld.close();
		}
    }
		
		
	}
	//=========================添加新的日程Start ================
	//========向eventtable添加
	public  boolean insetschedule(ScheduleManagerActivity smac ,int id,String title,
		int content ,int type,String person, String place,String alertstyle){
		Cursor cursor=null;
		
		SQLiteDatabase sld=null;
		try
		{
		  sld=openDatabase(smac);
		  
		   cursor=sld.query("eventtable", null, null, null, null, null, "id");
		String  sql="insert into eventtable(id,title,content,type,person,place,alertstyle) values("+id+",'"+title+"',"+content+","+type+",'"+person+"','"+place+"','"+alertstyle+"')";
			sld.execSQL(sql);   
		    sld.close();
			cursor.close();
			
			Toast.makeText(smac, "新日程添加成功", Toast.LENGTH_LONG).show();
		
		}catch(Exception e){
			Toast.makeText(smac, "日程数据库更新错误："+e.toString(), Toast.LENGTH_LONG).show();
			return false;
		}
		
		return false;
		
	}
  
    //==============向alerttable添加内容=========
    public  boolean insertalert (ScheduleManagerActivity smac,int alerttime,String nowtime,String alertone,String alerttwo,String alertthree,String alertfour,String alertfive,String alertsix,String alertseven){
    	SQLiteDatabase sld=null;
    	
    	//Cursor  cursor=null;
		try
		{
		  sld=openDatabase(smac);
		  
		//   cursor=sld.query("alerttable", null, null, null, null, null, "alerttime");
		   String sql="insert into alerttable values("+alerttime+",'"+nowtime+"','"+alertone+"','"+alerttwo+"','"+alertthree+"','"+alertfour+"','"+alertfive+"','"+alertsix+"','"+alertseven+"')";
			sld.execSQL(sql);
			sld.close();
			//cursor.close();
		//	Toast.makeText(smac, "AlertTable添加成功"+alerttime+nowtime+alertone+alerttwo+alertthree+alertfour+alertfive+alertsix+alertseven, Toast.LENGTH_LONG).show();
		
		}catch(Exception e){
			Toast.makeText(smac, "AlertTable数据库更新错误："+e.toString(), Toast.LENGTH_LONG).show();
			return false;
		}
    	
    	return false;
    }
	public int gettypeID(Context fa,String  selectedtype) {
		SQLiteDatabase sld=null;
		Cursor cursor=null;
		int typeid=0;
		try{
			sld=openDatabase(fa);
			
			String sql="select id,type from typetable";
			cursor=sld.rawQuery(sql, null);
			
			if (cursor.moveToLast()){
				typeid=cursor.getInt(0);
			}
			cursor.close();
			closeDatabase(sld,fa);
			
			
		}catch(Exception e){
			Toast.makeText(fa, "读取失败"+e.toString(), Toast.LENGTH_LONG).show();
		
		}
		
	   return typeid;
		
		
	}
	//像内容表中添加内容
	public  void insertcontent(Context applicationContext, int id,
			String text, String picpath, String soundpath) {
		SQLiteDatabase sld=null;
		try {
			sld=openDatabase(applicationContext);
			String sql="insert into  contenttable(id,text,picpath,soundpath) values ("+id+",'"+text+"','"+picpath+"','"+soundpath+"')";
			
			sld.execSQL(sql);
			closeDatabase(sld,applicationContext);
			
		}catch(Exception e){
			Toast.makeText(applicationContext, "添加失败"+e.toString(), Toast.LENGTH_LONG).show();
		}
	}
//====================获取内容	
	//根据ID查询内容表的内容
	public ArrayList<String> getcontent(Context context,int inid) {
		
		ArrayList<String> contentarray=new ArrayList<String>();
		SQLiteDatabase sld=null;
		Cursor cursor=null;
		contentarray.clear();
		try{
			/*String  drop="drop table contenttable";
			sld.execSQL(drop);*/
			sld=openDatabase(context);
			String sql="select id,text,picpath,soundpath from contenttable where id="+inid;
			cursor=sld.rawQuery(sql, null);
			if(cursor.moveToNext()){
				contentarray.add(cursor.getInt(0)+"#"+cursor.getString(1)+"#"+cursor.getString(2)+"#"+cursor.getString(3));
				
			}
			cursor.close();
			closeDatabase(sld,context);
		}
		catch(Exception e){
			Toast.makeText(context, "内容读取失败"+e.toString(), Toast.LENGTH_LONG).show();
		}
		
		return contentarray;
	}
	public ArrayList<String> getevent(Context applicationContext, int inid) {
		ArrayList<String>  eventarray=new ArrayList<String>();
		SQLiteDatabase sld=null;
		Cursor cursor=null;
		eventarray.clear();
		 try{
			 sld=openDatabase(applicationContext);
			 String sql="select id,title,content,type,person,place,alertstyle from eventtable where id="+inid;
			 
			 cursor=sld.rawQuery(sql, null);
			 if (cursor.moveToNext()){
				 eventarray.add(cursor.getInt(0)+"#"+cursor.getString(1)+"#"+cursor.getInt(2)+"#"+cursor.getInt(3)+"#"+cursor.getString(4)+"#"+cursor.getString(5)+"#"+cursor.getString(6));
			}
			 cursor.close();
			 closeDatabase(sld,applicationContext);

		 }catch(Exception e){
			 Toast.makeText(applicationContext, "event读取失败"+e.toString(), Toast.LENGTH_LONG).show();
		 }
		
		return eventarray;
	}
	//根据id获取类型
	public ArrayList<String> gettype(Context applicationContext, int typeid) {
		ArrayList<String>  typearray=new ArrayList<String>();
		SQLiteDatabase sld=null;
		Cursor cursor=null;
		typearray.clear();// TODO Auto-generated method stub
		try{
			sld=openDatabase(applicationContext);
			String sql="select id,type from typetable where id="+typeid;
			cursor=sld.rawQuery(sql, null);
			if (cursor.moveToNext()){
				typearray.add(cursor.getInt(0)+"#"+cursor.getString(1));
			}
			 cursor.close();
			 closeDatabase(sld,applicationContext);
			
		}catch(Exception e){
			Toast.makeText(applicationContext, "type读取失败"+e.toString(), Toast.LENGTH_LONG).show();
		}
		return typearray;
	}
	public ArrayList<String> gettimes(Context applicationContext, int eventid) {
		ArrayList<String>  timesarray=new ArrayList<String>();
		SQLiteDatabase sld=null;
		Cursor cursor=null;
		timesarray.clear();
		try{ 
			sld=openDatabase(applicationContext);
			String sql="select alerttime,nowtime,alertone,alerttwo,alertthree,alertfour,alertfive,alertsix,alertseven from alerttable where alerttime="+eventid;
			cursor=sld.rawQuery(sql, null);
			if (cursor.moveToLast()){
				timesarray.add(cursor.getInt(0)+"#"+cursor.getString(1)+"#"+cursor.getString(2)+"#"+cursor.getString(3)+"#"+cursor.getString(4)+"#"+cursor.getString(5)+"#"+cursor.getString(6)+"#"+cursor.getString(7)+"#"+cursor.getString(8));
			}
			 cursor.close();
			 closeDatabase(sld,applicationContext);
			
		}catch(Exception e){
			Toast.makeText(applicationContext, "alerttime读取失败"+e.toString(), Toast.LENGTH_LONG).show();
		}
		
		
		return timesarray;
	}
	//更新相关的数据
	public void updatealert(Context applicationContext,int eventid, String nowtime, String alerone, String alerttwo,
			String alertthree,String alertfour, String alertfive, String alertsix, String aldertseven
			) {
		    SQLiteDatabase sld=null;
		try{
			sld=openDatabase(applicationContext);
			String sql="update  alerttable set nowtime='"+nowtime+"',alertone='"+alerone+"',alerttwo='"+alerttwo+"',alertthree='"+alertthree+"',alertfour='"+alertfour+"',alertfive='"+alertfive+"',alertsix='"+alertsix+"',alertseven='"+aldertseven+"'  where alerttime="+eventid;
			sld.execSQL(sql);
			closeDatabase(sld,applicationContext);
			//Toast.makeText(applicationContext, "alerttable更新成功", Toast.LENGTH_LONG).show();
		}catch(Exception e){
			Toast.makeText(applicationContext, "alerttable更新失败"+e.toString(), Toast.LENGTH_LONG).show();
		}
		
		
	}
	public void updateschedule(Context applicationContext,
			int eventid, String tilte, int contentid, int typeid, String person,
			String place, String alertstyle) {
		  SQLiteDatabase sld=null;
		try{
			sld=openDatabase(applicationContext);
			String sql="update eventtable set title='"+tilte+"',content="+contentid+",type="+typeid+",person='"+person+"',place='"+place+"',alertstyle='"+alertstyle+"' where id="+eventid;
		    sld.execSQL(sql);
		    closeDatabase(sld,applicationContext);
		    //Toast.makeText(applicationContext, "eventtable更新成功", Toast.LENGTH_LONG).show();
		}catch(Exception e){
			Toast.makeText(applicationContext, "eventtable更新失败"+e.toString(), Toast.LENGTH_LONG).show();
		}
		
	}
	public void updatecontent(Context applicationContext, int eventid,
			String text, String picpath, String soundpath) {
		SQLiteDatabase sld=null;
		try{
			sld=openDatabase(applicationContext);
			String sql="update contenttable set text='"+text+"',picpath='"+picpath+"',soundpath='"+soundpath+"' where id="+eventid;
			sld.execSQL(sql);
			closeDatabase(sld,applicationContext);
			//Toast.makeText(applicationContext, "contenttable更新成功", Toast.LENGTH_LONG).show();
		}catch(Exception e){
			Toast.makeText(applicationContext, "contenttable更新失败"+e.toString(), Toast.LENGTH_LONG).show();
		}
		
	}
	public void inserttimetable(Context applicationContext, int id, int id2) {
		SQLiteDatabase sld=null;
		Cursor cursor=null;
		int tempid=0;
		try{
			 sld=openDatabase(applicationContext);
			 cursor=sld.query("timetable", null, null, null, null, null, "id");//执行语句得到结果集
	    	    if (cursor.moveToLast())
	    	    {   
	    	    	tempid=cursor.getInt(0)+1; 
	    	    }
			String sql="insert into timetable  values ( "+tempid+","+id+","+id2+")";
			sld.execSQL(sql);
			cursor.close();
			closeDatabase(sld,applicationContext);
			
		}catch(Exception e){
			Toast.makeText(applicationContext, "contenttable更新失败"+e.toString(), Toast.LENGTH_LONG).show();
		}
		
		}
		
	}
    
