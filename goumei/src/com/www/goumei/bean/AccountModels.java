package com.www.goumei.bean;

import java.io.Serializable;
import java.util.List;

public class AccountModels  implements Serializable {
    List<AccountData>  models;

	public List<AccountData> getModels() {
		return models;
	}

	public void setModels(List<AccountData> models) {
		this.models = models;
	}
    
    
    
}
