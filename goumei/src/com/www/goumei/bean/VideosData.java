package com.www.goumei.bean;

import java.io.Serializable;
import java.util.Map;

public class VideosData implements Serializable {

	
	Map<String, VideosDataList>   Body;
	
	String Bodys;

	public Map<String, VideosDataList> getBody() {
		return Body;
	}

	public void setBody(Map<String, VideosDataList> body) {
		Body = body;
	}

	public String getBodys() {
		return Bodys;
	}

	public void setBodys(String bodys) {
		Bodys = bodys;
	}
	 
}
