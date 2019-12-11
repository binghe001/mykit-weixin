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
package io.mykit.weixin.constants.wechat;

/**
 * @author liuyazhuang
 * @date 2018/10/29 18:23
 * @description 微信常量信息
 * @version 1.0.0
 */
public class WechatConstants {

    /**
     * null字符串
     */
    public static final String NULL_STRING = "null";

    /**
     * 微信配置的Token
     */
    public static final String TOKEN = "medcare";

    /**
     * 关注事件
     */
    public static final String WEHCAT_SUBSCRIBE = "subscribe";
    /**
     * 取消关注事件
     */
    public static final String WEHCAT_UNSUBSCRIBE = "unsubscribe";

    /**
     * 微信上配置的加解密消息key
     */
    public static final String ENCODING_AES_KEY = "4972756F3AAD02FA69C0F2D27BF3A0DC02FA69C756F";


    /**
     * 微信授权获取code state参数分隔标识
     */
    public static final String WECHAT_OAUTH2_STATE_SPLIT = "AAABBBCCCDDDEEE";

    /**
     * 微信信息
     */
    public static final String WECHAT_INFO = "wechat_info";
    /**
     * 微信openid
     */
    public static final String WECHAT_OPENID = "wechat_openid";

    /**
     * 缓存时间
     */
    public static final int WECHAT_CACHE_TIME = 1800;

    /**
     * 发送消息
     */
    public static final String SEND_YES = "send_yes";


    /**
     * 不发送消息
     */
    public static final String SEND_NO = "send_no";

    /**
     * 重试的结果
     */
    public static final String RETRY_TRUE = "retry_true";
    /**
     * 不是重试的结果
     */
    public static final String RETRY_FALSE = "retry_false";

    /**
     * 微信状态码
     */
    public static final String WEHCAT_ERROR_CODE = "errcode";

    /**
     * 微信正常状态码
     */
    public static final int WECHAT_CODE_NORMAL = 0;

    /**
     * 默认最大重试20次
     */
    public static final int MAX_RETRY_COUNT = 20;
    /**
     * 默认的初始化重试次数0
     */
    public static final int CURRENT_RETRY_INIT_COUNT = 0;

    /**
     * 默认获取昨天和今天的数据
     */
    public static final int BEFORE_DAY = 1;

}
