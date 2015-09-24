package com.www.goumei.http;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.www.goumei.http.req.SP_ID;

public class ReqBase2  implements Serializable{
	/**
	 * ApiVersion 	String 	必须 	示例值 		空
	AppKey 	String 	必须 	示例值 		空
	TimeStamp 	String 	必须 	示例值 		空
	Sign 	String 	必须 	示例值 		空
	ExecutorID 	Int 	必须 	示例值 		空
	ModuleKeyLogo 	String 	必须 	示例值 		空
	ActionKeyLogo 	String 
	 */
	public String apiVersion = "0.0.1";
	public String appKey = "1056491";
	public String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	public String sign = "D631A790384AE697874B07264EA1D8E7";
	public int executorID = Integer.parseInt(SP_ID.id);
	public String moduleKeyLogo = "";
	public String actionKeyLogo = "";
	public ReqBase2() {
		super();
	}

	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public int getExecutorID() {
		return executorID;
	}

	public void setExecutorID(int executorID) {
		this.executorID = executorID;
	}

	public String getModuleKeyLogo() {
		return moduleKeyLogo;
	}

	public void setModuleKeyLogo(String moduleKeyLogo) {
		this.moduleKeyLogo = moduleKeyLogo;
	}

	public String getActionKeyLogo() {
		return actionKeyLogo;
	}

	public void setActionKeyLogo(String actionKeyLogo) {
		this.actionKeyLogo = actionKeyLogo;
	}
    
}
