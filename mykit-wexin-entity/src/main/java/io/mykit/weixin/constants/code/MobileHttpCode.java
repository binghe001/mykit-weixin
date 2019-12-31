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

	/**
	 * 没有权限发送模板消息
	 */
	public static final int HTTP_NO_LIMIT_TO_SEND_TEMPLATE = 6005;
	/**
	 * 没有权限发送客服消息
	 */
	public static final int HTTP_NO_LIMIT_TO_SEND_CUSTOM = 6006;
	/**
	 * 微信生成二维码报错
	 */
	public static final int HTTP_WECHAT_CREATE_QRCODE_ERROR = 6007;
	/**
	 * 下载微信二维码失败
	 */
	public static final int HTTP_WECHAT_DOWNLOAD_QRCODE_ERROR = 6008;
	/**
	 * 微信二维码已存在，不可重复生成
	 */
	public static final int HTTP_WECHAT_QRCODE_EXISTS = 6009;
	/**
	 * 未生成二维码或二维码已失效
	 */
	public static final int HTTP_WECHAT_QRCODE_NOT_EXISTS = 6010;

}
