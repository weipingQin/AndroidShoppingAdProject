package com.www.goumei.bean;

import java.io.Serializable;

import android.R.integer;
/**
 * 评论实体类
 * @author json
 *
 */
public class CommentData  implements   Serializable {
	/*ID	Int				Primary，主键
	Content	String				评论内容
	AgreesCount	Int				同意数量
	IsDiplay	Boolean				是否显示
	IsReply	Boolean				是否是回复消息
	ReplyUserID	Int				回复人ID
	VideosID	Int				视频ID
	IsRead	Boolean				是否阅读
	CreateTime	DateTime				评论时间
	CommonetTime	String				评论时间描述*/
	int id;
	String Content;
	int AgreesCount;
	boolean IsDiplay;
	boolean IsReply;
	int ReplyUserID;
	int VideosID;
	boolean IsRead;
	String CreateTime;
	String CommonetTime;
	String userDisplayName;
	String userHeadsculpture;
	public String getUserDisplayName() {
		return userDisplayName;
	}
	public void setUserDisplayName(String userDisplayName) {
		this.userDisplayName = userDisplayName;
	}
	public String getUserHeadsculpture() {
		return userHeadsculpture;
	}
	public void setUserHeadsculpture(String userHeadsculpture) {
		this.userHeadsculpture = userHeadsculpture;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public int getAgreesCount() {
		return AgreesCount;
	}
	public void setAgreesCount(int agreesCount) {
		AgreesCount = agreesCount;
	}
	public boolean isIsDiplay() {
		return IsDiplay;
	}
	public void setIsDiplay(boolean isDiplay) {
		IsDiplay = isDiplay;
	}
	public boolean isIsReply() {
		return IsReply;
	}
	public void setIsReply(boolean isReply) {
		IsReply = isReply;
	}
	public int getReplyUserID() {
		return ReplyUserID;
	}
	public void setReplyUserID(int replyUserID) {
		ReplyUserID = replyUserID;
	}
	public int getVideosID() {
		return VideosID;
	}
	public void setVideosID(int videosID) {
		VideosID = videosID;
	}
	public boolean isIsRead() {
		return IsRead;
	}
	public void setIsRead(boolean isRead) {
		IsRead = isRead;
	}
	public String getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}
	public String getCommonetTime() {
		return CommonetTime;
	}
	public void setCommonetTime(String commonetTime) {
		CommonetTime = commonetTime;
	}
	
	
	    
}
