package com.bn.sn;

import java.util.Date;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.TextView;
public class FileEditDialog extends Dialog implements OnClickListener
{
    int id;
    Date date;
    ScheduleManagerActivity sa;
    int shituid;
	public FileEditDialog(Context context,int id,Date date,int shituid) {// 将id传过来
		super(context);
		// TODO Auto-generated constructor stub	
		this.id=id;
		this.sa=(ScheduleManagerActivity)context;
		this.date=date;
		this.shituid=shituid;
	}		
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.fileeditt);
        setContentView(R.layout.fileedit);
       
        TextView tv1=(TextView)this.findViewById(R.id.TextView01);
        tv1.setOnClickListener(this);   
        tv1.setText(R.string.newd);       
        TextView tv2=(TextView)this.findViewById(R.id.TextView02);
        tv2.setOnClickListener(this);      
        tv2.setText(R.string.edit);      
        TextView tv3=(TextView)this.findViewById(R.id.TextView03);
        tv3.setOnClickListener(this);      
        tv3.setText(R.string.delete);        
        TextView tv4=(TextView)this.findViewById(R.id.TextView04);
        tv4.setOnClickListener(this);    
        tv4.setText(R.string.back);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.TextView01:               //转入新建页面
			 sa.newschedule(date); // 传送给新建日期。        
			break;
		case R.id.TextView02:               //转入编辑页面
			 sa.editschedule(id);                              // 传送给新建日期。
			break;
        case R.id.TextView03:             	     
            DBUtil.deleteSchdule(sa,id+"");
            sa.setAlarm();
            	 if(shituid==2)//如果为日视图传过来的界面。
            	 {
            		 sa.CreatDayListView();
            	 }else if(shituid==3)//如果为周视图传过来的界面。
            	 {
            		 sa.weekListView();
            	 }else if(shituid==4)
            	 {   
            		 sa.bnsearch();
            		 sa.searchallcontent();
            	 }      
			break;
        case R.id.TextView04:          	 
            break;
		}
		dismiss();
	}
}
