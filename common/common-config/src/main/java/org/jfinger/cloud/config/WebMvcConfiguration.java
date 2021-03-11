package org.jfinger.cloud.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author qinfeng
 */
@Configuration
@EnableRedisHttpSession
@ConditionalOnProperty(name = "jfinger.uploadType", havingValue = "local")
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Value("${jfinger.path.upload}")
    private String upLoadPath;
    @Value("${jfinger.path.webapp}")
    private String webAppPath;
    @Value("${spring.resource.static-locations}")
    private String staticLocations;

    /**
     * 静态资源的配置 - 使得可以从磁盘中读取 Html、图片、视频、音频等
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("file:" + upLoadPath + "//", "file:" + webAppPath + "//")
                .addResourceLocations(staticLocations.split(","));
    }
}
