package com.www.goumei.bean;

public class Categories {
	// Primary，主键
	public int ID;
	// 分类名称
	public String Name;
	// 分类图标
	public String Icon;
	// 分类文字
	public String Text;
	// 分类描述
	public String Description;
	// 排序
	public int Sort;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getIcon() {
		return Icon;
	}

	public void setIcon(String icon) {
		Icon = icon;
	}

	public String getText() {
		return Text;
	}

	public void setText(String text) {
		Text = text;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public int getSort() {
		return Sort;
	}

	public void setSort(int sort) {
		Sort = sort;
	}

}
