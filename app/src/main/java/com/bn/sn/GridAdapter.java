package com.bn.sn;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter
{
	ScheduleManagerActivity context;//上下文
	int[] icon;//图片id数组
	String[] text;//文字数组
	//构造器接受图片文字等参数
	public GridAdapter(Context context,int[] icon,String[] text)
	{
		this.context=(ScheduleManagerActivity)context;
		this.icon=icon;
		this.text=text;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return icon.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//拿到一个LayoutInflater
		LayoutInflater factory=LayoutInflater.from(context);
		//将自定义的griditem.xml实例化,转换为View
		View view=(View)factory.inflate(R.layout.griditem, null);
		//从自定义页面中拿到控件引用并赋值
		ImageView iv=(ImageView)view.findViewById(R.id.icon);
		TextView tv=(TextView)view.findViewById(R.id.text);
		iv.setImageResource(icon[position]);
		tv.setText(text[position]);
		//给每一个项添加监听
		view.setPadding(5, 5, 5, 5);
		setListener(view,position);
		return view;
	}
	//自定义方法给各项添加监听
	public void setListener(View v,int index)
	{
		final int position=index;
		v.setOnClickListener(
				new OnClickListener()
				{
					@Override
					public void onClick(View v) {
						//0:新建日程，1：查询日程，2：系统设置，3：日视图，4：周视图，5：月视图，6：小助手，7：软件信息，8：退出
						switch(position)
						{
						case 0:context.newschedule(context.curDate);break;
						case 1:context.bnsearch();break;
						case 2:context.toSetView();break;//若position为2说明选择的是系统设置项
						case 3:context.createDay();break;
						case 4:context.createWeek();break;
						case 5:context.gotoMonthView();break;//月视图
						case 6://小助手，链接网站携程旅行网
							Intent intent= new Intent();        
						    intent.setAction("android.intent.action.VIEW");    
						    Uri content_url = Uri.parse("http://www.ctrip.com");   
						    intent.setData(content_url);  
						    context.startActivity(intent);
						    break;
						case 7://软件信息
							context.goAbout();break;
						case 8:context.saveMain();System.exit(0);

						}
					}
					
				}
		);
	}
}
