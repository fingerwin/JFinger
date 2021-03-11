package org.jfinger.cloud.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.codec.Encoder;
import feign.form.FormEncoder;
import org.jfinger.cloud.constant.CommonConstant;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Feign配置
 * 使用FeignClient进行服务间调用，传递headers信息
 */
@Configuration
public class FeignConfig implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            //添加token
            requestTemplate.header(CommonConstant.X_ACCESS_TOKEN, request.getHeader(CommonConstant.X_ACCESS_TOKEN));
        }
    }

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    //Feign表单提交配置
    @Bean
    @Primary
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    Encoder formEncoder() {
        return new FormEncoder(new SpringEncoder(this.messageConverters));
    }
}
