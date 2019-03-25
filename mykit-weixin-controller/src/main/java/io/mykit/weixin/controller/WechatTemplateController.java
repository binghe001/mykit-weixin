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
package io.mykit.weixin.controller;

import io.mykit.wechat.utils.common.StringUtils;
import io.mykit.wechat.utils.json.JsonUtils;
import io.mykit.weixin.constants.code.MobileHttpCode;
import io.mykit.weixin.params.WechatTemplateParams;
import io.mykit.weixin.service.WechatTemplateService;
import io.mykit.weixin.utils.resp.helper.ResponseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liuyazhuang
 * @date 2018/10/30 21:08
 * @description 微信模板消息
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "/wechat/template")
public class WechatTemplateController {
    private final Logger logger = LoggerFactory.getLogger(WechatTemplateController.class);
    @Resource
    private WechatTemplateService wechatTemplateService;
    /**
     * 发送模板消息
     * @param parameter
     * @param request
     * @param response
     */
    @RequestMapping(value = "/msg/send", method = RequestMethod.POST)
    public void sendMsg(String parameter, HttpServletRequest request, HttpServletResponse response){
        try{
            logger.info(parameter);
            if(StringUtils.isEmpty(parameter)){
                ResponseHelper.responseMessage(null, false, true, MobileHttpCode.HTTP_PARAMETER_INVALID, response);
                return;
            }
            WechatTemplateParams params = JsonUtils.json2Bean(parameter, WechatTemplateParams.class);
            int code = wechatTemplateService.sendWechatTemplateMessage(params);
            ResponseHelper.responseMessage(null, false, true, code, response);
        }catch (Exception e){
            e.printStackTrace();
            ResponseHelper.responseMessage(null, false, true, MobileHttpCode.HTTP_SERVER_EXCEPTION, response);
        }
    }

    /**
     * 发送模板消息
     * @param parameter
     * @param request
     * @param response
     */
    @RequestMapping(value = "/msg/send/v2", method = RequestMethod.POST)
    public void sendMsgV2(String parameter, HttpServletRequest request, HttpServletResponse response){
        try{
            if(StringUtils.isEmpty(parameter)){
                ResponseHelper.responseMessage(null, false, true, MobileHttpCode.HTTP_PARAMETER_INVALID, response);
                return;
            }
            WechatTemplateParams params = JsonUtils.json2Bean(parameter, WechatTemplateParams.class);
            int code = wechatTemplateService.sendWechatTemplateMessageV2(params);
            ResponseHelper.responseMessage(null, false, true, code, response);
        }catch (Exception e){
            e.printStackTrace();
            ResponseHelper.responseMessage(null, false, true, MobileHttpCode.HTTP_SERVER_EXCEPTION, response);
        }
    }
}
