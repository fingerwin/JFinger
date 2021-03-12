package org.jfinger.cloud.system.controller;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jfinger.cloud.constant.CacheConstant;
import org.jfinger.cloud.constant.CommonConstant;
import org.jfinger.cloud.entity.Result;
import org.jfinger.cloud.entity.data.SysDepart;
import org.jfinger.cloud.entity.data.SysUser;
import org.jfinger.cloud.entity.model.SysLoginModel;
import org.jfinger.cloud.entity.vo.LoginUser;
import org.jfinger.cloud.enumerate.LogType;
import org.jfinger.cloud.enumerate.SmsMode;
import org.jfinger.cloud.system.factory.SmsFactory;
import org.jfinger.cloud.system.service.ISysDepartService;
import org.jfinger.cloud.system.service.ISysDictService;
import org.jfinger.cloud.system.service.ISysLogService;
import org.jfinger.cloud.system.service.ISysUserService;
import org.jfinger.cloud.utils.cache.RedisUtils;
import org.jfinger.cloud.utils.common.CaptchaUtils;
import org.jfinger.cloud.utils.common.EncryptUtils;
import org.jfinger.cloud.utils.system.JwtUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description 用户登录
 * @Author finger
 * @Date 2021/2/2 0002
 * @Version 1.0
 */
@RestController
@RequestMapping("/sys")
@Api(tags = "用户登录相关")
@Slf4j
public class LoginController {
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysDepartService sysDepartService;

    @Autowired
    private ISysLogService sysLogService;

    @Autowired
    private ISysDictService sysDictService;

    @Autowired
    private SmsFactory smsFactory;

    @ApiOperation("登录接口")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result<?> login(@RequestBody SysLoginModel sysLoginModel) {
        Result<JSONObject> result = null;
        String username = sysLoginModel.getUsername();
        String password = sysLoginModel.getPassword();
        String captcha = sysLoginModel.getCaptcha();
        //1. 校验验证码
        if (captcha == null) {
            return Result.fail("验证码无效");
        }
        String lowerCaseCaptcha = captcha.toLowerCase();
        String realKey = EncryptUtils.md5(lowerCaseCaptcha + sysLoginModel.getCheckKey());
        Object checkCode = redisUtils.get(realKey);
        if (checkCode == null || !lowerCaseCaptcha.equals(checkCode.toString())) {
            return Result.fail("验证码错误");
        }
        //2. 校验用户是否有效
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUserName, username);
        SysUser sysUser = sysUserService.getOne(queryWrapper);
        result = sysUserService.checkUserIsEffective(sysUser);
        if (!result.isSuccess()) {
            return result;
        }
        //3. 校验用户名或密码是否正确
        String userpassword = EncryptUtils.encrypt(username, password, sysUser.getSalt());
        String syspassword = sysUser.getPassword();
        if (!syspassword.equals(userpassword)) {
            return Result.fail("用户名或密码错误");
        }
        //用户登录信息
        userInfo(sysUser, result);
        sysLogService.addLog("用户名: " + username + ",登录成功！", LogType.LOGIN, null);
        return result;
    }

    @PostMapping(value = "/smsCode")
    @ApiOperation("短信接口")
    public Result<?> sms(@RequestBody JSONObject json) {
        String mobile = json.get("mobile").toString();
        //手机号模式
        SmsMode mode = null;
        try {
            mode = SmsMode.resolve(json.getInteger("mode"));
        } catch (Exception e) {
        }
        if (mode == null)
            mode = SmsMode.SMS_CODE;
        if (StringUtils.isEmpty(mobile)) {
            return Result.fail("手机号不允许为空！");
        }
        Object object = redisUtils.get(mobile);
        if (object != null) {
            return Result.success("验证码10分钟内，仍然有效！");
        }
        //随机数
        String captcha = EncryptUtils.createRandom(true, 6);
        JSONObject obj = new JSONObject();
        obj.put("code", captcha);
        String prefix = null;
        switch (mode) {
            case SMS_CODE://短信验证码
                prefix = CommonConstant.PREFIX_SMS_CODE;
                break;
            case SMS_LOGIN://短信登录
                SysUser loginUser = sysUserService.getUserByPhone(mobile);
                Result r = sysUserService.checkUserIsEffective(loginUser);
                if (!r.isSuccess()) {
                    return r;
                }
                prefix = CommonConstant.PREFIX_SMS_LOGIN;
                break;
            case SMS_REGISTER://短信注册
                SysUser sysUser = sysUserService.getUserByPhone(mobile);
                if (sysUser != null) {
                    sysLogService.addLog("手机号已经注册，请直接登录！", LogType.LOGIN, null);
                    return Result.fail("手机号已经注册，请直接登录！");
                }
                prefix = CommonConstant.PREFIX_SMS_REGISTER;
                break;
        }
        String content = smsFactory.getProvider().buildCodeSms(null, captcha);
        Result result = smsFactory.getProvider().sendSms(mobile, content);
        if (result.isSuccess()) {
            redisUtils.set(prefix + mobile, captcha, 600);
            return Result.success("验证码发送成功！");
        } else
            return Result.fail("验证码发送失败！");
    }

    /**
     * 手机号登录接口
     *
     * @param json
     * @return
     */
    @ApiOperation("手机号登录接口,暂不使用")
    @PostMapping("/smsLogin")
    public Result<JSONObject> phoneLogin(@RequestBody JSONObject json) {
        String phone = json.getString("mobile");
        //校验用户有效性
        SysUser sysUser = sysUserService.getUserByPhone(phone);
        Result result = sysUserService.checkUserIsEffective(sysUser);
        if (!result.isSuccess()) {
            return result;
        }
        String captcha = json.getString("captcha");
        Object code = redisUtils.get(CommonConstant.PREFIX_SMS_LOGIN + phone);
        if (!captcha.equals(code)) {
            return Result.fail("手机验证码错误");
        }
        //用户信息
        userInfo(sysUser, result);
        //添加日志
        sysLogService.addLog("用户名: " + sysUser.getUserName() + ",登录成功！", LogType.LOGIN, null);
        return result;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ApiOperation("登出接口")
    public Result<?> logout(HttpServletRequest request) {
        //用户退出逻辑
        String token = request.getHeader(CommonConstant.X_ACCESS_TOKEN);
        if (StringUtils.isEmpty(token)) {
            return Result.fail("退出登录失败！");
        }
        String username = JwtUtils.getUsername(token);
        LoginUser sysUser = sysUserService.getLoginUserByName(username);
        if (sysUser != null) {
            sysLogService.addLog("用户名: " + sysUser.getUserName() + ",退出成功！", LogType.LOGIN, null);
            log.info(" 用户名:  " + sysUser.getUserName() + ",退出成功！ ");
            //清空用户登录Token缓存
            redisUtils.del(CommonConstant.PREFIX_USER_TOKEN + token);
            //清空用户登录Shiro权限缓存
            redisUtils.del(CommonConstant.PREFIX_USER_SHIRO_CACHE + sysUser.getId());
            //清空用户的缓存信息（包括部门信息），例如sys:cache:user::<username>
            redisUtils.del(String.format("%s::%s", CacheConstant.SYS_USERS_CACHE, sysUser.getUserName()));
            redisUtils.del(String.format("%s::%s", CacheConstant.SYS_USERS_CACHE_JWT, sysUser.getUserName()));
            //调用shiro的logout
            SecurityUtils.getSubject().logout();
            return Result.success("退出登录成功！");
        } else {
            return Result.fail("Token无效!");
        }
    }

    @GetMapping("visit")
    @ApiOperation("访问量获取")
    public Result<?> visitInfo() {
        return Result.success();
    }

    @RequestMapping(value = "/selectDepart", method = RequestMethod.PUT)
    @ApiOperation("登录成功选择用户当前部门(用户属于多个部门时)")
    public Result<?> selectDepart(@RequestBody LoginUser user, HttpServletRequest request) {
        return Result.success();
    }

    @ApiOperation("获取验证码")
    @GetMapping(value = "/captcha/{key}")
    public Result<String> randomImage(HttpServletRequest request, @PathVariable String key) {
        try {
            String code = EncryptUtils.createRandom(false, 4).toLowerCase();
            String redisKey = EncryptUtils.md5(code + key);
            redisUtils.set(redisKey, code, 60);
            // 将验证码存放在session中
            request.getSession().setAttribute("code", code);
            return Result.success("操作成功！", CaptchaUtils.generate(code));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("获取验证码出错");
        }
    }

    @ApiOperation("移动端登录")
    @RequestMapping(value = "/mLogin", method = RequestMethod.POST)
    public Result<?> mLogin(@RequestBody SysLoginModel sysLoginModel) throws Exception {
        return Result.fail("暂不支持！");
    }

    /**
     * 用户信息
     *
     * @param sysUser
     * @param result
     * @return
     */
    private Result<JSONObject> userInfo(SysUser sysUser, Result<JSONObject> result) {
        String sysPassword = sysUser.getPassword();
        String userName = sysUser.getUserName();
        // 生成token
        String token = JwtUtils.sign(userName, sysPassword);
        // 设置token缓存有效时间
        redisUtils.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        redisUtils.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtils.EXPIRE_TIME * 2 / 1000);
        LoginUser vo = new LoginUser();
        BeanUtils.copyProperties(sysUser, vo);
        // 获取用户部门信息
        JSONObject obj = new JSONObject();
        List<SysDepart> departs = sysDepartService.queryUserDeparts(sysUser.getId());
        obj.put("departs", departs);
        if (departs == null || departs.size() == 0) {
            obj.put("multi_depart", 0);
        } else if (departs.size() == 1) {
            sysUserService.updateUserDepart(userName, departs.get(0).getOrgCode());
            vo.setOrgCode(departs.get(0).getOrgCode());
            obj.put("multi_depart", 1);
        } else {
            //查询当前是否有登录部门
            // update-begin--Author:wangshuai Date:20200805 for：如果用戶为选择部门，数据库为存在上一次登录部门，则取一条存进去
            SysUser sysUserById = sysUserService.getById(sysUser.getId());
            if (StringUtils.isEmpty(sysUserById.getOrgCode())) {
                sysUserService.updateUserDepart(userName, departs.get(0).getOrgCode());
            }
            // update-end--Author:wangshuai Date:20200805 for：如果用戶为选择部门，数据库为存在上一次登录部门，则取一条存进去
            obj.put("multi_depart", 2);
        }
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<>();
        query.eq(SysDepart::getOrgCode, vo.getOrgCode());
        SysDepart selectDepart = sysDepartService.getOne(query, false);
        if (selectDepart != null) {
            vo.setOrgId(selectDepart.getId());
        }
        vo.setPassword(SecureUtil.md5(sysUser.getPassword()));
        redisUtils.set(CacheConstant.SYS_USERS_CACHE_JWT + ":" + token, vo);
        redisUtils.expire(CacheConstant.SYS_USERS_CACHE_JWT + ":" + token, JwtUtils.EXPIRE_TIME * 2 / 1000);
        obj.put("token", token);
        obj.put("userInfo", sysUser);
        obj.put("sysAllDictItems", sysDictService.queryAllDictItems());
        result.setResult(obj);
        result.success("登录成功");
        return result;
    }
}
