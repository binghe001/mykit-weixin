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
package io.mykit.weixin.mapper;

import io.mykit.weixin.entity.WechatKfaccountTextMsgFailed;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author binghe
 * @version 1.0.0
 * @description 处理发送错误信息
 */
public interface WechatKfaccountTextMsgFailedMapper {

    /**
     * 保存模板消息发送失败记录
     */
    int saveWechatKfaccountTextMsgFailed(WechatKfaccountTextMsgFailed WechatKfaccountTextMsgFailed);


    /**
     * 获取一定时间内的失败记录
     */
    List<WechatKfaccountTextMsgFailed> getWechatKfaccountTextMsgFailedListByTime(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 直接删除错误记录
     */
    int deleteWechatKfaccountTextMsgFailed(@Param("id") String id);


    /**
     * 更新当前重试次数
     */
    int updateCurrentRetryCount(@Param("currentRetryCount") Integer currentRetryCount, @Param("lastModifyTime") String lastModifyTime, @Param("id") String id);
}
