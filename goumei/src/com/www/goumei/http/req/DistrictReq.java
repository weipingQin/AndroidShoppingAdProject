package com.www.goumei.http.req;

import java.io.Serializable;

import com.www.goumei.http.ReqBase;
import com.www.goumei.utils.Constant;
/**
 * 区
 * @author json
 *
 */
public class DistrictReq extends ReqBase implements Serializable{
	private String CityID;
	private int PageNo=1;
	public DistrictReq(String id){
		CityID=id;
		this.pageSize=Constant.PAGE_MAX_SIZE;
	}
	public String getCityID() {
		return CityID;
	}
	public void setCityID(String cityID) {
		CityID = cityID;
	}
	public int getPageNo() {
		return PageNo;
	}
	public void setPageNo(int pageNo) {
		PageNo = pageNo;
	}
	
	



	
	
}
