package com.www.goumei.http.req;

import java.io.Serializable;

import com.www.goumei.http.ReqBase;
/**
 * 喜欢接口
 * @author json
 *
 */
public class PraiseMeReq extends ReqBase implements Serializable{
	private String UserID;
	private int PageNo;
	public PraiseMeReq(String createUserID,int pageNo){
		this.UserID=createUserID;
		this.PageNo=pageNo;
	}
	
	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public int getPageNo() {
		return PageNo;
	}
	public void setPageNo(int pageNo) {
		PageNo = pageNo;
	}
	
}
