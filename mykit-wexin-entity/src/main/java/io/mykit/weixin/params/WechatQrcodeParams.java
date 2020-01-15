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
package io.mykit.weixin.params;

import io.mykit.weixin.entity.WechatQrcode;
import lombok.Data;

/**
 * @author binghe
 * @version 1.0.0
 * @description 二维码参数
 */
@Data
public class WechatQrcodeParams extends WechatBaseParams {
    private static final long serialVersionUID = -9203715927236765481L;
    /**
     * 二维码类型，是永久二维码还是临时二维码
     * type_noexpire：永久二维码
     * type_expire: 临时二维码
     */
    private String qrcodeType = WechatQrcode.TYPE_NOEXPIRE;

    /**
     * 临时二维码有效时长, 以秒为单位
     */
    private Integer expireSeconds = 30 * 24 * 60 * 60;

    /**
     * 是否显示
     * type_show：显示
     * type_notshow：不显示
     */
    private String showType = "";

    /**
     * id
     */
    private String id = "";

    /**
     * 状态
     */
    private Integer status = 1;

}
