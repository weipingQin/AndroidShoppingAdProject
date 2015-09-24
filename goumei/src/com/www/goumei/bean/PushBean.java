package com.www.goumei.bean;

import java.io.Serializable;

public class PushBean implements Serializable{
	/*ID	Int				Primary，主键
	Name	String				推送角色名称
	PushUserID	Int				推送用户ID
	Content	String				推送内容
	link	String				关联地址
	PushUserHeadHeadsculpture	String				推送人头像
	CreateTime	DateTime				创建时间*/
	private String ID;
	private String name;
	private String pushUserID;
	private String Content;
	private String link;
	private String PushUserHeadHeadsculpture;
	private String CreateTime;
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPushUserID() {
		return pushUserID;
	}
	public void setPushUserID(String pushUserID) {
		this.pushUserID = pushUserID;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getPushUserHeadHeadsculpture() {
		return PushUserHeadHeadsculpture;
	}
	public void setPushUserHeadHeadsculpture(String pushUserHeadHeadsculpture) {
		PushUserHeadHeadsculpture = pushUserHeadHeadsculpture;
	}
	public String getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}
	
	
}
