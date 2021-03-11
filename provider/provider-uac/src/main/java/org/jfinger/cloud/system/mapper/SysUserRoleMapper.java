package org.jfinger.cloud.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jfinger.cloud.entity.data.SysUserRole;

import java.util.List;

/**
 * <p>
 * 用户角色表 Mapper 接口
 * </p>
 *
 * @Author finger
 * @since 2021-2-20
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    @Select("select role_code from sys_role where id in (select role_id from sys_user_role where user_id = (select id from sys_user where user_name=#{userName}))")
    List<String> getRoleByUserName(@Param("userName") String userName);

    @Select("select id from sys_role where id in (select role_id from sys_user_role where user_id = (select id from sys_user where user_name=#{userName}))")
    List<String> getRoleIdByUserName(@Param("userName") String userName);

}
