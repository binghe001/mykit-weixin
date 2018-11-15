package io.mykit.weixin.constants.code;

/**
 * 基本的HttpCode状态码(以1开头)
 * @author liuyazhuang
 *
 */
public abstract class BaseHttpCode {
	
	/**
	 * 客户端升级
	 */
	public static final int HTTP_UPGRADE = 0XFFFFFF;
	/**
	 * 正常
	 */
	public static final int HTTP_NORMAL = 1001;
	/**
	 * IO异常
	 */
	public static final int HTTP_IO_EXCEPTION = 1002;
	
	/**
	 * 网络连接异常
	 */
	public static final int HTTP_CONNECTION_EXCEPTION = 1003;
	
	/**
	 * 网络连接超时
	 */
	public static final int HTTP_CONNECTION_BEYOND_TIME_LIMIT = 1004;
	/**
	 * 参数无效
	 */
	public static final int HTTP_PARAMETER_INVALID = 1005;
	
	
	/**
	 * 空指针异常
	 */
	public static final int HTTP_NONE_POINTER_EXCEPTION = 1006;
	
	/**
	 * URL异常
	 */
	public static final int HTTP_URL_EXCEPTION = 1007;
	
	/**
	 * 未知主机
	 */
	public static final int HTTP_UNKNOWN_HOST = 1008;
	
	/**
	 * 服务器连接失败
	 */
	public static final int HTTP_SERVER_CONNECTION_FAILED = 1009;
	
	/**
	 * 协议解析异常
	 */
	public static final int HTTP_PARSING_EXCEPTION = 1010;
	
	/**
	 * Access_Token鉴权失败
	 */
	public static final int HTTP_ACCESS_TOKEN_ERROR = 1011;
	
	/**
	 * 验证码失效
	 */
	public static final int HTTP_VALIDATE_CODE_ERROR = 1012;
	
	/**
	 * 未知错误
	 */
	public static final int HTTP_UNKNOWN_ERROR = 1013;
	
	/**
	 * 参数解析异常
	 */
	public static final int HTTP_PARAMETER_PARING_EXCEPTION = 1014;
	
	/**
	 * 保存数据失败
	 */
	public static final int HTTP_SAVE_DATA_FAILED = 1015;
	
	/**
	 * 更新数据失败
	 */
	public static final int HTTP_UPDATE_DATA_FAILED = 1016;
	
	/**
	 * 查询数据失败
	 */
	public static final int HTTP_SEARCH_DATA_FAILED = 1017;
	
	/**
	 * 删除数据失败
	 */
	public static final int HTTP_DELETE_DATA_FAILED = 1018;
	
	/**
	 * 服务端异常
	 */
	public static final int HTTP_SERVER_EXCEPTION = 1019;	
	
	/**
	 * 登录失败
	 */
	public static final int HTTP_LOGIN_FAILED = 1020;
	
	/**
	 * 注册失败
	 */
	public static final int HTTP_REGISTER_FAILED = 1021;
	
	/**
	 * 账号已被注册或者已经绑定了其他信息
	 */
	public static final int HTTP_REGISTER_EXIST = 1022;
	
	/**
	 * 账号未注册
	 */
	public static final int HTTP_NOT_REGISTER = 1023;
	
	/**
	 * 数据验证失败
	 */
	public static final int HTTP_VALIDATE_FAILED = 1024;
	
	/**
	 * 数据解析失败
	 */
	public static final int HTTP_PARSE_FAILED = 1025;
	/**
	 * 权限不足
	 */
	public static final int HTTP_PARSE_LIMIT = 1026;
	/**
	 * 签名错误
	 */
	public static final int HTTP_SYSTEM_SIGN_ERROR = 1027;
	
	/**
	 * 密码错误
	 */
	public static final int HTTP_PWD_ERRPR = 1028;
	
	/**
	 * 请求成功,但数据为空
	 */
	public static final int HTTP_QUERY_SUCCESS_BUT_DATA_NULL = 1029;
	
	/**
	 * 登录超出设备限制
	 */
	public static final int HTTP_ACCOUNT_BEYOND_LIMIT = 1030;
	
	/**
	 * 用户被封禁,登录异常
	 */
	public static final int HTTP_USER_STATUS_NOT_NORMAL = 1031;
	
	/**
	 * 账户余额不足
	 */
	public static final int HTTP_ACCOUNT_NOT_ENOUGH = 1032;
}
