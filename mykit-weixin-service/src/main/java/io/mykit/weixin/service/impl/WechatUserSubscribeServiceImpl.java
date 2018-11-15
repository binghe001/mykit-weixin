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

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.mykit.wechat.mp.beans.xml.receive.event.WxSubscribeEventMessage;
import io.mykit.wechat.mp.beans.xml.receive.event.WxUnSubscribeEventMessage;
import io.mykit.wechat.utils.common.StringUtils;
import io.mykit.weixin.constants.wechat.WechatConstants;
import io.mykit.weixin.entity.WechatUserSubscribe;
import io.mykit.weixin.mapper.WechatUserSubscribeMapper;
import io.mykit.weixin.service.WechatUserSubscribeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liuyazhuang
 * @date 2018/10/29 15:55
 * @description 微信用户关注记录Service实现类
 * @version 1.0.0
 */
@Service
public class WechatUserSubscribeServiceImpl implements WechatUserSubscribeService {
    private final Logger logger = LoggerFactory.getLogger(WechatUserSubscribeServiceImpl.class);
    @Resource
    private WechatUserSubscribeMapper wechatUserSubscribeMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveOrUpdateWxSubscribeEventMessage(WxSubscribeEventMessage wxSubscribeEventMessage) {
        int count = 0;
        //获取唯一的一条记录
        WechatUserSubscribe wechatUserSubscribe = wechatUserSubscribeMapper.getWechatUserSubscribe(wxSubscribeEventMessage.getToUserName(), wxSubscribeEventMessage.getFromUserName());
        if(wechatUserSubscribe == null || StringUtils.isEmpty(wechatUserSubscribe.getId())){
            wechatUserSubscribe = new WechatUserSubscribe();
            wechatUserSubscribe.setEvent(wxSubscribeEventMessage.getEvent());
            wechatUserSubscribe.setMsgType(wxSubscribeEventMessage.getMsgType());
            wechatUserSubscribe.setOpenId(wxSubscribeEventMessage.getFromUserName());
            wechatUserSubscribe.setSlaveUser(wxSubscribeEventMessage.getToUserName());
            count = wechatUserSubscribeMapper.saveWechatUserSubscribe(wechatUserSubscribe);
            logger.debug("保存用户关注的记录信息参数：" + wxSubscribeEventMessage.toJsonString(wxSubscribeEventMessage) + ", 执行结果: " + count);
        }else{
            //类型不相同，处理相关逻辑，类型相同，忽略
            if (!wechatUserSubscribe.getEvent().equals(wxSubscribeEventMessage.getEvent()) && (WechatConstants.WEHCAT_SUBSCRIBE.equals(wxSubscribeEventMessage.getEvent()) || WechatConstants.WEHCAT_UNSUBSCRIBE.equals(wxSubscribeEventMessage.getEvent()))){
                count = wechatUserSubscribeMapper.updateEventStatus(wxSubscribeEventMessage.getEvent(), wxSubscribeEventMessage.getToUserName(), wxSubscribeEventMessage.getFromUserName());
                logger.debug("更新数据库event状态信息，结果为: " + count);
            }else{
                logger.debug("事件通知的event类型和数据库中的event类型相同，不做任何处理");
            }
        }
        return count;
    }

    @Override
    public int updateWxUnSubscribeEventMessage(WxUnSubscribeEventMessage wxUnSubscribeEventMessage) {
        int count = 0;
        //获取唯一的一条记录
        WechatUserSubscribe wechatUserSubscribe = wechatUserSubscribeMapper.getWechatUserSubscribe(wxUnSubscribeEventMessage.getToUserName(), wxUnSubscribeEventMessage.getFromUserName());
        //记录不为空，同时事件状态不一致，则更新事件状态
        if(wechatUserSubscribe != null && !wxUnSubscribeEventMessage.getEvent().equals(wechatUserSubscribe.getEvent()) && (WechatConstants.WEHCAT_SUBSCRIBE.equals(wxUnSubscribeEventMessage.getEvent()) || WechatConstants.WEHCAT_UNSUBSCRIBE.equals(wxUnSubscribeEventMessage.getEvent()))){
            count = wechatUserSubscribeMapper.updateEventStatus(wxUnSubscribeEventMessage.getEvent(), wxUnSubscribeEventMessage.getToUserName(), wxUnSubscribeEventMessage.getFromUserName());
        }
        return count;
    }

    @Override
    public WechatUserSubscribe getWechatUserSubscribe(String slaveUser, String openId) {
        return wechatUserSubscribeMapper.getWechatUserSubscribe(slaveUser, openId);
    }

    @Override
    public PageInfo<WechatUserSubscribe> queryPage(Integer page, Integer pageSize, Integer status) {
        PageHelper.startPage(page, pageSize);
        List<WechatUserSubscribe> list = wechatUserSubscribeMapper.getWechatUserSubscribes(status);
        return new PageInfo<WechatUserSubscribe>(list);
    }
}
