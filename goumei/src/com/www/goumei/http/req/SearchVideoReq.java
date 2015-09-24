package com.www.goumei.http.req;

import java.io.Serializable;

import com.www.goumei.http.ReqBase;
/**
 * 作品请求接口
 * @author json
 *
 */
public class SearchVideoReq extends ReqBase implements Serializable{
	private String SearchKeywords;
	private int PageNo;
	public SearchVideoReq(String SearchKeywords,int pageNo){
		this.SearchKeywords=SearchKeywords;
		this.PageNo=pageNo;
	}
	public int getPageNo() {
		return PageNo;
	}
	public void setPageNo(int pageNo) {
		PageNo = pageNo;
	}
	public String getSearchKeywords() {
		return SearchKeywords;
	}
	public void setSearchKeywords(String searchKeywords) {
		SearchKeywords = searchKeywords;
	}
	
}
