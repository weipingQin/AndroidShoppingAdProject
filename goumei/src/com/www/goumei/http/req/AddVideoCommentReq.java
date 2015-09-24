package com.www.goumei.http.req;

import java.io.Serializable;

import com.www.goumei.http.ReqBase;
/**
 * 添加评论
 * @author json
 *
 */
public class AddVideoCommentReq extends ReqBase implements Serializable{
	private String VideosID;
	private String Content;
	public AddVideoCommentReq(String videosId,String content){
		this.VideosID=videosId;
		this.Content=content;
	}
	public String getVideosID() {
		return VideosID;
	}
	public void setVideosID(String videosID) {
		VideosID = videosID;
	}
	
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	
	
	
}
