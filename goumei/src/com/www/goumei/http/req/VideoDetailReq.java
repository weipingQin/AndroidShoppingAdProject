package com.www.goumei.http.req;

import java.io.Serializable;

import com.www.goumei.http.ReqBase;
/**
 * 作品请求接口
 * @author json
 *
 */
public class VideoDetailReq extends ReqBase implements Serializable{
	private String VideosID;
	private int PageNo;
	public VideoDetailReq(String VideosID,int pageNo){
		this.VideosID=VideosID;
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
