package com.www.goumei.bean;

import java.io.Serializable;
import java.util.List;

public class UsersPraiseMeDataList  implements Serializable {
    List<UsersPraiseMeData>  models;

	public List<UsersPraiseMeData> getModels() {
		return models;
	}

	public void setModels(List<UsersPraiseMeData> models) {
		this.models = models;
	}
    
    
    
}
