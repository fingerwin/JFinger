package org.jfinger.cloud.handler;

import com.netflix.hystrix.exception.HystrixTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.jfinger.cloud.entity.Result;
import org.jfinger.cloud.enumerate.HttpStatus;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @Description fallback处理
 * @Author finger
 * @Date 2021/2/1 0001
 * @Version 1.0
 */
@RestController
@Slf4j
public class FallbackController {

    @RequestMapping(value = "/fallback")
    @ResponseStatus
    public Mono<Map<String, Object>> fallback(ServerWebExchange exchange) {
        Result<String> result = null;
        Exception exception = exchange.getAttribute(ServerWebExchangeUtils.HYSTRIX_EXECUTION_EXCEPTION_ATTR);
        ServerWebExchange delegate = ((ServerWebExchangeDecorator) exchange).getDelegate();
        log.error("接口调用失败，URL={}", delegate.getRequest().getURI(), exception);
        if (exception instanceof HystrixTimeoutException) {
            result = Result.fail(HttpStatus.RSP_SERVICE_UNAVAILABLE);
        } else if (exception != null && exception.getMessage() != null) {
            result = Result.fail(HttpStatus.RSP_SERVICE_UNAVAILABLE);
        } else {
            result = Result.fail(HttpStatus.RSP_BAD_GATEWAY);
        }
        return Mono.just((Map<String, Object>) result);
    }
}
