/**
 * Copyright 2020-9999 the original author or authors.
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
package io.mykit.weixin.config;

/**
 * @author binghe
 * @version 1.0.0
 * @description 加载文件的基础类
 */
public class BaseProp {

    /**
     * 微信扫码关注获取医生信息url
     */
    public static final String WEIXIN_QRCODE_SUBSCRIBE_URL = "weixin.qrcode.subscribe.url";
    /**
     * 微信扫码关注后加载的页面
     */
    public static final String WEIXIN_QRCODE_LOAD_PAGE = "weixin.qrcode.load.page";
    /**
     * 是否向业务系统获取数据
     */
    public static final String WEXIN_QRCODE_GET_DATA = "wexin.qrcode.get.data";
}
