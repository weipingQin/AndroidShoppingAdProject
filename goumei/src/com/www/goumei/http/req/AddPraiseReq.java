package com.www.goumei.http.req;

import java.io.Serializable;

import com.www.goumei.http.ReqBase;
/**
 * 添加关注
 * @author json
 *
 */
public class AddPraiseReq extends ReqBase implements Serializable{
	private String VideosID;
	private String UsersID;
	private String PraiseTime;
	public AddPraiseReq(String VideosID,String UsersID,String PraiseTime){
		this.VideosID=VideosID;
		this.UsersID=UsersID;
		this.PraiseTime=PraiseTime;
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
	public String getPraiseTime() {
		return PraiseTime;
	}
	public void setPraiseTime(String praiseTime) {
		PraiseTime = praiseTime;
	}
	
	
	
	
}
