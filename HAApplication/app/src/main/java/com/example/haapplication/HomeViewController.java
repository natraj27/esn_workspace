package com.example.haapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.w3c.dom.Document;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;

public class HomeViewController extends Activity{

	Button home;
	String ipAddress;
	Global global;
	Button wifiButton;
	Timer mTimer;
	XMLParser parser;
	private static final String LOG_TAG = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homeviewcontroller);
		this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		wifiButton = (Button) findViewById(R.id.button111);
		parser = new XMLParser();
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						ipAddress =  inetAddress.getHostAddress().toString();
						System.out.println(ipAddress);
						Global.ipAddress = ipAddress;
					}
				}
			}
		}
		catch (IOException ex) {
			Log.e(LOG_TAG, ex.toString());
		}
		System.out.println(Global.ipAddress);

		/* Checking for Wi-Fi Connection and blocking the screen with a transparent Image . It will check every 1/10th of a second*/
		try
		{
			mTimer = new Timer();
			mTimer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {

					ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
					NetworkInfo mWifi = connManager
							.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

					if (mWifi.isConnected()) {
						wifiButton.post(new Runnable()
						{
							public void run() {
								wifiButton.setVisibility(View.INVISIBLE);
							}});
					} else {
						wifiButton.post(new Runnable()
						{
							public void run() {
								wifiButton.setVisibility(View.VISIBLE);
							}
						});
					}	
				}
			}, 0, 10 );
		}
		catch(Exception e)
		{
			System.out.println("Exception in network info");
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void move(final View view)
	{
		try
		{
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
			String url = "http://"+Global.manualIP+"/status.xml";
			String xml = parser.getXmlFromUrl(url); // getting XML
			Document doc = parser.getDomElement(xml); // getting DOM element
			switch(view.getId())
			{
			case R.id.button1:
			{
				Global.gettabnum = 0;
				break;
			}
			case R.id.ImageButton01:
			{
				Global.gettabnum = 1;
				break;
			}
			case R.id.ImageButton02:
			{
				Global.gettabnum = 2;
				break;
			}
			case R.id.ImageButton03:
			{
				Global.gettabnum = 3;
				break;
			}
			default:
				break;
			}
			Intent myIntent = new Intent(HomeViewController.this,MainActivity.class);
			startActivity(myIntent);
		}
		catch(Exception e)
		{
            Toast.makeText(HomeViewController.this, "Pls Check your internet Connection!!!", Toast.LENGTH_SHORT).show();
			System.out.println("Exception in network info");
		}
	}
	
	public void Settings(final View view)
	{
		try{

		}
		catch(Exception e)
		{
			System.out.print("fkjbhkf");
		}
	}
}