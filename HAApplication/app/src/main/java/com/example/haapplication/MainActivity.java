package com.example.haapplication;

import java.util.Timer;
import java.util.TimerTask;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;

import android.app.TabActivity;
import android.view.Menu;
import android.view.View;

public class MainActivity extends TabActivity {

	Button wifiButton;
	Timer mTimer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		setContentView(R.layout.activity_main);
		wifiButton = (Button) findViewById(R.id.button111);
		Resources res = getResources(); // Resource object to get Drawables
		TabHost tabHost = getTabHost();  // The activity TabHost
		TabHost.TabSpec spec;  // Resusable TabSpec for each tab
		Intent intent;  // Reusable Intent for each tab

		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, HomeAppliances.class);

		spec = tabHost.newTabSpec("Home").setIndicator("Home",
				res.getDrawable(R.drawable.homeapp))
				.setContent(intent);
		tabHost.addTab(spec);
		
		intent = new Intent().setClass(this, Themes.class);
		spec = tabHost.newTabSpec("Themes").setIndicator("Themes",
				res.getDrawable(R.drawable.theme))
				.setContent(intent);
		tabHost.addTab(spec);
		
		intent = new Intent().setClass(this, Media.class);
		spec = tabHost.newTabSpec("Media").setIndicator("Media",
				res.getDrawable(R.drawable.movie))
				.setContent(intent);
		tabHost.addTab(spec);


		intent = new Intent().setClass(this, TestClass.class);
		spec = tabHost.newTabSpec("Settings").setIndicator("Settings",
				res.getDrawable(R.drawable.settings))
				.setContent(intent);
		tabHost.addTab(spec);

		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			TextView x = (TextView) tabHost.getTabWidget().getChildAt(i)
					.findViewById(android.R.id.title);
			x.setTextSize(25);
			x.setTextSize(10.0f);
			x.setTextColor(Color.WHITE);
		}
		if(Global.gettabnum == 0)
		{
			tabHost.setCurrentTab(0);
		}
		if(Global.gettabnum == 1)
		{
			tabHost.setCurrentTab(1);
		}
		if(Global.gettabnum == 2)
		{
			tabHost.setCurrentTab(2);
		}
		if(Global.gettabnum == 3)
		{
			tabHost.setCurrentTab(3);
		}
	//	tabHost.setBackgroundColor(Color.parseColor("#C54134"));
		tabHost.getTabWidget().getChildAt(1)
		.setBackgroundColor(Color.parseColor("#34495E"));
		tabHost.getTabWidget().getChildAt(0)
		.setBackgroundColor(Color.parseColor("#34495E"));
		tabHost.getTabWidget().getChildAt(2)
		.setBackgroundColor(Color.parseColor("#34495E"));
		tabHost.getTabWidget().getChildAt(3)
		.setBackgroundColor(Color.parseColor("#34495E"));

		/* Checking for Wi-Fi Connection and blocking the screen with a transparent Image . It will check every 1/10th of a second*/
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
