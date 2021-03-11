package org.jfinger.cloud.system.mapper;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jfinger.cloud.data.SysRole;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @Author scott
 * @since 2018-12-19
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 删除角色
     * @param roleId
     * @return
     */
    public boolean deleteRole(Integer roleId);

    /**
     * 批量删除角色
     * @param roleIds
     * @return
     */
    public boolean deleteBatchRole(String[] roleIds);

}
