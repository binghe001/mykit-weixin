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
package io.mykit.weixin.entity;

import io.mykit.weixin.entity.base.MonthShardingEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liuyazhuang
 * @date 2018/10/31 18:00
 * @description 微信开发者账号与模板消息关联表
 * @version 1.0.0
 */
@Data
public class WechatAccountTemplateJoin extends MonthShardingEntity{
    private static final long serialVersionUID = 4066662362824967346L;

    /**
     * 数据id
     */
    private String id;
    /**
     * 微信开发者账户id
     */
    private String accountId;

    /**
     * 微信消息模板id
     */
    private String templateId;

    /**
     * 消息模板类型，与WechatTemplate中的type一致
     */
    private String templateType;

    /**
     * 微信上配置的模板消息id
     */
    private String wechatTemplateId;

}
