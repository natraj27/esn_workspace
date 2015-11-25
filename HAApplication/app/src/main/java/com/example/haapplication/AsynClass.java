package com.example.haapplication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import android.os.AsyncTask;
import android.util.Log;

public class AsynClass extends AsyncTask<Void, Void, Void>{

	public static int serverport = 2001;
	Socket clientSocket;
	ServerSocket server;
	PrintWriter outPrintWriter;
	BufferedReader inputReader;
	String ret;
	boolean serverWorkingCondition;

	@Override
	protected Void doInBackground(Void... params) {
		//TODO Auto-generated method stub
		return null;
	}

	public void writeToStream(String message) {
		System.out.println(message);
		try {
			serverWorkingCondition = true;
			clientSocket = new Socket(Global.manualIP, serverport);
			inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			outPrintWriter = new PrintWriter(clientSocket.getOutputStream(), true);
			outPrintWriter.println(message);
			outPrintWriter.flush();
			outPrintWriter.close();
			clientSocket.close();
			Log.w(message, "Sent value");
		} catch (Exception e) {
			serverWorkingCondition = false;
			Log.i("AsynkTask", "writeToStream : Writing failed");
		}  
	}

	public void writeToStreamcontroller(String message) {
		System.out.println(message);

		try
		{
			serverWorkingCondition = true;
			clientSocket = new Socket(Global.manualIP, 2004);
			inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			outPrintWriter = new PrintWriter(clientSocket.getOutputStream(), true);
			outPrintWriter.println(message);
			outPrintWriter.flush();
			outPrintWriter.close();
			clientSocket.close();
			Log.w(message, "Sent value");

		} catch (Exception e)
		{
			serverWorkingCondition = false;
			Log.i("AsynkTask", "writeToStream : Writing failed");
		}  
	}


	public String readFromStream() {

		try
		{
			server = new ServerSocket(2002);
			Socket s = server.accept();
			inputReader = new BufferedReader(new InputStreamReader(s.getInputStream()));
			if (server.isBound()) {
				System.out.println("server is opened");

				Log.i("AsynkTask", "readFromStream : Reading message");
				ret=inputReader.readLine();	
				Log.i("AsynkTask", "readFromStream : read "+ret);
				s.close();
				server.close();
				return ret;

			}
			else
			{
				Log.i("AsynkTask", "readFromStream : Cannot Read, Socket is closed");
			}
		}
		catch (Exception e)
		{
			Log.i("AsynkTask", "readFromStream : Reading failed"+e.getClass());
		}
		return ret;
	}
}