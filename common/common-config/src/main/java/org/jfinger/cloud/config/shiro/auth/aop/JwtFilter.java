package org.jfinger.cloud.config.shiro.auth.aop;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.jfinger.cloud.constant.CommonConstant;
import org.jfinger.cloud.config.shiro.auth.JwtToken;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @Description: 鉴权登录拦截器
 * @Author: Scott
 * @Date: 2018/10/7
 **/
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {

    /**
     * 执行登录认证
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        try {
            executeLogin(request, response);
            return true;
        } catch (Exception e) {
            throw new AuthenticationException("Token失效，请重新登录", e);
        }
    }

    /**
     *
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader(CommonConstant.X_ACCESS_TOKEN);

        JwtToken jwtToken = new JwtToken(token);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(jwtToken);
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        try {
            return super.preHandle(request, response);
        } catch (AuthenticationException e) {
            log.error(e.getMessage());
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setStatus(HttpStatus.OK.value());
            JSONObject data = new JSONObject();
            data.put("code", 401);
            data.put("message", e.getMessage());
            /**获取OutputStream输出流*/
            OutputStream outputStream = response.getOutputStream();
            /**设置json返回格式*/
            ((HttpServletResponse) response).setHeader("content-type", "application/json");
            /**将字符转换成字节数组，指定以UTF-8编码进行转换*/
            byte[] dataByteArr = data.toJSONString().getBytes(StandardCharsets.UTF_8);
            /**使用OutputStream流向客户端输出字节数组*/
            outputStream.write(dataByteArr);
            return false;
        }
    }
}
