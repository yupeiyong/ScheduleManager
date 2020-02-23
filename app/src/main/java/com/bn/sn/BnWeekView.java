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

public class BnWeekView extends View    //画格子图。
{
	Calendar cd;
	//Calendar cd1;
	//第一次获得日历的标志值
	DateDialogListener  DialogListener;
	BnBeWeekViewListener actionListener;
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
	public void addActionListener(BnBeWeekViewListener actionListener) // 注册监听。
	 {
	    	this.actionListener=actionListener;
	 }
	 public void DateDialogListener(DateDialogListener actionListener) // 注册监听。
	 {
	    	this.DialogListener=actionListener;
	 }	
	public  void setTime(int currYear,int currMonths,int currDays)   //更新日期。
    {		   
           Constant.currDay1=currDays;
    	   Constant.currMonth1=currMonths;
    	   Constant.currYear1=currYear;  
    	   this.invalidate();
    	   Constant.flag1=false;
    	  actionListener.doAction(Constant.currYear1, Constant.currMonth1+1, Constant.currDay1);	    	   	      	   
    	  
   }	
    Paint paint;
	public BnWeekView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public BnWeekView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	public BnWeekView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	 paint=new Paint();
	 cd=Calendar.getInstance() ;
	// cd1=Calendar.getInstance() ;
	}     
	public boolean onTouchEvent(MotionEvent event)
	{	
		// 左箭头的点击事件。		
		if(event.getAction() == MotionEvent.ACTION_UP)
		{
			Constant.Scheduleprev_x=(int) event.getX();			
			Constant.Scheduleprev_y=(int) event.getY();
			
			if((Constant.Scheduleprev_x>Constant.leftpadding*13)&&(Constant.Scheduleprev_x<(Constant.leftpadding*18))) //x  90-110  给左箭头添加监听
			 {
				 if((Constant.Scheduleprev_y>Constant.toppadding*2)&&(Constant.Scheduleprev_y<(Constant.toppadding*9)))//y 10-45	没有限制年份。				 
				 {		
					   if((cd.get(Calendar.DAY_OF_MONTH)-7)>0)// 获得天数。
						{				
						  Constant.currYear1=cd.get(Calendar.YEAR);
						  Constant.currMonth1=cd.get(Calendar.MONTH);
						  Constant.currDay1=cd.get(Calendar.DAY_OF_MONTH)-7;							   
						  Constant.flag1=false;	
						 
						  actionListener.doAction(Constant.currYear1, Constant.currMonth1+1, Constant.currDay1);	
						  this.invalidate();
						}
					   else if((cd.get(Calendar.DAY_OF_MONTH)-7)<=0)//切换月份
						{	
							if((cd.get(Calendar.MONTH)-1)==-1) //如果为1月1号。则更换为上一年。
							{     
								  Constant.currYear1=cd.get(Calendar.YEAR)-1;
								  Constant.currMonth1=11;
								  Constant.currDay1=31+cd.get(Calendar.DAY_OF_MONTH)-7;
								
						  
							}else  //切换月份
							{
								Constant.currYear1=cd.get(Calendar.YEAR);
								Constant.currMonth1=cd.get(Calendar.MONTH)-1;						
								int getMonthDays=getMonthDays(Constant.currYear1,Constant.currMonth1);						
								Constant.currDay1=getMonthDays+cd.get(Calendar.DAY_OF_MONTH)-7;	
					
							}
								Constant.flag1=false;
								
								actionListener.doAction(Constant.currYear1, Constant.currMonth1+1, Constant.currDay1);	
								this.invalidate();
						}
					 }	 
		 }		
			if((Constant.Scheduleprev_x>Constant.leftpadding*44)&&(Constant.Scheduleprev_x<(Constant.leftpadding*49))) //  给右箭头添加监听
			 {
				 if((Constant.Scheduleprev_y>Constant.toppadding*2)&&(Constant.Scheduleprev_y<(Constant.toppadding*9)))				 
				 {	
					 int getMonthDays=getMonthDays(cd.get(Calendar.YEAR),cd.get(Calendar.MONTH));
					 if((cd.get(Calendar.DAY_OF_MONTH)+7)<=getMonthDays)//换天
					 {
						 Constant.currYear1=cd.get(Calendar.YEAR);
						 Constant.currMonth1=cd.get(Calendar.MONTH);
						 Constant.currDay1=cd.get(Calendar.DAY_OF_MONTH)+7;	
						 Constant.flag1=false;								 	
						 actionListener.doAction(Constant.currYear1, Constant.currMonth1+1, Constant.currDay1);	
						 this.invalidate();
					 }else if((cd.get(Calendar.DAY_OF_MONTH)+7)>getMonthDays)
					 {
						 if(cd.get(Calendar.MONTH)==11) //换年
						 {
							  Constant.currYear1=cd.get(Calendar.YEAR)+1;
							  Constant.currMonth1=0;
							  Constant.currDay1=cd.get(Calendar.DAY_OF_MONTH)+7-getMonthDays;							 
						 }else                          //换月份
						 {    
							    Constant.currYear1=cd.get(Calendar.YEAR);
								Constant.currMonth1=cd.get(Calendar.MONTH)+1;												
								Constant.currDay1=cd.get(Calendar.DAY_OF_MONTH)+7-getMonthDays;                            
						 }
								 Constant.flag1=false;
									
								 actionListener.doAction(Constant.currYear1, Constant.currMonth1+1, Constant.currDay1);	
								 this.invalidate();						 
					 }
			     }				 
			 }
	}
		if(event.getAction() == MotionEvent.ACTION_DOWN)// 首先计算点击在第几个格子。然后计算。当前日期在第几个格子。 相对计算点击的日期。
		{
			
			Constant.Scheduleprev_xdown=(int) event.getX();	 		
			Constant.Scheduleprev_ydown=(int) event.getY();
			if((Constant.Scheduleprev_xdown>Constant.leftpadding*20)&&(Constant.Scheduleprev_xdown<(Constant.leftpadding*43))) 
			 {
				 if((Constant.Scheduleprev_ydown>Constant.toppadding*4)&&(Constant.Scheduleprev_ydown<(Constant.toppadding*8)))				 
				 {
					
			    	DialogListener.dialogListener(cd);				    
				    int t=Constant.currMonth1+1;				   
				    if(Constant.flag1==true)
				    { 
				    	int c=cd.get(Calendar.MONTH)+1;
				        actionListener.doAction(cd.get(Calendar.YEAR),c,cd.get(Calendar.DAY_OF_MONTH));
				    }
				    else
				    {
				    	 actionListener.doAction(Constant.currYear1,t, Constant.currDay1);	
				    }
					 
				 }
			 }
			if((Constant.Scheduleprev_ydown>Constant.toppadding*12)&&(Constant.toppadding*22>Constant.Scheduleprev_ydown))
			{
				int m = 0;//计算点击的位置在第几个格子里面
			
		
				switch(Constant.Scheduleprev_xdown/(Constant.leftpadding*9+Constant.leftpadding/3))
			    {
				  case 0:m=1;
				         // 将 当前时间设置为点击的那个时间。
				  Constant.currYear1=Constant.weekYears[m];
				  Constant.currMonth1=Constant.weekMonths[m]-1;
				  Constant.currDay1=Constant.weekDays[m];
				  Constant.flag1=false;
				  this.invalidate();                                     
                  actionListener.doAction(Constant.weekYears[m], Constant.weekMonths[m], Constant.weekDays[m]);
               
				  break;				 				  
				  case 1:m=2;	
				  Constant.currYear1=Constant.weekYears[m];
				  Constant.currMonth1=Constant.weekMonths[m]-1;
				  Constant.currDay1=Constant.weekDays[m];
				  Constant.flag1=false;
				  this.invalidate(); 
				  actionListener.doAction(Constant.weekYears[m], Constant.weekMonths[m], Constant.weekDays[m]);				  
				  break;
				  case 2:m=3;
			
     			  actionListener.doAction(Constant.weekYears[m], Constant.weekMonths[m], Constant.weekDays[m]);
				 
				  break;
				  case 3:m=4;
				  Constant.currYear1=Constant.weekYears[m];
				  Constant.currMonth1=Constant.weekMonths[m]-1;
				  Constant.currDay1=Constant.weekDays[m];
				  Constant.flag1=false;
				  this.invalidate(); 
				  actionListener.doAction(Constant.weekYears[m], Constant.weekMonths[m], Constant.weekDays[m]);
				  
				  break;
				  case 4:m=5;
		
				  Constant.currYear1=Constant.weekYears[m];
				  Constant.currMonth1=Constant.weekMonths[m]-1;
				  Constant.currDay1=Constant.weekDays[m];
				  Constant.flag1=false;
				  this.invalidate();  
				  actionListener.doAction(Constant.weekYears[m], Constant.weekMonths[m], Constant.weekDays[m]);	
				 
				  break;
				  case 5:m=6;

				  Constant.currYear1=Constant.weekYears[m];
				  Constant.currMonth1=Constant.weekMonths[m]-1;
				  Constant.currDay1=Constant.weekDays[m];
				  Constant.flag1=false;
				  this.invalidate(); 
				  actionListener.doAction(Constant.weekYears[m], Constant.weekMonths[m], Constant.weekDays[m]);		
				  
				  break;
				  case 6:m=7;

					
				  Constant.currYear1=Constant.weekYears[m];
				  Constant.currMonth1=Constant.weekMonths[m]-1;
				  Constant.currDay1=Constant.weekDays[m];
				  Constant.flag1=false;
				  this.invalidate(); 
				  actionListener.doAction(Constant.weekYears[m], Constant.weekMonths[m], Constant.weekDays[m]);		
				  
				  break;				  
				}	
			}
		}			
		return true;
	}
	
	public void onDraw(Canvas canvas)
	{	
		
		//=======================画图 计算日期===================================	
		
		Bitmap prev=BitmapFactory.decodeResource(this.getResources(),R.drawable.prev);		
		canvas.drawBitmap(prev,Constant.leftpadding*13,Constant.toppadding*2 , paint);			
		Bitmap next=BitmapFactory.decodeResource(this.getResources(),R.drawable.next);
		canvas.drawBitmap(next,Constant.leftpadding*42,Constant.toppadding*2 , paint); //以上为画背景图。		
		if(Constant.flag1==true)//刚切换至此页面  第一次显示日期为今天的日期
		{		  	
		    cd.get(Calendar.YEAR);cd.get(Calendar.WEEK_OF_YEAR); 
		   
		}else
		{			
			 cd.set(Constant.currYear1,Constant.currMonth1,Constant.currDay1);	
		}  
		
		
		// 画年份。			
		Bitmap th=BitmapFactory.decodeResource(this.getResources(),imageIDs[cd.get(Calendar.YEAR)/1000]);
		canvas.drawBitmap(th,Constant.leftpadding*19+Constant.leftpadding/2,Constant.toppadding*2 , paint);	
		int bai=cd.get(Calendar.YEAR)-cd.get(Calendar.YEAR)/1000*1000;
		Bitmap ha=BitmapFactory.decodeResource(this.getResources(),imageIDs[bai/100]);			
		canvas.drawBitmap(ha,Constant.leftpadding*21+Constant.leftpadding/2,Constant.toppadding*2 , paint);		
		int shi=bai-bai/100*100;
		Bitmap ten=BitmapFactory.decodeResource(this.getResources(),imageIDs[shi/10]);       //画横线
		canvas.drawBitmap(ten,Constant.leftpadding*23+Constant.leftpadding/2,Constant.toppadding*2 , paint);
		
		int ge=shi-shi/10*10;
		
		Bitmap gee=BitmapFactory.decodeResource(this.getResources(),imageIDs[ge]);
		canvas.drawBitmap(gee,Constant.leftpadding*25+Constant.leftpadding/2,Constant.toppadding*2 , paint);	
		
        //画第 
		Bitmap di=BitmapFactory.decodeResource(this.getResources(),R.drawable.di);
		canvas.drawBitmap(di,Constant.leftpadding*28+Constant.leftpadding/2,Constant.toppadding*2 , paint);		
		
		 int i=cd.get(Calendar.WEEK_OF_YEAR);
		 if(cd.get(Calendar.DAY_OF_WEEK)==1)
		 {
			 i=i-1; //把周日设置成上一个周。
		 }	 
	     Bitmap jizhou=BitmapFactory.decodeResource(this.getResources(),imageIDs[i/10]);
	     canvas.drawBitmap(jizhou,Constant.leftpadding*34+Constant.leftpadding/2,Constant.toppadding*2 ,paint);
	     Bitmap jiji=BitmapFactory.decodeResource(this.getResources(),imageIDs[(i-i/10*10)]);
	     canvas.drawBitmap(jiji,Constant.leftpadding*36+Constant.leftpadding/2+Constant.leftpadding/2,Constant.toppadding*2 ,paint);
		//画周
	     Bitmap zhou=BitmapFactory.decodeResource(this.getResources(),R.drawable.zhou);
	     canvas.drawBitmap(zhou,Constant.leftpadding*37+Constant.leftpadding/2,Constant.toppadding*2 ,paint);		
	
		paint.setColor(0x99009999);
		canvas.drawLine(0,Constant.toppadding*15,Constant.leftpadding*96,Constant.toppadding*15,paint);
	    canvas.drawLine(0,Constant.toppadding*25,Constant.leftpadding*96,Constant.toppadding*25,paint);
		
	    
	    for(int ii=1;ii<7;ii++)
		{
		    canvas.drawLine((Constant.leftpadding*9+Constant.leftpadding/3)*ii,Constant.toppadding*15,(Constant.leftpadding*9+Constant.leftpadding/3)*ii,Constant.toppadding*25,paint); 
		}			
		
		    paint.setColor(0xFFFFFFFF);
		    Bitmap zhouyi=BitmapFactory.decodeResource(this.getResources(),R.drawable.zhou011);
		    canvas.drawBitmap(zhouyi,Constant.leftpadding*2,Constant.toppadding*15 ,paint);		//	10+9*5i					            
			
		    
		    Bitmap zhouer=BitmapFactory.decodeResource(this.getResources(),R.drawable.zhou022);
			canvas.drawBitmap(zhouer,Constant.leftpadding*11,Constant.toppadding*15 ,paint); 			
		   
			Bitmap zhousan=BitmapFactory.decodeResource(this.getResources(),R.drawable.zhou033);
            canvas.drawBitmap(zhousan,Constant.leftpadding*20,Constant.toppadding*15 ,paint);
     
            Bitmap zhousi=BitmapFactory.decodeResource(this.getResources(),R.drawable.zhou044);
            canvas.drawBitmap(zhousi,Constant.leftpadding*29,Constant.toppadding*15 ,paint);
		
            Bitmap zhouwu=BitmapFactory.decodeResource(this.getResources(),R.drawable.zhou055);
            canvas.drawBitmap(zhouwu,Constant.leftpadding*38,Constant.toppadding*15 ,paint);
		
            Bitmap zhouliu=BitmapFactory.decodeResource(this.getResources(),R.drawable.zhou066);
            canvas.drawBitmap(zhouliu,Constant.leftpadding*47,Constant.toppadding*15 ,paint);
		
		
            Bitmap zhouri=BitmapFactory.decodeResource(this.getResources(),R.drawable.zhou077);
            canvas.drawBitmap(zhouri,Constant.leftpadding*56,Constant.toppadding*15 ,paint);
		    
            //================
         if(cd.get(Calendar.DAY_OF_WEEK)!=1){
            	int t=cd.get(Calendar.MONTH)+1;
            	Constant.weekday[(Constant.leftpadding*4+9*Constant.leftpadding*(cd.get(Calendar.DAY_OF_WEEK)-2))/(Constant.leftpadding*9+Constant.leftpadding/3)+1]=cd.get(Calendar.YEAR)+" "+t+" "+cd.get(Calendar.DAY_OF_MONTH)+" ";// 将当前日期存在对应的格子里面。           	
                
            	
                
                Constant.weekYears[(Constant.leftpadding*4+9*Constant.leftpadding*(cd.get(Calendar.DAY_OF_WEEK)-2))/(Constant.leftpadding*9+Constant.leftpadding/3)+1]=cd.get(Calendar.YEAR);
                Constant.weekMonths[(Constant.leftpadding*4+9*Constant.leftpadding*(cd.get(Calendar.DAY_OF_WEEK)-2))/(Constant.leftpadding*9+Constant.leftpadding/3)+1]=t;
                Constant.weekDays[(Constant.leftpadding*4+9*Constant.leftpadding*(cd.get(Calendar.DAY_OF_WEEK)-2))/(Constant.leftpadding*9+Constant.leftpadding/3)+1]=cd.get(Calendar.DAY_OF_MONTH);
                
                
                
                
                
                
                
                
            	if(cd.get(Calendar.DAY_OF_MONTH)<10) //画天
				{
				  Bitmap zero=BitmapFactory.decodeResource(this.getResources(),imageIDs[0]);
				  canvas.drawBitmap(zero,Constant.leftpadding*2+9*Constant.leftpadding*(cd.get(Calendar.DAY_OF_WEEK)-2),Constant.toppadding*18 , paint);	
				  Bitmap CalendarMONTH=BitmapFactory.decodeResource(this.getResources(),imageIDs[cd.get(Calendar.DAY_OF_MONTH)]);
				  canvas.drawBitmap(CalendarMONTH,Constant.leftpadding*4+9*Constant.leftpadding*(cd.get(Calendar.DAY_OF_WEEK)-2),Constant.toppadding*18 , paint);// 显示日期坐标x为 Constant.leftpadding*23+Constant.leftpadding/2	  
				}else
				{
		         if(cd.get(Calendar.DAY_OF_MONTH)>9&&cd.get(Calendar.DAY_OF_MONTH)<20) // 10多号	
			   { 
				  Bitmap one=BitmapFactory.decodeResource(this.getResources(),imageIDs[1]);
				  canvas.drawBitmap(one,Constant.leftpadding*2+9*Constant.leftpadding*(cd.get(Calendar.DAY_OF_WEEK)-2),Constant.toppadding*18 , paint);
				  Bitmap days=BitmapFactory.decodeResource(this.getResources(),imageIDs[cd.get(Calendar.DAY_OF_MONTH)-10]);
				  canvas.drawBitmap(days,Constant.leftpadding*4+9*Constant.leftpadding*(cd.get(Calendar.DAY_OF_WEEK)-2),Constant.toppadding*18, paint);   				  
			   }else if(cd.get(Calendar.DAY_OF_MONTH)>19&&cd.get(Calendar.DAY_OF_MONTH)<30)
			   {
				   Bitmap two=BitmapFactory.decodeResource(this.getResources(),imageIDs[2]);
				   canvas.drawBitmap(two,Constant.leftpadding*2+9*Constant.leftpadding*(cd.get(Calendar.DAY_OF_WEEK)-2),Constant.toppadding*18 , paint);
				   Bitmap days=BitmapFactory.decodeResource(this.getResources(),imageIDs[cd.get(Calendar.DAY_OF_MONTH)-20]);
				   canvas.drawBitmap(days,Constant.leftpadding*4+9*Constant.leftpadding*(cd.get(Calendar.DAY_OF_WEEK)-2),Constant.toppadding*18, paint);    
			   }else
			   {
				   Bitmap three=BitmapFactory.decodeResource(this.getResources(),imageIDs[3]);
				   canvas.drawBitmap(three,Constant.leftpadding*2+9*Constant.leftpadding*(cd.get(Calendar.DAY_OF_WEEK)-2),Constant.toppadding*18, paint);
				   Bitmap days=BitmapFactory.decodeResource(this.getResources(),imageIDs[cd.get(Calendar.DAY_OF_MONTH)-30]);
				   canvas.drawBitmap(days,Constant.leftpadding*4+9*Constant.leftpadding*(cd.get(Calendar.DAY_OF_WEEK)-2),Constant.toppadding*18 , paint);				   
			   }
			   }
		      }else
		      {
		    	  int t=cd.get(Calendar.MONTH)+1;
	              Constant.weekday[7]=cd.get(Calendar.YEAR)+" "+t+" "+cd.get(Calendar.DAY_OF_MONTH)+" ";
	            
	              
	              Constant.weekYears[7]=cd.get(Calendar.YEAR);
	              Constant.weekMonths[7]=t;
	              Constant.weekDays[7]=cd.get(Calendar.DAY_OF_MONTH);
	              
	              
	              
	              
	              
	              
	              
	              
	              
	              
	              
	              
		    	  if(cd.get(Calendar.DAY_OF_MONTH)<10) //画天
					{
					  Bitmap zero=BitmapFactory.decodeResource(this.getResources(),imageIDs[0]);
					  canvas.drawBitmap(zero,Constant.leftpadding*56,Constant.toppadding*18, paint);	
					  Bitmap CalendarMONTH=BitmapFactory.decodeResource(this.getResources(),imageIDs[cd.get(Calendar.DAY_OF_MONTH)]);
					  canvas.drawBitmap(CalendarMONTH,Constant.leftpadding*58,Constant.toppadding*18 , paint);// 显示日期坐标x为 Constant.leftpadding*23+Constant.leftpadding/2	  
					}else
					{
			      if(cd.get(Calendar.DAY_OF_MONTH)>9&&cd.get(Calendar.DAY_OF_MONTH)<20) // 10多号	
				   { 
					  Bitmap one=BitmapFactory.decodeResource(this.getResources(),imageIDs[1]);
					  canvas.drawBitmap(one,Constant.leftpadding*56,Constant.toppadding*18, paint);
					  Bitmap days=BitmapFactory.decodeResource(this.getResources(),imageIDs[cd.get(Calendar.DAY_OF_MONTH)-10]);
					  canvas.drawBitmap(days,Constant.leftpadding*58,Constant.toppadding*18, paint);   
					  
				   }else if(cd.get(Calendar.DAY_OF_MONTH)>19&&cd.get(Calendar.DAY_OF_MONTH)<30)
				   {
					   Bitmap two=BitmapFactory.decodeResource(this.getResources(),imageIDs[2]);
					   canvas.drawBitmap(two,Constant.leftpadding*56,Constant.toppadding*18, paint);
					   Bitmap days=BitmapFactory.decodeResource(this.getResources(),imageIDs[cd.get(Calendar.DAY_OF_MONTH)-20]);
					   canvas.drawBitmap(days,Constant.leftpadding*58,Constant.toppadding*18, paint);    
				   }else
				   {
					   Bitmap three=BitmapFactory.decodeResource(this.getResources(),imageIDs[3]);
					   canvas.drawBitmap(three,Constant.leftpadding*56,Constant.toppadding*18, paint);
					   Bitmap days=BitmapFactory.decodeResource(this.getResources(),imageIDs[cd.get(Calendar.DAY_OF_MONTH)-30]);
					   canvas.drawBitmap(days,Constant.leftpadding*58,Constant.toppadding*18, paint);				   
				   }
				 }
		      }
	    
		      if(cd.get(Calendar.DAY_OF_WEEK)!=1) //说明不是周日
		      {
            	   int Years=cd.get(Calendar.YEAR);		    		  
	    	       int Months=cd.get(Calendar.MONTH);
	    	       int dayss=cd.get(Calendar.DAY_OF_MONTH);
		    	  for(int z=(cd.get(Calendar.DAY_OF_WEEK)-3);z>=0;z--) //画当前时间左面的时间。
		    	  {
		    		  		    		  
		    		  if((dayss-1)>0)// 获得天数。
						{						
						  dayss=dayss-1;						  
						}else if((dayss-1)==0)//切换月份
						{	
							if((Months-1)==-1) //如果为1月1号。则更换为上一年。
							{     
								Years=Years-1;
								Months=11;
								dayss=31;						  
							}else
							{
							
								Months=Months-1;						
								dayss=getMonthDays(Years,Months);																					
							}							
						}	
		    		  
		    		  int t=Months+1;// 将相应的日期 存入 数组中。
		    		   Constant.weekday[(Constant.leftpadding*4+9*Constant.leftpadding*z)/(Constant.leftpadding*9+Constant.leftpadding/3)+1]=Years+" "+t+" "+dayss+" ";
		    		   
		    		   
		    		   
		    		    Constant.weekYears[(Constant.leftpadding*4+9*Constant.leftpadding*z)/(Constant.leftpadding*9+Constant.leftpadding/3)+1]=Years;
		                Constant.weekMonths[(Constant.leftpadding*4+9*Constant.leftpadding*z)/(Constant.leftpadding*9+Constant.leftpadding/3)+1]=t;
		                Constant.weekDays[(Constant.leftpadding*4+9*Constant.leftpadding*z)/(Constant.leftpadding*9+Constant.leftpadding/3)+1]=dayss;
		    		   
		    		   
		    		   
		    		   
		    		   
		    		   
		    		   
		    		   
		    		   
		    		   if(dayss<10) //画天
						{
						  Bitmap zero=BitmapFactory.decodeResource(this.getResources(),imageIDs[0]);
						  canvas.drawBitmap(zero,Constant.leftpadding*2+9*Constant.leftpadding*z,Constant.toppadding*18 , paint);	
						  Bitmap CalendarMONTH=BitmapFactory.decodeResource(this.getResources(),imageIDs[dayss]);
						  canvas.drawBitmap(CalendarMONTH,Constant.leftpadding*4+9*Constant.leftpadding*z,Constant.toppadding*18 , paint);// 显示日期坐标x为 Constant.leftpadding*23+Constant.leftpadding/2	  
						}else
						{
				        if(dayss>9&&dayss<20) // 10多号	
					      { 
						  Bitmap one=BitmapFactory.decodeResource(this.getResources(),imageIDs[1]);
						  canvas.drawBitmap(one,Constant.leftpadding*2+9*Constant.leftpadding*z,Constant.toppadding*18 , paint);
						  Bitmap days=BitmapFactory.decodeResource(this.getResources(),imageIDs[dayss-10]);
						  canvas.drawBitmap(days,Constant.leftpadding*4+9*Constant.leftpadding*z,Constant.toppadding*18, paint);   
						  
					   }else if(dayss>19&&dayss<30)
					   {
						   Bitmap two=BitmapFactory.decodeResource(this.getResources(),imageIDs[2]);
						   canvas.drawBitmap(two,Constant.leftpadding*2+9*Constant.leftpadding*z,Constant.toppadding*18 , paint);
						   Bitmap days=BitmapFactory.decodeResource(this.getResources(),imageIDs[dayss-20]);
						   canvas.drawBitmap(days,Constant.leftpadding*4+9*Constant.leftpadding*z,Constant.toppadding*18, paint);    
					   }else
					   {
						   Bitmap three=BitmapFactory.decodeResource(this.getResources(),imageIDs[3]);
						   canvas.drawBitmap(three,Constant.leftpadding*2+9*Constant.leftpadding*z,Constant.toppadding*18, paint);
						   Bitmap days=BitmapFactory.decodeResource(this.getResources(),imageIDs[dayss-30]);
						   canvas.drawBitmap(days,Constant.leftpadding*4+9*Constant.leftpadding*z,Constant.toppadding*18 , paint);				   
					   }
					   }
		    	  } 	  
		    	   int Years1=cd.get(Calendar.YEAR);		    		  
	    	       int Months1=cd.get(Calendar.MONTH);
	    	       int dayss1=cd.get(Calendar.DAY_OF_MONTH);
	    	       	   for(int z=(cd.get(Calendar.DAY_OF_WEEK)-1);z<8;z++)
		    	   {		    		   
		    	       int getMonthDays=getMonthDays(Years1,Months1);
		    		   if(dayss1<getMonthDays)//换天
						 {
		    			  
		    			     dayss1=dayss1+1;				
						 }else if(dayss1==getMonthDays)
						 {
							 if(Months1==11) //换年
							 {
								 Years1=Years1+1;
								 Months1=0;
								 dayss1=1;							 
							 }else                          //换月份
							 {    
								    Months1=Months1+1;												
								    dayss1=1;							 
							 }
														 
						 }
		     
		    		   
		    		   int t=Months1+1;// 将相应的日期 存入 数组中。
		    		   Constant.weekday[(Constant.leftpadding*4+9*Constant.leftpadding*z)/(Constant.leftpadding*9+Constant.leftpadding/3)+1]=Years1+" "+t+" "+dayss1+" ";
		    		   
		    		   Constant.weekYears[(Constant.leftpadding*4+9*Constant.leftpadding*z)/(Constant.leftpadding*9+Constant.leftpadding/3)+1]=Years1;
		               Constant.weekMonths[(Constant.leftpadding*4+9*Constant.leftpadding*z)/(Constant.leftpadding*9+Constant.leftpadding/3)+1]=t;
		               Constant.weekDays[(Constant.leftpadding*4+9*Constant.leftpadding*z)/(Constant.leftpadding*9+Constant.leftpadding/3)+1]=dayss1;
  		    		 
		               if(dayss1<10) //画天
						{
						  Bitmap zero=BitmapFactory.decodeResource(this.getResources(),imageIDs[0]);
						  canvas.drawBitmap(zero,Constant.leftpadding*2+9*Constant.leftpadding*z,Constant.toppadding*18 , paint);	
						  Bitmap CalendarMONTH=BitmapFactory.decodeResource(this.getResources(),imageIDs[dayss1]);
						  canvas.drawBitmap(CalendarMONTH,Constant.leftpadding*4+9*Constant.leftpadding*z,Constant.toppadding*18 , paint);// 显示日期坐标x为 Constant.leftpadding*23+Constant.leftpadding/2	  
						}else
						{
				        if(dayss1>9&&dayss1<20) // 10多号	
					      { 
						  Bitmap one=BitmapFactory.decodeResource(this.getResources(),imageIDs[1]);
						  canvas.drawBitmap(one,Constant.leftpadding*2+9*Constant.leftpadding*z,Constant.toppadding*18 , paint);
						  Bitmap days=BitmapFactory.decodeResource(this.getResources(),imageIDs[dayss1-10]);
						  canvas.drawBitmap(days,Constant.leftpadding*4+9*Constant.leftpadding*z,Constant.toppadding*18, paint);   						  
					   }else if(dayss1>19&&dayss1<30)
					   {
						   Bitmap two=BitmapFactory.decodeResource(this.getResources(),imageIDs[2]);
						   canvas.drawBitmap(two,Constant.leftpadding*2+9*Constant.leftpadding*z,Constant.toppadding*18 , paint);
						   Bitmap days=BitmapFactory.decodeResource(this.getResources(),imageIDs[dayss1-20]);
						   canvas.drawBitmap(days,Constant.leftpadding*4+9*Constant.leftpadding*z,Constant.toppadding*18, paint);    
					   }else
					   {
						   Bitmap three=BitmapFactory.decodeResource(this.getResources(),imageIDs[3]);
						   canvas.drawBitmap(three,Constant.leftpadding*2+9*Constant.leftpadding*z,Constant.toppadding*18, paint);
						   Bitmap days=BitmapFactory.decodeResource(this.getResources(),imageIDs[dayss1-30]);
						   canvas.drawBitmap(days,Constant.leftpadding*4+9*Constant.leftpadding*z,Constant.toppadding*18 , paint);				   
					   }
    	               }	    	  
                     }
		      }
	      else //如果今天是周日的话
	      {
		    	   int Years2=cd.get(Calendar.YEAR);		    		  
	    	       int Months2=cd.get(Calendar.MONTH);
	    	       int dayss2=cd.get(Calendar.DAY_OF_MONTH);
      	    	   for(int z=5;z>=0;z--)
		    	  { 		    	     
			    		  if((dayss2-1)>0)// 获得天数。
							{							 
							  dayss2=dayss2-1;
							;							  
							}else if((dayss2-1)==0)//切换月份
							{	
								if((Months2-1)==-1) //如果为1月1号。则更换为上一年。
								{     
									Years2=Years2-1;
									Months2=11;
									dayss2=31;						  
								}else
								{
								//	Years2=cd.get(Calendar.YEAR);
									Months2=Months2-1;						
									dayss2=getMonthDays(Years2,Months2);
								
								}							
							}
			    		   int t=Months2+1;// 将相应的日期 存入 数组中。
			    		   Constant.weekday[(Constant.leftpadding*4+9*Constant.leftpadding*z)/(Constant.leftpadding*9+Constant.leftpadding/3)+1]=Years2+" "+t+" "+dayss2+" ";
			    		  
			    		   Constant.weekYears[(Constant.leftpadding*4+9*Constant.leftpadding*z)/(Constant.leftpadding*9+Constant.leftpadding/3)+1]=Years2;
			               Constant.weekMonths[(Constant.leftpadding*4+9*Constant.leftpadding*z)/(Constant.leftpadding*9+Constant.leftpadding/3)+1]=t;
			               Constant.weekDays[(Constant.leftpadding*4+9*Constant.leftpadding*z)/(Constant.leftpadding*9+Constant.leftpadding/3)+1]=dayss2;
			    		  
			    		  
			    		  if(dayss2<10) //画天
							{
							  Bitmap zero=BitmapFactory.decodeResource(this.getResources(),imageIDs[0]);
							  canvas.drawBitmap(zero,Constant.leftpadding*2+9*Constant.leftpadding*z,Constant.toppadding*18 , paint);	
							  Bitmap CalendarMONTH=BitmapFactory.decodeResource(this.getResources(),imageIDs[dayss2]);
							  canvas.drawBitmap(CalendarMONTH,Constant.leftpadding*4+9*Constant.leftpadding*z,Constant.toppadding*18 , paint);// 显示日期坐标x为 Constant.leftpadding*23+Constant.leftpadding/2	  
							}else
							{
					        if(dayss2>9&&dayss2<20) // 10多号	
						      { 
							  Bitmap one=BitmapFactory.decodeResource(this.getResources(),imageIDs[1]);
							  canvas.drawBitmap(one,Constant.leftpadding*2+9*Constant.leftpadding*z,Constant.toppadding*18 , paint);
							  Bitmap days=BitmapFactory.decodeResource(this.getResources(),imageIDs[dayss2-10]);
							  canvas.drawBitmap(days,Constant.leftpadding*4+9*Constant.leftpadding*z,Constant.toppadding*18, paint);   
							  
						   }else if(dayss2>19&&dayss2<30)
						   {
							   Bitmap two=BitmapFactory.decodeResource(this.getResources(),imageIDs[2]);
							   canvas.drawBitmap(two,Constant.leftpadding*2+9*Constant.leftpadding*z,Constant.toppadding*18 , paint);
							   Bitmap days=BitmapFactory.decodeResource(this.getResources(),imageIDs[dayss2-20]);
							   canvas.drawBitmap(days,Constant.leftpadding*4+9*Constant.leftpadding*z,Constant.toppadding*18, paint);    
						   }else
						   {
							   Bitmap three=BitmapFactory.decodeResource(this.getResources(),imageIDs[3]);
							   canvas.drawBitmap(three,Constant.leftpadding*2+9*Constant.leftpadding*z,Constant.toppadding*18, paint);
							   Bitmap days=BitmapFactory.decodeResource(this.getResources(),imageIDs[dayss2-30]);
							   canvas.drawBitmap(days,Constant.leftpadding*4+9*Constant.leftpadding*z,Constant.toppadding*18 , paint);				   
						   }
							
			    	  }	 
		    	  }
	            }
		    	 
		      }
		    	  
		      
		      
		      
		      
		      
		      
		      
		      
		      
		      
		      
		      
		
		
//=======================画riqi结束===============================				
		
	    
	
	
	
	
	
	
	
	
	
	
	
	
	
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
