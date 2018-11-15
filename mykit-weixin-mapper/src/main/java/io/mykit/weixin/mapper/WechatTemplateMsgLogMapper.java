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
package io.mykit.weixin.mapper;

import io.mykit.weixin.entity.WechatTemplateMsgLog;

/**
 * @author liuyazhuang
 * @date 2018/10/30 19:47
 * @description 微信模板消息
 * @version 1.0.0
 */
public interface WechatTemplateMsgLogMapper {

    /**
     * 保存微信消息记录
     * @param wechatTemplateMsgLog 微信模板消息记录
     * @return 保存记录条数
     */
    int saveWechatTemplateMsgLog(WechatTemplateMsgLog wechatTemplateMsgLog);
}
