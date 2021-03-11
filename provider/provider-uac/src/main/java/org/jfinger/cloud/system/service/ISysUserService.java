package org.jfinger.cloud.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jfinger.cloud.entity.data.SysUser;
import org.jfinger.cloud.entity.Result;
import org.jfinger.cloud.entity.vo.LoginUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 重置密码
     *
     * @param userName
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     * @return
     */
    public Result<?> resetPassword(String userName, String oldPassword, String newPassword, String confirmPassword);

    /**
     * 修改密码
     *
     * @param sysUser
     * @return
     */
    public Result<?> modifyPassword(SysUser sysUser);

    /**
     * 删除用户
     *
     * @param userId
     * @return
     */
    public boolean deleteUser(Integer userId);

    /**
     * 软删除用户
     *
     * @param userId
     * @return
     */
    public boolean softDeleteUser(Integer userId);

    /**
     * 批量删除用户
     *
     * @param userIds
     * @return
     */
    public boolean deleteBatchUsers(String userIds);

    public SysUser getUserByName(String userName);

    /**
     * 获取用户的登录信息实体
     *
     * @param userName
     * @return
     */
    public LoginUser getLoginUserByName(String userName);

    /**
     * 添加用户和用户角色关系
     *
     * @param user
     * @param roles
     */
    public void addUserWithRole(SysUser user, String roles);


    /**
     * 修改用户和用户角色关系
     *
     * @param user
     * @param roles
     */
    public void editUserWithRole(SysUser user, String roles);

    /**
     * 获取用户的授权角色
     *
     * @param userName
     * @return
     */
    public List<String> getRole(String userName);

    /**
     * 根据部门Id查询
     *
     * @param
     * @return
     */
    public IPage<SysUser> getUsersByDeptId(Page<SysUser> page, String departId, String userName);

    /**
     * 根据 userIds查询，查询用户所属部门的名称（多个部门名逗号隔开）
     *
     * @param
     * @return
     */
    public Map<Integer, String> getDeptNamesByUserIds(List<Integer> userIds);

    /**
     * 根据部门 Id 和 QueryWrapper 查询
     *
     * @param page
     * @param departId
     * @param queryWrapper
     * @return
     */
    public IPage<SysUser> getUserByDeptIdAndQueryWrapper(Page<SysUser> page, String departId, QueryWrapper<SysUser> queryWrapper);

    /**
     * 根据角色Id查询
     *
     * @param
     * @return
     */
    public IPage<SysUser> getUserByRoleId(Page<SysUser> page, String roleId, String userName);

    /**
     * 通过用户名获取用户角色集合
     *
     * @param userName 用户名
     * @return 角色集合
     */
    Set<String> getUserRolesSet(String userName);

    /**
     * 通过用户名获取用户权限集合
     *
     * @param userName 用户名
     * @return 权限集合
     */
    Set<String> getUserPermissionsSet(String userName);

    /**
     * 根据用户名设置部门ID
     *
     * @param userName
     * @param orgCode
     */
    void updateUserDepart(String userName, String orgCode);

    /**
     * 根据手机号获取用户名和密码
     */
    public SysUser getUserByPhone(String phone);


    /**
     * 根据邮箱获取用户
     */
    public SysUser getUserByEmail(String email);


    /**
     * 添加用户和用户部门关系
     *
     * @param user
     * @param selectedParts
     */
    void addUserWithDepart(SysUser user, String selectedParts);

    /**
     * 编辑用户和用户部门关系
     *
     * @param user
     * @param departs
     */
    void editUserWithDepart(SysUser user, String departs);

    /**
     * 校验用户是否有效
     *
     * @param sysUser
     * @return
     */
    Result checkUserIsEffective(SysUser sysUser);

    /**
     * 查询被逻辑删除的用户
     */
    List<SysUser> queryLogicDeleted();

    /**
     * 查询被逻辑删除的用户（可拼装查询条件）
     */
    List<SysUser> queryLogicDeleted(LambdaQueryWrapper<SysUser> wrapper);

    /**
     * 还原被逻辑删除的用户
     */
    boolean revertLogicDeleted(List<Integer> userIds, SysUser updateEntity);

    /**
     * 彻底删除被逻辑删除的用户
     */
    boolean removeLogicDeleted(List<Integer> userIds);

    /**
     * 更新手机号、邮箱空字符串为 null
     */
    @Transactional(rollbackFor = Exception.class)
    boolean updateNullPhoneEmail();

    /**
     * 根据部门Ids查询
     *
     * @param
     * @return
     */
    List<SysUser> queryByDeptIds(List<Integer> departIds, String userName);
}
