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

import lombok.Data;

import java.io.Serializable;

/**
 * @author liuyazhuang
 * @date 2018/10/30 19:13
 * @description 微信模板, 字典表
 * @version 1.0.0
 */
@Data
public class WechatTemplate implements Serializable {

    private static final long serialVersionUID = 1298102529899832126L;

    /**
     * 家医服务
     */
    public static final String TYPE_FD_SERVICE = "type_fd_service";
    /**
     * 消息提醒
     */
    public static final String TYPE_MSG_REMIND = "type_msg_remind";

    /**
     * 数据库id
     */
    private String id;

    /**
     * 类型，与accountId 组合唯一标识
     */
    private String type;

    /**
     * 微信上配置的模板消息id
     */
    private String wechatTemplateId;

    /**
     * 微信开发者账号表id
     */
    private String accountId;

    /**
     * 模板标题
     */
    private String title;

    /**
     * 模板内容
     */
    private String content;

    /**
     * 状态
     * 0:删除
     * 1:正常
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

}
