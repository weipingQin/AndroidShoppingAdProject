package com.www.goumei.http.req;

import java.io.Serializable;

import com.www.goumei.http.ReqBase;
/**
 * 关注列表
 * @author json
 *
 */
public class AttentionReq extends ReqBase implements Serializable{
	private String ID;
	private int PageNo;
	public AttentionReq(String id,int pageNo){
		this.ID=id;
		this.PageNo=pageNo;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public int getPageNo() {
		return PageNo;
	}
	public void setPageNo(int pageNo) {
		PageNo = pageNo;
	}
	
}
