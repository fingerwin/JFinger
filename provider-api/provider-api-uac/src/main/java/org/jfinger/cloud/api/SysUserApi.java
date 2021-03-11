package org.jfinger.cloud.api;

import org.jfinger.cloud.api.factory.SysUserApiFallbackFactory;
import org.jfinger.cloud.constant.ServiceConstant;
import org.jfinger.cloud.entity.Result;
import org.jfinger.cloud.entity.data.SysPermission;
import org.jfinger.cloud.entity.vo.SysUserCacheInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@Component
@FeignClient(contextId = "sysUserRemoteApi", value = ServiceConstant.PROVIDER_UAC, fallbackFactory = SysUserApiFallbackFactory.class)
public interface SysUserApi {
    /**
     * 通过用户名获取用户角色集合
     *
     * @param username 用户名
     * @return 角色集合
     */
    @GetMapping("/sys/user/rolesSet/{username}")
    Result<Set<String>> getUserRolesSet(@PathVariable("username") String username);

    /**
     * 通过用户名获取用户权限集合
     *
     * @param username 用户名
     * @return 权限集合
     */
    @GetMapping("/sys/user/permissionsSet/{username}")
    Result<Set<String>> getUserPermissionsSet(@PathVariable("username") String username);

    /**
     * 通过component查询菜单配置信息
     *
     * @param component
     * @return
     */
    @GetMapping("/sys/user/queryComponentPermission")
    List<SysPermission> queryComponentPermission(@RequestParam("component") String component);

    /**
     * 通过请求地址查找菜单信息
     *
     * @param method
     * @param path
     * @return
     */
    @GetMapping("/sys/user/queryRequestPermission")
    List<SysPermission> queryRequestPermission(@RequestParam("method") String method, @RequestParam("path") String path);

    /**
     * 根据用户名获取用户信息
     *
     * @param username
     * @return
     */
    @GetMapping("/sys/user/getCacheUser")
    SysUserCacheInfo getCacheUser(@RequestParam("username") String username);

}
