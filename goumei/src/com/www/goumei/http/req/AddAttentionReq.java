package com.www.goumei.http.req;

import java.io.Serializable;

import com.www.goumei.http.ReqBase;
/**
 * 添加关注
 * @author json
 *
 */
public class AddAttentionReq extends ReqBase implements Serializable{
	private String UserID;
	private String FansID;
	public AddAttentionReq(String id,String fansId){
		this.UserID=id;
		this.FansID=fansId;
	}
	public String getUserID() {
		return UserID;
	}
	public void setUserID(String userID) {
		UserID = userID;
	}
	public String getFansID() {
		return FansID;
	}
	public void setFansID(String fansID) {
		FansID = fansID;
	}
	
	
	
}
