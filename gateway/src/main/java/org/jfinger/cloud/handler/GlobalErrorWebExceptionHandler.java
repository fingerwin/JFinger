package org.jfinger.cloud.handler;

import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jfinger.cloud.entity.Result;
import org.jfinger.cloud.enumerate.HttpStatus;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Description 通用错误处理
 * @Author finger
 * @Date 2021/2/1 0001
 * @Version 1.0
 */
@Slf4j
@Order(-1)
@RequiredArgsConstructor
@Component
public class GlobalErrorWebExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        Result result = Result.fail(HttpStatus.RSP_BAD_GATEWAY);

        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        // header set
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        if (ex instanceof ResponseStatusException) {
            response.setStatusCode(((ResponseStatusException) ex).getStatus());
            HttpStatus status = HttpStatus.resolve(response.getStatusCode().value());
            if (status != null) {
                result = Result.fail(status);
            }
        }
        final byte[] data = JSON.toJSONString(result).getBytes();
        return response
                .writeWith(Mono.fromSupplier(() -> {
                    DataBufferFactory bufferFactory = response.bufferFactory();
                    return bufferFactory.wrap(data);
                }));
    }
}
