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

import io.mykit.wechat.utils.common.StringUtils;
import io.mykit.wechat.utils.json.JsonUtils;
import io.mykit.weixin.constants.wechat.WechatConstants;
import io.mykit.weixin.entity.WechatAccount;
import io.mykit.weixin.mapper.WechatAccountMapper;
import io.mykit.weixin.service.WechatAccountService;
import io.mykit.weixin.service.impl.base.WechatCacheServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liuyazhuang
 * @date 2018/10/8 13:36
 * @description 微信账户信息service实现类
 * @version 1.0.0
 */
@Service
public class WechatAccountServiceImpl extends WechatCacheServiceImpl implements WechatAccountService {
    private final Logger logger = LoggerFactory.getLogger(WechatAccountServiceImpl.class);
    @Resource
    private WechatAccountMapper wechatAccountMapper;

    @Override
    public List<WechatAccount> getAllWechatAccount(Integer... status) {
        return wechatAccountMapper.getAllWechatAccount(status);
    }

    @Override
    public WechatAccount getWechatAccountByForeignIdAndSystem(String foreignId, String foreignSystem) {
        String key = foreignId.concat(foreignSystem).concat("getWechatAccountByForeignIdAndSystem");
        WechatAccount wechatAccount = null;
        //从缓存中获取数据
        String result = getJedisCluster().get(key);
        //缓存存在
        if(!StringUtils.isEmpty(result) && !WechatConstants.NULL_STRING.equalsIgnoreCase(result)){
            //直接转化为结果返回
            wechatAccount = JsonUtils.json2Bean(result, WechatAccount.class);
        }else{
            wechatAccount =  wechatAccountMapper.getWechatAccountByForeignIdAndSystem(foreignId, foreignSystem);
            //将结果序列化后，存储到缓存当中
            getJedisCluster().setex(key, WechatConstants.WECHAT_CACHE_TIME, JsonUtils.bean2Json(wechatAccount));
        }
        return wechatAccount;
    }


    @Override
    public String getIdByForeignIdAndSystem(String foreignId, String foreignSystem) {
        String key = foreignId.concat(foreignSystem).concat("getIdByForeignIdAndSystem");
        String accountId = getJedisCluster().get(key);
        if(StringUtils.isEmpty(accountId) || WechatConstants.NULL_STRING.equalsIgnoreCase(accountId)){
            accountId = wechatAccountMapper.getIdByForeignIdAndSystem(foreignId, foreignSystem);
            getJedisCluster().setex(key, WechatConstants.WECHAT_CACHE_TIME, accountId);
        }
        return accountId;
    }
}
