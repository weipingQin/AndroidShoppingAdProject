package com.www.goumei.bean;

import java.io.Serializable;

public class SavePicResult implements Serializable {

	private static final long serialVersionUID = 1L;
	private String SavePath;

	public String getSavePath() {
		return SavePath;
	}

	public void setSavePath(String savePath) {
		SavePath = savePath;
	}

}
