package com.bn.sn;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;


public class EditDialog extends Dialog implements OnClickListener 
{
	ScheduleManagerActivity smActivity;//主activity
	String eventid;//对应的事件的数据库id
	public EditDialog(Context context,String eventid) {
		super(context);
		this.smActivity=(ScheduleManagerActivity)context;
		this.eventid=eventid;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//转到相应页面
		setContentView(R.layout.editlayout);
		//拿到个每一行布局的引用，添加监听
		LinearLayout llnew=(LinearLayout)findViewById(R.id.linearnew);
		llnew.setOnClickListener(this);
		LinearLayout lledit=(LinearLayout)findViewById(R.id.linearedit);
		lledit.setOnClickListener(this);
		LinearLayout lldelete=(LinearLayout)findViewById(R.id.lineardelete);
		lldelete.setOnClickListener(this);
		LinearLayout llback=(LinearLayout)findViewById(R.id.linearback);
		llback.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.linearnew:
			//转到新建页面
			smActivity.newschedule(smActivity.curDate);
			break;
		case R.id.linearedit:
			//转到编辑页面并传给其id
			smActivity.editschedule(Integer.parseInt(eventid));
			break;
		case R.id.lineardelete:
			//通过拿到的id从数据中删掉相关记录
			Log.d("delete", eventid+"-----------");
			DBUtil.deleteSchdule(smActivity, eventid);
			smActivity.setAlarm();//更新闹铃设置
			if(Constant.monthweekday==1) //判断显示哪个主界面界面
		       {
				smActivity.gotoMonthView();
		       }
		       else if(Constant.monthweekday==2)
		       {
		    	   smActivity.createWeek(); 
		       }else
		       {
		    	   smActivity.createDay();
		       }   
			break;
		case R.id.linearback:
			//返回到之前的页面
			if(Constant.monthweekday==1) //判断显示哪个主界面界面
		       {
				smActivity.gotoMonthView();
		       }
		       else if(Constant.monthweekday==2)
		       {
		    	   smActivity.createWeek(); 
		       }else
		       {
		    	   smActivity.createDay();
		       }            
			break;
		}
		//
		//只要选择了一项就关闭此对话框
		dismiss();
	}
	

}
