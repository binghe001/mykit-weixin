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
package io.mykit.weixin.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.mykit.wechat.mp.beans.json.oauth.WxOAuth2AccessToken;
import io.mykit.wechat.mp.beans.json.oauth.WxOAuth2Code;
import io.mykit.wechat.mp.beans.json.oauth.WxOAuth2UserInfo;
import io.mykit.wechat.mp.http.handler.oauth.WxOAuth2Handler;
import io.mykit.wechat.utils.common.StringUtils;
import io.mykit.wechat.utils.json.JsonUtils;
import io.mykit.weixin.constants.wechat.WechatConstants;
import io.mykit.weixin.entity.WechatAccount;
import io.mykit.weixin.entity.WechatUserInfo;
import io.mykit.weixin.mapper.WechatUserInfoMapper;
import io.mykit.weixin.service.WechatAccountService;
import io.mykit.weixin.service.WechatUserInfoService;
import io.mykit.weixin.service.impl.base.WechatCacheServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author liuyazhuang
 * @date 2018/10/30 15:19
 * @description 微信用户Service实现类
 * @version 1.0.0
 */
@Service
public class WechatUserInfoServiceImpl extends WechatCacheServiceImpl implements WechatUserInfoService {
    private final Logger logger = LoggerFactory.getLogger(WechatUserInfoServiceImpl.class);
    @Resource
    private WechatUserInfoMapper wechatUserInfoMapper;
    @Resource
    private WechatAccountService wechatAccountService;

    @Override
    @Transactional
    public int saveWechatUserInfo(String code, String foreignSystemId, String foreignSystem, String foreignId, String foreignType) throws Exception {
        int count = 0;
        String openId = this.getOpenId(foreignSystemId, foreignSystem, foreignId, foreignType);
        //微信openId为空，执行保存操作
        if(StringUtils.isEmpty(openId)){
            WechatAccount wechatAccount = wechatAccountService.getWechatAccountByForeignIdAndSystem(foreignSystemId, foreignSystem);
            if(wechatAccount != null){
                //从微信获取信息
                WxOAuth2Code wxOAuth2Code = new WxOAuth2Code();
                wxOAuth2Code.setCode(code);
                String ret = WxOAuth2Handler.getOAuth2AccessToken(wechatAccount.getAppId(), wechatAccount.getAppSecret(), wxOAuth2Code);
                if(StringUtils.isEmpty(ret)){
                    ret = WxOAuth2Handler.getOAuth2AccessToken(wechatAccount.getAppId(), wechatAccount.getAppSecret(), wxOAuth2Code);
                }
                logger.debug("调用微信接口，根据code获取微信openId返回结果====>>>" + ret);
                if(!StringUtils.isEmpty(ret)){
                    WxOAuth2AccessToken wxOAuth2AccessToken = JsonUtils.json2Bean(ret, WxOAuth2AccessToken.class);
                    if (wxOAuth2AccessToken != null && !StringUtils.isEmpty(wxOAuth2AccessToken.getOpenid())){
                        WechatUserInfo wechatUserInfo = new WechatUserInfo();
                        wechatUserInfo.setForeignSystemId(foreignSystemId);
                        wechatUserInfo.setForeignSystem(foreignSystem);
                        wechatUserInfo.setSlaveUser(wechatAccount.getSlaveUser());
                        wechatUserInfo.setOpenId(wxOAuth2AccessToken.getOpenid());
                        wechatUserInfo.setForeignId(foreignId);
                        wechatUserInfo.setForeignType(foreignType);
                        wechatUserInfo.setNickname("");
                        wechatUserInfo.setSex(0);
                        wechatUserInfo.setProvince("");
                        wechatUserInfo.setCity("");
                        wechatUserInfo.setCountry("");
                        wechatUserInfo.setHeadimgurl("");
                        wechatUserInfo.setPrivilege("");
                        wechatUserInfo.setUnionid("");
                        logger.info(JsonUtils.bean2Json(wechatUserInfo));
                        count = wechatUserInfoMapper.saveWechatUserInfo(wechatUserInfo);
                    }
                }
            }
        }
        return count;
    }

    @Override
    public String getOpenId(String foreignSystemId, String foreignSystem, String foreignId, String foreignType) {
        String key = foreignSystemId.concat(foreignSystem).concat(foreignId).concat(foreignType).concat(WechatConstants.WECHAT_OPENID);
        String openId = getJedisCluster().get(key);
        if(StringUtils.isEmpty(openId) || WechatConstants.NULL_STRING.equalsIgnoreCase(openId)){
            openId = wechatUserInfoMapper.getOpenId(foreignSystemId, foreignSystem, foreignId, foreignType);
            if(!StringUtils.isEmpty(openId)){
                getJedisCluster().setex(key, WechatConstants.WECHAT_CACHE_TIME, openId);
            }
        }
        return openId;
    }

    @Override
    public WechatUserInfo getWechatUserInfo(String foreignSystemId, String foreignSystem, String foreignId, String foreignType) {
        String key = foreignSystemId.concat(foreignSystem).concat(foreignId).concat(foreignType).concat(WechatConstants.WECHAT_INFO);
        WechatUserInfo wechatUserInfo = null;
        String result = getJedisCluster().get(key);
        if(StringUtils.isEmpty(result) || WechatConstants.NULL_STRING.equalsIgnoreCase(result)){
            wechatUserInfo = wechatUserInfoMapper.getWechatUserInfo(foreignSystemId, foreignSystem, foreignId, foreignType);
            getJedisCluster().setex(key, WechatConstants.WECHAT_CACHE_TIME, JsonUtils.bean2Json(wechatUserInfo));
        }else{
            wechatUserInfo = JsonUtils.json2Bean(result, WechatUserInfo.class);
        }
        return wechatUserInfo;
    }
}
