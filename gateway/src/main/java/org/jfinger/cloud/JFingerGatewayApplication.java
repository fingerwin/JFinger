package org.jfinger.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Description 路由模块入口
 * @Author finger
 * @Date 2021/1/28 0028
 * @Version 1.0
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
public class JFingerGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(JFingerGatewayApplication.class, args);
    }
}
