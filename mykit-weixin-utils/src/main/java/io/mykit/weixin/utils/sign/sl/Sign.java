package io.mykit.weixin.utils.sign.sl;

import io.mykit.wechat.utils.common.RandomUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;



/**
 * 签名实体，在构造方法中完成数据的签名操作
 * @author liuyazhuang
 *
 */
public class Sign {
	private String company = "";
	private String nonce_str = "";
	private String timeStamp = "";
	private String sign = "";
	
	/**
	 * 构造方法
	 */
	public Sign(){
		this.company = "6F79078F79D77550739EF61CD0DC2A83";
		this.nonce_str = RandomUtils.getRandomStringByLength(32);
		this.timeStamp = String.valueOf(System.currentTimeMillis());
		this.sign = SignUtils.getSign(toMap());
	}
	
	
	/**
	 * 重载构造方法
	 * @param company
	 * @param nonce_str
	 * @param timeStamp
	 * @param sign
	 */
	public Sign(String company, String nonce_str, String timeStamp, String sign) {
		this.company = company;
		this.nonce_str = nonce_str;
		this.timeStamp = timeStamp;
		this.sign = sign;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}


	public String getSign() {
		return sign;
	}


	public void setSign(String sign) {
		this.sign = sign;
	}

	//将类中的字段属性转化为对应的Map集合
	public Map<String,Object> toMap(){
        Map<String,Object> map = new HashMap<String, Object>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object obj;
            try {
                obj = field.get(this);
                if(obj!=null){
                    map.put(field.getName(), obj);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
