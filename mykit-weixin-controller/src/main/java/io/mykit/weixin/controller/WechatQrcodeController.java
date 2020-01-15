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
import io.mykit.weixin.entity.WechatQrcode;
import io.mykit.weixin.params.WechatQrcodeParams;
import io.mykit.weixin.service.WechatQrcodeServcie;
import io.mykit.weixin.utils.exception.MyException;
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
 * @author binghe
 * @version 1.0.0
 * @description 微信二维码相关的接口
 */
@Controller
@RequestMapping(value = "/wechat/qrcode")
public class WechatQrcodeController {
    private final Logger logger = LoggerFactory.getLogger(WechatQrcodeController.class);
    @Resource
    private WechatQrcodeServcie wechatQrcodeServcie;

    /**
     * 更新状态
     */
    @RequestMapping(value = "/status/update", method = RequestMethod.POST)
    public void updateStatus(String parameter, HttpServletRequest request, HttpServletResponse response){
        try{
            if(StringUtils.isEmpty(parameter)){
                ResponseHelper.responseMessage(null, false, true, MobileHttpCode.HTTP_PARAMETER_INVALID, response);
                return;
            }
            JSONObject jsonObject = JSONObject.parseObject(parameter);
            WechatQrcodeParams params = jsonObject.toJavaObject(WechatQrcodeParams.class);
            wechatQrcodeServcie.updateStatus(params);
            ResponseHelper.responseMessage(null, false, true, MobileHttpCode.HTTP_NORMAL, response);
        }catch (MyException e){
            e.printStackTrace();
            ResponseHelper.responseMessage(null, false, true, e.getCode(), response);
        }catch (Exception e){
            e.printStackTrace();
            ResponseHelper.responseMessage(null, false, true, MobileHttpCode.HTTP_SERVER_EXCEPTION, response);
        }
    }
    /**
     * 生成微信二维码
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public void create(String parameter, HttpServletRequest request, HttpServletResponse response){
        try{
            if(StringUtils.isEmpty(parameter)){
                ResponseHelper.responseMessage(null, false, true, MobileHttpCode.HTTP_PARAMETER_INVALID, response);
                return;
            }
            JSONObject jsonObject = JSONObject.parseObject(parameter);
            WechatQrcodeParams params = jsonObject.toJavaObject(WechatQrcodeParams.class);
            wechatQrcodeServcie.createAndSaveQrcode(params);
            ResponseHelper.responseMessage(null, false, true, MobileHttpCode.HTTP_NORMAL, response);
        }catch (MyException e){
            e.printStackTrace();
            ResponseHelper.responseMessage(null, false, true, e.getCode(), response);
        }catch (Exception e){
            e.printStackTrace();
            ResponseHelper.responseMessage(null, false, true, MobileHttpCode.HTTP_SERVER_EXCEPTION, response);
        }
    }
    /**
     * 查看二维码详细信息
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public void detail(String parameter, HttpServletRequest request, HttpServletResponse response){
        try{
            if(StringUtils.isEmpty(parameter)){
                logger.info("parameter参数为空");
                ResponseHelper.responseMessage(null, false, true, MobileHttpCode.HTTP_PARAMETER_INVALID, response);
                return;
            }
            JSONObject jsonObject = JSONObject.parseObject(parameter);
            WechatQrcodeParams params = jsonObject.toJavaObject(WechatQrcodeParams.class);
            WechatQrcode wechatQrcode = wechatQrcodeServcie.getWechatQrcode(params);
            ResponseHelper.responseMessage(wechatQrcode, false, true, MobileHttpCode.HTTP_NORMAL, response);
        }catch (MyException e){
            e.printStackTrace();
            logger.info("抛异常===>>>" + e.getMessage());
            logger.info("抛异常===>>>" + e.getStackTrace());
            ResponseHelper.responseMessage(null, false, true, e.getCode(), response);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("抛异常===>>>" + e.getMessage());
            logger.info("抛异常===>>>" + e.getStackTrace());
            ResponseHelper.responseMessage(null, false, true, MobileHttpCode.HTTP_SERVER_EXCEPTION, response);
        }
    }
}
