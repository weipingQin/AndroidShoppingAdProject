package com.www.goumei.http;



public class Urls {
	public static final String BASEURL = "http://121.41.82.43:8081/";
	/** 登录*/
	public static final String URL_LOGIN = BASEURL + "api/Users/Login";
	public static final String URL_REGISTER = BASEURL + "api/Users/Create";
	/** 关于*/
	public static final String URL_ABOUT = BASEURL + "api/Abouts/Get ";

	public static final String URL_VIDEOLIST = BASEURL + "api/Videos/GetList";
	/** 获取热门视频*/
	public static final String URL_GETHOTS = BASEURL + "api/Videos/GetHots";
	/**查询好友*/
	public static final String queryUser = BASEURL + "api/Users/GetList";
	/**获取个人资料*/
	public static final String URL_GETUSER_INFO = BASEURL + "api/Users/GetIndividual";
	/**添加关注 */
	public static final String URL_ADD_ATTENTION = BASEURL + "api/Attention/Create";
	/**取消关注 */
	public static final String URL_CANCEL_ATTENTION = BASEURL + "api/Attention/Cancel";
	/**添加喜欢 */
	public static final String URL_ADD_PRAISE = BASEURL + "api/VideosPraiseHistory/Create";
	/**取消喜欢 */
	public static final String URL_CANCLE_PRAISE = BASEURL + "api/VideosPraiseHistory/Cancel";
	/**添加评论 */
	public static final String URL_ADD_COMMENT = BASEURL + "api/Comments/Create";
	/**删除评论 */
	public static final String URL_DELETE_COMMENT = BASEURL + "api/Comments/Delete";
	/**意见反馈 */
	public static final String URL_Opinions = BASEURL + "api/Opinions/Create";
	/**修改个人资料 */
	public static final String URL_UPDATE_USER = BASEURL + "api/Users/Update";
	/**推送消息 */
	public static final String URL_PUSH_LIST = BASEURL + "api/Pushes/GetList";
	/**粉丝 */
	public static final String URL_FANS_LIST = BASEURL + "api/Users/GetFans";
	/**关注 */
	public static final String URL_ATTENTION_LIST = BASEURL + "api/Users/GetAttentions";
	/**评论 */
	public static final String URL_COMMENTS_LIST = BASEURL + "api/Comments/GetList";
	/**喜欢 */
	public static final String URL_PRAISEME_LIST = BASEURL + "api/Users/GetPraiseMe";
	/**上传头像 */
	public static final String URL_SAVE_IMAGE = BASEURL + "api/Common/SaveImage";
	/**创建店铺*/
	public static final String URL_CREATE_SHOP = BASEURL + "api/Shop/Create";
	/**查询店铺*/
	public static final String URL_GET_SHOP = BASEURL + "api/Shop/Get";
	/**修改店铺*/
	public static final String URL_UPDATE_SHOP = BASEURL + "api/Shop/Update";
	/**省*/
	public static final String URL_PROVINCE = BASEURL + "api/Province/GetList";
	/**市*/
	public static final String URL_CITY = BASEURL + "api/City/GetList";
	/**区*/
	public static final String URL_DISTRICT = BASEURL + "api/District/GetList";
	/**绑定账号信息*/
	public static final String URL_ACCOUNT = BASEURL + "api/UsersAccount/GetList";
	public static final String URL_ACCOUNT_BIND = BASEURL + "api/UsersAccount/Create";
	/**地方登陆*/
	public static final String URL_LOGIN_OTHER = BASEURL + "api/Users/Login";
	/**发现*/
	public static final String URL_DISCOVER=BASEURL +"api/Themes/GetList";
	/** 获取分类 */
	public static final String URL_CATEGORIES=BASEURL +"api/Categories/GetList";
	
	public static final String URL_DISCOVER_OPEN=BASEURL +"api/Videos/GetList";
	/**分类*/
	public static final String URL_CLASSIFY=BASEURL +"api/Categories/GetList";
	 /** 保存视频*/
	public static final String SaveVideo =BASEURL +"api/Common/SaveVideo";
	/** 保存封面图片*/
	public static final String SAVE_PICTURE = BASEURL + "api/Common/SaveImage";
	/** 上传视频信息 */
	public static final String SAVE_VIDEO_DETAILS = BASEURL + "api/Videos/Create";
	/**收取 手机验证码*/
	public static final String GetShortMessageCode =BASEURL +"api/ShortMessage/GetShortMessageCode";
	/**验证 手机验证码*/
	public static final String ValidateShortMessageCode=BASEURL +"api/ShortMessage/ValidateShortMessageCode";
    /**登录*/
	public static final String Login  =BASEURL +"api/Users/Login";
}
