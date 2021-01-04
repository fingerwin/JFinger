package org.jfinger.cloud.annotation;

import org.jfinger.cloud.enumerate.LogType;
import org.jfinger.cloud.enumerate.OperateType;

import java.lang.annotation.*;

/***
 * 系统日志注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JLog {
    /**
     * 日志内容
     *
     * @return
     */
    String value() default "";

    /**
     * 日志类型，见{@linkplain org.jfinger.cloud.enumerate.LogType LogType}
     */
    LogType logType() default LogType.OPERATION;

    /**
     * 操作日志类型，见{@linkplain org.jfinger.cloud.enumerate.OperateType OperateType}
     *
     * @return
     */
    OperateType operateType() default OperateType.NONE;
}
