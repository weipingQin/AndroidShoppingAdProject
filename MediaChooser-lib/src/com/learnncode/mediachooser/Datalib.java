package com.learnncode.mediachooser;

import android.app.Application;

public class Datalib extends Application{

	public String lib_path;
	

	

	
	public String getlib_path(){return lib_path;}
	public void setlib_path(String path){this.lib_path=path;}
	@Override
	public void onCreate(){
		lib_path="";
		super.onCreate();
	}

}