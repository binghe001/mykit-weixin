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
package io.mykit.weixin.service;

import io.mykit.weixin.entity.WechatTemplate;
import io.mykit.weixin.params.WechatTemplateParams;
import io.mykit.weixin.service.base.WechatCacheService;

/**
 * @author liuyazhuang
 * @date 2018/10/30 20:15
 * @description 微信模板service
 * @version 1.0.0
 */
public interface WechatTemplateService extends WechatCacheService {

    /**
     * 根据类型获取微信模板信息
     * @param type 类型
     * @return WechatTemplate模板
     */
    WechatTemplate getWechatTemplateByType(String type, String accountId);


    /**
     * 发送模板消息
     * @param wechatTemplateParams
     * @return
     */
    int sendWechatTemplateMessage(WechatTemplateParams wechatTemplateParams) throws Exception;
}
