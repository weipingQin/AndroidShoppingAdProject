package com.www.goumei.bean;

import java.io.Serializable;
import java.util.List;

public class PushModels  implements Serializable {
    List<PushBean>  models;

	public List<PushBean> getModels() {
		return models;
	}

	public void setModels(List<PushBean> models) {
		this.models = models;
	}
    
    
    
}
