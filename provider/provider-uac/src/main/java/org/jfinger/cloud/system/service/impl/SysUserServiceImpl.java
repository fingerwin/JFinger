package org.jfinger.cloud.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jfinger.cloud.constant.CacheConstant;
import org.jfinger.cloud.constant.CommonConstant;
import org.jfinger.cloud.entity.Result;
import org.jfinger.cloud.entity.data.SysPermission;
import org.jfinger.cloud.entity.data.SysUser;
import org.jfinger.cloud.entity.data.SysUserDepart;
import org.jfinger.cloud.entity.data.SysUserRole;
import org.jfinger.cloud.entity.model.SysUserDeptVo;
import org.jfinger.cloud.entity.vo.LoginUser;
import org.jfinger.cloud.enumerate.LogType;
import org.jfinger.cloud.system.mapper.*;
import org.jfinger.cloud.system.service.ISysLogService;
import org.jfinger.cloud.system.service.ISysUserService;
import org.jfinger.cloud.utils.common.EncryptUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @Author: scott
 * @Date: 2018-12-20
 */
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysUserDepartMapper sysUserDepartMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private ISysLogService sysLogService;

    @Override
    @CacheEvict(value = {CacheConstant.SYS_USERS_CACHE}, allEntries = true)
    public Result<?> resetPassword(String username, String oldpassword, String newpassword, String confirmpassword) {
        SysUser user = baseMapper.getUserByName(username);
        String encode = EncryptUtils.encrypt(username, oldpassword, user.getSalt());
        if (!user.getPassword().equals(encode)) {
            return Result.fail("旧密码输入错误!");
        }
        if (StringUtils.isEmpty(newpassword)) {
            return Result.fail("新密码不允许为空!");
        }
        if (!newpassword.equals(confirmpassword)) {
            return Result.fail("两次输入密码不一致!");
        }
        String password = EncryptUtils.encrypt(username, newpassword, user.getSalt());
        baseMapper.update(new SysUser().setPassword(password), new LambdaQueryWrapper<SysUser>().eq(SysUser::getId, user.getId()));
        return Result.success("密码重置成功!");
    }

    @Override
    @CacheEvict(value = {CacheConstant.SYS_USERS_CACHE}, allEntries = true)
    public Result<?> modifyPassword(SysUser sysUser) {
        String salt = EncryptUtils.createRandom(false, 8);
        sysUser.setSalt(salt);
        String password = sysUser.getPassword();
        String passwordEncode = EncryptUtils.encrypt(sysUser.getUserName(), password, salt);
        sysUser.setPassword(passwordEncode);
        baseMapper.updateById(sysUser);
        return Result.success("密码修改成功!");
    }

    @Override
    @CacheEvict(value = {CacheConstant.SYS_USERS_CACHE}, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(Integer userId) {
        return removeById(userId);
    }

    @Override
    public boolean softDeleteUser(Integer userId) {
        SysUser sysUser = baseMapper.selectById(userId);
        sysUser.setStatus(CommonConstant.STATUS_DELETE);
        return updateById(sysUser);
    }

    @Override
    @CacheEvict(value = {CacheConstant.SYS_USERS_CACHE}, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatchUsers(String userIds) {
        //1.删除用户
        List<Integer> idList = new ArrayList<>();
        for (String s : Arrays.asList(userIds.split(","))) {
            idList.add(Integer.valueOf(s));
        }
        return removeByIds(idList);
    }

    @Override
    public SysUser getUserByName(String userName) {
        return baseMapper.getUserByName(userName);
    }

    @Override
    public LoginUser getLoginUserByName(String userName) {
        if (StringUtils.isEmpty(userName))
            return null;
        LoginUser loginUser = new LoginUser();
        SysUser sysUser = getUserByName(userName);
        if (sysUser != null) {
            BeanUtils.copyProperties(sysUser, loginUser);
            return loginUser;
        }
        return null;
    }

    @Override
    @Transactional
    public void addUserWithRole(SysUser user, String roles) {
        save(user);
        if (!StringUtils.isEmpty(roles)) {
            String[] arr = roles.split(",");
            for (String roleId : arr) {
                SysUserRole userRole = new SysUserRole(user.getId(), Integer.valueOf(roleId));
                sysUserRoleMapper.insert(userRole);
            }
        }
    }

    @Override
    @CacheEvict(value = {CacheConstant.SYS_USERS_CACHE}, allEntries = true)
    @Transactional
    public void editUserWithRole(SysUser user, String roles) {
        updateById(user);
        //先删后加
        sysUserRoleMapper.delete(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, user.getId()));
        if (!StringUtils.isEmpty(roles)) {
            String[] arr = roles.split(",");
            for (String roleId : arr) {
                SysUserRole userRole = new SysUserRole(user.getId(), Integer.valueOf(roleId));
                sysUserRoleMapper.insert(userRole);
            }
        }
    }


    @Override
    public List<String> getRole(String username) {
        return sysUserRoleMapper.getRoleByUserName(username);
    }

    /**
     * 通过用户名获取用户角色集合
     *
     * @param userName 用户名
     * @return 角色集合
     */
    @Override
    public Set<String> getUserRolesSet(String userName) {
        // 查询用户拥有的角色集合
        List<String> roles = sysUserRoleMapper.getRoleByUserName(userName);
        return new HashSet<>(roles);
    }

    /**
     * 通过用户名获取用户权限集合
     *
     * @param username 用户名
     * @return 权限集合
     */
    @Override
    public Set<String> getUserPermissionsSet(String username) {
        Set<String> permissionSet = new HashSet<>();
        List<SysPermission> permissionList = sysPermissionMapper.queryByUser(username);
        for (SysPermission po : permissionList) {
            if (!StringUtils.isEmpty(po.getPerms())) {
                permissionSet.add(po.getPerms());
            }
        }
        return permissionSet;
    }

    /***
     * 根据部门Id查询
     * @param page
     * @param departId
     * @param username
     * @return
     */
    @Override
    public IPage<SysUser> getUsersByDeptId(Page<SysUser> page, String departId, String username) {
        return baseMapper.getUsersByDeptId(page, departId, username);
    }

    @Override
    public void updateUserDepart(String userName, String orgCode) {
        baseMapper.updateUserDepart(userName, orgCode);
    }

    @Override
    public SysUser getUserByPhone(String phone) {
        return baseMapper.getUserByPhone(phone);
    }


    @Override
    public SysUser getUserByEmail(String email) {
        return baseMapper.getUserByEmail(email);
    }

    @Override
    public Map<Integer, String> getDeptNamesByUserIds(List<Integer> userIds) {
        List<SysUserDeptVo> list = baseMapper.getDeptNamesByUserIds(userIds);
        Map<Integer, String> res = new HashMap<Integer, String>();
        list.forEach(item -> {
                    if (res.get(item.getUserId()) == null) {
                        res.put(item.getUserId(), item.getDepartName());
                    } else {
                        res.put(item.getUserId(), res.get(item.getUserId()) + "," + item.getDepartName());
                    }
                }
        );
        return res;
    }

    @Override
    public IPage<SysUser> getUserByDeptIdAndQueryWrapper(Page<SysUser> page, String departId, QueryWrapper<SysUser> queryWrapper) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = queryWrapper.lambda();

        lambdaQueryWrapper.eq(SysUser::getStatus, "1");
        lambdaQueryWrapper.inSql(SysUser::getId, "SELECT user_id FROM sys_user_depart WHERE dept_id = '" + departId + "'");

        return baseMapper.selectPage(page, lambdaQueryWrapper);
    }

    @Override
    public IPage<SysUser> getUserByRoleId(Page<SysUser> page, String roleId, String userName) {
        return baseMapper.getUsersByRoleId(page, roleId, userName);
    }

    @Override
    @Transactional
    public void addUserWithDepart(SysUser user, String selectedParts) {
        if (!StringUtils.isEmpty(selectedParts)) {
            String[] arr = selectedParts.split(",");
            for (String departId : arr) {
                SysUserDepart userDepart = new SysUserDepart(user.getId(), Integer.valueOf(departId));
                sysUserDepartMapper.insert(userDepart);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {CacheConstant.SYS_USERS_CACHE}, allEntries = true)
    public void editUserWithDepart(SysUser user, String departs) {
        updateById(user);  //更新角色的时候已经更新了一次了，可以再跟新一次
        String[] arr = {};
        if (!StringUtils.isEmpty(departs)) {
            arr = departs.split(",");
        }
        //先删后加
        sysUserDepartMapper.delete(new QueryWrapper<SysUserDepart>().lambda().eq(SysUserDepart::getUserId, user.getId()));
        if (!StringUtils.isEmpty(departs)) {
            for (String departId : arr) {
                SysUserDepart userDepart = new SysUserDepart(user.getId(), Integer.valueOf(departId));
                sysUserDepartMapper.insert(userDepart);
            }
        }
    }

    @Override
    public Result checkUserIsEffective(SysUser sysUser) {
        Result<?> result = Result.success();
        //情况1：根据用户信息查询，该用户不存在
        if (sysUser == null) {
            sysLogService.addLog("用户登录失败，用户不存在！", LogType.LOGIN, null);
            return Result.fail("该用户不存在，请注册");
        }
        //情况2：根据用户信息查询，该用户已注销
        if (CommonConstant.STATUS_DELETE.equals(sysUser.getStatus())) {
            sysLogService.addLog("用户登录失败，用户名:" + sysUser.getUserName() + "已注销！", LogType.LOGIN, null);
            return Result.fail("该用户已注销");
        }
        //情况3：根据用户信息查询，该用户已冻结
        if (CommonConstant.STATUS_DISABLED.equals(sysUser.getStatus())) {
            sysLogService.addLog("用户登录失败，用户名:" + sysUser.getUserName() + "已冻结！", LogType.LOGIN, null);
            return Result.fail("该用户已冻结");
        }
        return result;
    }

    @Override
    public List<SysUser> queryByDeptIds(List<Integer> departIds, String userName) {
        return baseMapper.queryByDeptIds(departIds, userName);
    }

    @Override
    public boolean updateNullPhoneEmail() {
        baseMapper.updateNullByEmptyString("email");
        baseMapper.updateNullByEmptyString("phone");
        return true;
    }

    @Override
    public List<SysUser> queryLogicDeleted() {
        return queryLogicDeleted(null);
    }

    @Override
    public List<SysUser> queryLogicDeleted(LambdaQueryWrapper<SysUser> wrapper) {
        if (wrapper == null) {
            wrapper = new LambdaQueryWrapper<>();
        }
        wrapper.eq(SysUser::getStatus, CommonConstant.STATUS_DELETE);
        return baseMapper.selectLogicDeleted(wrapper);
    }

    @Override
    public boolean revertLogicDeleted(List<Integer> userIds, SysUser updateEntity) {
        return baseMapper.revertLogicDeleted(userIds, updateEntity) > 0;
    }

    @Override
    public boolean removeLogicDeleted(List<Integer> userIds) {
        // 1. 删除用户
        int line = baseMapper.deleteLogicDeleted(userIds);
        // 2. 删除用户部门关系
        line += sysUserDepartMapper.delete(new LambdaQueryWrapper<SysUserDepart>().in(SysUserDepart::getUserId, userIds));
        //3. 删除用户角色关系
        line += sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().in(SysUserRole::getUserId, userIds));
        return line != 0;
    }
}
