package org.jfinger.cloud.aop;

import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jfinger.cloud.annotation.JLog;
import org.jfinger.cloud.api.SysCommonApi;
import org.jfinger.cloud.entity.SysLog;
import org.jfinger.cloud.entity.uac.LoginUser;
import org.jfinger.cloud.utils.IpUtils;
import org.jfinger.cloud.utils.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @Description 系统日志，切面处理类
 * @Author finger
 * @Date 2020/12/23 0023
 * @Version 1.0
 */
@Aspect
@Component
public class SysLogAspect {
    @Autowired
    private SysCommonApi sysCommonApi;

    @Pointcut("@annotation(org.jfinger.cloud.annotation.JLog)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        //保存日志
        saveSysLog(point, time);
        return result;
    }

    /**
     * 保存系统日志
     *
     * @param joinPoint
     * @param time
     */
    private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        SysLog sysLog = new SysLog();
        JLog jLog = method.getAnnotation(JLog.class);
        if (jLog != null) {
            //注解上的描述,操作日志内容
            sysLog.setLogContent(jLog.value());
            sysLog.setLogType(jLog.logType().getCode());
            sysLog.setOperateType(jLog.operateType().getCode());
        }

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName + "()");

        //获取request
        HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
        //请求的参数
        sysLog.setRequestParam(getReqestParams(request, joinPoint));

        //设置IP地址
        sysLog.setIp(IpUtils.getIpAddr(request));

        //获取登录用户信息
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (sysUser != null) {
            sysLog.setUserId(sysUser.getUserName());
            sysLog.setUserName(sysUser.getRealName());

        }
        //耗时
        sysLog.setCostTime(time);
        sysLog.setCreateTime(new Date());
        //保存系统日志
        sysCommonApi.saveSysLog(sysLog);
    }

    /**
     * @param request:   request
     * @param joinPoint: joinPoint
     * @Description: 获取请求参数
     * @author: scott
     * @date: 2020/4/16 0:10
     * @Return: java.lang.String
     */
    private String getReqestParams(HttpServletRequest request, JoinPoint joinPoint) {
        String httpMethod = request.getMethod();
        String params = "";
        if ("POST".equals(httpMethod) || "PUT".equals(httpMethod) || "PATCH".equals(httpMethod)) {
            Object[] paramsArray = joinPoint.getArgs();
            params = JSONObject.toJSONString(paramsArray);
        } else {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            // 请求的方法参数值
            Object[] args = joinPoint.getArgs();
            // 请求的方法参数名称
            LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
            String[] paramNames = u.getParameterNames(method);
            if (args != null && paramNames != null) {
                for (int i = 0; i < args.length; i++) {
                    params += "  " + paramNames[i] + ": " + args[i];
                }
            }
        }
        return params;
    }
}
