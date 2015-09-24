package com.www.goumei.http;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SaveVideoReq extends   ReqBase2 implements Serializable{
	byte[]  inputStream;
	public SaveVideoReq(byte[]  inputStream2){
		this.inputStream=inputStream2;
		
	}
	public byte[] getInputStream() {
		return inputStream;
	}
	public void setInputStream(byte[] inputStream) {
		this.inputStream = inputStream;
	}
	
    
}
