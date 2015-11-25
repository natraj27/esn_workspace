package com.example.haapplication;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
	
	LayoutInflater inflater;
	
	/*list of group */
	private ArrayList<Group> groups;
	public ExpandableListAdapter(Context context,ArrayList<Group> groups) {
	super();
		this.groups=groups;
		inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	/**
	 * @param child
	 * @param group
	 *  use for adding item to list view
	 */
	public void addItem(Child child,Group group) {
		if(!groups.contains(group)) {
			groups.add(group);
		}
		int index=groups.indexOf(group);
		ArrayList<Child> ch=groups.get(index).getChildrens();
		ch.add(child);
		groups.get(index).setChildrens(ch);
	}

    public Child getChild(int groupPosition, int childPosition) {
    	ArrayList<Child> ch=groups.get(groupPosition).getChildrens();
        return ch.get(childPosition);
    }
    
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
     
	@Override
	public int getChildrenCount(int groupPosition) {
		ArrayList<Child> ch=groups.get(groupPosition).getChildrens();
		return ch.size();
	}
	
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
            View convertView, ViewGroup parent) {
    	Child child= (Child) getChild(groupPosition,childPosition);
    	TextView childName=null;
    	if(convertView==null) {
      	  convertView=inflater.inflate(R.layout.child_view, null);
      	   
      	}
    	//childName=(TextView) convertView.findViewById(R.id.textViewChildName);
    	//childName.setText(child.getChildName());
		return convertView;
    }
    public Group getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    public int getGroupCount() {
        return groups.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
            ViewGroup parent) {
    	TextView groupName = null;
    	Group group=(Group) getGroup(groupPosition);
    	if(convertView==null) {
    	  convertView=inflater.inflate(R.layout.group_view, null);    	   
    	}
    	groupName=(TextView) convertView.findViewById(R.id.textViewGroupName);
    	groupName.setText(group.getGroupName());
		groupName.setPadding(90,10, 10, 10);

        return convertView;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean hasStableIds() {
        return true;
    }
}

