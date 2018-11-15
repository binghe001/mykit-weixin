package io.mykit.weixin.utils.sign.sl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.mykit.wechat.utils.common.CollectionUtils;
import org.apache.commons.httpclient.NameValuePair;


/**
 * 将签名拼接为参数
 * @author liuyazhuang
 *
 */
public class SignParams implements Serializable {

	private static final long serialVersionUID = 386412726223610281L;
	
	/**
	 * 获取NameValuePair封装的参数
	 * @param map:除签名以外要封装的参数
	 * @return
	 */
	public static NameValuePair[] getNameValuePairs(Map<String, String> map){
		Sign sign = new Sign();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new NameValuePair("company", sign.getCompany()));
		list.add(new NameValuePair("nonce_str", sign.getNonce_str()));
		list.add(new NameValuePair("timeStamp", sign.getTimeStamp()));
		list.add(new NameValuePair("sign", sign.getSign()));
		if(!CollectionUtils.isEmpty(map)){
			for(Map.Entry<String, String> entry : map.entrySet()){
				list.add(new NameValuePair(entry.getKey(), entry.getValue()));
			}
		}
		return list.toArray(new NameValuePair[list.size()]);
	}
	
	/**
	 * 只获取带签名的信息
	 * @return
	 */
	public static NameValuePair[] getNameValuePairs(){
		return getNameValuePairs(null);
	}
}
