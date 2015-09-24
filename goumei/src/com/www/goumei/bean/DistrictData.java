package com.www.goumei.bean;

import java.io.Serializable;

import com.www.goumei.http.ReqBase;
/*
 * 
 */
public class DistrictData extends ReqBase implements Serializable{
	/*ID	Long				Primary，主键
	DistrictName	String				街道名称
	CityID	Long				城市ID*/
	long ID;
	String ProvinceName;
	String CityName;
	long CityID;
	String DistrictName;
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
	public String getCityName() {
		return CityName;
	}
	public void setCityName(String cityName) {
		CityName = cityName;
	}
	public long getCityID() {
		return CityID;
	}
	public void setCityID(long cityID) {
		CityID = cityID;
	}
	public String getDistrictName() {
		return DistrictName;
	}
	public void setDistrictName(String districtName) {
		DistrictName = districtName;
	}
	
	
}

