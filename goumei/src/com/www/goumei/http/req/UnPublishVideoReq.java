package com.www.goumei.http.req;

import java.io.Serializable;

import com.www.goumei.http.ReqBase;
/**
 * 草稿箱
 * @author json
 *
 */
public class UnPublishVideoReq extends ReqBase implements Serializable{
	private String CreateUserID;
	private int PageNo;
	private boolean IsPublish=false;
	public UnPublishVideoReq(String createUserID,int pageNo){
		this.CreateUserID=createUserID;
		this.PageNo=pageNo;
	}
	public String getCreateUserID() {
		return CreateUserID;
	}
	public void setCreateUserID(String createUserID) {
		CreateUserID = createUserID;
	}
	public int getPageNo() {
		return PageNo;
	}
	public void setPageNo(int pageNo) {
		PageNo = pageNo;
	}
	public boolean isIsPublish() {
		return IsPublish;
	}
	public void setIsPublish(boolean isPublish) {
		IsPublish = isPublish;
	}
	
}
