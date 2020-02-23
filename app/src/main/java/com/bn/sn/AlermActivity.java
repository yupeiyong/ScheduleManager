package com.bn.sn;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AlermActivity extends Activity
{
	String eventTitle=null;
	MediaPlayer mp;//媒体播放器引用
	SharedPreferences songid;//存储现在铃声id
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//转到相应页面
		setContentView(R.layout.alermdialog);
		//接收intent传来的标题
		Intent intent=getIntent();
		eventTitle=intent.getStringExtra("title");
		//设置文本内容
		TextView titletv=(TextView)findViewById(R.id.showTitle);
		titletv.setText(eventTitle);
		//给确定按钮添加监听
		Button buttonok=(Button)findViewById(R.id.knowbutton);
		buttonok.setOnClickListener(
				new View.OnClickListener()
				{
					@Override
					public void onClick(View v) {
						//停止响铃
						mp.stop();//停止播放	
						//销毁activity
						AlermActivity.this.finish();
					}	
				}
		);
		
		//===============播放器设置===============
		songid=this.getSharedPreferences("bell",Context.MODE_PRIVATE);
	    int id=songid.getInt("bell", 0);//默认为0
		//如果以前有播放器则释放
		 if(mp!=null){mp.release();}
		 //创建新的播放器
		 mp=MediaPlayer.create(AlermActivity.this,Constant.SONGID[id]);
		 mp.setOnCompletionListener(//歌曲播放结束事件的监听器
				 new OnCompletionListener()
					{
						@Override
						public void onCompletion(MediaPlayer arg0) 
						{//歌曲播放结束停止播放并更新界面状态
							arg0.stop();
						}			
					}				 
		 );	
	    try 
		 {
			//进行播放前的准备工作，new方式创建的MediaPlayer一定需要prepare
			//否则报错,而creat方式设置的已经在准备状态不用再准备了
			//mp.prepare();
			//开始播放
			mp.start();
		 } catch (Exception e1) 
		 {					
			e1.printStackTrace();
		 }			
	}

}
