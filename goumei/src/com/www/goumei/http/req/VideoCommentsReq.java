package com.www.goumei.http.req;

import java.io.Serializable;

import com.www.goumei.http.ReqBase;
/**
 * 评论列表请求接口
 * @author json
 *
 */
public class VideoCommentsReq extends ReqBase implements Serializable{
	private String VideosID;
	private int PageNo;
	public VideoCommentsReq(String createUserID,int pageNo){
		this.VideosID=createUserID;
		this.PageNo=pageNo;
	}
	public String getVideosID() {
		return VideosID;
	}
	public void setVideosID(String videosID) {
		VideosID = videosID;
	}
	public int getPageNo() {
		return PageNo;
	}
	public void setPageNo(int pageNo) {
		PageNo = pageNo;
	}
	
	
}
