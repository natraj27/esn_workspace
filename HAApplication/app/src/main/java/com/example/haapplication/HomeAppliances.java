package com.example.haapplication;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeAppliances extends Activity{

	ScrollView scrollView;

	ImageButton hallTab;
	ImageButton bedroomTab;
	ImageButton kitchenTab;
	ImageButton childrenTab;
	ImageButton theatreTab;

	ImageButton switchButton1;
	ImageButton switchButton2;
	ImageButton switchButton3;
	ImageButton switchButton4;

	Button wifiButton;


	TextView hallText;
	TextView bedRoomText;
	TextView kitchenText;
	TextView childrenText;
	TextView theatreText;

	TextView bulbName1;
	TextView bulbName2;
	TextView bulbName3;
	TextView bulbName4;

	Thread m_objThreadClient;
	String compareString;
	String[] state_split_info;

	boolean bulb1isOn;
	boolean bulb2isOn;
	boolean bulb3isOn;
	boolean bulb4isOn;
	Timer mTimer;
	AsynClass asyncClass;

	ProgressBar progress;
	String updateString;
	int getbuttonNum;

	ItemList itemList;

	ArrayList<String> roomName;
	ArrayList<String> roomState ;
	ArrayList<String> deviceName;
	ArrayList<String> deviceState ;
	ArrayList<String> deviceType ;
	Themes theme;

	// XML node keys
	static final String KEY_ITEM = "ROOM"; // parent node
	static final String KEY_ID = "NAME";
	static final String KEY_NAME = "ROOM_NUMBER";

	static final String KEY_COST = "SWITCH";
	static final String KEY_DESC = "STATE";

	XMLParser parser;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homeappliances);
		this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		asyncClass = new AsynClass();
		parser = new XMLParser();
		theme = new Themes();
		compareString = "hall";
		scrollView=(ScrollView)findViewById(R.id.scrollView1);
		hallTab=(ImageButton)findViewById(R.id.ImageButton01);
		bedroomTab=(ImageButton)findViewById(R.id.ImageButton02);
		kitchenTab=(ImageButton)findViewById(R.id.ImageButton05);
		childrenTab=(ImageButton)findViewById(R.id.ImageButton03);
		theatreTab=(ImageButton)findViewById(R.id.ImageButton04);
		wifiButton=(Button)findViewById(R.id.button2);
		//progress=(ProgressBar)findViewById(R.id.progressBar1);


		hallText = (TextView) findViewById(R.id.textView10);
		bedRoomText = (TextView) findViewById(R.id.TextView05);
		kitchenText = (TextView) findViewById(R.id.textView7);
		childrenText = (TextView) findViewById(R.id.TextView06);
		theatreText = (TextView) findViewById(R.id.textView9);

		bulbName1 = (TextView) findViewById(R.id.textView2);
		bulbName2 = (TextView) findViewById(R.id.textView4);
		bulbName3 = (TextView) findViewById(R.id.textView3);
		bulbName4 = (TextView) findViewById(R.id.textView5);


		switchButton1 = (ImageButton) findViewById(R.id.imageView1);
		switchButton2 = (ImageButton) findViewById(R.id.ImageButton05);
		switchButton3 = (ImageButton) findViewById(R.id.ImageButton06);
		switchButton4 = (ImageButton) findViewById(R.id.ImageButton07);
		scrollView.setBackgroundColor(Color.parseColor("#FFA995"));

		bulb1isOn = false;
		bulb2isOn = false;
		bulb3isOn = false;
		bulb4isOn = false;
		//Global.updateUI = false;

		try
		{
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			String name = preferences.getString("Name","");
			System.out.println(name+"Hi i was looking for this");
		}
		catch(Exception e)
		{
			System.out.println(e+"This is exception");
		}
		try{
			String url = "http://"+Global.manualIP+"/status.xml";
			String xml = parser.getXmlFromUrl(url); // getting XML
			Document doc = parser.getDomElement(xml); // getting DOM element
			NodeList nl = doc.getElementsByTagName("ROOM");
			saxParsing();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				change();		
			}
		}, 500);

		mTimer = new Timer();
		mTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				try{
					if(Global.updateUI){
						String url = "http://"+Global.manualIP+"/status.xml";
						String xml = parser.getXmlFromUrl(url); // getting XML
						saxParsing();
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

	public void saxParsing()
	{
		try
		{
			Global.updateUI = false;
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			String name = preferences.getString("Name","");
			System.out.println(name+"Hi i was looking for this");
			Global.manualIP = name;
		}
		catch(Exception e)
		{
			System.out.println(e+"This is exception");
		}
		try 
		{
			theme.test();

			/** Handling XML */
			System.out.println("entered here dude sax parsing");
			System.out.println("http://"+ Global.manualIP +"/electric_config.xml");

			SAXParserFactory spf = SAXParserFactory.newInstance();

			SAXParser sp = spf.newSAXParser();

			XMLReader xr = sp.getXMLReader();

			/** Send URL to parse XML Tags */

			//URL sourceUrl = new URL("http://10.10.0.1/electric_config.xml");

			URL sourceUrl = new URL("http://"+ Global.manualIP +"/electric_config.xml");

			/** Create handler to handle XML Tags ( extends DefaultHandler ) */

			MyXMLHandler myXMLHandler = new MyXMLHandler();

			xr.setContentHandler(myXMLHandler);

			xr.parse(new InputSource(sourceUrl.openStream()));

		} catch (Exception e) {
			System.out.println("XML Pasing Excpetion = " + e);
		}

		itemList = MyXMLHandler.itemList;

		roomName = itemList.getroomName();
		roomState = itemList.getroomState();
		deviceName = itemList.getdeviceName();
		deviceState = itemList.getdeviceState();
		deviceType = itemList.getdeviceType();
		hallText.setText(roomName.get(0));
		bedRoomText.setText(roomName.get(1));
		kitchenText.setText(roomName.get(2));
		childrenText.setText(roomName.get(3));
		theatreText.setText(roomName.get(4));
		if(roomState.get(1).equals("enable"))
		{
			//	hallTab.setEnabled(true);
			//compareString = "bedroom";
		}
		//		else
		//		{
		//			if(roomState.get(1).equals("enable"))
		//			{
		//				compareString = "bedroom";
		//			}
		//			else if(roomState.get(2).equals("enable"))
		//			{
		//				compareString = "kitchen";
		//			}
		//			else if(roomState.get(3).equals("enable"))
		//			{
		//				compareString = "children";
		//			}
		//			hallTab.setEnabled(false);
		//		}
		//		if(roomState.get(1).equals("enable"))
		//		{
		//			bedroomTab.setEnabled(true) ;
		//		}
		//		else
		//		{
		//			bedroomTab.setEnabled(false);
		//			if(roomState.get(0).equals("enable"))
		//			{
		//				compareString = "hall";
		//			}
		//			else if(roomState.get(2).equals("enable"))
		//			{
		//				compareString = "kitchen";
		//			}
		//			else if(roomState.get(3).equals("enable"))
		//			{
		//				compareString = "children";
		//			}
		//		}
		//		if(roomState.get(2).equals("enable"))
		//		{
		//			kitchenTab.setEnabled(true);
		//		}
		//		else
		//		{
		//			kitchenTab.setEnabled(false);
		//			if(roomState.get(1).equals("enable"))
		//			{
		//				compareString = "bedroom";
		//			}
		//			else if(roomState.get(0).equals("enable"))
		//			{
		//				compareString = "hall";
		//			}
		//			else if(roomState.get(3).equals("enable"))
		//			{
		//				compareString = "children";
		//			}
		//		}
		//		if(roomState.get(3).equals("enable"))
		//		{
		//			childrenTab.setEnabled(true);
		//		}
		//		else
		//		{
		//			childrenTab.setEnabled(false);
		//			if(roomState.get(1).equals("enable"))
		//			{
		//				compareString = "bedroom";
		//			}
		//			else if(roomState.get(2).equals("enable"))
		//			{
		//				compareString = "kitchen";
		//			}
		//			else if(roomState.get(0).equals("enable"))
		//			{
		//				compareString = "hall";
		//			}
		//		}
		//		if(roomState.get(4).equals("enable"))
		//		{
		//			theatreTab.setEnabled(true);
		//		}
		//		else
		//		{
		//			theatreTab.setEnabled(false);
		//			if(roomState.get(1).equals("enable"))
		//			{
		//				compareString = "bedroom";
		//			}
		//			else if(roomState.get(2).equals("enable"))
		//			{
		//				compareString = "kitchen";
		//			}
		//			else if(roomState.get(3).equals("enable"))
		//			{
		//				compareString = "children";
		//			}
		//		}

		System.out.println(roomName);
		System.out.println(roomState);
		System.out.println(deviceName);
		System.out.println(deviceState);
		System.out.println(deviceType);
		ParsingResult();

		//			}
		//
		//		});
		//		m_objThreadClient.start();
	}

	public void ActionMethodforbuttons(final View view)
	{
		try{
			System.out.println("wifi connection is there");

			switch(view.getId())
			{
			case R.id.ImageButton01:
			{
				compareString = "hall";
				saxParsing();

				break;
			}
			case R.id.ImageButton02:
			{
				compareString = "bedroom";
				saxParsing();
				break;
			}
			case R.id.ImageButton06:
			{
				compareString = "kitchen";
				saxParsing();
				break;
			}
			case R.id.ImageButton03:
			{
				compareString = "children";
				saxParsing();
				break;
			}
			case R.id.ImageButton04:
			{
				compareString = "theator";
				saxParsing();
				break;
			}
			default:
				break;
			}
		}
		catch (Exception e) {
			Log.i("AsynkTask", "writeToStream : Writing failed");
		}
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				change();
			}
		}, 100);
	}

	public void change()
	{
		if	(compareString.equals("hall"))
		{
			hallTab.setImageResource(R.drawable.hallon);
			bedroomTab.setImageResource(R.drawable.mediaoff);


			hallTab.setBackgroundColor(Color.parseColor("#E74C3C"));
			bedroomTab.setBackgroundColor(Color.parseColor("#FFA995"));
			//			kitchenTab.setBackgroundColor(Color.parseColor("#FFA995"));
			//			childrenTab.setBackgroundColor(Color.parseColor("#FFA995"));
			//			theatreTab.setBackgroundColor(Color.parseColor("#FFA995"));
			//
			hallText.setBackgroundColor(Color.parseColor("#E74C3C"));
			bedRoomText.setBackgroundColor(Color.parseColor("#FFA995"));
			//			kitchenText.setBackgroundColor(Color.parseColor("#FFA995"));
			//			childrenText.setBackgroundColor(Color.parseColor("#FFA995"));
			//			theatreText.setBackgroundColor(Color.parseColor("#FFA995"));
			//ParsingResult();

		}
		else if	(compareString.equals("bedroom"))
		{
			hallTab.setImageResource(R.drawable.halloff);
			bedroomTab.setImageResource(R.drawable.mediaon);
			//			hallTab.setBackgroundColor(Color.parseColor("#FFA995"));
			bedroomTab.setBackgroundColor(Color.parseColor("#E74C3C"));
			hallTab.setBackgroundColor(Color.parseColor("#FFA995"));
			//			childrenTab.setBackgroundColor(Color.parseColor("#FFA995"));
			//			theatreTab.setBackgroundColor(Color.parseColor("#FFA995"));
			//
			hallText.setBackgroundColor(Color.parseColor("#FFA995"));
			bedRoomText.setBackgroundColor(Color.parseColor("#E74C3C"));
			//kitchenText.setBackgroundColor(Color.parseColor("#FFA995"));
			//			childrenText.setBackgroundColor(Color.parseColor("#FFA995"));
			//			theatreText.setBackgroundColor(Color.parseColor("#FFA995"));
		}
		else if	(compareString.equals("kitchen"))
		{
			bedroomTab.setImageResource(R.drawable.halloff);
			kitchenTab.setImageResource(R.drawable.mediaon);
			//			hallTab.setBackgroundColor(Color.parseColor("#FFA995"));
			bedroomTab.setBackgroundColor(Color.parseColor("#FFA995"));
			kitchenTab.setBackgroundColor(Color.parseColor("#E74C3C"));
			//			childrenTab.setBackgroundColor(Color.parseColor("#FFA995"));
			//			theatreTab.setBackgroundColor(Color.parseColor("#FFA995"));
			//
			//			hallText.setBackgroundColor(Color.parseColor("#FFA995"));
			bedRoomText.setBackgroundColor(Color.parseColor("#FFA995"));
			kitchenText.setBackgroundColor(Color.parseColor("#E74C3C"));
			//			childrenText.setBackgroundColor(Color.parseColor("#FFA995"));
			//			theatreText.setBackgroundColor(Color.parseColor("#FFA995"));
		}
		else if	(compareString.equals("children"))
		{

			hallTab.setBackgroundColor(Color.parseColor("#FFA995"));
			bedroomTab.setBackgroundColor(Color.parseColor("#FFA995"));
			kitchenTab.setBackgroundColor(Color.parseColor("#FFA995"));
			childrenTab.setBackgroundColor(Color.parseColor("#E74C3C"));
			theatreTab.setBackgroundColor(Color.parseColor("#FFA995"));

			hallText.setBackgroundColor(Color.parseColor("#FFA995"));
			bedRoomText.setBackgroundColor(Color.parseColor("#FFA995"));
			kitchenText.setBackgroundColor(Color.parseColor("#FFA995"));
			childrenText.setBackgroundColor(Color.parseColor("#E74C3C"));
			theatreText.setBackgroundColor(Color.parseColor("#FFA995"));
		}
		else if	(compareString.equals("theator")){

			hallTab.setBackgroundColor(Color.parseColor("#FFA995"));
			bedroomTab.setBackgroundColor(Color.parseColor("#FFA995"));
			kitchenTab.setBackgroundColor(Color.parseColor("#FFA995"));
			childrenTab.setBackgroundColor(Color.parseColor("#FFA995"));
			theatreTab.setBackgroundColor(Color.parseColor("#E74C3C"));

			hallText.setBackgroundColor(Color.parseColor("#FFA995"));
			bedRoomText.setBackgroundColor(Color.parseColor("#FFA995"));
			kitchenText.setBackgroundColor(Color.parseColor("#FFA995"));
			childrenText.setBackgroundColor(Color.parseColor("#FFA995"));
			theatreText.setBackgroundColor(Color.parseColor("#E74C3C"));
		}
	}

	public void bulbFunctionalityMethod(final View view)
	{
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (mWifi.isConnected()) {	
			try{
				String url = "http://"+Global.manualIP+"/status.xml";
				String xml = parser.getXmlFromUrl(url); // getting XML
				Document doc = parser.getDomElement(xml); // getting DOM element
				NodeList nl = doc.getElementsByTagName("ROOM");
				wifiButton.setVisibility(View.VISIBLE);
				//	progress.setVisibility(View.VISIBLE);
				//				switchButton1.setEnabled(false);
				//				switchButton2.setEnabled(false);
				//				switchButton3.setEnabled(false);
				//				switchButton4.setEnabled(false);
				if(compareString.equals("hall")){
					switch(view.getId())
					{
					case R.id.imageView1:
					{
						if(!bulb1isOn){
							updateString="ON";
							asyncClass.writeToStream("ATSW11ON" +'\0');
						}
						else
						{
							updateString="OFF";
							asyncClass.writeToStream("ATSW11OF" +'\0');
						}
						getbuttonNum = 1;
						break;
					}
					case R.id.ImageButton05:
					{
						if(!bulb2isOn){
							asyncClass.writeToStream("ATSW12ON" +'\0');
							updateString="ON";
						}
						else
						{
							asyncClass.writeToStream("ATSW12OF" +'\0');
							updateString="OFF";
						}
						getbuttonNum = 2;
						break;
					}
					case R.id.ImageButton06:
					{
						if(!bulb3isOn){
							asyncClass.writeToStream("ATSW13ON" +'\0');
							updateString="ON";
						}
						else
						{
							asyncClass.writeToStream("ATSW13OF" +'\0');
							updateString="OFF";
						}
						getbuttonNum = 3;
						break;
					}
					case R.id.ImageButton07:
					{
						if(!bulb4isOn){
							asyncClass.writeToStream("ATSW14ON" +'\0');
							updateString="ON";
						}
						else
						{
							asyncClass.writeToStream("ATSW14OF" +'\0');
							updateString="OFF";
						}
						getbuttonNum = 4;
						break;
					}
					default:
						break;
					}
				}
				else if(compareString.equals("bedroom")){
					switch(view.getId())
					{
					case R.id.imageView1:
					{
						if(!bulb1isOn){
							asyncClass.writeToStream("ATSW21ON" +'\0');
							updateString="ON";
						}
						else
						{
							asyncClass.writeToStream("ATSW21OF" +'\0');
							updateString="OFF";
						}
						getbuttonNum = 5;
						break;
					}
					case R.id.ImageButton05:
					{
						if(!bulb2isOn){
							asyncClass.writeToStream("ATSW22ON" +'\0');
							updateString="ON";
						}
						else
						{
							asyncClass.writeToStream("ATSW22OF" +'\0');
							updateString="OFF";
						}
						getbuttonNum = 6;
						break;
					}
					case R.id.ImageButton06:
					{
						if(!bulb3isOn){
							asyncClass.writeToStream("ATSW23ON" +'\0');
							updateString="ON";
						}
						else
						{
							asyncClass.writeToStream("ATSW23OF" +'\0');
							updateString="OFF";
						}
						getbuttonNum = 7;
						break;
					}
					case R.id.ImageButton07:
					{
						if(!bulb4isOn){
							asyncClass.writeToStream("ATSW24ON" +'\0');
							updateString="ON";
						}
						else
						{
							asyncClass.writeToStream("ATSW24OF" +'\0');
							updateString="OFF";
						}
						getbuttonNum = 8;
						break;
					}
					default:
						break;
					}
				}

				else if(compareString.equals("kitchen")){
					switch(view.getId())
					{
					case R.id.imageView1:
					{
						if(!bulb1isOn){
							asyncClass.writeToStream("ATSW31ON" +'\0');
							updateString="ON";
						}
						else
						{
							asyncClass.writeToStream("ATSW31OF" +'\0');
							updateString="OFF";
						}
						getbuttonNum = 9;
						break;
					}
					case R.id.ImageButton05:
					{
						if(!bulb2isOn){
							asyncClass.writeToStream("ATSW32ON" +'\0');
							updateString="ON";
						}
						else
						{
							asyncClass.writeToStream("ATSW32OF" +'\0');
							updateString="OFF";
						}
						getbuttonNum = 10;
						break;
					}
					case R.id.ImageButton06:
					{
						if(!bulb3isOn){
							asyncClass.writeToStream("ATSW33ON" +'\0');
							updateString="ON";
						}
						else
						{
							asyncClass.writeToStream("ATSW33OF" +'\0');
							updateString="OFF";
						}
						getbuttonNum = 11;
						break;
					}
					case R.id.ImageButton07:
					{
						if(!bulb4isOn){
							asyncClass.writeToStream("ATSW34ON" +'\0');
							updateString="ON";
						}
						else
						{
							asyncClass.writeToStream("ATSW34OF" +'\0');
							updateString="OFF";
						}
						getbuttonNum = 12;
						break;
					}
					default:
						break;
					}
				}

				else if(compareString.equals("children")){
					switch(view.getId())
					{
					case R.id.imageView1:
					{
						if(!bulb1isOn){
							asyncClass.writeToStream("ATSW41ON" +'\0');
							updateString="ON";
						}
						else
						{
							asyncClass.writeToStream("ATSW41OF" +'\0');
							updateString="OFF";
						}
						getbuttonNum = 13;
						break;
					}
					case R.id.ImageButton05:
					{
						if(!bulb2isOn){
							asyncClass.writeToStream("ATSW42ON" +'\0');
							updateString="ON";
						}
						else
						{
							asyncClass.writeToStream("ATSW42OF" +'\0');
							updateString="OFF";
						}
						getbuttonNum = 14;
						break;
					}
					case R.id.ImageButton06:
					{
						if(!bulb3isOn){
							asyncClass.writeToStream("ATSW43ON" +'\0');
							updateString="ON";
						}
						else
						{
							asyncClass.writeToStream("ATSW43OF" +'\0');
							updateString="OFF";
						}
						getbuttonNum = 15;
						break;
					}
					case R.id.ImageButton07:
					{
						if(!bulb4isOn){
							asyncClass.writeToStream("ATSW44ON" +'\0');
							updateString="ON";
						}
						else
						{
							asyncClass.writeToStream("ATSW44OF" +'\0');
							updateString="OFF";
						}
						getbuttonNum = 16;
						break;
					}
					default:
						break;
					}
				}
			}
			catch (Exception e) {
				Log.i("AsynkTask", "writeToStream : Writing failed");
			}
			try{
				String url = "http://"+Global.manualIP+"/status.xml";
				String xml = parser.getXmlFromUrl(url); // getting XML
				Document doc = parser.getDomElement(xml); // getting DOM element
				NodeList nl = doc.getElementsByTagName("ROOM");
				final Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						//						if(Global.updateUI){
						//						saxParsing();
						//						}
						//	else{
						ParsingResult();
						//}
					}
				}, 400);
			}
			catch (Exception e) 
			{
				Log.i("AsynkTask", "writeToStream : Writing failed");
				Toast.makeText(HomeAppliances.this, "Pls Check your internet Connection!!!", Toast.LENGTH_SHORT).show();
			}
		}
		else{
			// Do NOthing....
		}
	}

	public void ParsingResult()
	{
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		final NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (mWifi.isConnected()) {	
			System.out.println("wifi connection is there");
			System.out.println(Constants.URL);
			try
			{
				wifiButton.setVisibility(View.VISIBLE);
				//progress.setVisibility(View.VISIBLE);
				ArrayList<HashMap<String, String>> menuItems = new ArrayList<HashMap<String, String>>();

				String url = "http://"+Global.manualIP+"/status.xml";
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

				state_split_info = ley.split(" ");
				//				System.out.println(state_split_info[0]);
				//				System.out.println(state_split_info[1]);
				System.out.println(state_split_info[12]);
				System.out.println(state_split_info[13]);
				System.out.println(state_split_info[14]);
				System.out.println(state_split_info[15]);				
				UpdateButtonImages();
			}
			catch (Exception e)
			{
				Log.i("AsynkTask", "writeToStream : Writing failed");
				Toast.makeText(HomeAppliances.this, "Pls Check your internet Connection!!!", Toast.LENGTH_SHORT).show();
			}  
		}
		else
		{
			System.out.println("wifi connection is not there");
		}	
	}
	public void getStatus(View view) throws IOException
	{
		try{
			String url = "http://"+Global.manualIP+"/status.xml";
			String xml = parser.getXmlFromUrl(url); // getting XML
			Document doc = parser.getDomElement(xml); // getting DOM element
			NodeList nl = doc.getElementsByTagName("ROOM");
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					saxParsing();
				}
			}, 10);
		}
		catch (Exception e)
		{
			Toast.makeText(HomeAppliances.this, "Pls Check your internet Connection!!!", Toast.LENGTH_SHORT).show();
			Log.i("AsynkTask", "writeToStream : Writing failed");
		}  
	}

	public void UpdateButtonImages()
	{
		try
		{
			switchButton1.post(new Runnable()
			{
				public void run()
				{
					if(compareString.equals("hall"))
					{
						bulbName1.setText(deviceName.get(0));
						bulbName2.setText(deviceName.get(1));
						bulbName3.setText(deviceName.get(2));
						bulbName4.setText(deviceName.get(3));

						if (state_split_info[0].equals("ON"))
						{
							bulb1isOn = true;
							if(deviceType.get(0).equals("none"))
							{
								switchButton1.setEnabled(false);
							}
							else if(deviceType.get(0).equals("Bulb"))
							{
								switchButton1.setImageResource(R.drawable.buttonon);
							}
							else if(deviceType.get(0).equals("Fan"))
							{
								switchButton1.setImageResource(R.drawable.fan);
							}
						}
						else
						{
							bulb1isOn = false;
							if(deviceType.get(0).equals("none"))
							{
								switchButton1.setEnabled(false);
							}
							else if(deviceType.get(0).equals("Bulb"))
							{
								switchButton1.setImageResource(R.drawable.buttonoff);
							}
							else if(deviceType.get(0).equals("Fan"))
							{
								switchButton1.setImageResource(R.drawable.fanoff);
							}
						}
						if (state_split_info[1].equals("ON"))
						{
							bulb2isOn = true;
							if(deviceType.get(1).equals("none"))
							{
								switchButton2.setEnabled(false);
							}
							else if(deviceType.get(1).equals("Bulb"))
							{
								switchButton2.setImageResource(R.drawable.buttonon);
							}
							else if(deviceType.get(1).equals("Fan"))
							{
								switchButton2.setImageResource(R.drawable.fan);
							}
						}
						else
						{
							bulb2isOn = false;
							if(deviceType.get(1).equals("none"))
							{
								switchButton2.setEnabled(false);
							}
							else if(deviceType.get(1).equals("Bulb"))
							{
								switchButton2.setImageResource(R.drawable.buttonoff);
							}
							else if(deviceType.get(1).equals("Fan"))
							{
								switchButton2.setImageResource(R.drawable.fanoff);
							}
						}

						if (state_split_info[2].equals("ON"))
						{
							bulb3isOn = true;
							if(deviceType.get(2).equals("none"))
							{
								switchButton3.setEnabled(false);
							}
							else if(deviceType.get(2).equals("Bulb"))
							{
								switchButton3.setImageResource(R.drawable.buttonon);
							}
							else if(deviceType.get(2).equals("Fan"))
							{
								switchButton3.setImageResource(R.drawable.fan);
							}
						}
						else
						{
							bulb3isOn = false;
							if(deviceType.get(2).equals("none"))
							{
								switchButton3.setEnabled(false);
							}
							else if(deviceType.get(2).equals("Bulb"))
							{
								switchButton3.setImageResource(R.drawable.buttonoff);
							}
							else if(deviceType.get(2).equals("Fan"))
							{
								switchButton3.setImageResource(R.drawable.fanoff);
							}
						}
						if (state_split_info[3].equals("ON"))
						{
							bulb4isOn = true;
							if(deviceType.get(3).equals("none"))
							{
								switchButton4.setEnabled(false);
							}
							else if(deviceType.get(3).equals("Bulb"))
							{
								switchButton4.setImageResource(R.drawable.buttonon);
							}
							else if(deviceType.get(3).equals("Fan"))
							{
								switchButton4.setImageResource(R.drawable.fan);
							}
						}
						else
						{
							bulb4isOn = false;
							if(deviceType.get(3).equals("none"))
							{
								switchButton4.setEnabled(false);
							}
							else if(deviceType.get(3).equals("Bulb"))
							{
								switchButton4.setImageResource(R.drawable.buttonoff);
							}
							else if(deviceType.get(3).equals("Fan"))
							{
								switchButton4.setImageResource(R.drawable.fanoff);
							}
						}
					}
					else if(compareString.equals("bedroom"))
					{
						bulbName1.setText(deviceName.get(4));
						bulbName2.setText(deviceName.get(5));
						bulbName3.setText(deviceName.get(6));
						bulbName4.setText(deviceName.get(7));
						if (state_split_info[4].equals("ON"))
						{
							bulb1isOn = true;
							if(deviceType.get(4).equals("none"))
							{
								switchButton1.setEnabled(false);
							}
							else if(deviceType.get(4).equals("Bulb"))
							{
								switchButton1.setImageResource(R.drawable.buttonon);
							}
							else if(deviceType.get(4).equals("Fan"))
							{
								switchButton1.setImageResource(R.drawable.fan);
							}
						}
						else
						{
							bulb1isOn = false;
							if(deviceType.get(4).equals("none"))
							{
								switchButton1.setEnabled(false);
							}
							else if(deviceType.get(4).equals("Bulb"))
							{
								switchButton1.setImageResource(R.drawable.buttonoff);
								switchButton1.setEnabled(true);
							}
							else if(deviceType.get(4).equals("Fan"))
							{
								switchButton1.setImageResource(R.drawable.fanoff);
								switchButton1.setEnabled(true);
							}
						}
						if (state_split_info[5].equals("ON"))
						{
							bulb2isOn = true;
							if(deviceType.get(5).equals("none"))
							{
								switchButton2.setEnabled(false);
							}
							else if(deviceType.get(5).equals("Bulb"))
							{
								switchButton2.setImageResource(R.drawable.buttonon);
								switchButton2.setEnabled(true);
							}
							else if(deviceType.get(5).equals("Fan"))
							{
								switchButton2.setImageResource(R.drawable.fan);
								switchButton2.setEnabled(true);
							}
						}
						else
						{
							bulb2isOn = false;
							if(deviceType.get(5).equals("none"))
							{
								switchButton2.setEnabled(false);
							}
							else if(deviceType.get(5).equals("Bulb"))
							{
								switchButton2.setImageResource(R.drawable.buttonoff);
								switchButton2.setEnabled(true);
							}
							else if(deviceType.get(5).equals("Fan"))
							{
								switchButton2.setImageResource(R.drawable.fanoff);
								switchButton2.setEnabled(true);
							}
						}

						if (state_split_info[6].equals("ON"))
						{
							bulb3isOn = true;
							if(deviceType.get(6).equals("none"))
							{
								switchButton3.setEnabled(false);
							}
							else if(deviceType.get(6).equals("Bulb"))
							{
								switchButton3.setImageResource(R.drawable.buttonon);
								switchButton3.setEnabled(true);
							}
							else if(deviceType.get(6).equals("Fan"))
							{
								switchButton3.setImageResource(R.drawable.fan);
								switchButton3.setEnabled(true);
							}
						}
						else
						{
							bulb3isOn = false;
							if(deviceType.get(6).equals("none"))
							{
								switchButton3.setEnabled(false);
							}
							else if(deviceType.get(6).equals("Bulb"))
							{
								switchButton3.setImageResource(R.drawable.buttonoff);
								switchButton3.setEnabled(true);
							}
							else if(deviceType.get(6).equals("Fan"))
							{
								switchButton3.setImageResource(R.drawable.fanoff);
								switchButton3.setEnabled(true);
							}
						}

						if (state_split_info[7].equals("ON"))
						{
							bulb4isOn = true;
							if(deviceType.get(7).equals("none"))
							{
								switchButton4.setEnabled(false);
							}
							else if(deviceType.get(7).equals("Bulb"))
							{
								switchButton4.setImageResource(R.drawable.buttonon);
								switchButton4.setEnabled(true);
							}
							else if(deviceType.get(7).equals("Fan"))
							{
								switchButton4.setImageResource(R.drawable.fan);
								switchButton4.setEnabled(true);
							}
						}
						else
						{
							bulb4isOn = false;
							if(deviceType.get(7).equals("none"))
							{
								switchButton4.setEnabled(false);
							}
							else if(deviceType.get(7).equals("Bulb"))
							{
								switchButton4.setImageResource(R.drawable.buttonoff);
								switchButton4.setEnabled(true);
							}
							else if(deviceType.get(7).equals("Fan"))
							{
								switchButton4.setImageResource(R.drawable.fanoff);
								switchButton4.setEnabled(true);
							}
						}
					}	
					else if(compareString.equals("kitchen"))
					{
						bulbName1.setText(deviceName.get(8));
						bulbName2.setText(deviceName.get(9));
						bulbName3.setText(deviceName.get(10));
						bulbName4.setText(deviceName.get(11));
						if (state_split_info[8].equals("ON"))
						{
							bulb1isOn = true;
							if(deviceType.get(8).equals("none"))
							{
								switchButton1.setEnabled(false);
							}
							else if(deviceType.get(8).equals("Bulb"))
							{
								switchButton1.setImageResource(R.drawable.buttonon);
								switchButton1.setEnabled(true);
							}
							else if(deviceType.get(8).equals("Fan"))
							{
								switchButton1.setImageResource(R.drawable.fan);
								switchButton1.setEnabled(true);
							}
						}
						else
						{
							bulb1isOn = false;
							if(deviceType.get(8).equals("none"))
							{
								switchButton1.setEnabled(false);
							}
							else if(deviceType.get(8).equals("Bulb"))
							{
								switchButton1.setImageResource(R.drawable.buttonoff);
								switchButton1.setEnabled(true);
							}
							else if(deviceType.get(8).equals("Fan"))
							{
								switchButton1.setImageResource(R.drawable.fanoff);
								switchButton1.setEnabled(true);
							}
						}
						if (state_split_info[9].equals("ON"))
						{
							bulb2isOn = true;
							if(deviceType.get(9).equals("none"))
							{
								switchButton2.setEnabled(false);
							}
							else if(deviceType.get(9).equals("Bulb"))
							{
								switchButton2.setImageResource(R.drawable.buttonon);
								switchButton2.setEnabled(true);
							}
							else if(deviceType.get(9).equals("Fan"))
							{
								switchButton2.setImageResource(R.drawable.fan);
								switchButton2.setEnabled(true);
							}
						}
						else
						{
							bulb2isOn = false;
							if(deviceType.get(9).equals("none"))
							{
								switchButton2.setEnabled(false);
							}
							else if(deviceType.get(9).equals("Bulb"))
							{
								switchButton2.setImageResource(R.drawable.buttonoff);
								switchButton2.setEnabled(true);
							}
							else if(deviceType.get(9).equals("Fan"))
							{
								switchButton2.setImageResource(R.drawable.fanoff);
								switchButton2.setEnabled(true);
							}
						}

						if (state_split_info[10].equals("ON"))
						{
							bulb3isOn = true;
							if(deviceType.get(10).equals("none"))
							{
								switchButton3.setEnabled(false);
							}
							else if(deviceType.get(10).equals("Bulb"))
							{
								switchButton3.setImageResource(R.drawable.buttonon);
								switchButton3.setEnabled(true);
							}
							else if(deviceType.get(10).equals("Fan"))
							{
								switchButton3.setImageResource(R.drawable.fan);
								switchButton3.setEnabled(true);
							}
						}
						else
						{
							bulb3isOn = false;
							if(deviceType.get(10).equals("none"))
							{
								switchButton3.setEnabled(false);
							}
							else if(deviceType.get(10).equals("Bulb"))
							{
								switchButton3.setImageResource(R.drawable.buttonoff);
								switchButton3.setEnabled(true);
							}
							else if(deviceType.get(10).equals("Fan"))
							{
								switchButton3.setImageResource(R.drawable.fanoff);
								switchButton3.setEnabled(true);
							}
						}

						if (state_split_info[11].equals("ON"))
						{
							bulb4isOn = true;
							if(deviceType.get(11).equals("none"))
							{
								switchButton4.setEnabled(false);
							}
							else if(deviceType.get(11).equals("Bulb"))
							{
								switchButton4.setImageResource(R.drawable.buttonon);
								switchButton4.setEnabled(true);
							}
							else if(deviceType.get(11).equals("Fan"))
							{
								switchButton4.setImageResource(R.drawable.fan);
								switchButton4.setEnabled(true);
							}
						}
						else
						{
							bulb4isOn = false;
							if(deviceType.get(11).equals("none"))
							{
								switchButton4.setEnabled(false);
							}
							else if(deviceType.get(11).equals("Bulb"))
							{
								switchButton4.setImageResource(R.drawable.buttonoff);
								switchButton4.setEnabled(true);
							}
							else if(deviceType.get(11).equals("Fan"))
							{
								switchButton4.setImageResource(R.drawable.fanoff);
								switchButton4.setEnabled(true);
							}
						}
					}	

					else if(compareString.equals("children"))
					{
						bulbName1.setText(deviceName.get(12));
						bulbName2.setText(deviceName.get(13));
						bulbName3.setText(deviceName.get(14));
						bulbName4.setText(deviceName.get(15));
						if (state_split_info[12].equals("ON"))
						{
							bulb1isOn = true;
							if(deviceType.get(12).equals("none"))
							{
								switchButton1.setEnabled(false);
							}
							else if(deviceType.get(12).equals("Bulb"))
							{
								switchButton1.setImageResource(R.drawable.buttonon);
								switchButton1.setEnabled(true);
							}
							else if(deviceType.get(12).equals("Fan"))
							{
								switchButton1.setImageResource(R.drawable.fan);
								switchButton1.setEnabled(true);
							}
						}
						else
						{
							bulb1isOn = false;
							if(deviceType.get(12).equals("none"))
							{
								switchButton1.setEnabled(false);
							}
							else if(deviceType.get(12).equals("Bulb"))
							{
								switchButton1.setImageResource(R.drawable.buttonoff);
								switchButton1.setEnabled(true);
							}
							else if(deviceType.get(12).equals("Fan"))
							{
								switchButton1.setImageResource(R.drawable.fanoff);
								switchButton1.setEnabled(true);
							}
						}
						if (state_split_info[13].equals("ON"))
						{
							bulb2isOn = true;
							if(deviceType.get(13).equals("none"))
							{
								switchButton2.setEnabled(false);
							}
							else if(deviceType.get(13).equals("Bulb"))
							{
								switchButton2.setImageResource(R.drawable.buttonon);
								switchButton2.setEnabled(true);
							}
							else if(deviceType.get(13).equals("Fan"))
							{
								switchButton2.setImageResource(R.drawable.fan);
								switchButton2.setEnabled(true);
							}
						}
						else
						{
							bulb2isOn = false;
							if(deviceType.get(13).equals("none"))
							{
								switchButton2.setEnabled(false);
							}
							else if(deviceType.get(13).equals("Bulb"))
							{
								switchButton2.setImageResource(R.drawable.buttonoff);
								switchButton2.setEnabled(true);
							}
							else if(deviceType.get(13).equals("Fan"))
							{
								switchButton2.setImageResource(R.drawable.fanoff);
								switchButton2.setEnabled(true);
							}
						}

						if (state_split_info[14].equals("ON"))
						{
							bulb3isOn = true;
							if(deviceType.get(14).equals("none"))
							{
								switchButton3.setEnabled(false);
							}
							else if(deviceType.get(14).equals("Bulb"))
							{
								switchButton3.setImageResource(R.drawable.buttonon);
								switchButton3.setEnabled(true);
							}
							else if(deviceType.get(14).equals("Fan"))
							{
								switchButton3.setImageResource(R.drawable.fan);
								switchButton3.setEnabled(true);
							}
						}
						else
						{
							bulb3isOn = false;
							if(deviceType.get(14).equals("none"))
							{
								switchButton3.setEnabled(false);
							}
							else if(deviceType.get(14).equals("Bulb"))
							{
								switchButton3.setImageResource(R.drawable.buttonoff);
								switchButton3.setEnabled(true);
							}
							else if(deviceType.get(14).equals("Fan"))
							{
								switchButton3.setImageResource(R.drawable.fanoff);
								switchButton3.setEnabled(true);
							}
						}

						if (state_split_info[15].equals("ON"))
						{
							bulb4isOn = true;
							if(deviceType.get(15).equals("none"))
							{
								switchButton4.setEnabled(false);
							}
							else if(deviceType.get(15).equals("Bulb"))
							{
								switchButton4.setImageResource(R.drawable.buttonon);
								switchButton4.setEnabled(true);
							}
							else if(deviceType.get(15).equals("Fan"))
							{
								switchButton4.setImageResource(R.drawable.fan);
								switchButton4.setEnabled(true);
							}
						}
						else
						{
							bulb4isOn = false;
							if(deviceType.get(15).equals("none"))
							{
								switchButton4.setEnabled(false);
							}
							else if(deviceType.get(15).equals("Bulb"))
							{
								switchButton4.setImageResource(R.drawable.buttonoff);
								switchButton4.setEnabled(true);
							}
							else if(deviceType.get(15).equals("Fan"))
							{
								switchButton4.setImageResource(R.drawable.fanoff);
								switchButton4.setEnabled(true);
							}
						}
					}
					else if(compareString.equals("theator"))
					{
						bulbName1.setText(deviceName.get(16));
						bulbName2.setText(deviceName.get(17));
						bulbName3.setText(deviceName.get(18));
						bulbName4.setText(deviceName.get(19));
						if (state_split_info[16].equals("ON"))
						{
							bulb1isOn = true;
							if(deviceType.get(16).equals("none"))
							{
								switchButton1.setEnabled(false);
							}
							else if(deviceType.get(16).equals("Bulb"))
							{
								switchButton1.setImageResource(R.drawable.buttonon);
								switchButton1.setEnabled(true);
							}
							else if(deviceType.get(16).equals("Fan"))
							{
								switchButton1.setImageResource(R.drawable.fan);
								switchButton1.setEnabled(true);
							}
						}
						else
						{
							bulb1isOn = false;
							if(deviceType.get(16).equals("none"))
							{
								switchButton1.setEnabled(false);
							}
							else if(deviceType.get(16).equals("Bulb"))
							{
								switchButton1.setImageResource(R.drawable.buttonoff);
								switchButton1.setEnabled(true);
							}
							else if(deviceType.get(16).equals("Fan"))
							{
								switchButton1.setImageResource(R.drawable.fanoff);
								switchButton1.setEnabled(true);
							}
						}
						if (state_split_info[17].equals("ON"))
						{
							bulb2isOn = true;
							if(deviceType.get(17).equals("none"))
							{
								switchButton2.setEnabled(false);
							}
							else if(deviceType.get(17).equals("Bulb"))
							{
								switchButton2.setImageResource(R.drawable.buttonon);
								switchButton2.setEnabled(true);
							}
							else if(deviceType.get(17).equals("Fan"))
							{
								switchButton2.setImageResource(R.drawable.fan);
								switchButton2.setEnabled(true);
							}
						}
						else
						{
							bulb2isOn = false;
							if(deviceType.get(17).equals("none"))
							{
								switchButton2.setEnabled(false);
							}
							else if(deviceType.get(17).equals("Bulb"))
							{
								switchButton2.setImageResource(R.drawable.buttonoff);
								switchButton2.setEnabled(true);
							}
							else if(deviceType.get(17).equals("Fan"))
							{
								switchButton2.setImageResource(R.drawable.fanoff);
								switchButton2.setEnabled(true);
							}
						}
						if (state_split_info[18].equals("ON"))
						{
							bulb3isOn = true;
							if(deviceType.get(18).equals("none"))
							{
								switchButton3.setEnabled(false);
							}
							else if(deviceType.get(18).equals("Bulb"))
							{
								switchButton3.setImageResource(R.drawable.buttonon);
								switchButton3.setEnabled(true);
							}
							else if(deviceType.get(18).equals("Fan"))
							{
								switchButton3.setImageResource(R.drawable.fan);
								switchButton3.setEnabled(true);
							}
						}
						else
						{
							bulb3isOn = false;
							if(deviceType.get(18).equals("none"))
							{
								switchButton3.setEnabled(false);
							}
							else if(deviceType.get(18).equals("Bulb"))
							{
								switchButton3.setImageResource(R.drawable.buttonoff);
								switchButton3.setEnabled(true);
							}
							else if(deviceType.get(18).equals("Fan"))
							{
								switchButton3.setImageResource(R.drawable.fanoff);
								switchButton3.setEnabled(true);
							}
						}
						if (state_split_info[19].equals("ON"))
						{
							bulb4isOn = true;
							if(deviceType.get(19).equals("none"))
							{
								switchButton4.setEnabled(false);
							}
							else if(deviceType.get(19).equals("Bulb"))
							{
								switchButton4.setImageResource(R.drawable.buttonon);
								switchButton4.setEnabled(true);
							}
							else if(deviceType.get(19).equals("Fan"))
							{
								switchButton4.setImageResource(R.drawable.fan);
								switchButton4.setEnabled(true);
							}
						}
						else
						{
							bulb4isOn = false;
							if(deviceType.get(19).equals("none"))
							{
								switchButton4.setEnabled(false);
							}
							else if(deviceType.get(19).equals("Bulb"))
							{
								switchButton4.setImageResource(R.drawable.buttonoff);
								switchButton4.setEnabled(true);
							}
							else if(deviceType.get(19).equals("Fan"))
							{
								switchButton4.setImageResource(R.drawable.fanoff);
								switchButton4.setEnabled(true);
							}
						}
					}
					wifiButton.setVisibility(View.INVISIBLE);
					//	progress.setVisibility(View.INVISIBLE);
				}
			});
		}
		catch (Exception e) {
			Log.i("AsynkTask", "writeToStream : Writing failed");
		}
	}
}
