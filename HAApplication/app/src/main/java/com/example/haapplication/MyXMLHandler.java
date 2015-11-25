package com.example.haapplication;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MyXMLHandler extends DefaultHandler
{
	public static ItemList itemList;
	public boolean current = false;
	public String currentValue = null;

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub

		current = true;

		if (localName.equals("Equipment_settings"))
		{
			System.out.println("entered the handler");

			/** Start */ 
			itemList = new ItemList();

		} 
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		current = false;

		if(localName.equals("room_name"))
		{
			itemList.setroomName(currentValue);
		}
		else if(localName.equals("room_state"))
		{
			itemList.setroomState(currentValue);
		}
		else if(localName.equals("device_name"))
		{
			itemList.setdeviceName(currentValue);
		}

		else if(localName.equals("device_state"))
		{
			itemList.setdeviceState(currentValue);
		}
		else if(localName.equals("device_type"))
		{
			itemList.setdeviceType(currentValue);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub

		if(current)
		{
			currentValue = new String(ch, start, length);
			current=false;
		}
	}
}
