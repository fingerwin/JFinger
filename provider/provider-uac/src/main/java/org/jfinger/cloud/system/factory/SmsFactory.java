package org.jfinger.cloud.system.factory;

import lombok.extern.slf4j.Slf4j;
import org.jfinger.cloud.system.service.ISmsService;
import org.jfinger.cloud.system.service.impl.JlbSmsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @Description 短信工厂
 * @Author finger
 * @Date 2021/3/11 0011
 * @Version 1.0
 */
@Slf4j
@Configuration
public class SmsFactory {

    @Value("${jfinger.sms.provider}")
    private String provider;

    @Value("${jfinger.sms.accessKeyId}")
    private String key;

    @Value("${jfinger.sms.accessKeySecret}")
    private String secret;


    public ISmsService getProvider() throws RuntimeException {
        if (StringUtils.isEmpty(provider))
            throw new RuntimeException("未配置短信服务商");
        switch (provider.toLowerCase()) {
            case "jlb":
                if (StringUtils.isEmpty(key))
                    throw new RuntimeException("未配置短信SiKey");
                return new JlbSmsService(key);
            default:
                throw new RuntimeException("未实现的短信服务商接口");
        }
    }

}
