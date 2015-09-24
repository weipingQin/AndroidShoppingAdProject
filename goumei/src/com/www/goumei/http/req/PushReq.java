package com.www.goumei.http.req;

import java.io.Serializable;

import com.www.goumei.http.ReqBase;
/**
 * 获取用户信息
 * @author json
 *
 */
public class PushReq extends ReqBase implements Serializable{
	private String PushUserID;
	private int PageNo;
	public PushReq(String id,int pageNo){
		this.PushUserID=id;
		this.PageNo=pageNo;
	}
	public String getPushUserID() {
		return PushUserID;
	}
	public void setPushUserID(String pushUserID) {
		PushUserID = pushUserID;
	}
	public int getPageNo() {
		return PageNo;
	}
	public void setPageNo(int pageNo) {
		PageNo = pageNo;
	}
	
	
}
