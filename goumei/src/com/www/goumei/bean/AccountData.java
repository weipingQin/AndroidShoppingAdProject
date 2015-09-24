package com.www.goumei.bean;

import java.io.Serializable;

import com.www.goumei.http.ReqBase;
/*
 * 账号信息
 */
public class AccountData extends ReqBase implements Serializable{
	/*UsersID	Int	示例值		用户ID
	OpenID	String	示例值		授权Openid
	AccountType	Int	示例值		账号类型 1-QQ 2-微信 3-微博
	IsBind	Boolean	示例值		是否绑定
	Phone	String	示例值		绑定手机号码国家ID*/
	private String UsersID;
	private String OpenID;
	private int AccountType;
	private boolean IsBind;
	private String Phone;
	public String getUsersID() {
		return UsersID;
	}
	public void setUsersID(String usersID) {
		UsersID = usersID;
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
	public boolean isIsBind() {
		return IsBind;
	}
	public void setIsBind(boolean isBind) {
		IsBind = isBind;
	}
	public String getPhone() {
		return Phone;
	}
	public void setPhone(String phone) {
		Phone = phone;
	}
	
	
}

