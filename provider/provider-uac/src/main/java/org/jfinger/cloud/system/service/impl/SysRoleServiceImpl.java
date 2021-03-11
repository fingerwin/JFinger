package org.jfinger.cloud.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jfinger.cloud.entity.data.SysRole;
import org.jfinger.cloud.system.mapper.SysRoleMapper;
import org.jfinger.cloud.system.mapper.SysUserMapper;
import org.jfinger.cloud.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @Author finger
 * @since 2021-2-20
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
    @Autowired
    SysRoleMapper sysRoleMapper;
    @Autowired
    SysUserMapper sysUserMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRole(Integer roleId) {
        //1.删除角色和用户关系
        sysRoleMapper.deleteRoleUserRelation(roleId);
        //2.删除角色和权限关系
        sysRoleMapper.deleteRolePermissionRelation(roleId);
        //3.删除角色
        this.removeById(roleId);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatchRole(Integer[] roleIds) {
        //1.删除角色和用户关系
        sysUserMapper.deleteBathRoleUserRelation(roleIds);
        //2.删除角色和权限关系
        sysUserMapper.deleteBathRolePermissionRelation(roleIds);
        //3.删除角色
        this.removeByIds(Arrays.asList(roleIds));
        return true;
    }
}
