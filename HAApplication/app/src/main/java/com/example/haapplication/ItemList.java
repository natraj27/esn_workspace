package com.example.haapplication;

import java.util.ArrayList;
public class ItemList 
{
	ArrayList<String> roomName = new ArrayList<String>();
	ArrayList<String> roomState = new ArrayList<String>();
	ArrayList<String> deviceName = new ArrayList<String>();
	ArrayList<String> deviceType = new ArrayList<String>();
	ArrayList<String> deviceState = new ArrayList<String>();
	
	public ArrayList<String> getroomName() {
		return roomName;
	}
	public void setroomName(String roomname) {
		this.roomName.add(roomname);
	}
	public ArrayList<String> getroomState() {
		return roomState;
	}
	public void setroomState(String roomstate) {
		this.roomState.add(roomstate);
	}
	public ArrayList<String> getdeviceName() {
		return deviceName;
	}
	public void setdeviceName(String devicename) {
		this.deviceName.add(devicename);
	}
	public ArrayList<String> getdeviceType() {
		return deviceType;
	}
	public void setdeviceType(String devicetype) {
		this.deviceType.add(devicetype);
	}
	public ArrayList<String> getdeviceState() {
		return deviceState;
	}
	public void setdeviceState(String devicestate) {
		this.deviceState.add(devicestate);
	}
	
	
}
