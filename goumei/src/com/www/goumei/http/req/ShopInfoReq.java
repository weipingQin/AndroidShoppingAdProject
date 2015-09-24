package com.www.goumei.http.req;

import java.io.Serializable;

import com.www.goumei.http.ReqBase;
/**
 * 获取用户信息
 * @author json
 *
 */
public class ShopInfoReq extends ReqBase implements Serializable{
	private String ID;
	public ShopInfoReq(String id){
		this.ID=id;
	}
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}
	
	
}
