package io.mykit.weixin.utils.sign.sl;

import com.alibaba.fastjson.JSONObject;
import io.mykit.wechat.utils.common.JsonUtils;
import io.mykit.wechat.utils.common.MD5Hash;
import io.mykit.wechat.utils.common.StringUtils;
import io.mykit.weixin.utils.sign.base.BaseSignUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


/**
 * 系统签名工具类
 * @author liuyazhuang
 *
 */
public class SignUtils extends BaseSignUtils {

    private static final Logger logger = LoggerFactory.getLogger(SignUtils.class);
	//签名的key
	public static final String KEY = "432A7532943C39C1EBE335F10EB2E103";
    /**
     * 签名算法
     * @param o 要参与签名的数据对象
     * @return 签名
     * @throws IllegalAccessException
     */
    public static String getSign(Object o) throws IllegalAccessException {
        String result = getSort(o);
        if(result.endsWith("&")){
        	result = result.substring(0, result.lastIndexOf("&"));
        }
        result += "key=" + KEY;
        for(int i = 0; i < 4; i++){
        	result = MD5Hash.md5Java(result).toUpperCase();
        }
        return result;
    }

    /**
     * 获取签名
     * @param map
     * @return
     */
    public static String getSign(Map<String,Object> map){
        String result = getSort(map);
        if(result.endsWith("&")){
        	result = result.substring(0, result.lastIndexOf("&"));
        }
        result += "key=" + KEY;
        //Util.log("Sign Before MD5:" + result);
        for(int i = 0; i < 4; i++){
        	result = MD5Hash.md5Java(result).toUpperCase();
        }
        //Util.log("Sign Result:" + result);
        return result;
    }
    public static void main(String[] args) {
    	String result = "company=6F79078F79D77550739EF61CD0DC2A83&nonce_str=m878q039i722t8idi06klqeg1bs862is&timeStamp=1489992496484";
    	 result += "key=" + KEY;
         //Util.log("Sign Before MD5:" + result);
         for(int i = 0; i < 4; i++){
         	result = MD5Hash.md5Java(result).toUpperCase();
         }
		System.out.println(result);
		
		JSONObject jsonObject  = new JSONObject();
		jsonObject.put("company", "6F79078F79D77550739EF61CD0DC2A83");
		jsonObject.put("nonce_str", "m878q039i722t8idi06klqeg1bs862is");
		jsonObject.put("timeStamp", "1489992496484");
		jsonObject.put("sign", "CA5BDE2F0233236D71708F8374D87596");
		System.out.println(checkIsSignValidFromResponseObj(jsonObject));
	}
    /**
     * 检验签名
     * @return
     */
    public static boolean checkIsSignValidFromResponseObj(JSONObject jsonObject){
    	Map<String, Object> map = JsonUtils.parserToMap(jsonObject);
    	String signFromAPIResponse = map.get("sign").toString();
    	if(StringUtils.isEmpty(signFromAPIResponse)){
    		logger.debug("API返回的数据签名数据不存在，有可能被第三方篡改!!!");
    		return false;
    	}
        logger.debug("服务器回包里面的签名是:" + signFromAPIResponse);
    	//清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
        map.put("sign", "");
        //将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
        String signForAPIResponse = SignUtils.getSign(map);
        //比较两个签名是不是相等
        if(!signFromAPIResponse.equals(signForAPIResponse)){
            logger.debug("API返回的数据签名验证不通过，有可能被第三方篡改!!!");
        	return false;
        }
        logger.debug("恭喜，API返回的数据签名验证通过!!!");
    	return true;
    }
}
