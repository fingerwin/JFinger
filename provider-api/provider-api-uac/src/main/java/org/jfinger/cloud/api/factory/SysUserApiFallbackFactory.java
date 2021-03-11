package org.jfinger.cloud.api.factory;

import feign.hystrix.FallbackFactory;
import org.jfinger.cloud.api.SysUserApi;
import org.jfinger.cloud.api.fallback.SysUserApiFallback;
import org.springframework.stereotype.Component;

/**
 * @author scott
 * @date 2020/05/22
 */
@Component
public class SysUserApiFallbackFactory implements FallbackFactory<SysUserApi> {

    @Override
    public SysUserApiFallback create(Throwable throwable) {
        SysUserApiFallback remoteUserServiceFallback = new SysUserApiFallback();
        remoteUserServiceFallback.setCause(throwable);
        return remoteUserServiceFallback;
    }
}
