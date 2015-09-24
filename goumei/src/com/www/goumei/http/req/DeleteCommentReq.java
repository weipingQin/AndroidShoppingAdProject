package com.www.goumei.http.req;

import java.io.Serializable;

import com.www.goumei.http.ReqBase;
/**
 * 删除评论
 * @author json
 *
 */
public class DeleteCommentReq extends ReqBase implements Serializable{
	private String[] ID;
	public DeleteCommentReq(String[] id){
		this.ID=id;
	}
	public String[] getID() {
		return ID;
	}
	public void setID(String[] iD) {
		ID = iD;
	}
	
	
	
	
}
