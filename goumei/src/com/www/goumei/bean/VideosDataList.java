package com.www.goumei.bean;

import java.io.Serializable;
import java.util.List;

public class VideosDataList  implements Serializable {
    List<Videos>  models;
    int TotalCounts;
	public List<Videos> getModels() {
		return models;
	}

	public void setModels(List<Videos> models) {
		this.models = models;
	}

	public int getTotalCounts() {
		return TotalCounts;
	}

	public void setTotalCounts(int totalCounts) {
		TotalCounts = totalCounts;
	}
    
    
    
}
