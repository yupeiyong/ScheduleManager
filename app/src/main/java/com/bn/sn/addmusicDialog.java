package com.bn.sn;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

public class addmusicDialog  extends Dialog {

	public addmusicDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onCreate (Bundle savedInstanceState) 
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE); //去掉对话框的标题头部
		setContentView(R.layout.addmusic);	
	}
}
