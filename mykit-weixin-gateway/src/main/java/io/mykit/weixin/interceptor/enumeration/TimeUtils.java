package io.mykit.weixin.interceptor.enumeration;

/**
 * 时间操作
 * @author liuyazhuang
 *
 */
public class TimeUtils {
	
	public static final int SECONDS = 10;
	
	/**
	 * 时间单位枚举
	 * @author liuyazhuang
	 *
	 */
	public enum TimeUnit{
		YEAR, MONTH, WEEK, DAY, HOURS, MINUTES, SECONDS
	}
	
	/**
	 * 将对应的Value值转化为毫秒
	 * @param value
	 * @param timeUnit
	 * @return
	 */
	public static int getTime(int value, TimeUnit timeUnit){
		if(value == 0) return value;
		switch (timeUnit) {
		case YEAR:
			return value * 365 * 24 * 60 * 60;
		case MONTH:
			return value * 30 * 24 * 60 * 60;
		case WEEK:
			return value * 7 * 24 * 60 * 60;
		case DAY:
			return value * 24 * 60 * 60;
		case HOURS:
			return value * 60 * 60;
		case MINUTES:
			return value * 60;
		case SECONDS:
			return value;
		default:
			return value;
		}
	}
	
	/**
	 * 获取从现在开始的时间
	 * @param value
	 * @param timeUnit
	 * @return
	 */
	public static int getTimeFromCurrent(int value, TimeUnit timeUnit){
		return getTime(value, timeUnit);
	}
	
}
