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
import io.mykit.weixin.constants.code.MobileHttpCode;
import io.mykit.weixin.entity.WechatAccount;
import io.mykit.weixin.interceptor.annotation.SameUrlData;
import io.mykit.weixin.interceptor.enumeration.TimeUtils;
import io.mykit.weixin.service.WechatAccountService;
import io.mykit.weixin.utils.resp.helper.ResponseHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.List;

/**
 * @author liuyazhuang
 * @date 2018/10/8 13:40
 * @description 微信账户Controller
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "/wechat/account")
public class WechatAccountController {

    @Resource
    private WechatAccountService wechatAccountService;

    @ResponseBody
    @RequestMapping(value = "/all")
    public List<WechatAccount> getAllWechatAccount(){
        return wechatAccountService.getAllWechatAccount(new Integer[]{1});
    }

//    @ResponseBody
//    @RequestMapping(value = "/info")
//    public WechatAccount info(){
//        return wechatAccountService.getWechatAccountByForeignIdAndSystem("4235353453543", "system_test");
//    }

    @RequestMapping(value = "/info")
    @SameUrlData(value = 5, timeUnit = TimeUtils.TimeUnit.SECONDS)
    public void list(String parameter, HttpServletRequest request, HttpServletResponse response){
        JSONObject jsonObject = JSONObject.parseObject(parameter);
        WechatAccount wechatAccount = wechatAccountService.getWechatAccountByForeignIdAndSystem(jsonObject.getString("foreignId"), jsonObject.getString("foreignSystem"));
        ResponseHelper.responseMessage(wechatAccount, false, true, MobileHttpCode.HTTP_NORMAL, response);
    }
}
