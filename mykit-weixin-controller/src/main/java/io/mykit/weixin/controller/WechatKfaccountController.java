/**
 * Copyright 2019-2999 the original author or authors.
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
import io.mykit.weixin.constants.wechat.WechatConstants;
import io.mykit.weixin.params.WechatKfaccountTextMsgParams;
import io.mykit.weixin.service.WechatKfaccountTextMsgFailedService;
import io.mykit.weixin.service.WechatKfaccountTextMsgLogService;
import io.mykit.weixin.utils.exception.MyException;
import io.mykit.weixin.utils.resp.helper.ResponseHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2019/5/8
 * @description 微信客服相关的控制类
 */
@Controller
@RequestMapping(value = "/wechat/kfaccount")
public class WechatKfaccountController {
    @Resource
    private WechatKfaccountTextMsgLogService wechatKfaccountMsgLogService;
    @Resource
    private WechatKfaccountTextMsgFailedService wechatKfaccountTextMsgFailedService;

    /**
     * 发送微信客服文本消息
     * @param parameter
     * @param request
     * @param response
     */
    @RequestMapping(value = "/msg/text/send", method = RequestMethod.POST)
    public void sendWechatKfaccountTextMsg(String parameter, HttpServletRequest request, HttpServletResponse response){
        try{
            if(StringUtils.isEmpty(parameter)){
                ResponseHelper.responseMessage(null, false, true, MobileHttpCode.HTTP_PARAMETER_INVALID, response);
                return;
            }
            WechatKfaccountTextMsgParams params = JsonUtils.json2Bean(parameter, WechatKfaccountTextMsgParams.class);
            wechatKfaccountMsgLogService.sendWechatKfaccountTextMsg(params);
            ResponseHelper.responseMessage(null, false, true, MobileHttpCode.HTTP_NORMAL, response);
        }catch (MyException e){
            ResponseHelper.responseMessage(null, false, true, e.getCode(), response);
            //保存失败记录
            wechatKfaccountTextMsgFailedService.saveWechatKfaccountTextMsgFailed(parameter, e.getCode(), e.getMessage(), WechatConstants.MAX_RETRY_COUNT, WechatConstants.CURRENT_RETRY_INIT_COUNT);
        }catch (Exception e){
            e.printStackTrace();
            ResponseHelper.responseMessage(null, false, true, MobileHttpCode.HTTP_SERVER_EXCEPTION, response);
            //保存失败记录
            wechatKfaccountTextMsgFailedService.saveWechatKfaccountTextMsgFailed(parameter, MobileHttpCode.HTTP_SERVER_EXCEPTION, e.getMessage(), WechatConstants.MAX_RETRY_COUNT, WechatConstants.CURRENT_RETRY_INIT_COUNT);
        }
    }
}
