package com.www.goumei.bean;

/**
 * 帮助解析用户json的实体类
 * 
 * @author Administrator
 * 
 */
public class UsersInfo {
	private UserInfo user;
	private String executeTime;

	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}

	public String getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(String executeTime) {
		this.executeTime = executeTime;
	}

}
