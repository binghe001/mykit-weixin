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

/**
 * @author liuyazhuang
 * @date 2018/10/30 19:19
 * @description 发送模板消息记录
 * @version 1.0.0
 */
@Data
public class WechatTemplateMsgLog extends MonthShardingEntity {

    private static final long serialVersionUID = -2265868155567230548L;


    /**
     * 微信开发者账号表id
     */
    private String accountId;

    /**
     * 接收模板消息的微信用户openId
     */
    private String openId;

    /**
     * 微信模板id
     */
    private String templateId;

    /**
     * 类型，唯一标识
     */
    private String type;

    /**
     * 微信上配置的模板消息id
     */
    private String wechatTemplateId;

    /**
     * 模板标题
     */
    private String title;

    /**
     * 模板内容
     */
    private String content;

    /**
     * 其他业务系统传递的所有参数
     */
    private String parameter;

    /**
     * 结果
     */
    private String result;
}
