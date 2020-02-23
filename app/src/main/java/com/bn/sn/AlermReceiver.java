package com.bn.sn;

import java.util.Date;
import java.util.Map;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlermReceiver extends BroadcastReceiver 
{//接受闹钟广播的类
	
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		//测试提醒
		int eventid=arg1.getIntExtra("eventid", -1);//得到附加信息中的事件id
		//查询相应的事件名
		String title=null;
		if(eventid!=-1)
		{
			title=DBUtil.getTitleById(arg0, eventid);
		}
		Log.d("eventid",eventid+"=============AlermReceivergetIntExtra============");
		//再次查询设置下一闹钟
		Map<Date, Integer> map=DBUtil.findnearlyTime(arg0);

		for(Date d:map.keySet())
		{//遍历键值Date
			if(d!=null)
			{
				Log.d("minDateReceiver", d.toLocaleString());
				setAlerm(arg0,d,map.get(d));//设置闹钟测试
			}
			else
			{
				Log.d("minDate", "not found");
			}
		}
		
		Toast.makeText(arg0,title+ "时间到！", Toast.LENGTH_SHORT).show();
		//创建提示对话框
		Intent intent=new Intent(arg0,AlermActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//???????
		//将标题放入附加信息中
		intent.putExtra("title", title);
		//启动activity
		arg0.startActivity(intent);
	}
	AlarmManager alarmManager;//闹钟管理对象声明
	//设置闹钟方法
    public void setAlerm(Context context,Date date,int eventid)
    {
    	Toast.makeText(context, "设置闹铃"+date.toLocaleString(), Toast.LENGTH_SHORT).show();
    	//获取闹钟管理的对象
    	alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    	//找对应的接受者
    	Intent intent=new Intent(context,AlermReceiver.class);
    	intent.putExtra("eventid", eventid);//将id值附加到intent中
    	PendingIntent pendingIntent=PendingIntent.getBroadcast(context,0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
    	//设置闹钟 
    	alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent); 
    }

}
