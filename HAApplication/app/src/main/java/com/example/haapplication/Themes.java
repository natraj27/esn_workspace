package com.example.haapplication;

import java.io.IOException;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class Themes extends Activity
{
	Thread m_objThreadClient;
	AsynClass asyncClass;
	Button morning;
	Button afternoon;
	Button evening;
	Button night;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.themes);
		asyncClass = new AsynClass();
		morning=(Button)findViewById(R.id.button1);
		afternoon=(Button)findViewById(R.id.Button01);
		evening=(Button)findViewById(R.id.Button02);
		night=(Button)findViewById(R.id.Button03);
		try {
			Highlightthebuttontapped();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.print("Hey hi themes");
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void ApplytheTheme(final View view) throws IOException
	{
		Highlightthebuttontapped();

		try{
			switch(view.getId())
			{
			case R.id.button1:
			{
				morning.setBackgroundColor(Color.parseColor("#BDC3C7"));
				morning.setTextColor(Color.parseColor("#000000"));
				asyncClass.writeToStream("PROFMORNING" +'\0');
				break;
			}
			case R.id.Button01:
			{
				afternoon.setBackgroundColor(Color.parseColor("#BDC3C7"));
				afternoon.setTextColor(Color.parseColor("#000000"));
				asyncClass.writeToStream("PROFAFTERNOON" +'\0');
				break;
			}
			case R.id.Button02:
			{
				evening.setBackgroundColor(Color.parseColor("#BDC3C7"));
				evening.setTextColor(Color.parseColor("#000000"));
				asyncClass.writeToStream("PROFEVENING" +'\0');
				break;
			}
			case R.id.Button03:
			{
				night.setBackgroundColor(Color.parseColor("#BDC3C7"));
				night.setTextColor(Color.parseColor("#000000"));
				asyncClass.writeToStream("PROFNIGHT" +'\0');
				break;
			}
			default:
				break;
			}
		}
		catch (Exception e) {
			Log.i("AsynkTask", "writeToStream : Writing failed");
		}
	}

	public void Highlightthebuttontapped() throws IOException
	{
		morning.setBackgroundColor(Color.parseColor("#641F82"));
		afternoon.setBackgroundColor(Color.parseColor("#641F82"));
		evening.setBackgroundColor(Color.parseColor("#641F82"));
		night.setBackgroundColor(Color.parseColor("#641F82"));

		morning.setTextColor(Color.parseColor("#ffffff"));
		afternoon.setTextColor(Color.parseColor("#ffffff"));
		evening.setTextColor(Color.parseColor("#ffffff"));
		night.setTextColor(Color.parseColor("#ffffff"));
	}
	public void test()
	{
		System.out.println("yeah yeah");
		try{
			morning.post(new Runnable()
			{
				public void run()
				{
					System.out.println("yeah yeah yeah");
					morning.setBackgroundColor(Color.parseColor("#641F82"));
					afternoon.setBackgroundColor(Color.parseColor("#641F82"));
					evening.setBackgroundColor(Color.parseColor("#641F82"));
					night.setBackgroundColor(Color.parseColor("#641F82"));

					morning.setTextColor(Color.parseColor("#ffffff"));
					afternoon.setTextColor(Color.parseColor("#ffffff"));
					evening.setTextColor(Color.parseColor("#ffffff"));
					night.setTextColor(Color.parseColor("#ffffff"));
				}
			});
		}
		catch(Exception e)
		{

		}

	}
}
