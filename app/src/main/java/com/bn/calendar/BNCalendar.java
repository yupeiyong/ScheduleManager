package com.bn.calendar;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;

import com.bn.sn.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class BNCalendar extends View
{
	private CaActionListener cal;//日期显示部分监听接口对象声明
	private PrevActionLinstener prev;
	private NextActionLinstener next;
	private ControlCenterActionListener center;//上面控制日期年月的按钮的监听对象声明
	private FrontBlankActionListener front;
	private BehindBlankActionListener behind;
	
	private int oneWidth=40+3;//每个格子的宽度
	private int oneHeigtHeader=25;//表头每个格子的高度
	private int oneHeigtDate=35;//日期内容每个格子的高度
	private int TopPanning=0;//整体空间距离上部的调整距离
	private int LeftPanning=0;//整体控件横向调整值	
	private int prevButtonWidth=40;//向前一个月的按钮宽度
	private int shownumwidth=12;//显示年月的图片宽度
	private int todayButtonWidth=shownumwidth*7;//中间显示日期部分的总宽度
	
	private int chagedateHeight=40;//选择日期这一行的高度
	private int controlPanning=5;//控制图片之间的间距
	private int rowCount=6;//记录日历部分绘制的多少行
	//日历所占位置总宽度，加上了左边边界调控值，在后面容易计算
	private int totalWidth=oneWidth*7+LeftPanning;
	//日历所占位置总高度 
	private int totalHeight=TopPanning+oneHeigtHeader+oneHeigtDate*6+chagedateHeight;
	private Paint pt = new Paint();
	private Calendar calCalendar = Calendar.getInstance();//日历显示的时间
	//图片的id号
	private int[] ImageIds={0,R.drawable.num1,R.drawable.num2,
			R.drawable.num3,R.drawable.num4,R.drawable.num5,R.drawable.num6,
			R.drawable.num7,R.drawable.num8,R.drawable.num9,R.drawable.num10,R.drawable.num11,
			R.drawable.num12,R.drawable.num13,R.drawable.num14,R.drawable.num15,
			R.drawable.num16,R.drawable.num17,R.drawable.num18,R.drawable.num19,R.drawable.num20,
			R.drawable.num21,R.drawable.num22,R.drawable.num23,R.drawable.num24,
			R.drawable.num25,R.drawable.num26,R.drawable.num27,R.drawable.num28,
			R.drawable.num29,R.drawable.num30,R.drawable.num31};
	
	//上面显示年月图片的id
	private int[] showdateIds={R.drawable.show0,R.drawable.show1,
								R.drawable.show2,R.drawable.show3,
								R.drawable.show4,R.drawable.show5,
								R.drawable.show6,R.drawable.show7,
								R.drawable.show8,R.drawable.show9,R.drawable.show10};
	//显示下个月的图片id
	private int[] nextdateIds={R.drawable.nenum1,R.drawable.nenum2,R.drawable.nenum3,
								R.drawable.nenum4,R.drawable.nenum5,R.drawable.nenum6,
								R.drawable.nenum7,R.drawable.nenum8,R.drawable.nenum9,
								R.drawable.nenum10,R.drawable.nenum11,R.drawable.nenum12,
								R.drawable.nenum13};
	//显示上个月日期的图标id
	private int[] predateIds={R.drawable.nenum31,R.drawable.nenum30,R.drawable.nenum29,
								R.drawable.nenum28,R.drawable.nenum27,R.drawable.nenum26,
								R.drawable.nenum25,R.drawable.nenum24,R.drawable.nenum23,
								R.drawable.nenum22};
	//周一至周日的图片id
	private int[] ZhouImageIds=new int[10];
	//存储当前的年份，月份，天
	private int currYear;
	private int currMonth;//1-12表示
	private int currDay;
	//设置已周几为每个星期的第一天
	private int firstDayOfWeek=Calendar.MONDAY;
	//记录当前月份的第一天在日历中的格子编号
	private int firstDayOfMonthIndex;
	//记录当前月份的最后一天在日历中的格子编号
	private int lastDayOfMonthIndex;	
	//记录点击的x y 坐标
	int posX=0;
	int posY=0;
	boolean hasSelected=false;//标识是否有选中的天
	Calendar calToday=Calendar.getInstance();//系统当天的日期
	public static final int SHOW_DATAPICK = 0; //标识生成日期选择对话框
	//语句块初始化
	{
		//周几的图片id顺序按照Calendar中的进行
		ZhouImageIds[Calendar.MONDAY]=R.drawable.zhou01;
		ZhouImageIds[Calendar.TUESDAY]=R.drawable.zhou02;
		ZhouImageIds[Calendar.WEDNESDAY]=R.drawable.zhou03;
		ZhouImageIds[Calendar.THURSDAY]=R.drawable.zhou04;
		ZhouImageIds[Calendar.FRIDAY]=R.drawable.zhou05;
		ZhouImageIds[Calendar.SATURDAY]=R.drawable.zhou06;
		ZhouImageIds[Calendar.SUNDAY]=R.drawable.zhou07;	
		//初始时设置当前日历时间为系统当前时间
		calCalendar.setTimeInMillis(System.currentTimeMillis());
		//得到当前日期值
		currYear=calCalendar.get(Calendar.YEAR);
		currMonth=calCalendar.get(Calendar.MONTH)+1;
		currDay=calCalendar.get(Calendar.DAY_OF_MONTH);
		//初始化为本月的第一天
		updateCalendar(calCalendar,currYear,currMonth,currDay);
		calToday.setTimeInMillis(System.currentTimeMillis());
	}
	//添加点击日历范围内的监听器
	public void addCaActionListener(CaActionListener cal)
	{
		this.cal=cal;
	}
	//添加点击向前按钮的事件监听器
	public void addPrevActionLinstener(PrevActionLinstener prev)
	{
		this.prev=prev;
	}
	//添加点击向后按钮的事件监听器
	public void addNextActionLinstener(NextActionLinstener next)
	{
		this.next=next;
	}
	//添加年月控制按钮的监听器
	public void addControlCenterActionListener(ControlCenterActionListener center)
	{
		this.center=center;
	}
	//添加前面的空白位置的事件监听器
	public void addFrontBlankActionListener(FrontBlankActionListener front)
	{
		this.front=front;
	}
	//添加后面空白位置的监听器
	public void addBehindBlankActionListener(BehindBlankActionListener behind)
	{
		this.behind=behind;
	}
	
	//三种构造器========================
	public BNCalendar(Context context) {
		super(context);		
	}
	public BNCalendar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);	
	}
	public BNCalendar(Context context, AttributeSet attrs) {
		super(context, attrs);	
	}
	//三种构造器========================

	//画日历表头
	private void drawHeader(Canvas canvas)
	{
		//绘制日历总背景
		Bitmap bp=BitmapFactory.decodeResource(this.getResources(),R.drawable.bncalendar_bg);
		canvas.drawBitmap(bp,LeftPanning, TopPanning, pt);
		int iWeekDay=-1;
		for(int day=0;day<7;day++)
		{
			//若以周一为一周的第一天
			if (firstDayOfWeek == Calendar.MONDAY) {
				iWeekDay = day + Calendar.MONDAY;
				if (iWeekDay > Calendar.SATURDAY)
					iWeekDay = Calendar.SUNDAY;
			}
			if (firstDayOfWeek == Calendar.SUNDAY) {
				iWeekDay = day + Calendar.SUNDAY;
			}
			if(iWeekDay>0&&iWeekDay<8)
			{
				//根据是第几天加载相应图片
				bp=BitmapFactory.decodeResource(this.getResources(), ZhouImageIds[iWeekDay]);
				pt.setAntiAlias(true);//打开抗锯齿
				canvas.drawBitmap(bp, oneWidth*day+LeftPanning, TopPanning+chagedateHeight, pt);
			}
		}	
	}
	//画日历部分
	private void drawCalendar(Canvas canvas)
	{	
		calCalendar.set(Calendar.DAY_OF_MONTH, 1);//从每月的第一天开始画
		int days=getMonthDays(currYear,currMonth);//得到本月的天数
		//得到这个月的一号是星期几
		int startDay=calCalendar.get(Calendar.DAY_OF_WEEK);
		//计算一号的索引index   7X5 的格子从1开始编号即为0-34
		int startDayIndex=startDay-2;//默认是以星期一为一周的第一天
		if(firstDayOfWeek==Calendar.MONDAY)
		{//如果是以周一未第一天则将第一天的号-2 因为Calendar.MONDAY的值为2
			startDayIndex=startDay-2;
			if (startDayIndex < 0)//若小于0则是星期天
				startDayIndex = 6;
		}
		if(firstDayOfWeek==Calendar.SUNDAY)
		{
			startDayIndex=startDay-1;
			if (startDayIndex < 0)
				startDayIndex = 6;
		}
		firstDayOfMonthIndex=startDayIndex;//记录本月第一天的格子编号
		//根据格子编号index绘制一个月的日期
		int index=firstDayOfMonthIndex;
		int row;//当前绘制的格子所在行数从0开始
		int col;//格子所在列数 从0开始
		int iy;//绘制图片左上角y坐标
		int ix;//绘制图片左上角x坐标
		for(int i=1;i<=days;i++)
		{
			row=index/7;
			col=index%7;
			iy=row*oneHeigtDate+oneHeigtHeader+TopPanning+chagedateHeight;
			ix=col*oneWidth+LeftPanning;
			//若选中编号不在所有日期编号范围内则将第一天制定为选中的日期
			Bitmap bp;
			if(!hasSelected&&currYear==calToday.get(Calendar.YEAR)&&(currMonth-1)==calToday.get(Calendar.MONTH))
			{
				if(i==calToday.get(Calendar.DAY_OF_MONTH))
				{//若没有选中的日期且日历显示日期为当前年月则默认今天为选中的日子
					bp=BitmapFactory.decodeResource(this.getResources(), R.drawable.selectedbg);
					canvas.drawBitmap(bp, ix, iy, pt);
					bp=BitmapFactory.decodeResource(this.getResources(), ImageIds[i]);
				}
				else
				{
					bp=BitmapFactory.decodeResource(this.getResources(), ImageIds[i]);
				}
			}
			else if(i==currDay)
			{
				bp=BitmapFactory.decodeResource(this.getResources(), R.drawable.selectedbg);
				canvas.drawBitmap(bp, ix, iy, pt);
				bp=BitmapFactory.decodeResource(this.getResources(), ImageIds[i]);
			}
			else
			{
				bp=BitmapFactory.decodeResource(this.getResources(), ImageIds[i]);
			}
			
			pt.setAntiAlias(true);//打开抗锯齿
			canvas.drawBitmap(bp, ix, iy, pt);
			index++;
		}
		lastDayOfMonthIndex=index-1;//记录本月最后一天的格子编号
		//将前面和后面空白的部分填充上个月个下个月的日期
		//前一个月的
		//先后的上一个月的天数
		int preMonth=currMonth-1;//这个月的月份加一，判断是否到了上一年
		int preYear=currYear;
		if(preMonth==0)//若到了前一年
		{
			preYear--;
			preMonth=12;
		}
		//若当前天比现在显示的日期数大，则改变其位最后一天
		int predays=getMonthDays(preYear,preMonth);
		//绘制上个月的日期
		int c=0;//中间处理技术用
		Bitmap bpbg;//非本月部分图片
		for(int i=firstDayOfMonthIndex-1;i>=0;i--)
		{
			bpbg=BitmapFactory.decodeResource(this.getResources(), predateIds[31-predays+c++]);
			iy=oneHeigtHeader+TopPanning+chagedateHeight;
			ix=i*oneWidth+LeftPanning;
			
			canvas.drawBitmap(bpbg, ix, iy, pt);
		}
		//后一个月的
		c=0;
		//从最后本月最后一天的后一个位置开始到最后都画下一个月的日期
		for(int i=index;i<42;i++)
		{
			bpbg=BitmapFactory.decodeResource(this.getResources(), nextdateIds[c++]);
			row=i/7;
			col=i%7;
			iy=row*oneHeigtDate+oneHeigtHeader+TopPanning+chagedateHeight;
			ix=col*oneWidth+LeftPanning;
			canvas.drawBitmap(bpbg, ix, iy, pt);
		}
	}
	//绘制日历上方的控制选择按钮
	private void drawControl(Canvas canvas)
	{
		//第一个向前的按钮的图片的x y绘制坐标
		int ix=(int)(LeftPanning+1.5*oneWidth);
		int iy=TopPanning;
		Bitmap bp=BitmapFactory.decodeResource(this.getResources(), R.drawable.prev);
		canvas.drawBitmap(bp, ix, iy, pt);
		//中间显示日期的部分
		ix+=prevButtonWidth+controlPanning;
		//绘制日期		
		String text=currYear+"-"+currMonth;
		//将字符串拆分根据内容找对应的图片
		char[] datetochar=text.toCharArray();
		for(int i=0;i<datetochar.length;i++)
		{
			ix+=(i==0)?0:shownumwidth;//根据是第几个数计算横坐标
			if(datetochar[i]=='-')
			{
				bp=BitmapFactory.decodeResource(this.getResources(),showdateIds[10]);
				canvas.drawBitmap(bp, ix, iy, pt);
			}
			else
			{
				if(currMonth<10&&i==datetochar.length-1)
				{//若月份小于10并且是最后一个数则在月前先画个0
					bp=BitmapFactory.decodeResource(this.getResources(),showdateIds[0]);
					canvas.drawBitmap(bp, ix, iy, pt);
					ix+=shownumwidth;//向后移一位
					//画最后一位
					int num=Integer.parseInt(datetochar[i]+"");
					bp=BitmapFactory.decodeResource(this.getResources(),showdateIds[num]);
					canvas.drawBitmap(bp, ix, iy, pt);
				}
				else
				{
					int num=Integer.parseInt(datetochar[i]+"");
					bp=BitmapFactory.decodeResource(this.getResources(),showdateIds[num]);
					canvas.drawBitmap(bp, ix, iy, pt);
				}
			}	
		}
		//向下一个月的图片
		ix+=controlPanning+shownumwidth;
		bp=BitmapFactory.decodeResource(this.getResources(), R.drawable.next);
		canvas.drawBitmap(bp, ix, iy, pt);
	}
	
	private void drawFrame(Canvas canvas)
	{
		//水平的直线
		Bitmap bp=BitmapFactory.decodeResource(this.getResources(), R.drawable.horiline);
		//起始坐标
		float startX=LeftPanning;
		float startY=oneHeigtHeader+TopPanning+chagedateHeight;
		//画水平线
		for(int i=0;i<=rowCount;i++)
		{	
			canvas.drawBitmap(bp, startX, startY+i*oneHeigtDate, pt);
		}
		//六条竖线
		//起始坐标
		startX=LeftPanning+oneWidth;
		startY=TopPanning+chagedateHeight-3;
		bp=BitmapFactory.decodeResource(this.getResources(), R.drawable.verline2);
		for(int i=0;i<6;i++)
		{
			canvas.drawBitmap(bp, startX+i*oneWidth, startY, pt);
		}
		
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		drawHeader(canvas);//绘制表头
		drawCalendar(canvas);//绘制日历内容
		drawControl(canvas);//绘制控制栏
		drawFrame(canvas);//绘制边框中间线
	}	

	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		
		if(event.getAction()==MotionEvent.ACTION_DOWN)
		{
			posX=(int)event.getX();
			posY=(int)event.getY();
			Log.d("clickPositon",posX+","+posY);
			return true;
			
		}
		else if(event.getAction()==MotionEvent.ACTION_UP)
		{
			Log.d("OKPositon",posX+","+posY);
			
			//若点击位置在日历绘制范围内
			if(posX<=totalWidth &&posX>=LeftPanning && posY<=totalHeight && posY>=TopPanning+oneHeigtHeader+chagedateHeight)
			{		
				//根据点击位置计算出其所点击的格子编号
				//计算所在格子行号
				int inrow=(posY-oneHeigtHeader-TopPanning-chagedateHeight)/oneHeigtDate;//不用加调整值从0开始计算的
				//点击所在格子列号
				int incol=(posX-LeftPanning)/oneWidth;
				//点击格子的编号
				int posIndex=inrow*7+incol;
				//若格子编号是本月中的日期格子，则计算日期
				if(posIndex>=firstDayOfMonthIndex&&posIndex<=lastDayOfMonthIndex)
				{
					hasSelected=true;//设标记为有选中的项
					currDay=posIndex-firstDayOfMonthIndex+1;//通过其与本月一号编号的差值得到当前点击的是几号
					//更新当前的calCalendar对象的值
					updateCalendar(calCalendar,currYear,currMonth,currDay);
					Log.d("showDate", currYear+"年"+currMonth+"月"+currDay+"日");
					if(cal!=null)
					{
						Date date=new Date(calCalendar.getTimeInMillis());
						cal.doAction(date);
					}
					//刷新页面
					this.postInvalidate();
					return true;
				}
				//若点击的是月份前面的空白格则改变当前月份为前一个月，调用draw方法重画
				if(posIndex<firstDayOfMonthIndex)
				{
					hasSelected=true;//设标记为有选中的项
					//计算点击的是上个月的第几号
					int premonthdays=getMonthDays(currYear,currMonth-1);//上个月天数
					int selectday=premonthdays-(firstDayOfMonthIndex-posIndex)+1;//选中的上个月几号
					currDay=selectday;//设当天日为选中天
					preDate();//向前一个月更新时间
					//若前面空白位置添加了监听事件则调用
					if(front!=null)
					{
						Date date=new Date(calCalendar.getTimeInMillis());
						front.doAction(date);
					}
					//刷新页面
					this.postInvalidate();
					return true;
				}
				//若点击月份后面的地方则转到下一个月
				if(posIndex>lastDayOfMonthIndex)
				{
					hasSelected=true;//设标记为有选中的项
					int selectday=posIndex-lastDayOfMonthIndex;//选中的下个月几号
					currDay=selectday;//设当天日为选中天
					nextDate();//向后一个月更新时间
					//若添加了监听事件则调用
					if(behind!=null)
					{
						Date date=new Date(calCalendar.getTimeInMillis());
						behind.doAction(date);
					}
					//刷新页面
					this.postInvalidate();
					return true;
				}	
			}
			//若触摸范围是向前的图片
			if(posX<=LeftPanning+prevButtonWidth+1.5*oneWidth &&posX>=LeftPanning+1.5*oneWidth 
					&& posY<=TopPanning+chagedateHeight && posY>=TopPanning)
			{
				hasSelected=true;//设标记为有选中的项
				preDate();//向前一个月更新时间
				//若添加了监听器则调用方法
				if(prev!=null)
				{
					Date date=new Date(calCalendar.getTimeInMillis());
					prev.doAction(date);
				}
				//刷新页面
				this.postInvalidate();
				return true;
			}
			//若触摸位置为向后的图片
			if(posX<=LeftPanning+2*prevButtonWidth+todayButtonWidth+2*controlPanning+1.5*oneWidth 
					&&posX>=LeftPanning+prevButtonWidth+todayButtonWidth+2*controlPanning+1.5*oneWidth 
					&& posY<=TopPanning+chagedateHeight && posY>=TopPanning)
			{
				hasSelected=true;//设标记为有选中的项
				nextDate();//向后一个月更新时间
				//若添加了监听事件则回调监听器中的方法
				if(next!=null)
				{
					Date date=new Date(calCalendar.getTimeInMillis());
					next.doAction(date);
				}
				//刷新页面
				this.postInvalidate();
				return true;
			}
			
			//若触摸的是中间显示时间的图片
			if(posX<=LeftPanning+prevButtonWidth+todayButtonWidth+controlPanning+1.5*oneWidth 
					&&posX>=LeftPanning+prevButtonWidth+controlPanning+1.5*oneWidth 
					&& posY<=TopPanning+chagedateHeight && posY>=TopPanning)
			{
				hasSelected=true;//设标记为有选中的项
				//若给其添加了监听事件则调用
				if(center!=null)
				{
					Date date=new Date(calCalendar.getTimeInMillis());
					center.doAction(date);
				}
				return true;
			}
		}
		return false;
	}
	//向前更新月份的方法
	private void preDate()
	{
		currMonth--;
		if(currMonth==0)//若到了前一年
		{
			currYear--;
			currMonth=12;
		}
		//若当前天比现在显示的日期数大，则改变其位最后一天
		int days=getMonthDays(currYear,currMonth);
		if(currDay>days)
		{
			currDay=days;
		}
		//更新calCalendar的值为前一个月的一号
		updateCalendar(calCalendar,currYear,currMonth,currDay);
	}
	//向前更新月份的方法
	private void nextDate()
	{
		currMonth++;
		if(currMonth==13)//若出了0-11则转到下一年
		{
			currYear++;
			currMonth=1;
		}
		//若当前天比现在显示的日期数大，则改变其位最后一天
		int days=getMonthDays(currYear,currMonth);
		if(currDay>days)
		{
			currDay=days;
		}
		//更新calCalendar的值为后一个月的一号
		updateCalendar(calCalendar,currYear,currMonth,currDay);
	}
	//设置calCalendar的值
	public void updateCalendar(Calendar calendar,int year,int month,int day)
	{
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month-1);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calToday.setTimeInMillis(System.currentTimeMillis());//更新当天时间
	}
	//定义方法获得当前月的天数
	private int getMonthDays(int year,int month)
	{
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
	//成员变量的set get方法
	public int getFirstDayOfWeek() {
		return firstDayOfWeek;
	}
	public void setFirstDayOfWeek(int firstDayOfWeek) {
		this.firstDayOfWeek = firstDayOfWeek;
	}
	public int getCurrYear() {
		return currYear;
	}
	public void setCurrYear(int currYear) {
		this.currYear = currYear;
	}
	public int getCurrMonth() {
		return currMonth;
	}
	public void setCurrMonth(int currMonth) {
		this.currMonth = currMonth;
	}
	public void setDate(Date date)
	{
		hasSelected=true;//设标记为有选中的项
		this.currYear=date.getYear()+1900;
		this.currMonth = date.getMonth()+1;
		this.currDay=date.getDate();
		System.out.println(currYear+"-"+currMonth+"-"+currDay);
		//更新calCalendar的值为后一个月的一号
		updateCalendar(calCalendar,currYear,currMonth,currDay);
		//刷新页面
		this.postInvalidate();	
	}
	public Date getDate()
	{
		Date date=new Date(calCalendar.getTimeInMillis());
		date.setDate(currDay);
		return date;
	}
	public int getLeftPanning() {
		return LeftPanning;
	}
	public void setLeftPanning(int leftPanning) {
		LeftPanning = leftPanning;
	}
	
}
