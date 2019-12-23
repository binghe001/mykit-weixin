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
package io.mykit.weixin.controller;

import com.alibaba.fastjson.JSONObject;
import io.mykit.wechat.utils.common.StringUtils;
import io.mykit.weixin.constants.code.MobileHttpCode;
import io.mykit.weixin.params.WechatOpenIdParams;
import io.mykit.weixin.service.WechatUserSubscribeService;
import io.mykit.weixin.utils.resp.helper.ResponseHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author binghe
 * @version 1.0.0
 * @description 微信业务接口
 */
@Controller
@RequestMapping(value = "/wechat/business")
public class WechatBuinessController {

    @Resource
    private WechatUserSubscribeService wechatUserSubscribeService;
    /**
     * 根据openId获取数据
     */
    @RequestMapping(value = "/qrcode/subscribe/info", method = RequestMethod.POST)
    public void qrcodeSubscribeInfo(String parameter, HttpServletRequest request, HttpServletResponse response){
        if(StringUtils.isEmpty(parameter)){
            ResponseHelper.responseMessage(null, false, true, MobileHttpCode.HTTP_PARAMETER_INVALID, response);
            return;
        }
        JSONObject jsonObject = JSONObject.parseObject(parameter);
        if(jsonObject == null || jsonObject.isEmpty()){
            ResponseHelper.responseMessage(null, false, true, MobileHttpCode.HTTP_PARAMETER_INVALID, response);
            return;
        }
        WechatOpenIdParams params = jsonObject.toJavaObject(WechatOpenIdParams.class);
        String info = wechatUserSubscribeService.getQrcodeSubscribeInfo(params.getOpenId());
        ResponseHelper.responseMessage(info, false, true, MobileHttpCode.HTTP_NORMAL, response);
    }
}
