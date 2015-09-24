package com.www.goumei.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 用来解析主题详情实体类
 * 
 * @author Administrator
 * 
 */
public class ThemesDetails implements Serializable {
	private List<ThemeDetails> models;
	private int totalCounts;
	private String executeTime;

	public List<ThemeDetails> getModels() {
		return models;
	}

	public void setModels(List<ThemeDetails> models) {
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
