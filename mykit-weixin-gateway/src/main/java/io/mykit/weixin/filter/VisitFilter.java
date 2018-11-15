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
package io.mykit.weixin.filter;

import com.alibaba.fastjson.JSONObject;
import io.mykit.weixin.constants.code.MobileHttpCode;
import io.mykit.weixin.filter.base.BaseFilter;
import io.mykit.weixin.utils.HeaderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Component;

import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author liuyazhuang
 * @date 2018/10/11 17:20
 * @description 过滤访问请求并校验签名
 * @version 1.0.0
 */
@Component
@ServletComponentScan
@WebFilter(urlPatterns = "/*",filterName = "visitFilter")
public class VisitFilter extends BaseFilter {

    private final Logger logger = LoggerFactory.getLogger(VisitFilter.class);

    private static final String URI_NO_DATA = "/";

    @Override
    protected int doPostFilter(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return this.doGetFilter(request, response);
    }

    @Override
    protected int doGetFilter(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject jsonObject = HeaderUtils.parseHeaders(request);
        logger.info("system logs ===>>> " + jsonObject.toJSONString());
        String uri = request.getRequestURI();
        if(URI_NO_DATA.equals(uri)){
            return MobileHttpCode.HTTP_URL_EXCEPTION;
        }
        //验证签名
        boolean isValidate = this.isValidate(request);
        //验证系统签名，错误直接返回状态码
        if(!isValidate){
            return MobileHttpCode.HTTP_NORMAL;
        }
        return MobileHttpCode.HTTP_NORMAL;
    }
}
