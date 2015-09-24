package com.www.goumei.utils;
/**
 * 常量
 * @author json
 *
 */
public class Constant {
	/**
	 * 分页的个数
	 */
	public static final int PAGE_SIZE=10;
	/**
	 * 不分页
	 */
	public static final int PAGE_MAX_SIZE=99;
	/**
	 * 关注状态变化action
	 */
	public static final String ACTION_ATTENTION_STATUS="com.attention.status";
	/**
	 * 赞状态变化action
	 */
	public static final String ACTION_PRAISE_STATUS="com.praise.status";
	/**
	 * 寻找好友action
	 */
	public static final String ACTION_FIND_FRIEND="com.attention.friend";
	/**
	 * 粉丝action
	 */
	public static final String ACTION_ADD_FANS="com.attention.fans";
	/**
	 * 下标
	 */
	public static final String INTENT_INDEX="index";
	/**
	 * 视频id
	 */
	public static final String INTENT_VIDEO_ID="video_id";
	/**
	 * 状态
	 */
	public static final String INTENT_STATUS="status";
	/**
	 * 是否已赞状态
	 */
	public static final String INTENT_PRAISE_STATUS="praiise_status";
	/**
	 * 关注了
	 */
	public static final String INTENT_STATUS_TRUE="true";
	/**
	 * 取消关注了
	 */
	public static final String INTENT_STATUS_FALSE="false";
	/**
	 * 保存处理过的视频
	 */
	public static final String SP_PIC_SAVE="sp_save";
	/**
	 * wifi自动播放
	 */
	public static final String SP_WIFI_PLAY="sp_play";
	/**
	 * 删除评论action
	 */
	public static final String ACTION_COMMENT_DELETE="com.comment.delete";
	/**
	 * 用户键
	 */
	public static final String USERDATA="userdata";
	/**
	 * 用户id键
	 */
	public static final String OTHER_ID="otherId";
	/**
	 * 用户主页默认选项键
	 */
	public static final String HOME_PAGE_INDEX="homePageIndex";
	/**
	 * 店铺键
	 */
	public static final String SHOPDATA="shopdata";
	/**
	 * 店铺id键
	 */
	public static final String SHOPID="shopid";
	/**
	 * 地址id键
	 */
	public static final String ADDRESS="address";
	/**
	 * 视频键
	 */
	public static final String VIDEO="video";
	/**
	 * 回复人id
	 */
	public static final String REPLYUSERID="ReplyUserID";
	/**
	 * 当前用户id键
	 */
	public static final String ONLINE_ID="onlineID";
	/**
	 * 男性别
	 */
	public static final int SEX_M=1;
	/**
	 * 女性别
	 */
	public static final int SEX_F=0;
	/**
	 * 用户认证
	 */
	public static final int UserCertificationState_V=1;
	/**
	 * 已赞
	 */
	public static final int ISPRAISE_YES=1;
	/**
	 * 未赞
	 */
	public static final int ISPRAISE_NO=0;
	public static final String MESSAGE_RECEIVED_ACTION = "com.www.goumei.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";
}
