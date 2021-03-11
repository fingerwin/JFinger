package org.jfinger.cloud.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jfinger.cloud.entity.data.SysUser;
import org.jfinger.cloud.entity.model.SysUserDeptVo;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @Author finger
 * @since 2021-02-20
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    /**
     * 通过用户账号查询用户信息
     *
     * @param userName
     * @return
     */
    public SysUser getUserByName(@Param("userName") String userName);

    /**
     * 根据部门Id查询用户信息
     *
     * @param page
     * @param departId
     * @return
     */
    IPage<SysUser> getUsersByDeptId(Page page, @Param("departId") String departId, @Param("userName") String userName);

    /**
     * 根据用户Ids,查询用户所属部门名称信息
     *
     * @param userIds
     * @return
     */
    List<SysUserDeptVo> getDeptNamesByUserIds(@Param("userIds") List<Integer> userIds);

    /**
     * 根据角色Id查询用户信息
     *
     * @param page
     * @param
     * @return
     */
    IPage<SysUser> getUsersByRoleId(Page page, @Param("roleId") String roleId, @Param("userName") String userName);

    /**
     * 根据用户名设置部门ID
     *
     * @param userName
     * @param orgCode
     */
    void updateUserDepart(@Param("userName") String userName, @Param("orgCode") String orgCode);

    /**
     * 根据手机号查询用户信息
     *
     * @param phone
     * @return
     */
    public SysUser getUserByPhone(@Param("phone") String phone);


    /**
     * 根据邮箱查询用户信息
     *
     * @param email
     * @return
     */
    public SysUser getUserByEmail(@Param("email") String email);

    /**
     * @Author scott
     * @Date 2019/12/13 16:10
     * @Description: 批量删除角色与用户关系
     */
    void deleteBathRoleUserRelation(@Param("roleIdArray") Integer[] roleIdArray);

    /**
     * @Author scott
     * @Date 2019/12/13 16:10
     * @Description: 批量删除角色与权限关系
     */
    void deleteBathRolePermissionRelation(@Param("roleIdArray") Integer[] roleIdArray);

    /**
     * 更新空字符串为null【此写法有sql注入风险，禁止随便用】
     */
    int updateNullByEmptyString(@Param("fieldName") String fieldName);

    /**
     * 根据部门Ids,查询部门下用户信息
     *
     * @param departIds
     * @return
     */
    List<SysUser> queryByDeptIds(@Param("departIds") List<Integer> departIds, @Param("userName") String userName);

    /**
     * 查询被逻辑删除的用户
     */
    List<SysUser> selectLogicDeleted(@Param(Constants.WRAPPER) Wrapper<SysUser> wrapper);

    /**
     * 还原被逻辑删除的用户
     */
    int revertLogicDeleted(@Param("userIds") List<Integer> userIds, @Param("entity") SysUser entity);

    /**
     * 彻底删除被逻辑删除的用户
     */
    int deleteLogicDeleted(@Param("userIds") List<Integer> userIds);
}
