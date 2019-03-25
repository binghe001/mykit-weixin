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
package io.mykit.weixin.params;

import lombok.Data;

/**
 * @author liuyazhuang
 * @date 2018/10/30 20:36
 * @description 微信模板参数
 * @version 1.0.0
 */
@Data
public class WechatTemplateParams extends WechatBaseParams {
    private static final long serialVersionUID = 3257996049797226649L;

    //关键字的个数，以此来判断关键字有多少个
    private Integer keywordCount = 1;
    private String first = "";
    private String keyword1 = "";
    private String keyword2 = "";
    private String keyword3 = "";
    private String keyword4 = "";
    private String keyword5 = "";
    private String keyword6 = "";
    private String keyword7 = "";
    private String keyword8 = "";
    private String keyword9 = "";
    private String keyword10 = "";
    private String remark = "";
    private String templatetType = "";
    //模板跳转链接
    private String url = "";
}
