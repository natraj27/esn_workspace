package com.example.haapplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ListView;
import android.widget.ExpandableListView;

public class Media extends Activity{

	ListView listView ;
	Thread m_objThreadClient1;
	ArrayList<String> your_array_list = new ArrayList<String>();
	String[] state_split_info;
	Thread m_objThreadClient;
	static String URL = "http://10.10.0.1/media.xml";
	XMLParser parser;
	static final String KEY_COST = "SWITCH";
	static final String KEY_ITEM = "ROOM"; // parent node
	ArrayAdapter<String> arrayAdapter;
	AsynClass asyncClass;
	Timer mTimer;
	int position;
	Child child;

	/*our expandable adapter */
	ExpandableListAdapter expandableListAdapter;
	/*expandable list*/
	ExpandableListView expandableListView;
	/*list items*/
	ArrayList<Group> groups=new ArrayList<Group>();

	ArrayList<Child> childrens;
	ArrayList<Child> childrens1;

	ArrayList<Child> childrens2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.media_cont);

		listView = (ListView) findViewById(R.id.listView);
		parser = new XMLParser();
		asyncClass = new AsynClass();

		ParsingResult();
		/*instantiate adapter with our item list*/
		expandableListAdapter=new ExpandableListAdapter(this, groups);
		/*we get list view*/
		expandableListView=(ExpandableListView) findViewById(R.id.expandableListView1);



		/*set adapter to list view*/
		expandableListView.setAdapter(expandableListAdapter);
		expandableListView.setOnChildClickListener(new OnChildClickListener() {
			public boolean onChildClick(ExpandableListView expandablelistview,
					final View clickedView, int groupPosition, int childPosition, long childId) {
				try
				{
					asyncClass.writeToStreamcontroller("PLAY"+ position+'\0');
				}
				catch(Exception e)
				{
					System.out.println("Exception in network info");
				}
				return true;
			}
		});	
		expandableListView.setOnGroupExpandListener(new OnGroupExpandListener() {
			int previousItem = -1;
			public void onGroupExpand(int groupPosition) {
				position = groupPosition;
				if(groupPosition != previousItem )
					try
				{
						asyncClass.writeToStreamcontroller("PLAY"+ position+'\0');
						expandableListView.collapseGroup(previousItem );
						previousItem = groupPosition;
				}
				catch(Exception e)
				{
					System.out.println("Exception in network info");
				}		        
			}
		});
		
		mTimer = new Timer();
		mTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				try{
					if(Global.updateMedia){
						String url = "http://"+Global.manualIP+"/status.xml";
						String xml = parser.getXmlFromUrl(url); // getting XML
						ParsingResult();
					}
					else{					
					}
				}
				catch(Exception e)
				{
				}
			}
		}, 0, 10 );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void genarateData() {
		Group group;
		for (int i = 0; i < 1; i++) {

			childrens=new ArrayList<Child>();
			childrens.clear();

			childrens1=new ArrayList<Child>();
			childrens1.clear();

			childrens2=new ArrayList<Child>();
			childrens2.clear();

			String group1 = "Lights/Appliances";
			group=new Group("hey",group1, childrens);
			groups.add(group);

			childrens1.add(child);

			String group2 =  "Media";
			group=new Group("hey",group2, childrens1);
			groups.add(group);

			childrens2.add(child);

			String group3 =  "3 New Mails - Gmail";
			group=new Group("hey",group3, childrens1);
			groups.add(group);
		}
	}

	public void openXBMCapp(final View view)
	{
		PackageManager pm = getPackageManager();
		Intent intent = pm.getLaunchIntentForPackage("org.xbmc.android.remote");
		startActivity(intent);
	}

	public void ParsingResult()
	{
		Group group;
		try{
			ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
			final NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

			if (mWifi.isConnected()) {	
				System.out.println("this is working");

				ArrayList<HashMap<String, String>> menuItems = new ArrayList<HashMap<String, String>>();
				Global.updateMedia = false;
				String url = "http://"+Global.manualIP+"/media.xml";
				String xml = parser.getXmlFromUrl(url); // getting XML
				Document doc = parser.getDomElement(xml); // getting DOM element
				NodeList nl = doc.getElementsByTagName(KEY_ITEM);
				// looping through all item nodes <item>
				for (int i = 0; i < nl.getLength(); i++) {
					// creating new HashMap
					HashMap<String, String> map = new HashMap<String, String>();
					Element e = (Element) nl.item(i);
					map.put(KEY_COST, parser.getValue(e, KEY_COST));
					menuItems.add(map);
				}
				HashMap<String, String> str = menuItems.get(0);
				String split = str.toString();
				String[] separated = split.split(",");
				String ley = separated[0].substring(8);
				state_split_info = ley.split("###");
				System.out.println(state_split_info[0].concat("/0"));
				System.out.println(state_split_info[1]);
				System.out.println(state_split_info.length);
				//if(your_array_list.size()>0){
				your_array_list.clear();

				for(int i=0; i<state_split_info.length;i++)
				{

					childrens=new ArrayList<Child>();
					childrens.clear();
					for (int j = 0; j < 1; j++) {
						child=new Child(""+j, "I am Child "+j);
						childrens.add(child);
					}
					group=new Group("",state_split_info[i], childrens);
					groups.add(group);
					your_array_list.add(state_split_info[i]);
				}
				expandableListAdapter.notifyDataSetChanged();
			}
			else{
				your_array_list.clear();
				your_array_list.add("NO MEDIA");
				arrayAdapter.notifyDataSetChanged();
			}
		}
		catch(Exception e){
			System.out.println("Exception in network info");
		}
	}

	public void ControlMethod(final View view)
	{
		try
		{
			switch(view.getId())
			{
			case R.id.imageButton6:
			{
				asyncClass.writeToStreamcontroller("DECREASE_VOLUME" +'\0');
				break;
			}
			case R.id.imageButton2:
			{				
				asyncClass.writeToStreamcontroller("PAUSE_RESUME" +'\0');
				break;
			}
			case R.id.imageButton7:
			{
				asyncClass.writeToStreamcontroller("INCREASE_VOLUME" +'\0');
				break;
			}
			case R.id.imageView1:
			{
				asyncClass.writeToStreamcontroller("Backward" +'\0');
				break;
			}
			case R.id.imageButton4:
			{
				asyncClass.writeToStreamcontroller("Forward" +'\0');
				break;
			}
			case R.id.ImageButton01:
			{
				asyncClass.writeToStreamcontroller("TOGGLE_SUBTITLES" +'\0');
				break;
			}
			case R.id.imageButton1:
			{
				if(position > 0){
					position = position-1;
				}
				else{
					position = your_array_list.size()-1;
				}
				String posstr = Integer.toString(position);
				expandableListView.expandGroup(position);
				asyncClass.writeToStreamcontroller("PLAY"+posstr +'\0');
				break;
			}
			case R.id.button2:
			{
				asyncClass.writeToStreamcontroller("EXIT_OMXPLAYER" +'\0');
				expandableListView.collapseGroup(position);
				break;
			}
			case R.id.imageButton5:
			{
				if(position < your_array_list.size()-1)
				{
					position = position+1;
				}
				else
				{
					position=0;
				}
				System.out.println(position);
				System.out.println(your_array_list.size());

				String posstr = Integer.toString(position);
				expandableListView.expandGroup(position);
				asyncClass.writeToStreamcontroller("PLAY"+posstr +'\0');
				break;
			}
			default:
				break;
			}
		}
		catch(Exception e){
			System.out.println("Exception in network info");
		}
	}
}