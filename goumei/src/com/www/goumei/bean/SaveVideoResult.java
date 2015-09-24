package com.www.goumei.bean;

import java.io.Serializable;

public class SaveVideoResult implements Serializable {

	private String  savePath;
	private  String executeTime;
	public String getSavePath() {
		return savePath;
	}
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}
	public String getExecuteTime() {
		return executeTime;
	}
	public void setExecuteTime(String executeTime) {
		this.executeTime = executeTime;
	}
	
}
