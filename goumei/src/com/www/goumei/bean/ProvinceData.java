package com.www.goumei.bean;

import java.io.Serializable;

import com.www.goumei.http.ReqBase;
/*
 * 店铺
 */
public class ProvinceData extends ReqBase implements Serializable{
	/*ID	Long				Primary，主键
	ProvinceName	String				省份名称
	CountryID	Int				国家ID
	String LastChangeUser;*/
	long ID;
	String ProvinceName;
	int CountryID;
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public String getProvinceName() {
		return ProvinceName;
	}
	public void setProvinceName(String provinceName) {
		ProvinceName = provinceName;
	}
	public int getCountryID() {
		return CountryID;
	}
	public void setCountryID(int countryID) {
		CountryID = countryID;
	}
	
	
}

