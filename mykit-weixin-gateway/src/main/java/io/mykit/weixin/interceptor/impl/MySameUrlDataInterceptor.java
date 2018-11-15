/**
 * Copyright 2018-2118 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.mykit.weixin.interceptor.impl;

import io.mykit.wechat.cache.redis.builder.RedisClusterBuilder;
import io.mykit.wechat.utils.common.MD5Hash;
import io.mykit.wechat.utils.common.StringUtils;
import io.mykit.weixin.constants.code.MobileHttpCode;
import io.mykit.weixin.interceptor.SameUrlDataInterceptor;
import io.mykit.weixin.interceptor.annotation.SameUrlData;
import io.mykit.weixin.interceptor.enumeration.TimeUtils;
import io.mykit.weixin.utils.HeaderUtils;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * @author liuyazhuang
 * @date 2018/10/8 19:16
 * @description 具体拦截规则实现类
 * @version 1.0.0
 */
public class MySameUrlDataInterceptor extends SameUrlDataInterceptor {

    private JedisCluster jedisCluster;

    public MySameUrlDataInterceptor(){
        this.jedisCluster = RedisClusterBuilder.getInstance();
    }

    /**
     *  默认1分钟之内不可重复提交, 以秒为单位
     */
    private static final int EXPRIE_TIME =  60;


    /**
     * 验证同一个url数据是否相同提交  ,相同返回true，不相同返回false,
     * @param request
     * @return
     */
    @Override
    public Integer repeatDataValidator(HttpServletRequest request, HttpServletResponse response, SameUrlData annotation){
        //获取相关参数
        String params = request.getParameter("parameter");
        //参数为空，可能参数没有注入到parameter中，获取整个body的数据
        if(StringUtils.isEmpty(params)){
            params = getBodyData(request);
        }
        //参数为空，不算重复提交
        if(StringUtils.isEmpty(params)){
            return MobileHttpCode.HTTP_NORMAL;
        }
        //只包含特殊字符，不可提交
        if(isOnlySpeChar(params)){
            return MobileHttpCode.SYSTEM_HANDLER_SPECIAL_CHARACTER;
        }
        //包含数据库操作语句，不可提交
        if(constantsDDLStr(params)){
            return MobileHttpCode.SYSTEM_HANDLER_SPECIAL_CHARACTER;
        }
        //获取当前请求的url
        String url = request.getRequestURL().toString();
        //获取当前附加的header
        String headers = HeaderUtils.parseRepeatHeaders(request).toString();
        //对(请求头+参数+当前请求的url)的结果计算MD5值
        String md5Result = MD5Hash.md5Java(headers.concat(params).concat(url));
        //缓存中存在md5Result的key信息，则为重复提交，返回true
        if(jedisCluster.exists(md5Result)){
            return MobileHttpCode.SYSTEM_HANDLER_FREQUENTLY;
        }
        int value = EXPRIE_TIME;
        int times = annotation.value();
        if(times > 0){
            value = TimeUtils.getTimeFromCurrent(times, annotation.timeUnit());
        }
        //不是重复提交，则将信息保存在Memcached中
        System.out.println("重新设置缓存");
        jedisCluster.setex(md5Result, value, md5Result);
        return MobileHttpCode.HTTP_NORMAL;
    }

    //获取请求体中的字符串(POST)
    private String getBodyData(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        Enumeration<String> parameterNames = request.getParameterNames();
        if(parameterNames != null){
            while (parameterNames.hasMoreElements()) {
                String name = parameterNames.nextElement();
                sb.append(name.concat(request.getParameter(name)));
            }
        }
        return sb.toString();
    }

    /**
     * 是否包含数据库DDL语句
     * @param str
     * @return
     */
    private boolean constantsDDLStr(String str){
        //转化为小写
        str = str.toLowerCase();
        str = str.replaceAll("\\s*", "");
        return str.contains("createdatabase")
                || str.contains("createtable")
                || str.contains("deletefrom")
                || str.contains("altertable")
                || str.contains("alterdatabase")
                || str.contains("droptable")
                || str.contains("dropdatabase")
                || (str.contains("update") && str.contains("set"))
                || (str.contains("select") && str.contains("from"))
                || str.contains("insertinto");

    }


    /**
     * 是否只包含特殊字符
     * @param str
     * @return
     */
    private boolean isOnlySpeChar(String str){
        String regEx = "[`~!@#$%^&*()+=|{}':;'\\[\\]<>/?~！@#￥%……&*——+|{}【】‘；：”“’。，、？]+";
        str = str.replaceAll(regEx, "");
        return StringUtils.isEmpty(str.trim());
    }
    public static void main(String[] args) {
        MySameUrlDataInterceptor interceptor = new MySameUrlDataInterceptor();
        System.out.println(interceptor.isOnlySpeChar("@@@@&*&*((*&*&&^$%$#$#$#$%&^&**(&&(*@!$%&*()%#@!"));
        System.out.println(interceptor.constantsDDLStr("insert 		into "));
    }
}
