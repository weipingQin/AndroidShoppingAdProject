package com.www.goumei.bean;

import java.io.Serializable;

/**
 * 主题实体类
 * 
 * @author Eric.Chen
 * 
 */
public class ThemeBean implements Serializable {

	private int themeiD;
	private String name;
	private String cover;
	private String text;
	private String description;
	private int hits;
	private boolean isPublish;
	private int sort;
	private int height;
	private int width;

	public int getThemeiD() {
		return themeiD;
	}

	public void setThemeiD(int themeiD) {
		this.themeiD = themeiD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}

	public boolean isPublish() {
		return isPublish;
	}

	public void setPublish(boolean isPublish) {
		this.isPublish = isPublish;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

}
