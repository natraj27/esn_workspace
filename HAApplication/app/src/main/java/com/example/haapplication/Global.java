package com.example.haapplication;

import android.app.Application;

public class Global extends Application{

	public static String manualIP = "test";
	public static String ipAddress = "";
	public static int gettabnum = 0;
	public static boolean updateUI;
	public static boolean updateMedia;
	private static Global singleton;
	public static Global getInstance() {
		return singleton;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		singleton = this;
	}
}
