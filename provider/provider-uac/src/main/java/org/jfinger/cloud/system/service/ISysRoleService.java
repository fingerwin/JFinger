package org.jfinger.cloud.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jfinger.cloud.entity.data.SysRole;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @Author finger
 * @since 2020-12-19
 */
public interface ISysRoleService extends IService<SysRole> {
    //默认角色Id
    public static final Integer DEFAULT_ROLE_ID = 1;

    /**
     * 删除角色
     *
     * @param roleId
     * @return
     */
    public boolean deleteRole(Integer roleId);

    /**
     * 批量删除角色
     *
     * @param roleIds
     * @return
     */
    public boolean deleteBatchRole(Integer[] roleIds);

}
