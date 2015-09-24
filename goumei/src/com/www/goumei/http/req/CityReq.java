package com.www.goumei.http.req;

import java.io.Serializable;

import com.www.goumei.http.ReqBase;
import com.www.goumei.utils.Constant;
/**
 * 省列表
 * @author json
 *
 */
public class CityReq extends ReqBase implements Serializable{
	private String ProvinceID;
	private int PageNo=1;
	
	public CityReq(String id){
		this.ProvinceID=id;
		this.PageNo=1;
		this.pageSize=Constant.PAGE_MAX_SIZE;
	}

	public String getProvinceID() {
		return ProvinceID;
	}

	public void setProvinceID(String provinceID) {
		ProvinceID = provinceID;
	}

	public int getPageNo() {
		return PageNo;
	}

	public void setPageNo(int pageNo) {
		PageNo = pageNo;
	}



	
	
}
