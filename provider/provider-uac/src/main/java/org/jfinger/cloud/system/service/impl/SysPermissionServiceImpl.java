package org.jfinger.cloud.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jfinger.cloud.constant.CommonConstant;
import org.jfinger.cloud.entity.data.SysPermission;
import org.jfinger.cloud.entity.model.TreeModel;
import org.jfinger.cloud.enumerate.MenuType;
import org.jfinger.cloud.exception.JFingerException;
import org.jfinger.cloud.system.mapper.SysPermissionMapper;
import org.jfinger.cloud.system.mapper.SysRolePermissionMapper;
import org.jfinger.cloud.system.service.ISysPermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

    @Resource
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Override
    public List<TreeModel> queryListByParentId(Integer parentId) {
        return baseMapper.queryListByParentId(parentId);
    }

    /**
     * 真实删除
     */
    @Override
    @Transactional
    public void deletePermission(Integer id) throws JFingerException {
        SysPermission sysPermission = this.getById(id);
        if (sysPermission == null) {
            throw new JFingerException("未找到菜单信息");
        }
        Integer pid = sysPermission.getParentId();
        if (pid != null) {
            int count = this.count(new QueryWrapper<SysPermission>().lambda().eq(SysPermission::getParentId, pid));
            if (count == 1) {
                //若父节点无其他子节点，则该父节点是叶子节点
                baseMapper.setMenuLeaf(pid, 1);
            }
        }
        baseMapper.deleteById(id);
        // 该节点可能是子节点但也可能是其它节点的父节点,所以需要级联删除
        removeChildrenBy(sysPermission.getId());
        //关联删除
        Map map = new HashMap<>();
        map.put("permission_id", id);
        //删除角色授权表
        sysRolePermissionMapper.deleteByMap(map);
    }

    /**
     * 根据父id删除其关联的子节点数据
     *
     * @return
     */
    public void removeChildrenBy(Integer parentId) {
        LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<>();
        // 封装查询条件parentId为主键,
        query.eq(SysPermission::getParentId, parentId);
        // 查出该主键下的所有子级
        List<SysPermission> permissionList = this.list(query);
        if (permissionList != null && permissionList.size() > 0) {
            Integer id = null; // id
            int num = 0; // 查出的子级数量
            // 如果查出的集合不为空, 则先删除所有
            this.remove(query);
            // 再遍历刚才查出的集合, 根据每个对象,查找其是否仍有子级
            for (int i = 0, len = permissionList.size(); i < len; i++) {
                id = permissionList.get(i).getId();
                Map map = new HashMap<>();
                map.put("permission_id", id);
                //删除角色授权表
                sysRolePermissionMapper.deleteByMap(map);
                num = this.count(new LambdaQueryWrapper<SysPermission>().eq(SysPermission::getParentId, id));
                // 如果有, 则递归
                if (num > 0) {
                    this.removeChildrenBy(id);
                }
            }
        }
    }

    /**
     * 逻辑删除
     */
    @Override
    public void deletePermissionLogical(Integer id) throws JFingerException {
        SysPermission sysPermission = getById(id);
        if (sysPermission == null) {
            throw new JFingerException("未找到菜单信息");
        }
        Integer pid = sysPermission.getParentId();
        int count = this.count(new QueryWrapper<SysPermission>().lambda().eq(SysPermission::getParentId, pid));
        if (count == 1) {
            //若父节点无其他子节点，则该父节点是叶子节点
            baseMapper.setMenuLeaf(pid, 1);
        }
        sysPermission.setStatus(CommonConstant.STATUS_DELETE);
        this.updateById(sysPermission);
    }

    @Override
    public void addPermission(SysPermission sysPermission) throws JFingerException {
        //----------------------------------------------------------------------
        //判断是否是一级菜单，是的话清空父菜单
        if (MenuType.PARENT.getCode() == sysPermission.getMenuType()) {
            sysPermission.setParentId(null);
        }
        //----------------------------------------------------------------------
        Integer pid = sysPermission.getParentId();
        if (pid != null) {
            //设置父节点不为叶子节点
            baseMapper.setMenuLeaf(pid, 0);
        }
        sysPermission.setCreateTime(new Date());
        sysPermission.setStatus(CommonConstant.STATUS_DELETE);
        sysPermission.setLeaf(true);
        this.save(sysPermission);
    }

    @Override
    public void editPermission(SysPermission sysPermission) throws JFingerException {
        SysPermission p = this.getById(sysPermission.getId());
        //TODO 该节点判断是否还有子节点
        if (p == null) {
            throw new JFingerException("未找到菜单信息");
        } else {
            sysPermission.setUpdateTime(new Date());
            //----------------------------------------------------------------------
            //Step1.判断是否是一级菜单，是的话清空父菜单ID
            if (MenuType.PARENT.getCode() == sysPermission.getMenuType()) {
                sysPermission.setParentId(null);
            }
            //Step2.判断菜单下级是否有菜单，无则设置为叶子节点
            int count = this.count(new QueryWrapper<SysPermission>().lambda().eq(SysPermission::getParentId, sysPermission.getId()));
            if (count == 0) {
                sysPermission.setLeaf(true);
            }
            //----------------------------------------------------------------------
            this.updateById(sysPermission);

            //如果当前菜单的父菜单变了，则需要修改新父菜单和老父菜单的，叶子节点状态
            Integer pid = sysPermission.getParentId();
            if ((pid != null) && !pid.equals(p.getParentId()) || (pid != null) && (p.getParentId() != null)) {
                //a.设置新的父菜单不为叶子节点
                baseMapper.setMenuLeaf(pid, 0);
                //b.判断老的菜单下是否还有其他子菜单，没有的话则设置为叶子节点
                int cc = this.count(new QueryWrapper<SysPermission>().lambda().eq(SysPermission::getParentId, p.getParentId()));
                if (cc == 0) {
                    if (p.getParentId() != null) {
                        baseMapper.setMenuLeaf(p.getParentId(), 1);
                    }
                }

            }
        }

    }

    @Override
    public List<SysPermission> queryByUser(String username) {
        return baseMapper.queryByUser(username);
    }

    /**
     * 获取模糊匹配规则的数据权限URL
     */
    @Override
    public List<String> queryPermissionUrlWithStar() {
        return this.baseMapper.queryPermissionUrlWithStar();
    }

    @Override
    public boolean hasPermission(String username, SysPermission sysPermission) {
        int count = baseMapper.queryCountByUsername(username, sysPermission);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean hasPermission(String username, String url) {
        SysPermission sysPermission = new SysPermission();
        sysPermission.setUrl(url);
        int count = baseMapper.queryCountByUsername(username, sysPermission);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }
}
