package io.mykit.weixin.utils.sign.rsa;

import io.mykit.wechat.utils.common.StringUtils;
import io.mykit.weixin.utils.sign.base.Base64;
import io.mykit.weixin.utils.sign.base.BaseSignUtils;

import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Map;


/**
 * RSA签名
 * @author liuyazhuang
 *
 */
public class RSASignUtils extends BaseSignUtils {
	
	private static final String ALGORITHM = "RSA";

	private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	private static final String DEFAULT_CHARSET = "UTF-8";
	
	private static final String CHAR = "&";
	private static final String SIGN = "sign";
	private static final String SIGN_TYPE = "sign_type";
	
	/**
	 * 获取签名
	 * @param map:封装所有的key与value键值对的map
	 * @param privateKey
	 * @return
	 */
	public static String getSignURLencode(Map<String, Object> map, String privateKey) throws Exception{
		String sign = getSign(map, privateKey);
		return URLEncoder.encode(sign, DEFAULT_CHARSET);
	}
	
	/**
	 * 获取签名
	 * @param o
	 * @return
	 * @throws Exception
	 */
	public static String getSignURLencode(Object o, String privateKey) throws Exception{
		String sign = getSign(o, privateKey);
		return URLEncoder.encode(sign, DEFAULT_CHARSET);
	}
	
	/**
	 * 获取签名
	 * @param map:封装所有的key与value键值对的map
	 * @param privateKey
	 * @return
	 */
	public static String getSign(Map<String, Object> map, String privateKey) throws Exception{
		if(map.containsKey(SIGN)) map.remove(SIGN);
		if(map.containsKey(SIGN_TYPE)) map.remove(SIGN_TYPE);
		String ret = getSort(map);
		if(ret.endsWith(CHAR)){
			ret = ret.substring(0, (ret.length() - CHAR.length()));
		}
		return getSign(ret, privateKey);
	}
	
	/**
	 * 获取签名
	 * @param o
	 * @return
	 * @throws Exception
	 */
	public static String getSign(Object o, String privateKey) throws Exception{
		String ret = getSort(o);
		if(ret.endsWith(CHAR)){
			ret = ret.substring(0, (ret.length() - CHAR.length()));
		}
		return getSign(ret, privateKey);
	}
	
	/**
	 * 验证签名
	 * @param map 封装所有的key与value键值对的map
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static boolean checkSignURLencode(Map<String, Object> map, String privateKey)throws Exception{
		//不包含签名代表的key,返回false
		if(!map.containsKey(SIGN)) return false;
		//获取签名数据
		Object obj = map.remove(SIGN);
		//签名数据为空,直接返回false
		if(obj == null) return false;
		String oldSign = obj.toString();
		//签名为空,返回false
		if(StringUtils.isEmpty(oldSign)) return false;
		String newSign = getSignURLencode(map, privateKey);
		return (oldSign.equals(newSign));
	}
	/**
	 * 验证签名
	 * @param map 封装所有的key与value键值对的map
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static boolean checkSign(Map<String, Object> map, String privateKey)throws Exception{
		//不包含签名代表的key,返回false
		if(!map.containsKey(SIGN)) return false;
		//获取签名数据
		Object obj = map.remove(SIGN);
		//签名数据为空,直接返回false
		if(obj == null) return false;
		String oldSign = obj.toString();
		//签名为空,返回false
		if(StringUtils.isEmpty(oldSign)) return false;
		String newSign = getSign(map, privateKey);
		return (oldSign.equals(newSign));
	}

	/**
	 * 获取签名
	 * @param content:排序好的待签名字符串
	 * @param privateKey:私钥
	 * @return
	 */
	public static String getSign(String content, String privateKey) throws Exception{
		PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
		KeyFactory keyf = KeyFactory.getInstance(ALGORITHM);
		PrivateKey priKey = keyf.generatePrivate(priPKCS8);
		java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
		signature.initSign(priKey);
		signature.update(content.getBytes(DEFAULT_CHARSET));
		byte[] signed = signature.sign();
		return Base64.encode(signed);
	}
}
