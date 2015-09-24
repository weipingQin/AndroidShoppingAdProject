package com.www.goumei.http.req;

import java.io.Serializable;

import com.www.goumei.http.ReqBase;
/**
 * 绑定第三方应用
 * @author json
 *
 */
public class BindOtherReq extends ReqBase implements Serializable{
	/*OpenID	String	示例值		授权Openid
	AccountType	Int	示例值		账号类型 1-QQ 2-微信 3-微博*/
	private int UsersID;
	private boolean IsBind;
	private String OpenID;
	private int AccountType;
	public BindOtherReq(int usersId,String OpenID,int AccountType){
		this.OpenID=OpenID;
		this.AccountType=AccountType;
		this.UsersID=usersId;
		this.IsBind=true;
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
	public int getUsersID() {
		return UsersID;
	}
	public void setUsersID(int usersID) {
		UsersID = usersID;
	}
	public boolean isIsBind() {
		return IsBind;
	}
	public void setIsBind(boolean isBind) {
		IsBind = isBind;
	}
	
	
	
}
