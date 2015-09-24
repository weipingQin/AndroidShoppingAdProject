package com.www.goumei.http.req;

import java.io.Serializable;

import com.www.goumei.http.ReqBase;
/**
 * 意见反馈
 * @author json
 *
 */
public class OpinionsReq extends ReqBase implements Serializable{
	private String Content;
	private String Contact;
	public OpinionsReq(String Content,String Contact){
		this.Content=Content;
		this.Contact=Contact;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getContact() {
		return Contact;
	}
	public void setContact(String contact) {
		Contact = contact;
	}
	
	
}
