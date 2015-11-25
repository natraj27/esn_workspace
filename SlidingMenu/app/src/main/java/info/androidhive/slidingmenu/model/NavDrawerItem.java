package info.androidhive.slidingmenu.model;

public class NavDrawerItem {
	
	private String title;
	private int icon;
	private String count = "0";
	private String uname;


	// boolean to set visiblity of the counter
	private boolean isCounterVisible = false;
	
	public NavDrawerItem(){}

	public NavDrawerItem(String title){
		this.title = title;

	}
	

	public String getTitle(){
		return this.title;
	}
	

	
	public void setTitle(String title){
		this.title = title;
	}
	

}
