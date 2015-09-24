package com.www.goumei.http.req;

import java.io.Serializable;

import com.www.goumei.http.ReqBase;
/**
 * 获取用户信息
 * @author json
 *
 */
public class UserInfoReq extends ReqBase implements Serializable{
	private String ID;
	public UserInfoReq(String id){
		this.ID=id;
	}
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}
	
	
}
