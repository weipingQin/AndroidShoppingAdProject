package com.www.goumei.bean;

import java.io.Serializable;

public class Videos   implements   Serializable {
	/*ID	Int				Primary，主键
	Title	String				标题
	Cover	String				封面
	Hits	Int				点击次数
	PraiseCount	Int				点赞次数
	PublishTime	DateTime				发布时间
	IsPublish	Boolean				是否发布
	Duration	Int				视频长度（时间）
	UserID	Int				用户ID
	UserDisplayName	String				用户名
	UserHeadsculpture	String				用户头像
	UserCertificationState	Int				用户认证状态
	ThemeName	String				主题名称
	CategoryName	String				分类名称
	IsPraise	Int				是否喜欢了这个视频 1 - 是 0 - 否
	Link	String				视频关联商铺链接地址
	LinkDescription	String				视频关联商品描述
	Url	String				视频地址
	IsAttention	Int				是否关注了这个视频的用户 1 - 是 0 - 否*/
	public int ID;
	public String title;
	public String cover;
	public int hits;
	public int praiseCount;
	public String publishTime;
	public boolean isPublish;
	public int duration;
	public int userID;
	public String userDisplayName;
	public String userHeadsculpture;
	public int userCertificationState;
	public String themeName;
	public String categoryName;
	public int IsPraise;
	public String Link;
	public String LinkDescription;
	public String Url;
	public int IsAttention;
	public int themeID;
	public int categoryID;
	
	
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
		return hits;
	}
	public void setHits(int hits) {
		this.hits = hits;
	}
	public int getPraiseCount() {
		return praiseCount;
	}
	public void setPraiseCount(int praiseCount) {
		this.praiseCount = praiseCount;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public boolean isPublish() {
		return isPublish;
	}
	public void setPublish(boolean isPublish) {
		this.isPublish = isPublish;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public String getUserDisplayName() {
		return userDisplayName;
	}
	public void setUserDisplayName(String userDisplayName) {
		this.userDisplayName = userDisplayName;
	}
	public String getUserHeadsculpture() {
		return userHeadsculpture;
	}
	public void setUserHeadsculpture(String userHeadsculpture) {
		this.userHeadsculpture = userHeadsculpture;
	}
	public int getUserCertificationState() {
		return userCertificationState;
	}
	public void setUserCertificationState(int userCertificationState) {
		this.userCertificationState = userCertificationState;
	}
	public String getThemeName() {
		return themeName;
	}
	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public int getIsPraise() {
		return IsPraise;
	}
	public void setIsPraise(int isPraise) {
		IsPraise = isPraise;
	}
	public String getLink() {
		return Link;
	}
	public void setLink(String link) {
		Link = link;
	}
	public String getLinkDescription() {
		return LinkDescription;
	}
	public void setLinkDescription(String linkDescription) {
		LinkDescription = linkDescription;
	}
	public String getUrl() {
		return Url;
	}
	public void setUrl(String url) {
		Url = url;
	}
	public int getIsAttention() {
		return IsAttention;
	}
	public void setIsAttention(int isAttention) {
		IsAttention = isAttention;
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
}
