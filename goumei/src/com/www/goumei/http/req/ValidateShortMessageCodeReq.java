package com.www.goumei.http.req;

import java.io.Serializable;

import com.www.goumei.http.ReqBase;
/**
 * 获取用户信息
 * @author json
 *
 */
public class ValidateShortMessageCodeReq extends ReqBase implements Serializable{
	private String Telphone;
	private String Code;
	public ValidateShortMessageCodeReq(String telphone,String code ){
		this.Telphone=telphone;
		this.Code=code;
	}
	public String getTelphone() {
		return Telphone;
	}
	public void setTelphone(String telphone) {
		Telphone = telphone;
	}
	public String getCode() {
		return Code;
	}
	public void setCode(String code) {
		Code = code;
	}
	
	
	
	
}
