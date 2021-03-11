package org.jfinger.cloud.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.jfinger.cloud.entity.data.SysUser;
import org.jfinger.cloud.entity.data.SysUserDepart;
import org.jfinger.cloud.entity.model.DepartIdModel;

import java.util.List;

/**
 * <p>
 * SysUserDpeart用户组织机构service
 * </p>
 * @Author ZhiLin
 *
 */
public interface ISysUserDepartService extends IService<SysUserDepart> {


	/**
	 * 根据指定用户id查询部门信息
	 * @param userId
	 * @return
	 */
	List<DepartIdModel> queryDepartIdsOfUser(Integer userId);


	/**
	 * 根据部门id查询用户信息
	 * @param deptId
	 * @return
	 */
	List<SysUser> queryUserByDeptId(Integer deptId);
  	/**
	 * 根据部门code，查询当前部门和下级部门的用户信息
	 */
	public List<SysUser> queryUserByDeptCode(String deptCode, String realName);
}
