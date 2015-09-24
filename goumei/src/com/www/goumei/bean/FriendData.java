package com.www.goumei.bean;

import java.io.Serializable;

import android.R.integer;

public class FriendData implements Serializable {
	/*
	 * \"iD\": 1, \"displayName\": \"穆轻寒\", \"telphone\": \"15618275259\",
	 * \"password\": \"123456\", \"headsculpture\": \"https:
	 * //ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1477579747,
	 * 1739345058&fm=116&gp=0.jpg\", \"sex\": 1, \"aear\": \"中国上海\",
	 * \"birthday\": \"1991-06-09\", \"constellation\": \"双子座\",
	 * \"individualitySignature\": \"回首望，灯火依旧，人已陌路\", \"certificationState\": 0,
	 * \"shopID\": -2147483648, \"shopAuditStatus\": -2147483648, \"fansCount\":
	 * 2, \"videosCount\": 7, \"commentsCount\": 0, \"attentionsCount\": 2,
	 * \"isAttention\": 1, \"videoPraiseCount\": 0, \"isAttentionEachOther\": 0
	 */
	int iD;
	String displayName;
	String telphone;
	String headsculpture;
	int sex;
	String aear;
	String birthday;
	String constellation;
	String individualitySignature;
	String certificationState;
	int shopID;
	int shopAuditStatus;
	int fansCount;
	int videosCount;
	int commentsCount;
	int attentionsCount;
	int isAttention;
	int videoPraiseCount;
	int isAttentionEachOther;
	public int getID() {
		return iD;
	}
	public void setID(int iD) {
		this.iD = iD;
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
	public String getHeadsculpture() {
		return headsculpture;
	}
	public void setHeadsculpture(String headsculpture) {
		this.headsculpture = headsculpture;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getAear() {
		return aear;
	}
	public void setAear(String aear) {
		this.aear = aear;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getConstellation() {
		return constellation;
	}
	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}
	public String getIndividualitySignature() {
		return individualitySignature;
	}
	public void setIndividualitySignature(String individualitySignature) {
		this.individualitySignature = individualitySignature;
	}
	public String getCertificationState() {
		return certificationState;
	}
	public void setCertificationState(String certificationState) {
		this.certificationState = certificationState;
	}
	public int getShopID() {
		return shopID;
	}
	public void setShopID(int shopID) {
		this.shopID = shopID;
	}
	public int getShopAuditStatus() {
		return shopAuditStatus;
	}
	public void setShopAuditStatus(int shopAuditStatus) {
		this.shopAuditStatus = shopAuditStatus;
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
