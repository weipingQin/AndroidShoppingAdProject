package com.www.goumei.bean;

import java.io.Serializable;

import com.www.goumei.http.ReqBase;
/*
 * 店铺
 */
public class ShopData extends ReqBase implements Serializable{
	/*	ID	Int				Primary，主键
	Name	String				店铺名称
	Contacts	String				联系人
	Telphone	String				电话号码
	QQ	String				QQ
	Email	String				邮箱*/
	String LastChangeUser;
	int ID;
	String Name;
	String Contacts;
	String Telphone;
	String QQ;
	String Email;
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getContacts() {
		return Contacts;
	}
	public void setContacts(String contacts) {
		Contacts = contacts;
	}
	public String getTelphone() {
		return Telphone;
	}
	public void setTelphone(String telphone) {
		Telphone = telphone;
	}
	public String getQQ() {
		return QQ;
	}
	public void setQQ(String qQ) {
		QQ = qQ;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getLastChangeUser() {
		return LastChangeUser;
	}
	public void setLastChangeUser(String lastChangeUser) {
		LastChangeUser = lastChangeUser;
	}
	
	
	}

