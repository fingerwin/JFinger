package org.jfinger.cloud.enumerate;

import lombok.Getter;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description Http状态
 * @Author finger
 * @Date 2020/12/23 0023
 * @Version 1.0
 */
public enum HttpStatus {
    RSP_OK(200, "成功"),
    RSP_REDIRECT(301, "重定向"),
    RSP_NO_AUTH(401, "未授权"),
    RSP_FORBIDDEN(403, "禁止访问"),
    RSP_NOT_FOUND(404, "未找到路径"),
    RSP_NOT_ALLOWED(405, "请求方法不存在"),
    RSP_ENTITY_TOP_LARGE(413, "请求数据或文件太大"),
    RSP_INTERNAL_ERROR(500, "系统错误"),
    RSP_BAD_GATEWAY(502, "网关错误"),
    RSP_SERVICE_UNAVAILABLE(503, "服务异常");

    private static final Map<Integer, HttpStatus> mappings = new HashMap(16);

    @Getter
    private Integer code;

    @Getter
    private String content;

    HttpStatus(Integer code, String content) {
        this.code = code;
        this.content = content;
    }

    /**
     * 根据状态获取Message
     *
     * @param code 状态码
     * @return
     */
    public static String getContentByCode(Integer code) {
        for (HttpStatus instance : values()) {
            if (instance.code.equals(code)) {
                return instance.getContent();
            }
        }
        return null;
    }

    @Nullable
    public static HttpStatus resolve(@Nullable Integer code) {
        return code != null ? mappings.get(code) : null;
    }

    public boolean matches(Integer code) {
        return this == resolve(code);
    }

    static {
        HttpStatus[] all = values();
        int len = all.length;

        for (int i = 0; i < len; ++i) {
            HttpStatus status = all[i];
            mappings.put(status.getCode(), status);
        }
    }
}
