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
package io.mykit.weixin.entity.base;

import io.mykit.wechat.utils.common.DateUtils;
import lombok.Data;

import java.util.Date;

/**
 * @author liuyazhuang
 * @date 2018/10/8 11:19
 * @description 按照日期分片
 * @version 1.0.0
 */
@Data
public class DateShardingEntity extends BaseEntity {

    private static final long serialVersionUID = 8739873230152050580L;

    /**
     * 按日期进行时间分片，格式yyyyMMdd
     */
    private String dateSharding;

    public DateShardingEntity(Date date){
        super(date);
        this.dateSharding = DateUtils.parseDateToString(date, DateUtils.DATE_DATE);
    }

    public DateShardingEntity(){
        super();
        this.dateSharding = DateUtils.parseDateToString(new Date(), DateUtils.DATE_DATE);
    }
}
