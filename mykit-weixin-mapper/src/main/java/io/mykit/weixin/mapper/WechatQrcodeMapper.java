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
package io.mykit.weixin.mapper;

import io.mykit.weixin.entity.WechatQrcode;
import org.apache.ibatis.annotations.Param;

/**
 * @author binghe
 * @version 1.0.0
 * @description 微信二维码
 */
public interface WechatQrcodeMapper {

    /**
     * 保存微信二维码数据
     * @param wechatQrcode 微信二维码数据
     * @return 保存的记录条数
     */
    int saveWechatQrcode(WechatQrcode wechatQrcode);


    /**
     * 获取二维码信息
     */
    WechatQrcode getWechatQrcode(@Param("foreignSystemId") String foreignSystemId,
                                 @Param("foreignSystem") String foreignSystem,
                                 @Param("foreignId") String foreignId,
                                 @Param("foreignType") String foreignType,
                                 @Param("qrcodeType") String qrcodeType,
                                 @Param("currentTime") Long currentTime,
                                 @Param("showType") String showType);


    /**
     * 更新状态
     */
    int updateStatus(@Param("status") Integer status, @Param("id") String id);

}
