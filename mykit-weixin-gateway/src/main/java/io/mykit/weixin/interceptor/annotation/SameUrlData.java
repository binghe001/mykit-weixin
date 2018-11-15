package io.mykit.weixin.interceptor.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import io.mykit.weixin.interceptor.enumeration.TimeUtils.TimeUnit;


/**
 * 自定义注解防止表单重复提交
 * @author liuyazhuang
 *
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SameUrlData {
	
	//缓存时间的数值，最小单位为秒
	int value() default 0;
	
	//时间单位
	TimeUnit timeUnit() default TimeUnit.SECONDS;
}