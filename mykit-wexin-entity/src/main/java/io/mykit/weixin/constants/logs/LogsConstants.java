package io.mykit.weixin.constants.logs;

/**
 * 系统日志常量类
 * @author liuyazhuang
 *
 */
public class LogsConstants {
	
	/**
	 * 缓存的系统日志Key
	 */
	public static final String CACHE_KEY = "system_logs";
	
	/**
	 * 缓存的日志消息key
	 */
	public static final String CACHE_MESSAGE_KEY = "system_message_logs";
	
	/**
	 * 缓存查询的消息日志
	 */
	public static final String CACHE_QUERY_MESSAGE_KEY = "system_query_message_logs";
	
	/**
	 * 1分钟缓存
	 */
	public static final long TIME_EXPIRE = System.currentTimeMillis() + 60 * 1000;
	
	/**
	 * 长度设置
	 */
	public static final int LIST_SIZE = 100;
	
	/**
	 * ip地址
	 */
	public static final String IP = "ip";
	
	/**
	 * Mac地址
	 */
	public static final String MAC = "mac";
	
	/**
	 * 客户端类型(type_user:用户端; type_doc:医生端)
	 */
	public static final String CLIENT_TYPE = "client-type";
	/**
	 * 应用市场类型(String,标识各大应用市场)
	 */
	public static final String SHOP_TYPE = "shop-type";
	/**
	 * 操作系统类型(String, type_ios:IOS  type_android:Android； type_h5:前端; type_sp:小程序; type_back:后台)
	 */
	public static final String SYSTEM_TYPE = "system-type";
	/**
	 * 操作系统版本(String)
	 */
	public static final String SYSTEM_VERSION = "system-version";
	/**
	 * 手机型号
	 */
	public static final String PHONE_MODEL = "phone-model";
	/**
	 * 当前APP版本
	 */
	public static final String APP_VERSION = "app-version";
	
	/**
	 * 操作系统类型-IOS操作系统
	 */
	public static final String TYPE_IOS = "type_ios";
	/**
	 * 操作系统类型-ANDROID操作系统
	 */
	public static final String TYPE_ANDROID = "type_android";
	/**
	 * 操作系统类型-H5前端
	 */
	public static final String TYPE_H5 = "type_h5";
	/**
	 * 小程序
	 */
	public static final String TYPE_SP = "type_sp";
	/**
	 * 后台
	 */
	public static final String TYPE_BACK = "type_back";
	/**
	 * APP
	 */
	public static final String TYPE_APP = "type_app";
	/**
	 * 纬度
	 */
	public static final String LAT = "lat";
	/**
	 * 经度
	 */
	public static final String LNG = "lng";
}
