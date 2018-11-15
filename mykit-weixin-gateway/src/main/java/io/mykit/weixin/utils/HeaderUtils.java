package io.mykit.weixin.utils;

import com.alibaba.fastjson.JSONObject;
import io.mykit.weixin.constants.logs.LogsConstants;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取Header中的数据工具
 * @author liuyazhuang
 *
 */
public class HeaderUtils {
	
	public static final String IP = "IP";
	public static final String MAC = "MAC";
	public static final String CLIENTTYPE = "clientType";
	public static final String SHOPTYPE = "shopType";
	public static final String SYSTEMTYPE = "systemType";
	public static final String SYSTEMVERSION = "systemVersion";
	public static final String PHONEMODEL = "phoneModel";
	public static final String APPVERSION = "appVersion";
	public static final String API = "api";
	public static final String LAT = "lat";
	public static final String LNG = "lng";
	
	/**
	 * 获取HttpServletRequest中的所有header
	 * @param req
	 * @return
	 */
	public static Map<String, String> getHeaders(HttpServletRequest req){
		Map<String, String> headers = new HashMap<String, String>();
		String IP = req.getHeader(LogsConstants.IP) == null ? "" : req.getHeader(LogsConstants.IP);
		headers.put(LogsConstants.IP, IP);
		String Mac = req.getHeader(LogsConstants.MAC) == null ? "" : req.getHeader(LogsConstants.MAC);
		headers.put(LogsConstants.MAC, Mac);
		String clientType = req.getHeader(LogsConstants.CLIENT_TYPE) == null ? "" : req.getHeader(LogsConstants.CLIENT_TYPE);
		headers.put(LogsConstants.CLIENT_TYPE, clientType);
		String shopType = req.getHeader(LogsConstants.SHOP_TYPE) == null ? "" : req.getHeader(LogsConstants.SHOP_TYPE);
		headers.put(LogsConstants.SHOP_TYPE, shopType);
		String systemType = req.getHeader(LogsConstants.SYSTEM_TYPE) == null ? "" : req.getHeader(LogsConstants.SYSTEM_TYPE);
		headers.put(LogsConstants.SYSTEM_TYPE, systemType);
		String systemVersion = req.getHeader(LogsConstants.SYSTEM_VERSION) == null ? "" : req.getHeader(LogsConstants.SYSTEM_VERSION);
		headers.put(LogsConstants.SYSTEM_VERSION, systemVersion);
		String phoneModel = req.getHeader(LogsConstants.PHONE_MODEL) == null ? "" : req.getHeader(LogsConstants.PHONE_MODEL);
		headers.put(LogsConstants.PHONE_MODEL, phoneModel);
		String appVersion = req.getHeader(LogsConstants.APP_VERSION) == null ? "" : req.getHeader(LogsConstants.APP_VERSION);
		headers.put(LogsConstants.APP_VERSION, appVersion);
		String lat = req.getHeader(LogsConstants.LAT) == null ? "" : req.getHeader(LogsConstants.LAT);
		headers.put(LogsConstants.LAT, lat);
		String lng = req.getHeader(LogsConstants.LNG) == null ? "" : req.getHeader(LogsConstants.LNG);
		headers.put(LogsConstants.LNG, lng);
		return headers;
	}

	/**
	 * 防止重复提交需要的信息
	 * @param req
	 * @return
	 */
	public static JSONObject parseRepeatHeaders(HttpServletRequest req){
		String Mac = req.getHeader(LogsConstants.MAC) == null ? "" : req.getHeader(LogsConstants.MAC);
		String clientType = req.getHeader(LogsConstants.CLIENT_TYPE) == null ? "" : req.getHeader(LogsConstants.CLIENT_TYPE);
		String shopType = req.getHeader(LogsConstants.SHOP_TYPE) == null ? "" : req.getHeader(LogsConstants.SHOP_TYPE);
		String systemType = req.getHeader(LogsConstants.SYSTEM_TYPE) == null ? "" : req.getHeader(LogsConstants.SYSTEM_TYPE);
		String systemVersion = req.getHeader(LogsConstants.SYSTEM_VERSION) == null ? "" : req.getHeader(LogsConstants.SYSTEM_VERSION);
		String phoneModel = req.getHeader(LogsConstants.PHONE_MODEL) == null ? "" : req.getHeader(LogsConstants.PHONE_MODEL);
		String appVersion = req.getHeader(LogsConstants.APP_VERSION) == null ? "" : req.getHeader(LogsConstants.APP_VERSION);
		String api = req.getRequestURL().toString();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(HeaderUtils.MAC, Mac);
		jsonObject.put(HeaderUtils.CLIENTTYPE, clientType);
		jsonObject.put(HeaderUtils.SHOPTYPE, shopType);
		jsonObject.put(HeaderUtils.SYSTEMTYPE, systemType);
		jsonObject.put(HeaderUtils.SYSTEMVERSION, systemVersion);
		jsonObject.put(HeaderUtils.PHONEMODEL, phoneModel);
		jsonObject.put(HeaderUtils.APPVERSION, appVersion);
		jsonObject.put(HeaderUtils.API, api);
		return jsonObject;
	}

	/**
	 * 获取Header中指定的数据
	 * @param req
	 * @return
	 */
	public static JSONObject parseHeaders(HttpServletRequest req){
		String IP = req.getHeader(LogsConstants.IP) == null ? "" : req.getHeader(LogsConstants.IP);
		String Mac = req.getHeader(LogsConstants.MAC) == null ? "" : req.getHeader(LogsConstants.MAC);
		String clientType = req.getHeader(LogsConstants.CLIENT_TYPE) == null ? "" : req.getHeader(LogsConstants.CLIENT_TYPE);
		String shopType = req.getHeader(LogsConstants.SHOP_TYPE) == null ? "" : req.getHeader(LogsConstants.SHOP_TYPE);
		String systemType = req.getHeader(LogsConstants.SYSTEM_TYPE) == null ? "" : req.getHeader(LogsConstants.SYSTEM_TYPE);
		String systemVersion = req.getHeader(LogsConstants.SYSTEM_VERSION) == null ? "" : req.getHeader(LogsConstants.SYSTEM_VERSION);
		String phoneModel = req.getHeader(LogsConstants.PHONE_MODEL) == null ? "" : req.getHeader(LogsConstants.PHONE_MODEL);
		String appVersion = req.getHeader(LogsConstants.APP_VERSION) == null ? "" : req.getHeader(LogsConstants.APP_VERSION);
		String lat = req.getHeader(LogsConstants.LAT) == null ? "" : req.getHeader(LogsConstants.LAT);
		String lng = req.getHeader(LogsConstants.LNG) == null ? "" : req.getHeader(LogsConstants.LNG);
		String api = req.getRequestURL().toString();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(HeaderUtils.IP, IP);
		jsonObject.put(HeaderUtils.MAC, Mac);
		jsonObject.put(HeaderUtils.CLIENTTYPE, clientType);
		jsonObject.put(HeaderUtils.SHOPTYPE, shopType);
		jsonObject.put(HeaderUtils.SYSTEMTYPE, systemType);
		jsonObject.put(HeaderUtils.SYSTEMVERSION, systemVersion);
		jsonObject.put(HeaderUtils.PHONEMODEL, phoneModel);
		jsonObject.put(HeaderUtils.APPVERSION, appVersion);
		jsonObject.put(HeaderUtils.API, api);
		jsonObject.put(HeaderUtils.LAT, lat);
		jsonObject.put(HeaderUtils.LNG, lng);
		return jsonObject;
	}
}
