package org.jeecg.modules.api.factory;

import feign.hystrix.FallbackFactory;
import org.jeecg.modules.api.SysBaseRemoteApi;
import org.jeecg.modules.api.SysDepartRemoteApi;
import org.jeecg.modules.api.fallback.SysBaseRemoteApiFallback;
import org.jeecg.modules.api.fallback.SysDepartRemoteApiFallback;
import org.springframework.stereotype.Component;

/**
 * @author taoyan
 * @date 2020/05/22
 */
@Component
public class SysDepartRemoteApiFallbackFactory implements FallbackFactory<SysDepartRemoteApi> {

    @Override
    public SysDepartRemoteApi create(Throwable throwable) {
        SysDepartRemoteApiFallback fallback = new SysDepartRemoteApiFallback();
        fallback.setCause(throwable);
        return fallback;
    }

}
