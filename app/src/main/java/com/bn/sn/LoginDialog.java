package com.bn.sn;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.*;
import android.view.View;
import android.view.View.OnClickListener;

public class LoginDialog extends Dialog implements OnClickListener 
{
	ScheduleManagerActivity activity;//将主Activity对象传入用于显示toast
	EditText etpwd;//输入密码框的引用
	Button okButton;
	Button cancelButton;
	String pwd;//保存传来的密码
	
	public LoginDialog(Context context,String pwd) {
		super(context);	
		this.pwd=pwd;
		this.activity=(ScheduleManagerActivity)context;
	}
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置标题
        this.setTitle(R.string.mgsinpwd);
        //显示登录的页面
        setContentView(R.layout.login);
        //得到各自的引用，添加监听
        okButton=(Button)findViewById(R.id.okbutton);
        cancelButton=(Button)findViewById(R.id.cancelbutton);
        etpwd=(EditText)findViewById(R.id.inputpwd);
        //添加监听
        okButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }
	@Override
	public void onClick(View v) 
	{
		switch(v.getId())
		{
		case R.id.okbutton://若点击的是确定则检查输入的密码
			String input=etpwd.getText().toString().trim();
			if(input!=null&&input.equals(pwd))
			{
				if(Constant.monthweekday==1)
    			{
					activity.gotoMonthView();
           		     // 返回到月视图
    			}else if(Constant.monthweekday==2)
    			{
    				activity.createWeek();// 返回到周视图
    				
    				
    			}else 
    			{
    				activity.createDay();//返回到日试图
    			}	 	
				//关闭对话框
				dismiss();
			}
			else
			{
				Toast.makeText(activity, "请输入正确密码", Toast.LENGTH_SHORT).show();
			}
			
			break;
		case R.id.cancelbutton:
			System.exit(0);
			break;
		}
	}


}
