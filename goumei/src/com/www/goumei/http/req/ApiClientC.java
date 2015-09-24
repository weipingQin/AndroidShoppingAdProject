package com.www.goumei.http.req;

import org.json.JSONObject;

import com.www.goumei.bean.AccountModels;
import com.www.goumei.bean.CityDataModels;
import com.www.goumei.bean.CommentDataList;
import com.www.goumei.bean.DistrictDataModels;
import com.www.goumei.bean.FriendsDataList;
import com.www.goumei.bean.ProvinceDataModels;
import com.www.goumei.bean.PushModels;
import com.www.goumei.bean.SaveVideoResult;
import com.www.goumei.bean.ShopData;
import com.www.goumei.bean.ShopModel;
import com.www.goumei.bean.ShopResult;
import com.www.goumei.bean.UserData;
import com.www.goumei.bean.UserDataB;
import com.www.goumei.bean.UserInfo;
import com.www.goumei.bean.UsersInfo;
import com.www.goumei.bean.UsersPraiseMeDataList;
import com.www.goumei.bean.VideosDataList;
import com.www.goumei.http.ApiClient;
import com.www.goumei.http.FastJSONHelper;
import com.www.goumei.http.SaveVideoReq;
import com.www.goumei.http.Urls;
import com.www.goumei.utils.FileUtil;
import com.www.goumei.utils.L;
import com.www.goumei.utils.StringUtils;
import com.www.goumei.utils.TimeUtils;

public class ApiClientC extends ApiClient{
	
	/**
	 * 登录验证
	 * 
	 * @param openID
	 * @param type 1-QQ 2-微信 3-微博
	 */
	public static UserInfo loginClient(String openID, int  type) {
		UserInfo info = null;
		String re = "";
    	String bodyString=""; 
		LoginOtherReq req=new LoginOtherReq(openID, type);
		String jsonstr=FastJSONHelper.serialize(req);
    	
    	if(!StringUtils.isEmpty(jsonstr)){
    		try {
        		re = _post_httpPostFormJsonStr(Urls.URL_LOGIN_OTHER , jsonstr);
        		if(!StringUtils.isEmpty(re)){
        			JSONObject jsonObject = new JSONObject(re);
        			 bodyString = jsonObject.optString("Body");
        		}
        		UsersInfo usersInfo = FastJSONHelper.deserialize(bodyString,
						UsersInfo.class);
				if (usersInfo != null) {
					info = usersInfo.getUser();
				}        		
        	} catch (Exception e) {
        		e.getLocalizedMessage();
        	}
    	}
		return info;
	}
	/**
	 * 绑定第三方账号
	 * 
	 * @param openID
	 * @param type 1-QQ 2-微信 3-微博
	 */
	public static String bindClient(int UsersID,String openID, int  type) {
		UserInfo info = null;
		String re = "";
		String bodyString=""; 
		BindOtherReq req=new BindOtherReq(UsersID,openID, type);
		String jsonstr=FastJSONHelper.serialize(req);
		
		if(!StringUtils.isEmpty(jsonstr)){
			try {
				re = _post_httpPostFormJsonStr(Urls.URL_ACCOUNT_BIND , jsonstr);
				if(!StringUtils.isEmpty(re)){
        			JSONObject jsonObject = new JSONObject(re);
        			 bodyString = jsonObject.optString("Body");
        			 JSONObject obj=new JSONObject(bodyString);
        			 
        			 if(!obj.isNull("errCode")){
        				 return "";
        			 }else{
        				 return "1";
        			 }
        		}
			} catch (Exception e) {
				e.getLocalizedMessage();
			}
		}
		return "";
	}
	
	/**
	 * 获取制定用户的视频
	 * @param id 用户id
	 * @param pageNo 当前页数
	 * @return
	 */
	public static VideosDataList getVideosById(String id,final int pageNo){
		String re = "";
    	String bodyString=""; 
    	VideosDataList   videosDataList= null;	
    	MyVideoReq req=new MyVideoReq(id,pageNo);
    	String jsonstr=FastJSONHelper.serialize(req);
    	
    	if(!StringUtils.isEmpty(jsonstr)){
    		try {
        		re = _post_httpPostFormJsonStr(Urls.URL_VIDEOLIST , jsonstr);
        		if(!StringUtils.isEmpty(re)){
        			JSONObject jsonObject = new JSONObject(re);
        			 bodyString = jsonObject.optString("Body");
        		}
        		videosDataList=FastJSONHelper.deserialize(bodyString, VideosDataList.class);
        		
        	} catch (Exception e) {
        		e.getLocalizedMessage();
        	}
    	}
    	
		return videosDataList;
	}
	/**
	 * 搜索的视频
	 * @param id 用户id
	 * @param pageNo 当前页数
	 * @return
	 */
	public static VideosDataList getVideosBySearchKeywords(String SearchKeywords,final int pageNo){
		String re = "";
		String bodyString=""; 
		VideosDataList   videosDataList= null;	
		SearchVideoReq req=new SearchVideoReq(SearchKeywords,pageNo);
		String jsonstr=FastJSONHelper.serialize(req);
		
		if(!StringUtils.isEmpty(jsonstr)){
			try {
				re = _post_httpPostFormJsonStr(Urls.URL_VIDEOLIST , jsonstr);
				if(!StringUtils.isEmpty(re)){
					JSONObject jsonObject = new JSONObject(re);
					bodyString = jsonObject.optString("Body");
				}
				videosDataList=FastJSONHelper.deserialize(bodyString, VideosDataList.class);
				
			} catch (Exception e) {
				e.getLocalizedMessage();
			}
		}
		
		return videosDataList;
	}
	/**
	 * 获取制定用户的视频
	 * @param id 视频id
	 * @param pageNo 当前页数
	 * @return
	 */
	public static VideosDataList getVideosDetailById(String id,final int pageNo){
		String re = "";
		String bodyString=""; 
		VideosDataList   videosDataList= null;	
		VideoDetailReq req=new VideoDetailReq(id,pageNo);
		String jsonstr=FastJSONHelper.serialize(req);
		
		if(!StringUtils.isEmpty(jsonstr)){
			try {
				re = _post_httpPostFormJsonStr(Urls.URL_VIDEOLIST , jsonstr);
				if(!StringUtils.isEmpty(re)){
					JSONObject jsonObject = new JSONObject(re);
					bodyString = jsonObject.optString("Body");
				}
				videosDataList=FastJSONHelper.deserialize(bodyString, VideosDataList.class);
				
			} catch (Exception e) {
				e.getLocalizedMessage();
			}
		}
		
		return videosDataList;
	}
	/**
	 * 省列表
	 * @param id 用户id
	 * @param pageNo 当前页数
	 * @return
	 */
	public static ProvinceDataModels getProvinceById(){
		String re = "";
		String bodyString=""; 
		ProvinceDataModels   videosDataList= null;	
		ProvinceReq req=new ProvinceReq();
		String jsonstr=FastJSONHelper.serialize(req);
		
		if(!StringUtils.isEmpty(jsonstr)){
			try {
				re = _post_httpPostFormJsonStr(Urls.URL_PROVINCE , jsonstr);
				if(!StringUtils.isEmpty(re)){
					JSONObject jsonObject = new JSONObject(re);
					bodyString = jsonObject.optString("Body");
				}
				videosDataList=FastJSONHelper.deserialize(bodyString, ProvinceDataModels.class);
				
			} catch (Exception e) {
				e.getLocalizedMessage();
			}
		}
		
		return videosDataList;
	}
	/**
	 * 市列表
	 * @param id 用户id
	 * @param pageNo 当前页数
	 * @return
	 */
	public static CityDataModels getCitysById(String id){
		String re = "";
		String bodyString=""; 
		CityDataModels   videosDataList= null;	
		CityReq req=new CityReq(id);
		String jsonstr=FastJSONHelper.serialize(req);
		
		if(!StringUtils.isEmpty(jsonstr)){
			try {
				re = _post_httpPostFormJsonStr(Urls.URL_CITY , jsonstr);
				if(!StringUtils.isEmpty(re)){
					JSONObject jsonObject = new JSONObject(re);
					bodyString = jsonObject.optString("Body");
				}
				videosDataList=FastJSONHelper.deserialize(bodyString, CityDataModels.class);
				
			} catch (Exception e) {
				e.getLocalizedMessage();
			}
		}
		
		return videosDataList;
	}
	/**
	 * 区列表
	 * @param id 用户id
	 * @param pageNo 当前页数
	 * @return
	 */
	public static DistrictDataModels getDistrictsById(String id){
		String re = "";
		String bodyString=""; 
		DistrictDataModels   videosDataList= null;	
		DistrictReq req=new DistrictReq(id);
		String jsonstr=FastJSONHelper.serialize(req);
		
		if(!StringUtils.isEmpty(jsonstr)){
			try {
				re = _post_httpPostFormJsonStr(Urls.URL_DISTRICT , jsonstr);
				if(!StringUtils.isEmpty(re)){
					JSONObject jsonObject = new JSONObject(re);
					bodyString = jsonObject.optString("Body");
				}
				videosDataList=FastJSONHelper.deserialize(bodyString, DistrictDataModels.class);
				
			} catch (Exception e) {
				e.getLocalizedMessage();
			}
		}
		
		return videosDataList;
	}
	/**
	 * 获取制定用户的喜欢
	 * @param id 用户id
	 * @param pageNo 当前页数
	 * @return
	 */
	public static UsersPraiseMeDataList getPraiseMeById(String id,final int pageNo){
		String re = "";
		String bodyString=""; 
		UsersPraiseMeDataList   videosDataList= null;	
		PraiseMeReq req=new PraiseMeReq(id,pageNo);
		String jsonstr=FastJSONHelper.serialize(req);
		
		if(!StringUtils.isEmpty(jsonstr)){
			try {
				re = _post_httpPostFormJsonStr(Urls.URL_PRAISEME_LIST , jsonstr);
				if(!StringUtils.isEmpty(re)){
					JSONObject jsonObject = new JSONObject(re);
					bodyString = jsonObject.optString("Body");
				}
				videosDataList=FastJSONHelper.deserialize(bodyString, UsersPraiseMeDataList.class);
				
			} catch (Exception e) {
				e.getLocalizedMessage();
			}
		}
		
		return videosDataList;
	}
	/**
	 * 获取制定用户的评论
	 * @param id 用户id
	 * @param pageNo 当前页数
	 * @return
	 */
	public static CommentDataList getCommentById(String id,final int pageNo){
		String re = "";
		String bodyString=""; 
		CommentDataList   videosDataList= null;	
		MyCommentsReq req=new MyCommentsReq(id,pageNo);
		String jsonstr=FastJSONHelper.serialize(req);
		
		if(!StringUtils.isEmpty(jsonstr)){
			try {
				re = _post_httpPostFormJsonStr(Urls.URL_COMMENTS_LIST , jsonstr);
				if(!StringUtils.isEmpty(re)){
					JSONObject jsonObject = new JSONObject(re);
					bodyString = jsonObject.optString("Body");
				}
				videosDataList=FastJSONHelper.deserialize(bodyString, CommentDataList.class);
				
			} catch (Exception e) {
				e.getLocalizedMessage();
			}
		}
		
		return videosDataList;
	}
	/**
	 * 获取制定视频的评论
	 * @param id 视频id
	 * @param pageNo 当前页数
	 * @return
	 */
	public static CommentDataList getCommentByVideoId(String id,final int pageNo){
		String re = "";
		String bodyString=""; 
		CommentDataList   videosDataList= null;	
		VideoCommentsReq req=new VideoCommentsReq(id,pageNo);
		String jsonstr=FastJSONHelper.serialize(req);
		
		if(!StringUtils.isEmpty(jsonstr)){
			try {
				re = _post_httpPostFormJsonStr(Urls.URL_COMMENTS_LIST , jsonstr);
				if(!StringUtils.isEmpty(re)){
					JSONObject jsonObject = new JSONObject(re);
					bodyString = jsonObject.optString("Body");
				}
				videosDataList=FastJSONHelper.deserialize(bodyString, CommentDataList.class);
				
			} catch (Exception e) {
				e.getLocalizedMessage();
			}
		}
		
		return videosDataList;
	}
	/**
	 * 获取制定用户的视频
	 * @param id 用户id
	 * @param pageNo 当前页数
	 * @return
	 */
	public static VideosDataList getUpPublishVideosById(String id,final int pageNo){
		String re = "";
		String bodyString=""; 
		VideosDataList   videosDataList= null;	
		UnPublishVideoReq req=new UnPublishVideoReq(id,pageNo);
		String jsonstr=FastJSONHelper.serialize(req);
		
		if(!StringUtils.isEmpty(jsonstr)){
			try {
				re = _post_httpPostFormJsonStr(Urls.URL_VIDEOLIST , jsonstr);
				if(!StringUtils.isEmpty(re)){
					JSONObject jsonObject = new JSONObject(re);
					bodyString = jsonObject.optString("Body");
				}
				videosDataList=FastJSONHelper.deserialize(bodyString, VideosDataList.class);
				
			} catch (Exception e) {
				e.getLocalizedMessage();
			}
		}
		
		return videosDataList;
	}
	/**
	 * 获取个人信息
	 * @param id
	 * @return
	 */
	public static UserDataB getUserInfo(String id){
		if(id==null){
			return null;
		}
		UserDataB user=null;
		String re = "";
    	String bodyString=""; 
    	UserInfoReq req=new UserInfoReq(id);
    	String jsonstr=FastJSONHelper.serialize(req);
    	L.e("jsonstr", "jsonstr---" + re);
    	if(!StringUtils.isEmpty(jsonstr)){
    		try {
        		re = _post_httpPostFormJsonStr(Urls.URL_GETUSER_INFO , jsonstr);
        		if(!StringUtils.isEmpty(re)){
        			JSONObject jsonObject = new JSONObject(re);
        			 bodyString = jsonObject.optString("Body");
        		}
        		user=FastJSONHelper.deserialize(bodyString, UserDataB.class);
        		
        	} catch (Exception e) {
        		e.getLocalizedMessage();
        	}
    	}
		return user;
	}
	
	/**
	 * 添加关注
	 * @param id
	 * @param fansId
	 * @return
	 */
	public static String addAttention(String id,String fansId){
		if(id==null){
			return null;
		}
		//UserDataB user=null;
		String re = "";
    	String bodyString=""; 
    	AddAttentionReq req=new AddAttentionReq(fansId,id);
    	String jsonstr=FastJSONHelper.serialize(req);
    	
    	if(!StringUtils.isEmpty(jsonstr)){
    		try {
        		re = _post_httpPostFormJsonStr(Urls.URL_ADD_ATTENTION , jsonstr);
        		if(!StringUtils.isEmpty(re)){
        			JSONObject jsonObject = new JSONObject(re);
        			 bodyString = jsonObject.optString("Body");
        			 JSONObject obj=new JSONObject(bodyString);
        			 
        			 if(!obj.isNull("errCode")){
        				 return "";
        			 }else{
        				 return "1";
        			 }
        		}
        		
        		
        	} catch (Exception e) {
        		e.getLocalizedMessage();
        	}
    	}
		return "";
	}
	/**
	 * 取消关注
	 * @param id
	 * @param fansId
	 * @return
	 */
	public static String cancelAttention(String id,String fansId){
		if(id==null){
			return null;
		}
		//UserDataB user=null;
		String re = "";
		String bodyString=""; 
		AddAttentionReq req=new AddAttentionReq(fansId,id);
		String jsonstr=FastJSONHelper.serialize(req);
		
		if(!StringUtils.isEmpty(jsonstr)){
			try {
				re = _post_httpPostFormJsonStr(Urls.URL_CANCEL_ATTENTION , jsonstr);
				if(!StringUtils.isEmpty(re)){
					JSONObject jsonObject = new JSONObject(re);
					bodyString = jsonObject.optString("Body");
					JSONObject obj=new JSONObject(bodyString);
					
					if(!obj.isNull("errCode")){
						return "";
					}else{
						return "1";
					}
				}
				
				
			} catch (Exception e) {
				e.getLocalizedMessage();
			}
		}
		return "";
	}
	/**
	 * 添加喜欢
	 * @param id
	 * @param fansId
	 * @return
	 */
	public static String addPraise(String VideosID,String UsersID){
		if(VideosID==null){
			return null;
		}
		String re = "";
		String bodyString=""; 
		AddPraiseReq req=new AddPraiseReq(VideosID,UsersID,TimeUtils.getTime());
		String jsonstr=FastJSONHelper.serialize(req);
		
		if(!StringUtils.isEmpty(jsonstr)){
			try {
				re = _post_httpPostFormJsonStr(Urls.URL_ADD_PRAISE , jsonstr);
				if(!StringUtils.isEmpty(re)){
					JSONObject jsonObject = new JSONObject(re);
					bodyString = jsonObject.optString("Body");
					JSONObject obj=new JSONObject(bodyString);
					
					if(!obj.isNull("errCode")){
						return "";
					}else{
						return "1";
					}
				}
				
				
			} catch (Exception e) {
				e.getLocalizedMessage();
			}
		}
		return "";
	}
	/**
	 * 添加喜欢
	 * @param id
	 * @param fansId
	 * @return
	 */
	public static String canclePraise(String VideosID,String UsersID){
		if(VideosID==null){
			return null;
		}
		String re = "";
		String bodyString=""; 
		CanclePraiseReq req=new CanclePraiseReq(VideosID,UsersID);
		String jsonstr=FastJSONHelper.serialize(req);
		
		if(!StringUtils.isEmpty(jsonstr)){
			try {
				re = _post_httpPostFormJsonStr(Urls.URL_CANCLE_PRAISE , jsonstr);
				if(!StringUtils.isEmpty(re)){
					JSONObject jsonObject = new JSONObject(re);
					bodyString = jsonObject.optString("Body");
					JSONObject obj=new JSONObject(bodyString);
					
					if(!obj.isNull("errCode")){
						return "";
					}else{
						return "1";
					}
				}
				
				
			} catch (Exception e) {
				e.getLocalizedMessage();
			}
		}
		return "";
	}
	/**
	 * 添加评论
	 * @param id
	 * @param fansId
	 * @return
	 */
	public static String addComment(String videosId,String content,boolean isReply,String replyUserID){
		if(videosId==null){
			return "";
		}
		String re = "";
		String bodyString=""; 
		String jsonstr="";
		if(isReply){
			AddCommentReq req=new AddCommentReq(videosId, content, isReply, replyUserID);
			jsonstr=FastJSONHelper.serialize(req);
		}else{
			AddVideoCommentReq req=new AddVideoCommentReq(videosId, content);
			jsonstr=FastJSONHelper.serialize(req);
		}
		
		
		
		if(!StringUtils.isEmpty(jsonstr)){
			try {
				re = _post_httpPostFormJsonStr(Urls.URL_ADD_COMMENT , jsonstr);
				if(!StringUtils.isEmpty(re)){
					JSONObject jsonObject = new JSONObject(re);
					bodyString = jsonObject.optString("Body");
					JSONObject obj=new JSONObject(bodyString);
					
					if(!obj.isNull("errCode")){
						return "";
					}else{
						return "1";
					}
				}
				
				
			} catch (Exception e) {
				e.getLocalizedMessage();
			}
		}
		return "";
	}
	/**
	 * 添加评论
	 * @param id
	 * @param fansId
	 * @return
	 */
	public static String addComment(String videosId,String content){
		if(videosId==null){
			return "";
		}
		String re = "";
		String bodyString=""; 
		AddVideoCommentReq req=new AddVideoCommentReq(videosId, content);
		String jsonstr=FastJSONHelper.serialize(req);
		
		if(!StringUtils.isEmpty(jsonstr)){
			try {
				re = _post_httpPostFormJsonStr(Urls.URL_ADD_COMMENT , jsonstr);
				if(!StringUtils.isEmpty(re)){
					JSONObject jsonObject = new JSONObject(re);
					bodyString = jsonObject.optString("Body");
					JSONObject obj=new JSONObject(bodyString);
					
					if(!obj.isNull("errCode")){
						return "";
					}else{
						return "1";
					}
				}
				
				
			} catch (Exception e) {
				e.getLocalizedMessage();
			}
		}
		return "";
	}
	/**
	 * 验证短信码
	 * @param id
	 * @param fansId
	 * @return
	 */
	public static String validateMessage(String telphone,String code){
		if(telphone==null||code==null){
			return "";
		}
		String re = "";
		String bodyString=""; 
		ValidateShortMessageCodeReq req=new ValidateShortMessageCodeReq(telphone, code);
		String jsonstr=FastJSONHelper.serialize(req);
		
		if(!StringUtils.isEmpty(jsonstr)){
			try {
				re = _post_httpPostFormJsonStr(Urls.ValidateShortMessageCode , jsonstr);
				if(!StringUtils.isEmpty(re)){
					JSONObject jsonObject = new JSONObject(re);
					bodyString = jsonObject.optString("Body");
					JSONObject obj=new JSONObject(bodyString);
					boolean IsPass=obj.getBoolean("isPass");
					if(IsPass){
						return "1";
					}else{
						return "";
					}
				}
				
				
			} catch (Exception e) {
				e.getLocalizedMessage();
			}
		}
		return "";
	}
	/**
	 * 修改店铺
	 * @param id
	 * @param fansId
	 * @return
	 */
	public static String updateShop(UpdateShopReq req){
		if(req==null){
			return "";
		}
		//UserDataB user=null;
		String re = "";
		String bodyString=""; 
		String jsonstr=FastJSONHelper.serialize(req);
		
		if(!StringUtils.isEmpty(jsonstr)){
			try {
				re = _post_httpPostFormJsonStr(Urls.URL_UPDATE_SHOP , jsonstr);
				if(!StringUtils.isEmpty(re)){
					JSONObject jsonObject = new JSONObject(re);
					bodyString = jsonObject.optString("Body");
					JSONObject obj=new JSONObject(bodyString);
					
					if(!obj.isNull("errCode")){
						return "";
					}else{
						return "1";
					}
				}
				
				
			} catch (Exception e) {
				e.getLocalizedMessage();
			}
		}
		return "";
	}
	/**
	 * 删除评论
	 * @param id
	 * @param createid
	 * @return
	 */
	public static String DeleteComment(String[] id,String createid){
		if(id==null){
			return null;
		}
		//UserDataB user=null;
		String re = "";
		String bodyString=""; 
		DeleteCommentReq req=new DeleteCommentReq(id);
		String jsonstr=FastJSONHelper.serialize(req);
		
		if(!StringUtils.isEmpty(jsonstr)){
			try {
				re = _post_httpPostFormJsonStr(Urls.URL_DELETE_COMMENT , jsonstr);
				if(!StringUtils.isEmpty(re)){
					JSONObject jsonObject = new JSONObject(re);
					bodyString = jsonObject.optString("Body");
					JSONObject obj=new JSONObject(bodyString);
					
					if(!obj.isNull("errCode")){
						return "";
					}else{
						return "1";
					}
				}
				
				
			} catch (Exception e) {
				e.getLocalizedMessage();
			}
		}
		return "";
	}
	/**
	 * 添加意见
	 * @param content 内容
	 * @param contact 联系方式
	 * @return
	 */
	public static String addOpinions(String content,String contact){
		if(content==null || contact==null){
			return null;
		}
		
		String re = "";
		String bodyString=""; 
		OpinionsReq op=new OpinionsReq(content, contact);
		String jsonstr=FastJSONHelper.serialize(op);
		
		if(!StringUtils.isEmpty(jsonstr)){
			try {
				re = _post_httpPostFormJsonStr(Urls.URL_Opinions , jsonstr);
				if(!StringUtils.isEmpty(re)){
					JSONObject jsonObject = new JSONObject(re);
					bodyString = jsonObject.optString("Body");
					JSONObject obj=new JSONObject(bodyString);
					
					if(!obj.isNull("errCode")){
						return "";
					}else{
						return "1";
					}
				}
				
				
			} catch (Exception e) {
				e.getLocalizedMessage();
			}
		}
		return "";
	}
	/**
	 * 修改个人信息
	 * @param content 内容
	 * @param contact 联系方式
	 * @return
	 */
	public static String updateUser(UserData user){
		if(user==null){
			return null;
		}
		
		String re = "";
		String bodyString=""; 
		
		String jsonstr=FastJSONHelper.serialize(user);
		
		if(!StringUtils.isEmpty(jsonstr)){
			try {
				re = _post_httpPostFormJsonStr(Urls.URL_UPDATE_USER , jsonstr);
				if(!StringUtils.isEmpty(re)){
					JSONObject jsonObject = new JSONObject(re);
					bodyString = jsonObject.optString("Body");
					JSONObject obj=new JSONObject(bodyString);
					
					if(!obj.isNull("errCode")){
						return "";
					}else{
						return "1";
					}
				}
				
				
			} catch (Exception e) {
				e.getLocalizedMessage();
			}
		}
		return "";
	}
	/**
	 * 上传头像
	 * @param filePath
	 * @return
	 */
	public static SaveVideoResult SaveImage(final String filePath) {
		//String newUrl = "";
		//final ReqBase reqBase = new ReqBase();
		SaveVideoResult saveVideoResult = null;

		String re = "";
		String bodyString = "";
		byte[] video_byte = FileUtil.getBytes(filePath);

		SaveVideoReq saveVideoReq = new SaveVideoReq(video_byte);
		String jsonstr = FastJSONHelper.serialize(saveVideoReq);

		try {
			re = _post_httpPostFormJsonStr(Urls.URL_SAVE_IMAGE, jsonstr);
			if (!StringUtils.isEmpty(re)) {
				JSONObject jsonObject = new JSONObject(re);
				bodyString = jsonObject.optString("Body");
			}
			saveVideoResult = FastJSONHelper.deserialize(bodyString,
					SaveVideoResult.class);

		} catch (Exception e) {
			e.getLocalizedMessage();
		}
		return saveVideoResult;
	}
	/**
	 * 创建店铺
	 * @param filePath
	 * @return
	 */
	public static ShopResult createShop(ShopData shop) {
		//String newUrl = "";
		ShopResult saveVideoResult = null;
		
		String re = "";
		String bodyString = "";
		String jsonstr = FastJSONHelper.serialize(shop);
		
		try {
			re = _post_httpPostFormJsonStr(Urls.URL_CREATE_SHOP, jsonstr);
			if (!StringUtils.isEmpty(re)) {
				JSONObject jsonObject = new JSONObject(re);
				bodyString = jsonObject.optString("Body");
			}
			saveVideoResult = FastJSONHelper.deserialize(bodyString,
					ShopResult.class);
			
		} catch (Exception e) {
			e.getLocalizedMessage();
		}
		return saveVideoResult;
	}
	/**
	 * 查找店铺
	 * @param filePath
	 * @return
	 */
	public static ShopModel GetShop(String id) {
		//String newUrl = "";
		ShopModel saveVideoResult = null;
		
		String re = "";
		String bodyString = "";
		ShopInfoReq req=new ShopInfoReq(id);
		String jsonstr = FastJSONHelper.serialize(req);
		
		// String jsonStr=obj.toString();
		
		try {
			re = _post_httpPostFormJsonStr(Urls.URL_GET_SHOP, jsonstr);
			if (!StringUtils.isEmpty(re)) {
				JSONObject jsonObject = new JSONObject(re);
				bodyString = jsonObject.optString("Body");
			}
			saveVideoResult = FastJSONHelper.deserialize(bodyString,
					ShopModel.class);
			
		} catch (Exception e) {
			e.getLocalizedMessage();
		}
		return saveVideoResult;
	}
	
	/**
	 * 获取制定用户的推送
	 * @param id 用户id
	 * @param pageNo 当前页数
	 * @return
	 */
	public static PushModels getPushes(String id,final int pageNo){
		String re = "";
		String bodyString=""; 
		PushModels   videosDataList= null;	
		PushReq req=new PushReq(id,pageNo);
		String jsonstr=FastJSONHelper.serialize(req);
		
		if(!StringUtils.isEmpty(jsonstr)){
			try {
				re = _post_httpPostFormJsonStr(Urls.URL_PUSH_LIST , jsonstr);
				if(!StringUtils.isEmpty(re)){
					JSONObject jsonObject = new JSONObject(re);
					bodyString = jsonObject.optString("Body");
				}
				videosDataList=FastJSONHelper.deserialize(bodyString, PushModels.class);
				
			} catch (Exception e) {
				e.getLocalizedMessage();
			}
		}
		
		return videosDataList;
	}
	/**
	 * 绑定用户信息
	 * @param id 用户id
	 * @param pageNo 当前页数
	 * @return
	 */
	public static AccountModels getAccounts(final int pageNo){
		String re = "";
		String bodyString=""; 
		AccountModels   videosDataList= null;	
		AccountReq req=new AccountReq(pageNo);
		String jsonstr=FastJSONHelper.serialize(req);
		
		if(!StringUtils.isEmpty(jsonstr)){
			try {
				re = _post_httpPostFormJsonStr(Urls.URL_ACCOUNT , jsonstr);
				if(!StringUtils.isEmpty(re)){
					JSONObject jsonObject = new JSONObject(re);
					bodyString = jsonObject.optString("Body");
				}
				videosDataList=FastJSONHelper.deserialize(bodyString, AccountModels.class);
				
			} catch (Exception e) {
				e.getLocalizedMessage();
			}
		}
		
		return videosDataList;
	}
	/**
	 * 获取制定用户的粉丝列表
	 * @param id 用户id
	 * @param pageNo 当前页数
	 * @return
	 */
	public static FriendsDataList getFans(String id,final int pageNo){
		String re = "";
		String bodyString=""; 
		FriendsDataList   videosDataList= null;	
		FansReq req=new FansReq(id,pageNo);
		String jsonstr=FastJSONHelper.serialize(req);
		
		if(!StringUtils.isEmpty(jsonstr)){
			try {
				re = _post_httpPostFormJsonStr(Urls.URL_FANS_LIST , jsonstr);
				if(!StringUtils.isEmpty(re)){
					JSONObject jsonObject = new JSONObject(re);
					bodyString = jsonObject.optString("Body");
				}
				videosDataList=FastJSONHelper.deserialize(bodyString, FriendsDataList.class);;
				
			} catch (Exception e) {
				e.getLocalizedMessage();
			}
		}
		
		return videosDataList;
	}
	/**
	 * 获取制定用户的关注列表
	 * @param id 用户id
	 * @param pageNo 当前页数
	 * @return
	 */
	public static FriendsDataList getAttention(String id,final int pageNo){
		String re = "";
		String bodyString=""; 
		FriendsDataList   videosDataList= null;	
		FansReq req=new FansReq(id,pageNo);
		String jsonstr=FastJSONHelper.serialize(req);
		
		if(!StringUtils.isEmpty(jsonstr)){
			try {
				re = _post_httpPostFormJsonStr(Urls.URL_ATTENTION_LIST , jsonstr);
				if(!StringUtils.isEmpty(re)){
					JSONObject jsonObject = new JSONObject(re);
					bodyString = jsonObject.optString("Body");
				}
				videosDataList=FastJSONHelper.deserialize(bodyString, FriendsDataList.class);;
				
			} catch (Exception e) {
				e.getLocalizedMessage();
			}
		}
		
		return videosDataList;
	}

	
	
	
	
	
	
	
	
	
}
