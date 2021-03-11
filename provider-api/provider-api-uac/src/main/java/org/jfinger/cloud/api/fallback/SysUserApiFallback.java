package org.jfinger.cloud.api.fallback;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jfinger.cloud.api.SysUserApi;
import org.jfinger.cloud.entity.Result;
import org.jfinger.cloud.entity.data.SysPermission;
import org.jfinger.cloud.entity.vo.SysUserCacheInfo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @author scott
 * @date 2020/05/22
 */
@Slf4j
@Component
public class SysUserApiFallback implements SysUserApi {
    @Setter
    private Throwable cause;


    @Override
    public Result<Set<String>> getUserRolesSet(String username) {
        log.error("获取用户角色集合 {}", username, cause);
        return null;
    }

    @Override
    public Result<Set<String>> getUserPermissionsSet(String username) {
        log.error("获取用户权限集合 {}", username, cause);
        return null;
    }

    @Override
    public List<SysPermission> queryComponentPermission(String component) {
        return null;
    }

    @Override
    public List<SysPermission> queryRequestPermission(String method, String path) {
        return null;
    }

    @Override
    public SysUserCacheInfo getCacheUser(String username) {
        return null;
    }
}
