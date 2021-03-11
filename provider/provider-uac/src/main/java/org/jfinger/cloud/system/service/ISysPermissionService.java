package org.jfinger.cloud.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jfinger.cloud.entity.data.SysPermission;
import org.jfinger.cloud.entity.model.TreeModel;
import org.jfinger.cloud.exception.JFingerException;

import java.util.List;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
public interface ISysPermissionService extends IService<SysPermission> {

    public List<TreeModel> queryListByParentId(Integer parentId);

    /**
     * 真实删除
     */
    public void deletePermission(Integer id) throws JFingerException;

    /**
     * 逻辑删除
     */
    public void deletePermissionLogical(Integer id) throws JFingerException;

    public void addPermission(SysPermission sysPermission) throws JFingerException;

    public void editPermission(SysPermission sysPermission) throws JFingerException;

    public List<SysPermission> queryByUser(String username);

    /**
     * 查询出带有特殊符号的菜单地址的集合
     *
     * @return
     */
    public List<String> queryPermissionUrlWithStar();

    /**
     * 判断用户否拥有权限
     *
     * @param username
     * @param sysPermission
     * @return
     */
    public boolean hasPermission(String username, SysPermission sysPermission);

    /**
     * 根据用户和请求地址判断是否有此权限
     *
     * @param username
     * @param url
     * @return
     */
    public boolean hasPermission(String username, String url);
}
