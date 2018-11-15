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
package io.mykit.weixin.task;

import io.mykit.weixin.service.WechatUserInfoService;

/**
 * @author liuyazhuang
 * @date 2018/10/30 16:07
 * @description
 * @version 1.0.0
 */
public class WechatUserInfoTask implements Runnable {
    //微信code
    private String code;
    //其他机构在业务系统中的id
    private String foreignSystemId;
    //业务系统改唯一标识
    private String foreignSystem;
    //用户在其他系统的id
    private String foreignId;
    //用户在其他系统的类型
    private String foreignType;
    //微信用户信息service句柄
    private WechatUserInfoService wechatUserInfoService;

    public WechatUserInfoTask(String code, String foreignSystemId, String foreignSystem, String foreignId, String foreignType, WechatUserInfoService wechatUserInfoService) {
        this.code = code;
        this.foreignSystemId = foreignSystemId;
        this.foreignSystem = foreignSystem;
        this.foreignId = foreignId;
        this.foreignType = foreignType;
        this.wechatUserInfoService = wechatUserInfoService;
    }

    @Override
    public void run() {
        try {
            this.wechatUserInfoService.saveWechatUserInfo(code, foreignSystemId, foreignSystem, foreignId, foreignType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
