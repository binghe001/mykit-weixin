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
package io.mykit.weixin.filter.base;

import com.alibaba.fastjson.JSONObject;
import io.mykit.wechat.utils.common.StringUtils;
import io.mykit.weixin.constants.code.MobileHttpCode;
import io.mykit.weixin.utils.resp.helper.ResponseHelper;
import io.mykit.weixin.utils.sign.sl.Sign;
import io.mykit.weixin.utils.sign.sl.SignUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author liuyazhuang
 * @date 2018/10/11 17:12
 * @description 基本的过滤器 所有的Web过滤器都要继承这个过滤器
 * @version 1.0.0
 */
public abstract class BaseFilter implements Filter {

    /**
     * 验证签名是否正确
     * @param request
     * @return
     * @throws IOException
     */
    protected boolean isValidate(HttpServletRequest request) throws IOException{
        String company  = request.getParameter("company");
        String nonce_str  = request.getParameter("nonce_str");
        String timeStamp  = request.getParameter("timeStamp");
        String sign  = request.getParameter("sign");
        if(StringUtils.isEmpty(company) || StringUtils.isEmpty(nonce_str)
                || StringUtils.isEmpty(timeStamp) || StringUtils.isEmpty(sign)){
            return false;
        }
        Sign s = new Sign(company, nonce_str, timeStamp, sign);
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(s));
        return SignUtils.checkIsSignValidFromResponseObj(jsonObject);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        try {
            int code = doPostFilter(req, resp);
            if(code != MobileHttpCode.HTTP_NORMAL){
                doFailed(resp,req, code);
                return;
            }
        } catch (Exception e) {
            this.doException(resp, req);
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

    /**
     * 失败的操作
     * @param resp
     * @throws IOException
     */
    protected void doFailed(HttpServletResponse resp, HttpServletRequest request, int httpCode) throws IOException {
        ResponseHelper.responseMessage(null, false, false, httpCode, resp);
    }

    /**
     * 异常的操作
     * @param resp
     * @throws IOException
     */
    protected void doException(HttpServletResponse resp,HttpServletRequest request) {
        ResponseHelper.responseMessage(null, false, false, MobileHttpCode.HTTP_SERVER_EXCEPTION, resp);
    }

    /**
     * 过滤POST请求数据
     * @param request
     * @param response
     * @return
     */
    protected abstract int doPostFilter(HttpServletRequest request,HttpServletResponse response) throws IOException;

    /**
     * 过滤GET请求数据
     * @param request
     * @param response
     * @return
     */
    protected abstract int doGetFilter(HttpServletRequest request,HttpServletResponse response) throws IOException;
}
