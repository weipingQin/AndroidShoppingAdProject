package com.www.goumei.http.req;

import java.io.Serializable;

import com.www.goumei.http.ReqBase;
import com.www.goumei.utils.Constant;
/**
 * 省列表
 * @author json
 *
 */
public class ProvinceReq extends ReqBase implements Serializable{
	private String CountryID="1";
	private int PageNo=1;
	
	public ProvinceReq(){
		this.CountryID="1";
		this.PageNo=1;
		this.pageSize=Constant.PAGE_MAX_SIZE;
	}

	public String getCountryID() {
		return CountryID;
	}

	public void setCountryID(String countryID) {
		CountryID = countryID;
	}

	public int getPageNo() {
		return PageNo;
	}

	public void setPageNo(int pageNo) {
		PageNo = pageNo;
	}

	
	
}
