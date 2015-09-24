package com.www.goumei.http;

public class LoginReq  extends  ReqBase2{
    	
   String   Telphone ;
   String   Password ;
	public LoginReq(String  Telphone2,String  Password2){
		this.Telphone=Telphone2;
		this.Password=Password2;
	}
public String getTelphone() {
	return Telphone;
}

public String getPassword() {
	return Password;
}
public void setPassword(String password) {
	Password = password;
}
public void setTelphone(String telphone) {
	Telphone = telphone;
}
   
}
