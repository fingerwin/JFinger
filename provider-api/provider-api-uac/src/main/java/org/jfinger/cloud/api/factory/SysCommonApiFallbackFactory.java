package org.jfinger.cloud.api.factory;

import org.jfinger.cloud.api.fallback.SysCommonApiFallback;
import org.jfinger.cloud.fallback.ApiFallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @Description 系统通用API Fallback Factory
 * @Author finger
 * @Date 2020/12/24 0024
 * @Version 1.0
 */
@Component
public class SysCommonApiFallbackFactory extends ApiFallbackFactory<SysCommonApiFallback> {
    public SysCommonApiFallbackFactory(SysCommonApiFallback sysCommonApiFallback) {
        super(sysCommonApiFallback);
    }
}
