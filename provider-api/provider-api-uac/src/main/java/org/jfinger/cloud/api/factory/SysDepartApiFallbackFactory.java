package org.jfinger.cloud.api.factory;

import feign.hystrix.FallbackFactory;
import org.jfinger.cloud.api.SysDepartApi;
import org.jfinger.cloud.api.fallback.SysDepartApiFallback;
import org.springframework.stereotype.Component;

/**
 * @author taoyan
 * @date 2020/05/22
 */
@Component
public class SysDepartApiFallbackFactory implements FallbackFactory<SysDepartApi> {

    @Override
    public SysDepartApi create(Throwable throwable) {
        SysDepartApiFallback fallback = new SysDepartApiFallback();
        fallback.setCause(throwable);
        return fallback;
    }

}
