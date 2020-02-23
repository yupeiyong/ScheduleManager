package com.bn.sn;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableAdapterER extends BaseExpandableListAdapter{
	ScheduleManagerActivity amActivity;
	List<String> groupArray;
	List<List<String>> childArray;
	public ExpandableAdapterER(Context amActivity,List<String> groupArray,List<List<String>> childArray)
	{
		this.amActivity=(ScheduleManagerActivity)amActivity;//得到赋值后面得到view对象用
		this.groupArray=groupArray;
		this.childArray=childArray;
	}
		@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
			
		return  childArray.get(groupPosition).get(childPosition); 
	}
	@Override
	public long getChildId(int groupPosition, int childPosition) { //每一项在 相应组里面的id
		// TODO Auto-generated method stub		
		return childPosition; 
	}
	@Override
	public View getChildView(int groupPosition, int childPosition,      //子项的视图
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = amActivity.getLayoutInflater().inflate(R.layout.childitem, null);
		TextView tv = (TextView)view.findViewById(R.id.childLabel);  //再其他类里面找到配置文件中的控件
		//将对应编号的list取出再取相应的值，分割取出后面字符串内容
		String ss = childArray.get(groupPosition).get(childPosition); 			
		tv.setText(ss);
		return view;
	}
	@Override
	public int getChildrenCount(int groupPosition) { // 选中那一组有多少子项
		// TODO Auto-generated method stub	
		return childArray.get(groupPosition).size();
	}
	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return  groupArray.get(groupPosition); 
	}
	@Override
	public int getGroupCount() {                //一共有多少组
		// TODO Auto-generated method stub
      	return groupArray.size();
	}

	@Override
	public long getGroupId(int groupPosition) {  //每组在列表中的位置的id。
		// TODO Auto-generated method stub	
		return groupPosition;
	}
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,   //组的视图
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = amActivity.getLayoutInflater().inflate(R.layout.childitem, null);
		TextView tv = (TextView)view.findViewById(R.id.childLabel);
		//将对应编号的list取出再取相应的值，分割取出后面字符串内容
		String ss = groupArray.get(groupPosition); 		
		tv.setText(ss);		
		return view;
	}
	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}


}
