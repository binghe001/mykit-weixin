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
package io.mykit.weixin.params;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liuyazhuang
 * @date 2018/10/30 20:37
 * @description 基本参数
 * @version 1.0.0
 */
@Data
public class WechatBaseParams implements Serializable {
    private static final long serialVersionUID = -5240931137248273502L;
    //其他机构在业务系统中的id
    private String foreignSystemId = "";
    //其他业务系统标识
    private String foreignSystem = "";
    //微信用户在其他业务系统中的id
    private String foreignId = "";
    //微信用户在其他业务系统中的类型(可用作权限划分)
    private String foreignType = "";
}
