package org.jfinger.cloud.api;

import org.jfinger.cloud.api.factory.SysCommonApiFallbackFactory;
import org.jfinger.cloud.constant.ServiceConstant;
import org.jfinger.cloud.entity.Result;
import org.jfinger.cloud.entity.SysLog;
import org.jfinger.cloud.entity.uac.LoginUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

/**
 * @Description 系统通用功能API
 * @Author finger
 * @Date 2020/12/23 0023
 * @Version 1.0
 */
@Component
@FeignClient(contextId = "sysBaseRemoteApi", value = ServiceConstant.PROVIDER_UAC, fallbackFactory = SysCommonApiFallbackFactory.class)
public interface SysCommonApi {

    /**
     * 保存日志
     *
     * @param syslog
     */
    @PostMapping("/sys/log")
    void saveSysLog(@RequestBody SysLog syslog);

    /**
     * 通过用户名查询登录用户信息
     * @param userName
     * @return
     */
    @GetMapping("/sys/user/{userName}")
    Result<LoginUser> getUserByName(@PathVariable("userName") String userName);

    /**
     * 通过编码和存储的value查询字典text、
     *
     * @return
     */
    @GetMapping("/sys/dict")
    String queryDictTextByKey(@RequestParam("code") String code, @RequestParam("key") String key);

    /**
     * 通过编码和存储的value查询表字典的text
     *
     * @param table 表名
     * @param text  表字段
     * @param code  表字段
     * @param key   表字段code的值
     * @return
     */
    @GetMapping("/sys/dict/table")
    String queryTableDictTextByKey(@RequestParam("table") String table, @RequestParam("text") String text, @RequestParam("code") String code, @RequestParam("key") String key);

}
