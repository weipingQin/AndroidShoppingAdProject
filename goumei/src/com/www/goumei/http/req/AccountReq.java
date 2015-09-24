package com.www.goumei.http.req;

import java.io.Serializable;

import com.www.goumei.http.ReqBase;
/**
 * 获取用户信息
 * @author json
 *
 */
public class AccountReq extends ReqBase implements Serializable{
	private int PageNo;
	public AccountReq(int pageNo){
		this.PageNo=pageNo;
	}
	
	public int getPageNo() {
		return PageNo;
	}
	public void setPageNo(int pageNo) {
		PageNo = pageNo;
	}
	
	
}
