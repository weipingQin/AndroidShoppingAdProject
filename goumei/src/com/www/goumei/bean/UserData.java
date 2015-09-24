package com.www.goumei.bean;

import java.io.Serializable;

import com.www.goumei.http.ReqBase;
/*
 * 用户
 */
public class UserData extends ReqBase implements Serializable{
/**
 * \"iD\":1,
 * \"displayName\":\"穆轻寒\",
 * \"telphone\":\"15618275259\",
 * \"password\":\"123456\",
 * \"headsculpture\":\"https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1477579747,1739345058&fm=116&gp=0.jpg\",
 * \"sex\":1,
 * \"aear\":\"中国 上海\",
 * \"birthday\":\"1991-06-09\",
 * \"constellation\":\"双子座\",
 * \"individualitySignature\":\"回首望，灯火依旧，人已陌路\",
 * \"certificationState\":0,
 * \"shopID\":-2147483648,
 * \"shopAuditStatus\":-2147483648,
 * \"fansCount\":2,
 * \"videosCount\":7,
 * \"commentsCount\":0,
 * \"attentionsCount\":2,
 * \"isAttention\":1,
 * \"videoPraiseCount\":0,
 * \"isAttentionEachOther\":1
 */
	private int id;
	private String displayName;
	private String password;
	private String headsculpture;
	private int sex;
	private String aear;
	private String birthday;
	private String constellation;
	private String individualitySignature;
	/**
	 * 买家 = 0 卖家 = 1 认证中 = 2 
	 * 0代表未认证 1代码已认证 
	 */
	private int certificationState;
	private int shopID;
	private int shopAuditStatus;
	private int fansCount;
	private int videosCount;
	private int commentsCount;
	private int attentionsCount;
	private int videoPraiseCount;
	private int isAttentionEachOther;
	private int isAttention;
	public int getIsAttention() {
		return isAttention;
	}
	public void setIsAttention(int isAttention) {
		this.isAttention = isAttention;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	public int getCertificationState() {
		return certificationState;
	}
	public void setCertificationState(int certificationState) {
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

