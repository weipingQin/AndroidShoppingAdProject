package com.www.goumei.http;

public class ValidateShortMessageCodeReq  extends  ReqBase2{
    	
   String   Telphone ;
   String   Code;
	public ValidateShortMessageCodeReq(String  Telphone2,String  Code2){
		this.Telphone=Telphone2;
		this.Code=Code2;
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
