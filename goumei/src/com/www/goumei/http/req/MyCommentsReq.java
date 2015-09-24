package com.www.goumei.http.req;

import java.io.Serializable;

import com.www.goumei.http.ReqBase;
/**
 * 评论列表请求接口
 * @author json
 *
 */
public class MyCommentsReq extends ReqBase implements Serializable{
	private String CreateUserID;
	private int PageNo;
	public MyCommentsReq(String createUserID,int pageNo){
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
	
}
