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
package io.mykit.weixin.entity;

import com.alibaba.fastjson.JSONObject;
import io.mykit.weixin.entity.base.MonthShardingEntity;
import lombok.Data;

import java.math.BigInteger;

/**
 * @author binghe
 * @version 1.0.0
 * @description 生成的用户二维码
 */
@Data
public class WechatQrcode extends MonthShardingEntity {
    private static final long serialVersionUID = -6196356043169752175L;

    /**
     * 永久二维码
     */
    public static final String TYPE_NOEXPIRE = "type_noexpire";
    /**
     * 临时二维码
     */
    public static final String TYPE_EXPIRE = "type_expire";

    /**
     * 外部的id,其他业务或系统的关联性ID
     */
    private String foreignSystemId = "";

    /**
     * 其他系统的唯一标识
     */
    private String foreignSystem = "";

    /**
     * 用户在其他系统上的id
     */
    private String foreignId = "";

    /**
     * 用户在其他系统中的类型
     */
    private String foreignType = "";

    /**
     * 二维码中的信息
     */
    private String qrcodeInfo = "";

    /**
     * 微信返回的用于下载二维码图片的标识
     */
    private String ticket = "";
    /**
     * 生成二维码微信返回的Url
     * 二维码图片解析后的地址，开发者可根据该地址自行生成需要的二维码图片
     *
     */
    private String wechatQrcodeUrl = "";

    /**
     * 二维码访问链接
     */
    private String qrcodeUrl = "";

    /**
     * 二维码图片在服务器上存储路径
     */
    private String qrcodePath = "";

    /**
     * 如果是临时二维码，代表过期的时间点的时间戳，毫秒值
     */
    private Long expireTime = 0L;

    /**
     * 如果是临时二维码，代表过期时间点的时间，格式为yyyy-MM-dd HH:mm:ss
     */
    private String expireTimeStr = "";

    /**
     * 二维码类型，是永久二维码还是临时二维码
     * type_noexpire：永久二维码
     * type_expire: 临时二维码
     */
    private String qrcodeType = "";

    public String toJsonString(){
        return JSONObject.toJSONString(this);
    }
}
