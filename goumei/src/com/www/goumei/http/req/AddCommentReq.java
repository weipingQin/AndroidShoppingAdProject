package com.www.goumei.http.req;

import java.io.Serializable;

import com.www.goumei.http.ReqBase;
/**
 * 添加评论
 * @author json
 *
 */
public class AddCommentReq extends ReqBase implements Serializable{
	private String VideosID;
	private String ReplyUserID=null;
	private boolean IsReply;
	private String Content;
	public AddCommentReq(String videosId,String content,boolean isReply,String replyUserID){
		this.VideosID=videosId;
		if(isReply){
		}
		this.ReplyUserID=replyUserID;
		
		this.IsReply=isReply;
		this.Content=content;
	}
	public String getVideosID() {
		return VideosID;
	}
	public void setVideosID(String videosID) {
		VideosID = videosID;
	}
	public String getReplyUserID() {
		return ReplyUserID;
	}
	public void setReplyUserID(String replyUserID) {
		ReplyUserID = replyUserID;
	}
	public boolean isIsReply() {
		return IsReply;
	}
	public void setIsReply(boolean isReply) {
		IsReply = isReply;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	
	
	
}
