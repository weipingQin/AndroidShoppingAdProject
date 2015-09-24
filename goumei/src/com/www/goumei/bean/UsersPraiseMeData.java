package com.www.goumei.bean;

/**
 * 喜欢实体
 * 
 * @author json
 * 
 */

public class UsersPraiseMeData {

	/*ID	Int				空
	IdentityNo	Int				身份标识
	DisplayName	String				用户昵称
	Headsculpture	String				头像
	PraiseVideoID	Int				喜欢我的视频的ID
	PraiseVideoCover	String				喜欢我的视频的封面
	PraiseTime	DateTime				喜欢我的时间
	PraiseTimeDescription	String				喜欢我的时间
	PraiseContent	String				喜欢我的内容*/
	int ID;
	int IdentityNo;
	String DisplayName;
	String PraiseVideoID;
	String Headsculpture;
	String PraiseVideoCover;
	String PraiseTime;
	String PraiseTimeDescription;
	String PraiseContent;
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public int getIdentityNo() {
		return IdentityNo;
	}
	public void setIdentityNo(int identityNo) {
		IdentityNo = identityNo;
	}
	public String getDisplayName() {
		return DisplayName;
	}
	public void setDisplayName(String displayName) {
		DisplayName = displayName;
	}
	public String getPraiseVideoID() {
		return PraiseVideoID;
	}
	public void setPraiseVideoID(String praiseVideoID) {
		PraiseVideoID = praiseVideoID;
	}
	public String getPraiseVideoCover() {
		return PraiseVideoCover;
	}
	public void setPraiseVideoCover(String praiseVideoCover) {
		PraiseVideoCover = praiseVideoCover;
	}
	public String getPraiseTime() {
		return PraiseTime;
	}
	public void setPraiseTime(String praiseTime) {
		PraiseTime = praiseTime;
	}
	public String getPraiseTimeDescription() {
		return PraiseTimeDescription;
	}
	public void setPraiseTimeDescription(String praiseTimeDescription) {
		PraiseTimeDescription = praiseTimeDescription;
	}
	public String getPraiseContent() {
		return PraiseContent;
	}
	public void setPraiseContent(String praiseContent) {
		PraiseContent = praiseContent;
	}
	public String getHeadsculpture() {
		return Headsculpture;
	}
	public void setHeadsculpture(String headsculpture) {
		Headsculpture = headsculpture;
	}

}
