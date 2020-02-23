package com.bn.sn;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;


public class ChangeDateDialog extends Dialog
{
	ScheduleManagerActivity smactivity;
	public ChangeDateDialog(Context context) {
		super(context);	
	}
	@Override
	public void onCreate (Bundle savedInstanceState) 
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE); //去掉对话框的标题头部
		setContentView(R.layout.selectdate);	
	}
}
