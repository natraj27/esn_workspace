package com.example.haapplication;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TestClass extends Activity{

	XMLParser parser;
	EditText urlText;
	HomeAppliances home;
	AsynClass async;
	String ipStore;
	boolean updateText;
	Button connect;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		parser = new XMLParser();
		urlText = (EditText) findViewById(R.id.editText1);
		connect = (Button) findViewById(R.id.button1);

		home =new HomeAppliances();
		async = new AsynClass();
		Global.manualIP = ipStore;
		urlText.setText(ipStore);		 
		urlText.setFocusable(false);
		urlText.setFocusableInTouchMode(false);
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(urlText.getWindowToken(), 0);

		urlText.setOnTouchListener(new View.OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.setFocusable(true);
				v.setFocusableInTouchMode(true);
				System.out.println("hey this is working");
				connect.setEnabled(true);
				return false;
			}
		});

		try
		{
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			String name = preferences.getString("Name","");
			System.out.println(name+"Hi i was looking for this");
			urlText.setText(name);
		}
		catch(Exception e)
		{
			System.out.println(e+"This is exception");
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void SetUrl(final View view)
	{
		System.out.println(urlText.getText());
		String t ="http://"+urlText.getText().toString()+"/status.xml";
		try{
			Global.manualIP = urlText.getText().toString();
			System.out.println(Global.manualIP + "this is what i was looking for");
			String xml = parser.getXmlFromUrl(t); // getting XML
			Document doc = parser.getDomElement(xml); // getting DOM element
			NodeList nl = doc.getElementsByTagName("ROOM");
			
			System.out.println("this works");
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("Name",urlText.getText().toString());
			editor.commit();
			dispatchDoneKey();
			connect.setEnabled(false);
			Global.updateUI = true;
			Global.updateMedia = true;
		}
		catch(Exception e)
		{
			System.out.println("sax parsing exception");

			System.out.println(e);
		}
	}

	public void rebootButtonAction(final View view)
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

		try{
			switch(view.getId())
			{
			case R.id.imageButton1:
			{
				async.writeToStream("REBOOT_CONTROLLER"+ '\0');
				break;
			}
			case R.id.ImageButton01:
			{
				async.writeToStream("REBOOT_OS"+ '\0');
				break;
			}
			case R.id.ImageButton02:
			{
				async.writeToStream("REBOOT_AGENTS"+ '\0');
				break;
			}
			default:
				break;
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	public void dispatchDoneKey() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		  imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
	}
	
	
}
