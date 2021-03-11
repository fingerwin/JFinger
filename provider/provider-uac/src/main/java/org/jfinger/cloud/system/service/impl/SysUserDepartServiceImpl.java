package org.jfinger.cloud.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jfinger.cloud.entity.data.SysDepart;
import org.jfinger.cloud.entity.data.SysUser;
import org.jfinger.cloud.entity.data.SysUserDepart;
import org.jfinger.cloud.entity.model.DepartIdModel;
import org.jfinger.cloud.system.mapper.SysUserDepartMapper;
import org.jfinger.cloud.system.service.ISysDepartService;
import org.jfinger.cloud.system.service.ISysUserDepartService;
import org.jfinger.cloud.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户部门表实现类
 * <p/>
 *
 * @Author ZhiLin
 * @since 2019-02-22
 */
@Service
public class SysUserDepartServiceImpl extends ServiceImpl<SysUserDepartMapper, SysUserDepart> implements ISysUserDepartService {
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private ISysUserService sysUserService;


    /**
     * 根据用户id查询部门信息
     */
    @Override
    public List<DepartIdModel> queryDepartIdsOfUser(Integer userId) {
        LambdaQueryWrapper<SysUserDepart> queryUDep = new LambdaQueryWrapper<SysUserDepart>();
        LambdaQueryWrapper<SysDepart> queryDep = new LambdaQueryWrapper<SysDepart>();
        try {
            queryUDep.eq(SysUserDepart::getUserId, userId);
            List<Integer> deptIdList = new ArrayList<>();
            List<DepartIdModel> depIdModelList = new ArrayList<>();
            List<SysUserDepart> userDepList = this.list(queryUDep);
            if (userDepList != null && userDepList.size() > 0) {
                for (SysUserDepart userDepart : userDepList) {
                    deptIdList.add(userDepart.getDeptId());
                }
                queryDep.in(SysDepart::getId, deptIdList);
                List<SysDepart> depList = sysDepartService.list(queryDep);
                if (depList != null || depList.size() > 0) {
                    for (SysDepart depart : depList) {
                        depIdModelList.add(new DepartIdModel().convertByUserDepart(depart));
                    }
                }
                return depIdModelList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据部门id查询用户信息
     */
    @Override
    public List<SysUser> queryUserByDeptId(Integer deptId) {
        LambdaQueryWrapper<SysUserDepart> queryUDep = new LambdaQueryWrapper<SysUserDepart>();
        queryUDep.eq(SysUserDepart::getDeptId, deptId);
        List<Integer> userIdList = new ArrayList<>();
        List<SysUserDepart> uDepList = this.list(queryUDep);
        if (uDepList != null && uDepList.size() > 0) {
            for (SysUserDepart uDep : uDepList) {
                userIdList.add(uDep.getUserId());
            }
            List<SysUser> userList = (List<SysUser>) sysUserService.listByIds(userIdList);
            for (SysUser sysUser : userList) {
                sysUser.setSalt("");
                sysUser.setPassword("");
            }
            return userList;
        }
        return new ArrayList<SysUser>();
    }

    /**
     * 根据部门code，查询当前部门和下级部门的 用户信息
     */
    @Override
    public List<SysUser> queryUserByDeptCode(String deptCode, String realName) {
        LambdaQueryWrapper<SysDepart> queryByDepCode = new LambdaQueryWrapper<SysDepart>();
        queryByDepCode.likeRight(SysDepart::getOrgCode, deptCode);
        List<SysDepart> sysDepartList = sysDepartService.list(queryByDepCode);
        List<Integer> depIds = sysDepartList.stream().map(SysDepart::getId).collect(Collectors.toList());

        LambdaQueryWrapper<SysUserDepart> queryUDep = new LambdaQueryWrapper<SysUserDepart>();
        queryUDep.in(SysUserDepart::getDeptId, depIds);
        List<Integer> userIdList = new ArrayList<>();
        List<SysUserDepart> uDepList = this.list(queryUDep);
        if (uDepList != null && uDepList.size() > 0) {
            for (SysUserDepart uDep : uDepList) {
                userIdList.add(uDep.getUserId());
            }
            LambdaQueryWrapper<SysUser> queryUser = new LambdaQueryWrapper<SysUser>();
            queryUser.in(SysUser::getId, userIdList);
            if (!StringUtils.isEmpty(realName)) {
                queryUser.like(SysUser::getRealName, realName.trim());
            }
            List<SysUser> userList = sysUserService.list(queryUser);
            for (SysUser sysUser : userList) {
                sysUser.setSalt("");
                sysUser.setPassword("");
            }
            return userList;
        }
        return new ArrayList<SysUser>();
    }
}
