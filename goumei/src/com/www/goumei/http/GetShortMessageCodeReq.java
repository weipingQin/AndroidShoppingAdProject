package com.www.goumei.http;

public class GetShortMessageCodeReq  extends  ReqBase2{
    	
   String   Telphone ;
	public GetShortMessageCodeReq(String  Telphone2){
		this.Telphone=Telphone2;
		
	}
public String getTelphone() {
	return Telphone;
}

public void setTelphone(String telphone) {
	Telphone = telphone;
}
   
}
