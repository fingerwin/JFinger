package org.jfinger.cloud.system.controller;


import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jfinger.cloud.constant.CommonConstant;
import org.jfinger.cloud.entity.Result;
import org.jfinger.cloud.entity.data.*;
import org.jfinger.cloud.entity.model.DepartIdModel;
import org.jfinger.cloud.entity.model.SysDepartUsersVO;
import org.jfinger.cloud.entity.model.SysUserRoleVO;
import org.jfinger.cloud.entity.vo.LoginUser;
import org.jfinger.cloud.entity.vo.SysUserCacheInfo;
import org.jfinger.cloud.enumerate.LogType;
import org.jfinger.cloud.enumerate.OperateType;
import org.jfinger.cloud.system.service.*;
import org.jfinger.cloud.utils.cache.RedisUtils;
import org.jfinger.cloud.utils.common.EncryptUtils;
import org.jfinger.cloud.utils.mybatis.plus.WrapperUtils;
import org.jfinger.cloud.utils.system.JwtUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static org.jfinger.cloud.system.service.ISysRoleService.DEFAULT_ROLE_ID;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
@Slf4j
@RestController
@RequestMapping("/sys/user")
public class SysUserController {
    @Autowired
    private ISysLogService sysLogService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysDepartService sysDepartService;

    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Autowired
    private ISysUserDepartService sysUserDepartService;

    @Autowired
    private RedisUtils redisUtil;

    @Value("${jfinger.path.upload}")
    private String upLoadPath;

    @Autowired
    private ISysPermissionService sysPermissionService;

    @Autowired
    private ISysDictService sysDictService;

    /**
     * 获取用户列表数据
     *
     * @param user
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<IPage<SysUser>> queryPageList(SysUser user,
                                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        QueryWrapper<SysUser> queryWrapper = WrapperUtils.buildQueryWrapper(user, req.getParameterMap());
        Page<SysUser> page = new Page<SysUser>(pageNo, pageSize);
        IPage<SysUser> userList = sysUserService.page(page, queryWrapper);
        //批量查询用户的所属部门
        List<Integer> userIds = userList.getRecords().stream().map(SysUser::getId).collect(Collectors.toList());
        if (userIds != null && userIds.size() > 0) {
            Map<Integer, String> useDeptNames = sysUserService.getDeptNamesByUserIds(userIds);
            userList.getRecords().forEach(item -> {
                item.setOrgCode(useDeptNames.get(item.getId()));
            });
        }
        return Result.success(userList);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<SysUser> add(@RequestBody JSONObject json) {
        String selectedRoles = json.getString("selectedroles");
        String selectedDeparts = json.getString("selecteddeparts");
        SysUser user = JSON.parseObject(json.toJSONString(), SysUser.class);
        user.setCreateTime(new Date());//设置创建时间
        String salt = EncryptUtils.createRandom(8);
        user.setSalt(salt);
        String encryptCode = EncryptUtils.encrypt(user.getUserName(), user.getPassword(), salt);
        user.setPassword(encryptCode);
        sysUserService.addUserWithRole(user, selectedRoles);
        sysUserService.addUserWithDepart(user, selectedDeparts);
        sysLogService.addLog("添加用户： " + user.getUserName(), LogType.OPERATION, OperateType.INSERT);
        return Result.success("添加成功！");
    }

    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    public Result<SysUser> edit(@RequestBody JSONObject json) {
        SysUser sysUser = sysUserService.getById(json.getString("id"));
        if (sysUser == null) {
            return Result.fail("未找到对应用户");
        } else {
            sysLogService.addLog("编辑用户： " + sysUser.getUserName(), LogType.OPERATION, OperateType.MODIFY);
            SysUser user = JSON.parseObject(json.toJSONString(), SysUser.class);
            //密码单独修改
            user.setPassword(sysUser.getPassword());
            String roles = json.getString("selectedroles");
            String departs = json.getString("selecteddeparts");
            sysUserService.editUserWithRole(user, roles);
            sysUserService.editUserWithDepart(user, departs);
            sysUserService.updateNullPhoneEmail();
            return Result.success("修改成功!");
        }
    }

    /**
     * 删除用户
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<?> delete(@RequestParam(name = "id") Integer id) {
        SysUser sysUser = sysUserService.getById(id);
        if (sysUser == null) {
            return Result.fail("未找到对应用户");
        }
        sysLogService.addLog("编辑用户： " + sysUser.getUserName(), LogType.OPERATION, OperateType.DELETE);
        sysUserService.deleteUser(id);
        return Result.success("删除用户成功");
    }

    /**
     * 批量删除用户
     */
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        sysLogService.addLog("批量删除用户， ids： " + ids, LogType.OPERATION, OperateType.DELETE);
        sysUserService.deleteBatchUsers(ids);
        return Result.success("批量删除用户成功");
    }

    /**
     * 冻结&解冻用户
     *
     * @param json
     * @return
     */
    @RequestMapping(value = "/frozenBatch", method = RequestMethod.PUT)
    public Result<SysUser> frozenBatch(@RequestBody JSONObject json) {
        String ids = json.getString("ids");
        String status = json.getString("status");
        String[] arr = ids.split(",");
        for (String id : arr) {
            if (StringUtils.isNotEmpty(id)) {
                this.sysUserService.update(new SysUser().setStatus(Integer.parseInt(status)),
                        new UpdateWrapper<SysUser>().lambda().eq(SysUser::getId, id));
            }
        }
        return Result.success("操作成功!");
    }

    @RequestMapping(value = "/queryById", method = RequestMethod.GET)
    public Result<SysUser> queryById(@RequestParam(name = "id") Integer id) {
        SysUser sysUser = sysUserService.getById(id);
        if (sysUser == null) {
            return Result.fail("未找到对应用户");
        }
        return Result.success(sysUser);
    }

    @RequestMapping(value = "/queryUserRole", method = RequestMethod.GET)
    public Result<List<Integer>> queryUserRole(@RequestParam(name = "userId") Integer userId) {
        List<String> list = new ArrayList<String>();
        List<SysUserRole> userRole = sysUserRoleService.list(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, userId));
        if (userRole == null || userRole.size() <= 0) {
            return Result.fail("未找到用户相关角色信息");
        }
        return Result.success(userRole.stream().map(SysUserRole::getId).collect(Collectors.toList()));
    }


    /**
     * 校验用户账号是否唯一<br>
     * 根据需要校验的条件设置具体SysUser实体的属性值
     *
     * @param sysUser
     * @return
     */
    @RequestMapping(value = "/checkOnlyUser", method = RequestMethod.GET)
    public Result<Boolean> checkOnlyUser(SysUser sysUser) {
        //通过传入信息查询新的用户信息
        SysUser user = sysUserService.getOne(new QueryWrapper<SysUser>(sysUser));
        return user != null ? Result.fail("用户账号已存在") : Result.success();
    }

    /**
     * 修改密码
     */
    @RequestMapping(value = "/changePassword", method = RequestMethod.PUT)
    public Result<?> changePassword(@RequestBody SysUser sysUser) {
        SysUser user = sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserName, sysUser.getUserName()));
        if (user == null) {
            return Result.fail("用户不存在！");
        }
        sysUser.setId(user.getId());
        return sysUserService.modifyPassword(sysUser);
    }

    /**
     * 查询指定用户和部门关联的数据
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/userDepartList", method = RequestMethod.GET)
    public Result<List<DepartIdModel>> getUserDepartsList(@RequestParam(name = "userId") Integer userId) {
        Result<List<DepartIdModel>> result = new Result<>();
        List<DepartIdModel> depIdModelList = sysUserDepartService.queryDepartIdsOfUser(userId);
        if (depIdModelList != null && depIdModelList.size() > 0) {
            return Result.success("查找成功", depIdModelList);
        } else {
            return Result.fail("查找失败");
        }
    }

    /**
     * 根据部门id查询用户信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/queryUserByDepId", method = RequestMethod.GET)
    public Result<List<SysUser>> queryUserByDepId(@RequestParam(name = "id") String id, @RequestParam(name = "realName", required = false) String realName) {
        SysDepart sysDepart = sysDepartService.getById(id);
        List<SysUser> userList = sysUserDepartService.queryUserByDeptCode(sysDepart.getOrgCode(), realName);
        //批量查询用户的所属部门
        List<Integer> userIds = userList.stream().map(SysUser::getId).collect(Collectors.toList());
        if (userIds != null && userIds.size() > 0) {
            Map<Integer, String> useDepNames = sysUserService.getDeptNamesByUserIds(userIds);
            userList.forEach(item -> {
                item.setOrgCode(useDepNames.get(item.getId()));
            });
        }
        return Result.success(userList);
    }

    /**
     * @param userIds
     * @return
     * @功能：根据id 批量查询
     */
    @RequestMapping(value = "/queryByIds", method = RequestMethod.GET)
    public Result<Collection<SysUser>> queryByIds(@RequestParam String userIds) {
        return Result.success(sysUserService.listByIds(Arrays.asList(userIds.split(","))));
    }

    /**
     * 首页用户重置密码
     */
    @RequestMapping(value = "/updatePassword", method = RequestMethod.PUT)
    public Result<?> changPassword(@RequestBody JSONObject json) {
        String userName = json.getString("username");
        String oldpassword = json.getString("oldpassword");
        String password = json.getString("password");
        String confirmpassword = json.getString("confirmpassword");
        SysUser user = this.sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserName, userName));
        if (user == null) {
            return Result.fail("用户不存在！");
        }
        return sysUserService.resetPassword(userName, oldpassword, password, confirmpassword);
    }

    @RequestMapping(value = "/userRoleList", method = RequestMethod.GET)
    public Result<IPage<SysUser>> userRoleList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<SysUser>> result = new Result<IPage<SysUser>>();
        Page<SysUser> page = new Page<SysUser>(pageNo, pageSize);
        String roleId = req.getParameter("roleId");
        String username = req.getParameter("username");
        IPage<SysUser> pageList = sysUserService.getUserByRoleId(page, roleId, username);
        return Result.success(pageList);
    }

    /**
     * 给指定角色添加用户
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/addSysUserRole", method = RequestMethod.POST)
    public Result<String> addSysUserRole(@RequestBody SysUserRoleVO sysUserRoleVO) {
        Integer sysRoleId = sysUserRoleVO.getRoleId();
        for (Integer sysUserId : sysUserRoleVO.getUserIdList()) {
            SysUserRole sysUserRole = new SysUserRole(sysUserId, sysRoleId);
            QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<SysUserRole>();
            queryWrapper.eq("role_id", sysRoleId).eq("user_id", sysUserId);
            SysUserRole one = sysUserRoleService.getOne(queryWrapper);
            if (one == null) {
                sysUserRoleService.save(sysUserRole);
            }
        }
        return Result.success("添加成功!");
    }

    /**
     * 删除指定角色的用户关系
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/deleteUserRole", method = RequestMethod.DELETE)
    public Result<SysUserRole> deleteUserRole(@RequestParam(name = "roleId") Integer roleId,
                                              @RequestParam(name = "userId") Integer userId) {
        QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId).eq("user_id", userId);
        sysUserRoleService.remove(queryWrapper);
        return Result.success("删除成功!");
    }

    /**
     * 批量删除指定角色的用户关系
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/deleteUserRoleBatch", method = RequestMethod.DELETE)
    public Result<SysUserRole> deleteUserRoleBatch(
            @RequestParam(name = "roleId") String roleId,
            @RequestParam(name = "userIds") String userIds) {
        QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId).in("user_id", Arrays.asList(userIds.split(",")));
        sysUserRoleService.remove(queryWrapper);
        return Result.success("删除成功!");
    }

    /**
     * 部门用户列表
     */
    @RequestMapping(value = "/departUserList", method = RequestMethod.GET)
    public Result<IPage<SysUser>> departUserList(HttpServletRequest req,
                                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
//        Result<IPage<SysUser>> result = new Result<IPage<SysUser>>();
//        Page<SysUser> page = new Page<SysUser>(pageNo, pageSize);
//        String depId = req.getParameter("depId");
//        String username = req.getParameter("username");
//        //根据部门ID查询,当前和下级所有的部门IDS
//        List<String> subDepids = new ArrayList<>();
//        //部门id为空时，查询我的部门下所有用户
//        if (StringUtils.isEmpty(depId)) {
//            LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
//            int userIdentity = user.getUserIdentity() != null ? user.getUserIdentity() : CommonConstant.USER_IDENTITY_1;
//            if (StringUtils.isNotEmpty(userIdentity) && userIdentity == CommonConstant.USER_IDENTITY_2) {
//                subDepids = sysDepartService.getMySubDepIdsByDepId(user.getDepartIds());
//            }
//        } else {
//            subDepids = sysDepartService.getSubDepIdsByDepId(depId);
//        }
//        if (subDepids != null && subDepids.size() > 0) {
//            IPage<SysUser> pageList = sysUserService.getUserByDepIds(page, subDepids, username);
//            //批量查询用户的所属部门
//            //step.1 先拿到全部的 useids
//            //step.2 通过 useids，一次性查询用户的所属部门名字
//            List<String> userIds = pageList.getRecords().stream().map(SysUser::getId).collect(Collectors.toList());
//            if (userIds != null && userIds.size() > 0) {
//                Map<String, String> useDepNames = sysUserService.getDepNamesByUserIds(userIds);
//                pageList.getRecords().forEach(item -> {
//                    //批量查询用户的所属部门
//                    item.setOrgCode(useDepNames.get(item.getId()));
//                });
//            }
//            result.setSuccess(true);
//            result.setResult(pageList);
//        } else {
//            result.setSuccess(true);
//            result.setResult(null);
//        }
        return Result.fail("未实现");
    }


    /**
     * 根据 orgCode 查询用户，包括子部门下的用户
     * 若某个用户包含多个部门，则会显示多条记录，可自行处理成单条记录
     */
    @GetMapping("/queryByOrgCode")
    public Result<?> queryByDepartId(SysUser userParams,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     @RequestParam(name = "orgCode") String orgCode) {
//        IPage<SysUserSysDepartModel> pageList = sysUserService.queryUserByOrgCode(orgCode, userParams, new Page(pageNo, pageSize));
//        return Result.ok(pageList);
        return Result.fail("未实现");
    }

    /**
     * 根据 orgCode 查询用户，包括子部门下的用户
     * 针对通讯录模块做的接口，将多个部门的用户合并成一条记录，并转成对前端友好的格式
     */
    @GetMapping("/queryByOrgCodeForAddressList")
    public Result<?> queryByOrgCodeForAddressList(SysUser userParams,
                                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                  @RequestParam(name = "orgCode", required = false) String orgCode) {
//        IPage page = new Page(pageNo, pageSize);
//        IPage<SysUserSysDepartModel> pageList = sysUserService.queryUserByOrgCode(orgCode, userParams, page);
//        List<SysUserSysDepartModel> list = pageList.getRecords();
//
//        // 记录所有出现过的 user, key = userId
//        Map<String, JSONObject> hasUser = new HashMap<>(list.size());
//
//        JSONArray resultJson = new JSONArray(list.size());
//
//        for (SysUserSysDepartModel item : list) {
//            String userId = item.getId();
//            // userId
//            JSONObject getModel = hasUser.get(userId);
//            // 之前已存在过该用户，直接合并数据
//            if (getModel != null) {
//                String departName = getModel.get("departName").toString();
//                getModel.put("departName", (departName + " | " + item.getDepartName()));
//            } else {
//                // 将用户对象转换为json格式，并将部门信息合并到 json 中
//                JSONObject json = JSON.parseObject(JSON.toJSONString(item));
//                json.remove("id");
//                json.put("userId", userId);
//                json.put("departId", item.getDepartId());
//                json.put("departName", item.getDepartName());
////                json.put("avatar", item.getSysUser().getAvatar());
//                resultJson.add(json);
//                hasUser.put(userId, json);
//            }
//        }
//
//        IPage<JSONObject> result = new Page<>(pageNo, pageSize, pageList.getTotal());
//        result.setRecords(resultJson.toJavaList(JSONObject.class));
//        return Result.ok(result);
        return Result.fail("未实现");
    }

    /**
     * 给指定部门添加对应的用户
     */
    @RequestMapping(value = "/editSysDepartWithUser", method = RequestMethod.POST)
    public Result<String> editSysDepartWithUser(@RequestBody SysDepartUsersVO sysDepartUsersVO) {
        Integer sysDepId = sysDepartUsersVO.getDepId();
        for (Integer sysUserId : sysDepartUsersVO.getUserIdList()) {
            SysUserDepart sysUserDepart = new SysUserDepart(sysUserId, sysDepId);
            QueryWrapper<SysUserDepart> queryWrapper = new QueryWrapper<SysUserDepart>();
            queryWrapper.eq("dept_id", sysDepId).eq("user_id", sysUserId);
            SysUserDepart userDepart = sysUserDepartService.getOne(queryWrapper);
            if (userDepart == null) {
                sysUserDepartService.save(sysUserDepart);
            }
        }
        return Result.success("添加成功!");
    }

    /**
     * 删除指定机构的用户关系
     */
    @RequestMapping(value = "/deleteUserInDepart", method = RequestMethod.DELETE)
    public Result<SysUserDepart> deleteUserInDepart(@RequestParam(name = "depId") String depId,
                                                    @RequestParam(name = "userId") String userId) {
        QueryWrapper<SysUserDepart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dept_id", depId).eq("user_id", userId);
        if (sysUserDepartService.remove(queryWrapper)) {
            return Result.success("删除成功!");
        } else
            return Result.fail("当前选中部门与用户无关联关系!");
    }

    /**
     * 批量删除指定机构的用户关系
     */
    @RequestMapping(value = "/deleteUserInDepartBatch", method = RequestMethod.DELETE)
    public Result<SysUserDepart> deleteUserInDepartBatch(@RequestParam(name = "depId") String depId,
                                                         @RequestParam(name = "userIds") String userIds) {
        QueryWrapper<SysUserDepart> queryWrapper = new QueryWrapper<SysUserDepart>();
        queryWrapper.eq("dept_id", depId).in("user_id", Arrays.asList(userIds.split(",")));
        sysUserDepartService.remove(queryWrapper);
        return Result.success("删除成功!");
    }

    /**
     * 查询当前用户的所有部门/当前部门编码
     *
     * @return
     */
    @RequestMapping(value = "/getCurrentUserDeparts", method = RequestMethod.GET)
    public Result<Map<String, Object>> getCurrentUserDeparts() {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<SysDepart> list = this.sysDepartService.queryUserDeparts(sysUser.getId());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", list);
        map.put("orgCode", sysUser.getOrgCode());
        return Result.success(map);
    }


    /**
     * 用户注册接口
     *
     * @param json
     * @param user
     * @return
     */
    @PostMapping("/register")
    public Result<JSONObject> userRegister(@RequestBody JSONObject json, SysUser user) {
        String phone = json.getString("phone");
        String smscode = json.getString("smscode");
        Object code = redisUtil.get(phone);
        String username = json.getString("username");
        //未设置用户名，则用手机号作为用户名
        if (StringUtils.isEmpty(username)) {
            username = phone;
        }
        //未设置密码，则随机生成一个密码
        String password = json.getString("password");
        if (StringUtils.isEmpty(password)) {
            password = RandomUtil.randomString(8);
        }
        String email = json.getString("email");
        if (sysUserService.getUserByName(username) != null) {
            return Result.fail("用户名已注册");
        }
        if (sysUserService.getUserByPhone(phone) != null) {
            return Result.fail("该手机号已注册");
        }
        if (StringUtils.isNotEmpty(email) && sysUserService.getUserByEmail(email) != null) {
            return Result.fail("邮箱已被注册");
        }
        if (!smscode.equals(code))
            return Result.fail("手机验证码错误");
        try {
            user.setCreateTime(new Date());// 设置创建时间
            String salt = EncryptUtils.createRandom(8);
            String passwordEncode = EncryptUtils.encrypt(username, password, salt);
            user.setSalt(salt);
            user.setUserName(username);
            user.setRealName(username);
            user.setPassword(passwordEncode);
            user.setEmail(email);
            user.setPhone(phone);
            user.setStatus(CommonConstant.STATUS_NORMAL);
            //默认临时角色 test
            sysUserService.addUserWithRole(user, String.valueOf(DEFAULT_ROLE_ID));
            return Result.success("注册成功");
        } catch (Exception e) {
            return Result.fail("注册失败");
        }
    }

    /**
     * 根据用户名或手机号查询用户信息
     *
     * @param
     * @return
     */
    @GetMapping("/querySysUser")
    public Result<Map<String, Object>> querySysUser(SysUser sysUser) {
        String phone = sysUser.getPhone();
        String username = sysUser.getUserName();
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(phone)) {
            SysUser user = sysUserService.getUserByPhone(phone);
            if (user != null) {
                map.put("username", user.getUserName());
                map.put("phone", user.getPhone());
                return Result.success(map);
            }
        }
        if (StringUtils.isNotEmpty(username)) {
            SysUser user = sysUserService.getUserByName(username);
            if (user != null) {
                map.put("username", user.getUserName());
                map.put("phone", user.getPhone());
                return Result.success(map);
            }
        }
        return Result.fail("验证失败");
    }

    /**
     * 用户手机号验证
     */
    @PostMapping("/phoneVerification")
    public Result<String> phoneVerification(@RequestBody JSONObject json) {
        String phone = json.getString("phone");
        String smscode = json.getString("smscode");
        Object code = redisUtil.get(phone);
        if (!smscode.equals(code)) {
            return Result.fail("手机验证码错误");
        }
        redisUtil.set(phone, smscode);
        return Result.success();
    }

    /**
     * 用户更改密码
     */
    @GetMapping("/passwordChange")
    public Result<SysUser> passwordChange(@RequestParam(name = "username") String username,
                                          @RequestParam(name = "password") String password,
                                          @RequestParam(name = "smscode") String smscode,
                                          @RequestParam(name = "phone") String phone) {
        if (StringUtils.isEmpty(username))
            return Result.fail("用户名为空");
        if (StringUtils.isEmpty(password))
            return Result.fail("密码为空");
        if (StringUtils.isEmpty(smscode))
            return Result.fail("短信验证码为空");
        if (StringUtils.isEmpty(phone))
            return Result.fail("手机号为空");
        Object object = redisUtil.get(phone);
        if (null == object)
            return Result.fail("短信验证码失效！");
        if (!smscode.equals(object))
            return Result.fail("短信验证码不匹配！");
        SysUser sysUser = sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserName, username).eq(SysUser::getPhone, phone));
        if (sysUser == null) {
            return Result.fail("未找到用户！");
        } else {
            String salt = EncryptUtils.createRandom(8);
            sysUser.setSalt(salt);
            String passwordEncode = EncryptUtils.encrypt(sysUser.getUserName(), password, salt);
            sysUser.setPassword(passwordEncode);
            sysUserService.updateById(sysUser);
            return Result.success("密码重置完成！");
        }
    }

    /**
     * 根据TOKEN获取用户的部分信息（返回的数据是可供表单设计器使用的数据）
     *
     * @return
     */
    @GetMapping("/getUserSectionInfoByToken")
    public Result<?> getUserSectionInfoByToken(HttpServletRequest
                                                       request, @RequestParam(name = "token", required = false) String token) {
        String username;
        // 如果没有传递token，就从header中获取token并获取用户信息
        if (StringUtils.isEmpty(token)) {
            username = JwtUtils.getUserNameByToken(request);
        } else {
            username = JwtUtils.getUsername(token);
        }
        log.info(" ------ 通过令牌获取部分用户信息，当前用户： " + username);
        // 根据用户名查询用户信息
        SysUser sysUser = sysUserService.getUserByName(username);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sysUserId", sysUser.getId());
        map.put("sysUserCode", sysUser.getUserName()); // 当前登录用户登录账号
        map.put("sysUserName", sysUser.getRealName()); // 当前登录用户真实名称
        map.put("sysOrgCode", sysUser.getOrgCode()); // 当前登录用户部门编号
        log.info(" ------ 通过令牌获取部分用户信息，已获取的用户信息： " + map);
        return Result.success(map);
    }

    /**
     * 【APP端接口】获取用户列表  根据用户名和真实名 模糊匹配
     *
     * @param keyword
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/appUserList")
    public Result<?> appUserList(@RequestParam(name = "keyword", required = false) String keyword,
                                 @RequestParam(name = "username", required = false) String username,
                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<SysUser> query = new LambdaQueryWrapper<>();
        query.eq(SysUser::getStatus, CommonConstant.STATUS_NORMAL);
        if (StringUtils.isNotEmpty(username)) {
            query.eq(SysUser::getUserName, username);
        } else {
            query.and(i -> i.like(SysUser::getUserName, keyword).or().like(SysUser::getRealName, keyword));
        }
        Page<SysUser> page = new Page<>(pageNo, pageSize);
        IPage<SysUser> res = this.sysUserService.page(page, query);
        return Result.success(res);
    }

    /**
     * 获取被逻辑删除的用户列表，无分页
     *
     * @return logicDeletedUserList
     */
    @GetMapping("/recycleBin")
    public Result getRecycleBin() {
        List<SysUser> logicDeletedUserList = sysUserService.queryLogicDeleted();
        if (logicDeletedUserList.size() > 0) {
            // 批量查询用户的所属部门
            // step.1 先拿到全部的 userIds
            List<Integer> userIds = logicDeletedUserList.stream().map(SysUser::getId).collect(Collectors.toList());
            // step.2 通过 userIds，一次性查询用户的所属部门名字
            Map<Integer, String> useDepNames = sysUserService.getDeptNamesByUserIds(userIds);
            logicDeletedUserList.forEach(item -> item.setOrgCode(useDepNames.get(item.getId())));
        }
        return Result.success(logicDeletedUserList);
    }

    /**
     * 还原被逻辑删除的用户
     *
     * @param json
     * @return
     */
    @RequestMapping(value = "/putRecycleBin", method = RequestMethod.PUT)
    public Result putRecycleBin(@RequestBody JSONObject json, HttpServletRequest request) {
        String userIds = json.getString("userIds");
        if (StringUtils.isNotBlank(userIds)) {
            SysUser updateUser = new SysUser();
            updateUser.setUpdateBy(JwtUtils.getUserNameByToken(request));
            List<Integer> idList = new ArrayList<>();
            Arrays.asList(userIds.split(",")).forEach(s -> {
                idList.add(Integer.valueOf(s));
            });
            sysUserService.revertLogicDeleted(idList, updateUser);
        }
        return Result.success("还原成功");
    }

    /**
     * 彻底删除用户
     *
     * @param userIds 被删除的用户ID，多个id用半角逗号分割
     * @return
     */
    @RequestMapping(value = "/deleteRecycleBin", method = RequestMethod.DELETE)
    public Result deleteRecycleBin(@RequestParam("userIds") String userIds) {
        if (StringUtils.isNotBlank(userIds)) {
            List<Integer> idList = new ArrayList<>();
            Arrays.asList(userIds.split(",")).forEach(s -> {
                idList.add(Integer.valueOf(s));
            });
            sysUserService.removeLogicDeleted(idList);
        }
        return Result.success("删除成功");
    }


    /**
     * 移动端修改用户信息
     *
     * @param json
     * @return
     */
    @RequestMapping(value = "/appEdit", method = RequestMethod.PUT)
    public Result<SysUser> appEdit(@RequestBody JSONObject json) {
        SysUser sysUser = sysUserService.getById(json.getString("id"));
        sysLogService.addLog("移动端编辑用户，id： " + json.getString("id"), LogType.OPERATION, OperateType.MODIFY);
        if (sysUser == null) {
            return Result.fail("未找到对应用户!");
        } else {
            SysUser user = JSON.parseObject(json.toJSONString(), SysUser.class);
            user.setUpdateTime(new Date());
            user.setPassword(sysUser.getPassword());
            sysUserService.updateById(user);
            return Result.success();
        }
    }


    @GetMapping("/rolesSet/{username}")
    public Result<Object> getUserRolesSet(@PathVariable("username") String username) {
        return Result.success(sysUserService.getUserRolesSet(username));
    }

    @GetMapping("/permissionsSet/{username}")
    public Result<Object> getUserPermissionsSet(@PathVariable("username") String username) {
        return Result.success(sysUserService.getUserPermissionsSet(username));
    }

    @GetMapping(value = "/info/{username}")
    public Result<Object> info(@PathVariable("username") String username) {
        SysUser sysUser = sysUserService.getUserByName(username);
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(sysUser, loginUser);
        return Result.success(loginUser);
    }

    /**
     * 通过编码和存储的value查询字典text
     *
     * @param code
     * @param key
     * @return
     */
    @GetMapping(value = "/queryDictTextByKey")
    public String queryDictTextByKey(@RequestParam(name = "code") String code, @RequestParam(name = "key") String
            key) {
        return sysDictService.queryDictTextByKey(code, key);
    }

    /**
     * 通过编码和存储的value查询表字典的text
     *
     * @param table 表名
     * @param text  表字段
     * @param code  表字段
     * @param key   表字段code的值
     * @return
     */
    @GetMapping(value = "/queryTableDictTextByKey")
    public String queryTableDictTextByKey(@RequestParam(name = "table") String table,
                                          @RequestParam(name = "text") String text,
                                          @RequestParam(name = "code") String code,
                                          @RequestParam(name = "key") String key) {
        return sysDictService.queryTableDictTextByKey(table, text, code, key);
    }


    /**
     * 通过component查询菜单配置信息
     *
     * @param component
     * @return
     */
    @GetMapping(value = "queryComponentPermission")
    public List<SysPermission> queryComponentPermission(@RequestParam(name = "component") String component) {
        LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<>();
        query.eq(SysPermission::getStatus, CommonConstant.STATUS_NORMAL);
        query.eq(SysPermission::getComponent, component);
        List<SysPermission> currentSyspermission = sysPermissionService.list(query);
        return currentSyspermission;
    }

    /**
     * 通过请求地址查找菜单信息
     *
     * @param method
     * @param path
     * @return
     */
    @GetMapping(value = "queryRequestPermission")
    public List<SysPermission> queryRequestPermission(@RequestParam(name = "method") String method,
                                                      @RequestParam(name = "path") String path) {
//        //1.直接通过前端请求地址查询菜单
//        LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<SysPermission>();
//        query.eq(SysPermission::getMenuType, 2);
//        query.eq(SysPermission::getStatus, CommonConstant.STATUS_NORMAL);
//        query.eq(SysPermission::getUrl, path);
//        List<SysPermission> currentSyspermission = sysPermissionService.list(query);
//        //2.未找到 再通过自定义匹配URL 获取菜单
//        if (currentSyspermission == null || currentSyspermission.size() == 0) {
//            //通过自定义URL匹配规则 获取菜单（实现通过菜单配置数据权限规则，实际上针对获取数据接口进行数据规则控制）
//            String userMatchUrl = UrlMatchEnum.getMatchResultByUrl(path);
//            LambdaQueryWrapper<SysPermission> queryQserMatch = new LambdaQueryWrapper<SysPermission>();
//            queryQserMatch.eq(SysPermission::getMenuType, 1);
//            queryQserMatch.eq(SysPermission::getStatus, CommonConstant.STATUS_NORMAL);
//            queryQserMatch.eq(SysPermission::getUrl, userMatchUrl);
//            if (StringUtils.isNotEmpty(userMatchUrl)) {
//                currentSyspermission = sysPermissionService.list(queryQserMatch);
//            }
//        }
//        //3.未找到 再通过正则匹配获取菜单
//        if (currentSyspermission == null || currentSyspermission.size() == 0) {
//            //通过正则匹配权限配置
//            String regUrl = getRegexpUrl(path);
//            if (regUrl != null) {
//                currentSyspermission = sysPermissionService.list(new LambdaQueryWrapper<SysPermission>().eq(SysPermission::getMenuType, 2).eq(SysPermission::getUrl, regUrl).eq(SysPermission::getDelFlag, 0));
//            }
//        }
//        return currentSyspermission;
        return null;
    }

    /**
     * 根据username获取用户信息
     *
     * @param username
     * @return
     */
    @GetMapping(value = "getCacheUser")
    public SysUserCacheInfo getCacheUser(@RequestParam(name = "username") String username) {
        return null;//sysUserService.getCacheUser(username);
    }


    /**
     * 根据请求地址获取正则
     *
     * @param url
     * @return
     */
    private String getRegexpUrl(String url) {
        List<String> list = sysPermissionService.queryPermissionUrlWithStar();
        if (list != null && list.size() > 0) {
            for (String p : list) {
                PathMatcher matcher = new AntPathMatcher();
                if (matcher.match(p, url)) {
                    return p;
                }
            }
        }
        return null;
    }
}
