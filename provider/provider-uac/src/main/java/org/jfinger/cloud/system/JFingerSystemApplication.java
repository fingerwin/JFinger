package org.jfinger.cloud.system;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Description 系统模块入口类
 * @Author finger
 * @Date 2021/2/2 0002
 * @Version 1.0
 */
@Slf4j
@EnableSwagger2
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"org.jfinger.cloud"})
@EnableFeignClients(basePackages = {"org.jfinger.cloud.*"})
public class JFingerSystemApplication {

    public static void main(String[] args) throws UnknownHostException {

        ConfigurableApplicationContext application = SpringApplication.run(JFingerSystemApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        log.info("\n----------------------------------------------------------\n\t" +
                "Application JFinger-Boot is running! Access URLs:\n\t" +
                "Local: \t\thttp://localhost:" + port + "/\n\t" +
                "External: \thttp://" + ip + ":" + port + "/\n\t" +
                "Swagger-UI: \t\thttp://" + ip + ":" + port + "/doc.html\n" +
                "----------------------------------------------------------");

    }
}
