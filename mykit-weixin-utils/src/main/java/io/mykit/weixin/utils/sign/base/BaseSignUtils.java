package io.mykit.weixin.utils.sign.base;

import io.mykit.wechat.utils.common.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;


/**
 * 最基本的签名类
 * @author liuyazhuang
 *
 */
public class BaseSignUtils {
	
	/**
	 * 获取排序的代签名字符串
	 * @param o
	 * @return
	 * @throws IllegalAccessException
	 */
	public static String getSort(Object o) throws IllegalAccessException{
		
		ArrayList<String> list = new ArrayList<String>();
        Class<?> cls = o.getClass();
        Field[] fields = cls.getDeclaredFields();
        
        for (Field f : fields) {
            f.setAccessible(true);
            if (!StringUtils.isEmpty(f.get(o))) {
                list.add(f.getName() + "=" + f.get(o) + "&");
            }
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        return sb.toString();
	}
	
	/**
	 * 获取排序的代签名字符串
	 * @param map
	 * @return
	 * @throws IllegalAccessException
	 */
	public static String getSort(Map<String,Object> map){
		ArrayList<String> list = new ArrayList<String>();
        for(Map.Entry<String,Object> entry:map.entrySet()){
            if(!StringUtils.isEmpty(entry.getValue())){
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        return sb.toString();
	}
}
