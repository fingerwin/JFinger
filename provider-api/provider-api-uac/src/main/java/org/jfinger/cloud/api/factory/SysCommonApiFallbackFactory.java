package org.jfinger.cloud.api.factory;

import feign.hystrix.FallbackFactory;
import org.jfinger.cloud.api.SysCommonApi;
import org.jfinger.cloud.api.fallback.SysCommonApiFallback;
import org.springframework.stereotype.Component;

/**
 * @Description 系统通用功能fallback工厂
 * @Author finger
 * @Date 2021/2/2 0002
 * @Version 1.0
 */
@Component
public class SysCommonApiFallbackFactory implements FallbackFactory<SysCommonApi> {

    @Override
    public SysCommonApi create(Throwable throwable) {
        SysCommonApiFallback fallback = new SysCommonApiFallback();
        fallback.setCause(throwable);
        return fallback;
    }
}
