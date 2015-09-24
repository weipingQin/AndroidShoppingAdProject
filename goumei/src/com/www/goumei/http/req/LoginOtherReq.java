package com.www.goumei.http.req;

import java.io.Serializable;

import com.www.goumei.http.ReqBase;
/**
 * 获取用户信息
 * @author json
 *
 */
public class LoginOtherReq extends ReqBase implements Serializable{
	/*OpenID	String	示例值		授权Openid
	AccountType	Int	示例值		账号类型 1-QQ 2-微信 3-微博*/
	private String OpenID;
	private int AccountType;
	public LoginOtherReq(String OpenID,int AccountType){
		this.OpenID=OpenID;
		this.AccountType=AccountType;
	}
	public String getOpenID() {
		return OpenID;
	}
	public void setOpenID(String openID) {
		OpenID = openID;
	}
	public int getAccountType() {
		return AccountType;
	}
	public void setAccountType(int accountType) {
		AccountType = accountType;
	}
	
	
	
}
