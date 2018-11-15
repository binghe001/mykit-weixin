package io.mykit.weixin.utils.resp.helper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.mykit.wechat.utils.common.FileCopyUtils;
import io.mykit.weixin.utils.sign.sl.Sign;

import javax.servlet.http.HttpServletResponse;
import java.io.*;


/**
 * 响应帮助类，统一响应客户端的请求
 * @author liuyazhuang
 *
 */
public final class ResponseHelper {

	public static final String CODE = "code";
	public static final String MESSAGE = "message";
	public static final String SIGN = "sign";
	public static final String CODESTR = "codeStr";

	/**
	 * 对外方法
	 * @param message:响应的主体信息(只返回状态码时传入null)
	 * @param isArray:标识为是否为数组类型,true:是;false：否
	 * @param code:服务端返回的相应状态码
	 * @param response:HttpServletResponse对象
	 */
	public static synchronized void responseMessage(Object message, boolean isArray, boolean isSign, Integer code, HttpServletResponse response){
		//发送响应消息
		sendMessage(getMessageForSend(message, isArray, isSign, code), response);
	}

	/**
	 * 对外方法
	 * @param message:响应的主体信息(只返回状态码时传入null)
	 * @param isArray:标识为是否为数组类型,true:是;false：否
	 * @param code:服务端返回的相应状态码
	 * @param response:HttpServletResponse对象
	 */
	public static synchronized void responseMessage(Object message, boolean isArray, boolean isSign, Integer code, String codeStr, HttpServletResponse response){
		//发送响应消息
		sendMessage(getMessageForSend(message, isArray, isSign, code, codeStr), response);
	}


	/**
	 * 获取即将发送的消息
	 * @param message：业务的返回结果
	 * @param isArray：是否是数组或者集合
	 * @param isSign：是否需要签名
	 * @param code：状态码
	 * @return：返回封装好的字符串
	 */
	public static String getMessageForSend(Object message, boolean isArray, boolean isSign, Integer code){
		String msg = null;
		//只返回状态码
		if(message == null){
			msg = getMessage(code, isSign);
		}else{
			msg = getMessage(message, isArray, code, isSign);
		}
		return msg;
	}
	/**
	 * 获取即将发送的消息
	 * @param message：业务的返回结果
	 * @param isArray：是否是数组或者集合
	 * @param isSign：是否需要签名
	 * @param code：状态码
	 * @return：返回封装好的字符串
	 */
	public static String getMessageForSend(Object message, boolean isArray, boolean isSign, Integer code, String codeStr){
		String msg = null;
		//只返回状态码
		if(message == null){
			msg = getMessage(code, codeStr, isSign);
		}else{
			msg = getMessage(message, isArray, code, codeStr, isSign);
		}
		return msg;
	}


	/**
	 * 未跨域的时候调用
	 * @param message:响应的主体信息
	 * @param isArray:标识为是否为数组类型,true:是;false：否
	 * @param code:服务端返回的相应状态码
	 * @return:返回的完整响应信息
	 */
	private static String getMessage(Object message, boolean isArray, Integer code, boolean isSign){
		JSONObject jsonObject = new JSONObject();
		if(isSign){
			jsonObject.put(SIGN, JSONObject.parseObject(JSONObject.toJSONString(new Sign())));
		}
		jsonObject.put(CODE, code);
		if(isArray){
			JSONArray jsonArray = JSONArray.parseArray(JSONArray.toJSONString(message,SerializerFeature.WriteMapNullValue
					,SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.WriteNullNumberAsZero, SerializerFeature.DisableCircularReferenceDetect));
			jsonObject.put(MESSAGE, jsonArray);
		}else{
			JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(message,SerializerFeature.WriteMapNullValue
					,SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.WriteNullNumberAsZero, SerializerFeature.DisableCircularReferenceDetect));
			jsonObject.put(MESSAGE, object);
		}
		return jsonObject.toString();
	}

	/**
	 * 未跨域的时候调用
	 * @param message:响应的主体信息
	 * @param isArray:标识为是否为数组类型,true:是;false：否
	 * @param code:服务端返回的相应状态码
	 * @return:返回的完整响应信息
	 */
	private static String getMessage(Object message, boolean isArray, Integer code, String codeStr, boolean isSign){
		JSONObject jsonObject = new JSONObject();
		if(isSign){
			jsonObject.put(SIGN, JSONObject.parseObject(JSONObject.toJSONString(new Sign())));
		}
		jsonObject.put(CODE, code);
		jsonObject.put(CODESTR, codeStr);
		if(isArray){
			JSONArray jsonArray = JSONArray.parseArray(JSONArray.toJSONString(message,SerializerFeature.WriteMapNullValue
					,SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.WriteNullNumberAsZero, SerializerFeature.DisableCircularReferenceDetect));
			jsonObject.put(MESSAGE, jsonArray);
		}else{
			JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(message,SerializerFeature.WriteMapNullValue
					,SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.WriteNullNumberAsZero, SerializerFeature.DisableCircularReferenceDetect));
			jsonObject.put(MESSAGE, object);
		}
		return jsonObject.toString();
	}

	/**
	 * 未跨域时只返回状态码调用
	 * @param code:状态码
	 * @return
	 */
	private static String getMessage(Integer code, boolean isSign){
		JSONObject jsonObject = new JSONObject();
		if(isSign){
			jsonObject.put(SIGN, JSON.toJSON(new Sign()));
		}
		jsonObject.put(CODE, code);
		return jsonObject.toString();
	}
	/**
	 * 未跨域时只返回状态码调用
	 * @param code:状态码
	 * @return
	 */
	private static String getMessage(Integer code, String codeStr,  boolean isSign){
		JSONObject jsonObject = new JSONObject();
		if(isSign){
			jsonObject.put(SIGN, JSON.toJSON(new Sign()));
		}
		jsonObject.put(CODE, code);
		jsonObject.put(CODESTR, codeStr);
		return jsonObject.toString();
	}


	/**
	 * 跨域时调用(封装跨域访问时的完整响应信息)
	 * @param message:响应的主体信息
	 * @param jsoncallback:web客户端跨域访问时的key,从客户端请求中获取
	 * @param isArray:标识为是否为数组类型,true:是;false：否
	 * @param code:服务端返回的相应状态码
	 * @return:返回的完整响应信息
	 */
//	private String getMessage(Object message, Object jsoncallback, boolean isArray, Integer code){
//		StringBuffer sb = new StringBuffer();
//		sb.append(jsoncallback);
//		sb.append("(");
//		JSONObject jsonObject = new JSONObject();
//		jsonObject.put(MobileServerConstants.CODE, code);
//		if(isArray){
//			JsonArray JsonArray = JsonArray.fromObject(message);
//			jsonObject.put(MobileServerConstants.MESSAGE, JsonArray);
//		}else{
//			JSONObject msg = JSONObject.fromObject(message);
//			jsonObject.put(MobileServerConstants.MESSAGE, msg);
//		}
//		sb.append(jsonObject);
//		sb.append(")");
//		return sb.toString();
//	}

	/**
	 * 只返回状态时调用
	 * @param jsoncallback：web客户端跨域访问时的key,从客户端请求中获取
	 * @param code:服务端返回的相应状态码
	 * @return:返回的响应信息
	 */
//	private String getMessage(Object jsoncallback, Integer code){
//		StringBuffer sb = new StringBuffer();
//		sb.append(jsoncallback);
//		sb.append("(");
//		JSONObject jsonObject = new JSONObject();
//		jsonObject.put(MobileServerConstants.CODE, code);
//		sb.append(jsonObject);
//		sb.append(")");
//		return sb.toString();
//	}

	/**
	 * 响应客户端请求
	 * @param message:响应的完整信息
	 * @param response:HttpServletResponse对象
	 */
	public static void sendMessage(String message, HttpServletResponse response){
		PrintWriter printWriter = null;
		try {
			printWriter = response.getWriter();
			response.setHeader("Content-Type", "application/json; charset=utf-8");
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods",
					"GET, POST, PUT, DELETE, OPTIONS");
			response.setHeader("Access-Control-Allow-Headers",
					"origin, content-type, accept, x-requested-with, sid, mycustom, smuser, access-token, user-id, role-type, Content-Type, x-requested-with, X-Custom-Header, HaiYi-Access-Token, Access-token, hosp-id, client-type, app-version, type, increase-count, ip, mac, shop-type, system-type, system-version, phone-model, lat, lng ");
			response.setHeader("Allow", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
			response.setHeader("Access-Control-Allow-Credentials", "true");
			printWriter.write(message);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(printWriter != null){
				printWriter.flush();
				printWriter.close();
				printWriter = null;
			}
		}
	}

	/**
	 * 发送xml格式数据
	 * @param message:响应的完整信息
	 * @param response:HttpServletResponse对象
	 */
	public static void sendXmlMessage(String message, HttpServletResponse response){
		PrintWriter printWriter = null;
		try {
			printWriter = response.getWriter();
			printWriter.write(message);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(printWriter != null){
				printWriter.flush();
				printWriter.close();
				printWriter = null;
			}
		}
	}

	/**
	 * 下载文件
	 * @param file
	 * @param response
	 */
	public static void sendFile(File file, HttpServletResponse response){
		try {
			response.addHeader("Content-Type", "application/octet-stream");
			response.addHeader("Content-Length", String.valueOf(file.length()));
			response.addHeader("Last-Modified", String.valueOf(file.lastModified()));
			response.setHeader("Content-disposition", "attachment; filename=" + new String(file.getName().getBytes("utf-8"), "ISO8859-1"));
			InputStream in = new FileInputStream(file);
			BufferedInputStream bin = new BufferedInputStream(in);
			OutputStream out = response.getOutputStream();
			BufferedOutputStream bout = new BufferedOutputStream(out);
			FileCopyUtils.copy(bin, bout);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
