package com.www.goumei.bean;

/**
 * 用户实体类
 * 
 * @author Eric.Chen
 * 
 */
public class UserInfo {
	private int ID;
	private String displayName;
	private String telphone;
	private int fansCount;
	private int videosCount;
	private int commentsCount;
	private int attentionsCount;
	private int isAttention;
	private int videoPraiseCount;
	private int isAttentionEachOther;

	

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public int getFansCount() {
		return fansCount;
	}

	public void setFansCount(int fansCount) {
		this.fansCount = fansCount;
	}

	public int getVideosCount() {
		return videosCount;
	}

	public void setVideosCount(int videosCount) {
		this.videosCount = videosCount;
	}

	public int getCommentsCount() {
		return commentsCount;
	}

	public void setCommentsCount(int commentsCount) {
		this.commentsCount = commentsCount;
	}

	public int getAttentionsCount() {
		return attentionsCount;
	}

	public void setAttentionsCount(int attentionsCount) {
		this.attentionsCount = attentionsCount;
	}

	public int getIsAttention() {
		return isAttention;
	}

	public void setIsAttention(int isAttention) {
		this.isAttention = isAttention;
	}

	public int getVideoPraiseCount() {
		return videoPraiseCount;
	}

	public void setVideoPraiseCount(int videoPraiseCount) {
		this.videoPraiseCount = videoPraiseCount;
	}

	public int getIsAttentionEachOther() {
		return isAttentionEachOther;
	}

	public void setIsAttentionEachOther(int isAttentionEachOther) {
		this.isAttentionEachOther = isAttentionEachOther;
	}

}
