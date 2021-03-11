package org.jfinger.cloud.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jfinger.cloud.entity.data.SysRole;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @Author finger
 * @since 2021-2-20
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 删除角色与用户关系
     *
     * @param roleId 角色Id
     */
    @Delete("delete from sys_user_role where role_id = #{roleId}")
    void deleteRoleUserRelation(@Param("roleId") Integer roleId);


    /**
     * 删除角色与权限关系
     *
     * @param roleId 角色Id
     */
    @Delete("delete from sys_role_permission where role_id = #{roleId}")
    void deleteRolePermissionRelation(@Param("roleId") Integer roleId);

}
