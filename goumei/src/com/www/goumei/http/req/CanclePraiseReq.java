package com.www.goumei.http.req;

import java.io.Serializable;

import com.www.goumei.http.ReqBase;
/**
 * 添加关注
 * @author json
 *
 */
public class CanclePraiseReq extends ReqBase implements Serializable{
	private String VideosID;
	private String UsersID;
	public CanclePraiseReq(String VideosID,String UsersID){
		this.VideosID=VideosID;
		this.UsersID=UsersID;
	}
	public String getVideosID() {
		return VideosID;
	}
	public void setVideosID(String videosID) {
		VideosID = videosID;
	}
	public String getUsersID() {
		return UsersID;
	}
	public void setUsersID(String usersID) {
		UsersID = usersID;
	}
	
	
	
	
}
