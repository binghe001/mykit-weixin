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
import io.mykit.wechat.utils.common.UUIDUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author liuyazhuang
 * @date 2018/10/8 11:14
 * @description 基础实体类
 * @version 1.0.0
 */
@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = -6336634671510846782L;

    public static final int STATUS_NORMAL = 1;
    public static final int STATUS_DELETE = 0;

    /**
     * 主键
     */
    private String id;

    /**
     * 创建时间，序列化为字符串，格式为yyyy-MM-dd HH:mm:ss
     */
    private String createTime;


    /**
     * 最后更新时间
     */
    private String lastModifyTime;

    /**
     * 创建日期，yyyy-MM-dd
     */
    private String createDate;

    /**
     * 状态
     * 1：正常
     * 0：删除
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark = "";

    public BaseEntity(){
        this.id = UUIDUtils.getUUID();
        Date date = new Date();
        this.createTime = DateUtils.parseDateToString(date, DateUtils.DATE_TIME_FORMAT);
        this.lastModifyTime = DateUtils.parseDateToString(date, DateUtils.DATE_TIME_FORMAT);
        this.createDate = DateUtils.parseDateToString(date, DateUtils.DATE_FORMAT);
        this.status = STATUS_NORMAL;
    }

    public BaseEntity(Date date){
        this.id = UUIDUtils.getUUID();
        this.createTime = DateUtils.parseDateToString(date, DateUtils.DATE_TIME_FORMAT);
        this.lastModifyTime = DateUtils.parseDateToString(date, DateUtils.DATE_TIME_FORMAT);
        this.createDate = DateUtils.parseDateToString(date, DateUtils.DATE_FORMAT);
        this.status = STATUS_NORMAL;
    }


}
