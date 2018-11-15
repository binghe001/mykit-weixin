package io.mykit.weixin.constants.code;

/**
 * 状态码
 * @author liuyazhuang
 *
 */
public class MobileHttpCode extends BaseHttpCode {

	/**
	 * 操作过于频繁
	 */
	public static final int SYSTEM_HANDLER_FREQUENTLY = 2319;
	/**
	 * 只包含特殊字符或敏感词
	 */
	public static final int SYSTEM_HANDLER_SPECIAL_CHARACTER = 2320;


	/**
	 * 未获取到微信开发者账户信息
	 */
	public static final int HTTP_NOT_GET_WECHAT_ACCOUNT = 6001;
	/**
	 * 未获取到微信消息模板
	 */
	public static final int HTTP_NOT_GET_WECHAT_TEMPLATE = 6002;
	/**
	 * 未获取到微信用户的openid
	 */
	public static final int HTTP_NOT_GET_WECHAT_OPEN_ID = 6003;
	/**
	 * 微信模板消息推送失败
	 */
	public static final int HTTP_NOT_GET_WECHAT_TEMPLATE_SEND_FAILED = 6004;

}
