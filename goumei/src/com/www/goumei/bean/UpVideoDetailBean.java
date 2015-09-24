package com.www.goumei.bean;

import java.io.Serializable;
import java.util.Date;

public class UpVideoDetailBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 上传用户 */
	int userID;
	/** 标题 */
	String title;
	/** 封面 */
	String cover;
	/** 点击次数 */
	int Hits;
	/** 点赞次数 */
	int praiseCount;
	/** 发布时间 */
	Date publishTime;
	/** 是否发布 */
	Boolean isPublish;
	/** 视频长度（时间） */
	int Duration;
	/** 主题ID */
	int themeID;
	/** 分类ID */
	int categoryID;
	/** 视频地址 */
	String url;
	/** 视频关联商铺链接地址 */
	String link;
	/** 视频关联商品描述 */
	String linkDescription;

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public int getHits() {
		return Hits;
	}

	public void setHits(int hits) {
		Hits = hits;
	}

	public int getPraiseCount() {
		return praiseCount;
	}

	public void setPraiseCount(int praiseCount) {
		this.praiseCount = praiseCount;
	}

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	public Boolean getIsPublish() {
		return isPublish;
	}

	public void setIsPublish(Boolean isPublish) {
		this.isPublish = isPublish;
	}

	public int getDuration() {
		return Duration;
	}

	public void setDuration(int duration) {
		Duration = duration;
	}

	public int getThemeID() {
		return themeID;
	}

	public void setThemeID(int themeID) {
		this.themeID = themeID;
	}

	public int getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getLinkDescription() {
		return linkDescription;
	}

	public void setLinkDescription(String linkDescription) {
		this.linkDescription = linkDescription;
	}

}
