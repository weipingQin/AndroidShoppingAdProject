package com.www.goumei.bean;

import java.io.Serializable;
import java.util.List;

public class FriendsDataList  implements Serializable {
    List<FriendData>  models;

	public List<FriendData> getModels() {
		return models;
	}

	public void setModels(List<FriendData> models) {
		this.models = models;
	}
    
    
    
}
