package org.jfinger.cloud.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.jfinger.cloud.entity.data.SysDepart;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 部门 Mapper 接口
 * <p>
 *
 * @Author: Steve
 * @Since： 2019-01-22
 */
@Mapper
public interface SysDepartMapper extends BaseMapper<SysDepart> {

    /**
     * 根据用户ID查询部门集合
     */
    public List<SysDepart> queryUserDeparts(@Param("userId") Integer userId);

    /**
     * 根据用户名查询部门
     *
     * @param username
     * @return
     */
    public List<SysDepart> queryDepartsByUserName(@Param("username") String username);

    @Select("select id from sys_depart where org_code=#{orgCode}")
    public String queryDepartIdByOrgCode(@Param("orgCode") String orgCode);

    @Select("select id,parent_id from sys_depart where id=#{departId}")
    public SysDepart getParentDepartId(@Param("departId") String departId);

    /**
     * 根据部门Id查询,当前和下级所有部门IDS
     *
     * @param departId
     * @return
     */
    List<String> getSubDepIdsByDeptId(@Param("departId") String departId);

    /**
     * 根据部门编码获取部门下所有IDS
     *
     * @param orgCodes
     * @return
     */
    List<String> getSubDeptIdsByOrgCodes(@org.apache.ibatis.annotations.Param("orgCodes") String[] orgCodes);

}
