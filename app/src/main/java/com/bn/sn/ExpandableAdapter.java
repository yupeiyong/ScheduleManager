package com.bn.sn;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableAdapter extends BaseExpandableListAdapter
{
	//主activity的对象引用
	ScheduleManagerActivity amActivity;
	//分组名称以(id#类型名)的形式给出，以方便再查询
	List<String> groupName;
	//各分组的子数据
	List<List<String>> childName;
	//构造器
	public ExpandableAdapter(Context amActivity,List<String> groupName,List<List<String>> childName)
	{
		this.amActivity=(ScheduleManagerActivity)amActivity;//得到赋值后面得到view对象用
		this.groupName=groupName;
		this.childName=childName;
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childName.get(groupPosition).get(childPosition).split("#")[1];
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean arg2, View arg3,
			ViewGroup arg4) 
	{
		View view = amActivity.getLayoutInflater().inflate(R.layout.childitem, null);
		TextView tv = (TextView)view.findViewById(R.id.childLabel);
		//将对应编号的list取出再取相应的值，分割取出后面字符串内容
		String ss = childName.get(groupPosition).get(childPosition).split("#")[1];
		tv.setText(ss);
		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return childName.get(groupPosition).size();//相应分组的子项数量
	}

	@Override
	public Object getGroup(int arg0) {
		return groupName.get(arg0).split("#")[1];
	}

	@Override
	public int getGroupCount() {
		return groupName.size();//分组数
	}

	@Override
	public long getGroupId(int arg0) {
		return arg0;
	}

	@Override
	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) 
	{
		//将分组的xml实例化赋值，将其返回类列表
		View view = amActivity.getLayoutInflater().inflate(R.layout.groupitem, null);
		TextView tv = (TextView)view.findViewById(R.id.groupLabel);
		String ss = groupName.get(arg0).split("#")[1];//将得到的id#类型名数据分割，取后面名字
		tv.setText(ss);
		return view;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}

}
