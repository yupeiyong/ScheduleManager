package com.bn.sn;
import java.util.Calendar;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class BnScheduleManagerView  extends View
{  
	static Calendar cd;
	//第一次获得日历的标志值
	BnScheduleViewListener actionListener;
	private Paint paint;
	int [] imageIDs=
	{
			R.drawable.show0,R.drawable.show1,
			R.drawable.show2,R.drawable.show3,
			R.drawable.show4,R.drawable.show5,
			R.drawable.show6,R.drawable.show7,
			R.drawable.show8,R.drawable.show9,
			R.drawable.show10
	};
	int [] imageIDs1=
	{
			R.drawable.zhou07,R.drawable.zhou01,R.drawable.zhou02,R.drawable.zhou03,R.drawable.zhou04,R.drawable.zhou05,R.drawable.zhou06
	};
	public void addActionListener(BnScheduleViewListener actionListener) // 注册监听。
	 {
	    	this.actionListener=actionListener;
	 }
	public BnScheduleManagerView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	public BnScheduleManagerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		paint=new Paint();
		cd=Calendar.getInstance() ;	
		//this.invalidate();
	}
     public  void setTime(int currYear,int currMonths,int currDays)   /////写日期弄。
    {    	  
    	
    	   Constant.currDay=currDays;
    	   Constant.currMonth=currMonths;
    	   Constant.currYear=currYear;   
    	   Constant.flag2=false;
    	   Constant.flag=false;
    	   cd.set(Constant.currYear,Constant.currMonth,Constant.currDay);    	 
    	   actionListener.doAction(cd);   	    	   
    	  this.invalidate();
   }
	public boolean onTouchEvent(MotionEvent event)   // 触发各种事件。
	{	 		
		if(event.getAction() == MotionEvent.ACTION_UP)
		{  
			 Constant.Scheduleprev_x=(int) event.getX();
			 Constant.Scheduleprev_y=(int) event.getY();
			 if(Constant.Scheduleprev_x>Constant.leftpadding*17&&Constant.Scheduleprev_x<(Constant.leftpadding*21)) //x  90-110  给左箭头添加监听
			 {
				 if(Constant.Scheduleprev_y>Constant.toppadding*3&&Constant.Scheduleprev_y<(Constant.toppadding*8))//y 15-30	没有限制年份。				 
				 {									 
					
					if((cd.get(Calendar.DAY_OF_MONTH)-1)>0)// 获得天数。
					{
					  Constant.currYear=cd.get(Calendar.YEAR);
					  Constant.currMonth=cd.get(Calendar.MONTH);
					  Constant.currDay=cd.get(Calendar.DAY_OF_MONTH)-1;							   
					  Constant.flag=false;
					  Constant.flag2=false;	
					 cd.set(Constant.currYear,Constant.currMonth, Constant.currDay);
					 actionListener.doAction(cd);
					 this.invalidate();					
					}else if((cd.get(Calendar.DAY_OF_MONTH)-1)==0)//切换月份
					{	
						if((cd.get(Calendar.MONTH)-1)==-1) //如果为1月1号。则更换为上一年。
						{     
							  Constant.currYear=cd.get(Calendar.YEAR)-1;
							  Constant.currMonth=11;
							  Constant.currDay=31;						  
						}else
						{
							Constant.currYear=cd.get(Calendar.YEAR);
							Constant.currMonth=cd.get(Calendar.MONTH)-1;						
							int getMonthDays=getMonthDays(Constant.currYear,Constant.currMonth);						
							Constant.currDay=getMonthDays;							
						}
						  Constant.flag=false;
						  Constant.flag2=false;
						  cd.set(Constant.currYear,Constant.currMonth, Constant.currDay);
							 actionListener.doAction(cd);
						  this.invalidate();
					}
				 }				 
			 }					 
			 if(Constant.Scheduleprev_x>Constant.leftpadding*42&&Constant.Scheduleprev_x<(Constant.leftpadding*46))//给右箭头添加监听
			 {
				 if(Constant.Scheduleprev_y>Constant.toppadding*3&&Constant.Scheduleprev_y<(Constant.toppadding*8))
				 {
					 cd.get(Calendar.YEAR);
					 cd.get(Calendar.MONTH);
					 cd.get(Calendar.DAY_OF_MONTH);
					 int getMonthDays=getMonthDays(cd.get(Calendar.YEAR),cd.get(Calendar.MONTH));
					 if(cd.get(Calendar.DAY_OF_MONTH)<getMonthDays)//换天
					 {
						 Constant.currYear=cd.get(Calendar.YEAR);
						 Constant.currMonth=cd.get(Calendar.MONTH);
						 Constant.currDay=cd.get(Calendar.DAY_OF_MONTH)+1;	
						 Constant.flag=false;
						  Constant.flag2=false;
						  cd.set(Constant.currYear,Constant.currMonth, Constant.currDay);
							 actionListener.doAction(cd);
						  this.invalidate();	//将日历作为参数传送过去。	
						  
					 }else if(cd.get(Calendar.DAY_OF_MONTH)==getMonthDays)
					 {
						 if(cd.get(Calendar.MONTH)==11) //换年
						 {
							  Constant.currYear=cd.get(Calendar.YEAR)+1;
							  Constant.currMonth=0;
							  Constant.currDay=1;							 
						 }else                          //换月份
						 {    
							    Constant.currYear=cd.get(Calendar.YEAR);
								Constant.currMonth=cd.get(Calendar.MONTH)+1;												
								Constant.currDay=1;							 
						 }
						  Constant.flag=false;
						  Constant.flag2=false;
						  cd.set(Constant.currYear,Constant.currMonth, Constant.currDay);
							 actionListener.doAction(cd);
						  this.invalidate();
					 }
				 }				 
			 }			 
			 if(Constant.Scheduleprev_x>Constant.leftpadding*22&&Constant.Scheduleprev_x<Constant.leftpadding*40)
			 {
				 if(Constant.Scheduleprev_y>Constant.toppadding*3&&Constant.Scheduleprev_y<(Constant.toppadding*8))
				 {	Constant.flag2=true;			 
					  actionListener.doAction(cd);	//将日历作为参数传送过去。	
					  
				 }
			}			 		 
         }
	 return true; 		
	}	 
	public void onDraw(Canvas canvas)  //画图。
	{		
		//Bitmap calendar_bg=BitmapFactory.decodeResource(this.getResources(),R.drawable.calendar_bgg);
		//canvas.drawBitmap(calendar_bg,Constant.leftpadding ,Constant.toppadding , paint); 
		
		
		Bitmap prev=BitmapFactory.decodeResource(this.getResources(),R.drawable.prev);		
		canvas.drawBitmap(prev,Constant.leftpadding*15,Constant.toppadding*2 , paint);
		
				
		Bitmap next=BitmapFactory.decodeResource(this.getResources(),R.drawable.next);
		canvas.drawBitmap(next,Constant.leftpadding*39,Constant.toppadding*2 , paint); //以上为画背景图。	     
		
		//显示日历。		
		if(Constant.flag==true)//刚切换至此页面  第一次显示日期为今天的日期
		{		  	
		   cd.get(Calendar.MONTH);cd.get(Calendar.DAY_OF_MONTH); 				
		}else
		{
			
			cd.set(Constant.currYear,Constant.currMonth,Constant.currDay);
			
		}
		if((cd.get(Calendar.MONTH)+1)<10)  // 画月份
		{
		  Bitmap zero=BitmapFactory.decodeResource(this.getResources(),imageIDs[0]);
		  canvas.drawBitmap(zero,Constant.leftpadding*21+Constant.leftpadding/2,Constant.toppadding*2 , paint);	
		  Bitmap CalendarMONTH=BitmapFactory.decodeResource(this.getResources(),imageIDs[cd.get(Calendar.MONTH)+1]);
		  canvas.drawBitmap(CalendarMONTH,Constant.leftpadding*24,Constant.toppadding*2 , paint);// 显示日期坐标x为 Constant.leftpadding*23+Constant.leftpadding/2	  
		}else
		{
			Bitmap one=BitmapFactory.decodeResource(this.getResources(),imageIDs[1]);
			canvas.drawBitmap(one,Constant.leftpadding*21+Constant.leftpadding/2,Constant.toppadding*2 , paint);
			Bitmap CalendarMONTH=BitmapFactory.decodeResource(this.getResources(),imageIDs[cd.get(Calendar.MONTH)-9]);			
			canvas.drawBitmap(CalendarMONTH,Constant.leftpadding*24,Constant.toppadding*2 , paint);			
		}	
		Bitmap horiline1=BitmapFactory.decodeResource(this.getResources(),imageIDs[10]);       //画横线
		canvas.drawBitmap(horiline1,Constant.leftpadding*26,Constant.toppadding*2 , paint);
		if(cd.get(Calendar.DAY_OF_MONTH)<10) //画天
		{
		  Bitmap zero=BitmapFactory.decodeResource(this.getResources(),imageIDs[0]);
		  canvas.drawBitmap(zero,Constant.leftpadding*28-Constant.leftpadding/2,Constant.toppadding*2 , paint);	
		  Bitmap CalendarMONTH=BitmapFactory.decodeResource(this.getResources(),imageIDs[cd.get(Calendar.DAY_OF_MONTH)]);
		  canvas.drawBitmap(CalendarMONTH,Constant.leftpadding*30,Constant.toppadding*2 , paint);// 显示日期坐标x为 Constant.leftpadding*23+Constant.leftpadding/2	  
		}else
		{
		   if(cd.get(Calendar.DAY_OF_MONTH)>9&&cd.get(Calendar.DAY_OF_MONTH)<20) // 10多号	
		   { 
			  Bitmap one=BitmapFactory.decodeResource(this.getResources(),imageIDs[1]);
			  canvas.drawBitmap(one,Constant.leftpadding*28-Constant.leftpadding/2,Constant.toppadding*2 , paint);
			  Bitmap days=BitmapFactory.decodeResource(this.getResources(),imageIDs[cd.get(Calendar.DAY_OF_MONTH)-10]);
			  canvas.drawBitmap(days,Constant.leftpadding*30,Constant.toppadding*2 , paint);   
			  
		   }else if(cd.get(Calendar.DAY_OF_MONTH)>19&&cd.get(Calendar.DAY_OF_MONTH)<30)
		   {
			   Bitmap two=BitmapFactory.decodeResource(this.getResources(),imageIDs[2]);
			   canvas.drawBitmap(two,Constant.leftpadding*28-Constant.leftpadding/2,Constant.toppadding*2 , paint);
			   Bitmap days=BitmapFactory.decodeResource(this.getResources(),imageIDs[cd.get(Calendar.DAY_OF_MONTH)-20]);
			   canvas.drawBitmap(days,Constant.leftpadding*30,Constant.toppadding*2 , paint);    
		   }else
		   {
			   Bitmap three=BitmapFactory.decodeResource(this.getResources(),imageIDs[3]);
			   canvas.drawBitmap(three,Constant.leftpadding*28-Constant.leftpadding/2,Constant.toppadding*2 , paint);
			   Bitmap days=BitmapFactory.decodeResource(this.getResources(),imageIDs[cd.get(Calendar.DAY_OF_MONTH)-30]);
			   canvas.drawBitmap(days,Constant.leftpadding*30,Constant.toppadding*2 , paint);
			   
		   }
		   
		}		 
		Bitmap week=BitmapFactory.decodeResource(this.getResources(),imageIDs1[cd.get(Calendar.DAY_OF_WEEK)-1]); //画周几          		
		canvas.drawBitmap(week,Constant.leftpadding*33,Constant.toppadding*3+Constant.toppadding/2 , paint);
      
		
		
		
		
		
		
	}	 

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

}
