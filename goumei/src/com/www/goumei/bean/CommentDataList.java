package com.www.goumei.bean;

import java.io.Serializable;
import java.util.List;

public class CommentDataList  implements Serializable {
    List<CommentData>  models;

	public List<CommentData> getModels() {
		return models;
	}

	public void setModels(List<CommentData> models) {
		this.models = models;
	}
    
    
    
}
