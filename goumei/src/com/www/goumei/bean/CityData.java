package com.www.goumei.bean;

import java.io.Serializable;

import com.www.goumei.http.ReqBase;
/*
 * 城市
 */
public class CityData extends ReqBase implements Serializable{
	/*ID	Long				Primary，主键
	CityName	String				名称
	ZipCode	String				邮编
	ProvinceID	Long				省份ID*/
	long ID;
	String ProvinceName;
	String CityName;
	String ZipCode;
	long ProvinceID;
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
	public String getZipCode() {
		return ZipCode;
	}
	public void setZipCode(String zipCode) {
		ZipCode = zipCode;
	}
	public long getProvinceID() {
		return ProvinceID;
	}
	public void setProvinceID(long provinceID) {
		ProvinceID = provinceID;
	}
	
	
}

