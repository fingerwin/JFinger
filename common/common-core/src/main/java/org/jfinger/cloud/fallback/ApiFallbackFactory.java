package org.jfinger.cloud.fallback;

import feign.hystrix.FallbackFactory;

/**
 * @Description fallback factory
 * @Author finger
 * @Date 2020/12/23 0023
 * @Version 1.0
 */
public class ApiFallbackFactory<T extends ApiFallback> implements FallbackFactory<T> {

    private final T fallback;

    public ApiFallbackFactory(T t) {
        this.fallback = t;
    }

    @Override
    public T create(Throwable cause) {
        fallback.setCause(cause);
        return fallback;
    }
}
