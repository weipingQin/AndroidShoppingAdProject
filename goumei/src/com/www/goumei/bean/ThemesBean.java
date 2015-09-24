package com.www.goumei.bean;

import java.io.Serializable;
import java.util.List;

public class ThemesBean implements Serializable {

	private List<ThemeBean> models;
	private int totalCounts;
	private String executeTime;

	public List<ThemeBean> getModels() {
		return models;
	}

	public void setModels(List<ThemeBean> models) {
		this.models = models;
	}

	public int getTotalCounts() {
		return totalCounts;
	}

	public void setTotalCounts(int totalCounts) {
		this.totalCounts = totalCounts;
	}

	public String getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(String executeTime) {
		this.executeTime = executeTime;
	}

}
