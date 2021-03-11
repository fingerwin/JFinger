package org.jfinger.cloud.enumerate;

import lombok.Getter;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 日志类型
 * @Author finger
 * @Date 2020/12/23 0023
 * @Version 1.0
 */
public enum LogType {

    OPERATION(0, "操作日志"),
    LOGIN(1, "登录日志"),
    JOB(2, "定时任务");

    private static final Map<Integer, LogType> mappings = new HashMap(16);

    @Getter
    private Integer code;

    @Getter
    private String content;

    LogType(Integer code, String content) {
        this.code = code;
        this.content = content;
    }

    /**
     * 根据编码获取内容
     *
     * @param code 状态码
     * @return
     */
    public static String getContentByCode(Integer code) {
        for (LogType instance : LogType.values()) {
            if (instance.code.equals(code)) {
                return instance.getContent();
            }
        }
        return null;
    }

    @Nullable
    public static LogType resolve(@Nullable Integer code) {
        return code != null ? mappings.get(code) : null;
    }

    public boolean matches(Integer code) {
        return this == resolve(code);
    }

    static {
        LogType[] all = values();
        int len = all.length;

        for (int i = 0; i < len; ++i) {
            LogType type = all[i];
            mappings.put(type.getCode(), type);
        }
    }
}
