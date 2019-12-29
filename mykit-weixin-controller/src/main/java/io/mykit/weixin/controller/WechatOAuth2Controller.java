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

import com.alibaba.fastjson.JSONObject;
import io.mykit.wechat.utils.common.StringUtils;
import io.mykit.wechat.utils.json.JsonUtils;
import io.mykit.weixin.constants.code.MobileHttpCode;
import io.mykit.weixin.constants.wechat.WechatConstants;
import io.mykit.weixin.entity.WechatAccount;
import io.mykit.weixin.params.WechatOAuth2Params;
import io.mykit.weixin.service.WechatAccountService;
import io.mykit.weixin.service.WechatUserInfoService;
import io.mykit.weixin.task.WechatUserInfoTask;
import io.mykit.weixin.utils.resp.helper.ResponseHelper;
import io.mykit.weixin.utils.thread.ExecutorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

/**
 * @author liuyazhuang
 * @date 2018/10/30 10:25
 * @description 微信页面处理
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "/wechat/oauth2")
public class WechatOAuth2Controller {

    private final Logger logger = LoggerFactory.getLogger(WechatOAuth2Controller.class);

    @Resource
    private WechatAccountService wechatAccountService;

    @Resource
    private WechatUserInfoService wechatUserInfoService;
    /**
     * 用户微信授权登录, 不能用ajax调用，通过form表单提交
     * @param parameter
     * @param request
     * @param response
     */
    @Deprecated
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(String parameter, HttpServletRequest request, HttpServletResponse response) {
       try{
           JSONObject jsonObject = JSONObject.parseObject(parameter);
           if(jsonObject == null){
               ResponseHelper.responseMessage(null, false, true, MobileHttpCode.HTTP_PARAMETER_INVALID, response);
               return;
           }
           WechatOAuth2Params params = jsonObject.toJavaObject(WechatOAuth2Params.class);
           WechatAccount wechatAccount = wechatAccountService.getWechatAccountByForeignIdAndSystem(params.getForeignSystemId(), params.getForeignSystem());
           if (wechatAccount == null){
               ResponseHelper.responseMessage(null, false, true, MobileHttpCode.HTTP_SEARCH_DATA_FAILED, response);
               return;
           }
           //这里是回调的url
           String redirect_uri = URLEncoder.encode(params.getCallbackUrl(), "UTF-8");
           String state = params.getForeignId().concat(WechatConstants.WECHAT_OAUTH2_STATE_SPLIT).concat(params.getForeignType());
           String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
                   "appid=" + wechatAccount.getAppId() +
                   "&redirect_uri="+ redirect_uri +
                   "&response_type=code" +
                   "&scope=snsapi_userinfo" +
                   "&state="+state+"#wechat_redirect";
           response.sendRedirect(url);
       }catch (Exception e){
           ResponseHelper.responseMessage(null, false, true, MobileHttpCode.HTTP_SERVER_EXCEPTION, response);
            e.printStackTrace();
       }
    }
    /**
     * 用户微信授权登录
     * @param request
     * @param response
     */
    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public void form(HttpServletRequest request, HttpServletResponse response) {
       try{
           String foreignSystemId = request.getParameter("foreignSystemId");
           String foreignSystem = request.getParameter("foreignSystem");
           String callbackUrl = request.getParameter("callbackUrl");

           logger.debug(foreignSystemId + "=====>>>" + foreignSystem + "=====>>> " + callbackUrl);
           //String foreignId = request.getParameter("foreignId");
           //String foreignType = request.getParameter("foreignType");
           if(StringUtils.isEmpty(foreignSystemId)
                   || StringUtils.isEmpty(foreignSystem)
                   || StringUtils.isEmpty(callbackUrl)
                   /*|| StringUtils.isEmpty(foreignId)
                   || StringUtils.isEmpty(foreignType)*/){
               ResponseHelper.responseMessage(null, false, true, MobileHttpCode.HTTP_PARAMETER_INVALID, response);
               return;
           }
           WechatAccount wechatAccount = wechatAccountService.getWechatAccountByForeignIdAndSystem(foreignSystemId, foreignSystem);
           if (wechatAccount == null){
               ResponseHelper.responseMessage(null, false, true, MobileHttpCode.HTTP_SEARCH_DATA_FAILED, response);
               return;
           }
           //这里是回调的url
           String redirect_uri = URLEncoder.encode(callbackUrl, "UTF-8");
           String state = foreignSystemId.concat(WechatConstants.WECHAT_OAUTH2_STATE_SPLIT).concat(foreignSystem);
           //静默授权
           String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
                   "appid=" + wechatAccount.getAppId() +
                   "&redirect_uri="+ redirect_uri +
                   "&response_type=code" +
                   "&scope=snsapi_base" +
                   "&state="+state+"#wechat_redirect";
           response.sendRedirect(url);
       }catch (Exception e){
           ResponseHelper.responseMessage(null, false, true, MobileHttpCode.HTTP_SERVER_EXCEPTION, response);
            e.printStackTrace();
       }
    }

    /**
     * 根据code获取微信用户信息
     * @param parameter
     * @param request
     * @param response
     */
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public void user(String parameter, HttpServletRequest request, HttpServletResponse response){
        logger.debug(parameter);
        try{
            if(StringUtils.isEmpty(parameter)){
                ResponseHelper.responseMessage(null, false, true, MobileHttpCode.HTTP_PARAMETER_INVALID, response);
                return;
            }
            WechatOAuth2Params params = JsonUtils.json2Bean(parameter, WechatOAuth2Params.class);
            String code = params.getCode();
            String state = params.getState();
            String[] states = state.split(WechatConstants.WECHAT_OAUTH2_STATE_SPLIT);
            //String code, String foreignSystemId, String foreignSystem, String foreignId, String foreignType, WechatUserInfoService wechatUserInfoService
            WechatUserInfoTask wechatUserInfoTask = new WechatUserInfoTask(code, states[0], states[1], params.getForeignId(), params.getForeignType(), wechatUserInfoService);
            //提交到线程处理
            ExecutorUtils.executeThread(wechatUserInfoTask);
            ResponseHelper.responseMessage(null, false, true, MobileHttpCode.HTTP_NORMAL, response);
        }catch (Exception e){
            e.printStackTrace();
            ResponseHelper.responseMessage(null, false, true, MobileHttpCode.HTTP_SERVER_EXCEPTION, response);
        }
    }

    /**
     * 获取微信用户的openid
     * @param parameter
     * @param request
     * @param response
     */
    @RequestMapping(value = "/openid", method = RequestMethod.POST)
    public void openId(String parameter, HttpServletRequest request, HttpServletResponse response){
        if(StringUtils.isEmpty(parameter)){
            ResponseHelper.responseMessage(null, false, true, MobileHttpCode.HTTP_PARAMETER_INVALID, response);
            return;
        }
        WechatOAuth2Params params = JsonUtils.json2Bean(parameter, WechatOAuth2Params.class);
        String openid = wechatUserInfoService.getOpenId(params.getForeignSystemId(), params.getForeignSystem(), params.getForeignId(), params.getForeignType());
        if(StringUtils.isEmpty(openid)){
            ResponseHelper.responseMessage(null, false, true, MobileHttpCode.HTTP_SEARCH_DATA_FAILED, response);
            return;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("openid", openid);
        ResponseHelper.responseMessage(jsonObject, false, true, MobileHttpCode.HTTP_NORMAL, response);
    }

}
