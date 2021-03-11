package org.jfinger.cloud.enumerate;

import lombok.Getter;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 操作类型
 * @Author finger
 * @Date 2020/12/23 0023
 * @Version 1.0
 */
public enum OperateType {
    NONE(0, "无"),
    QUERY(1, "查询"),
    INSERT(2, "添加"),
    MODIFY(3, "修改"),
    DELETE(4, "删除"),
    IMPORT(5, "导入"),
    EXPORT(6, "导出");

    private static final Map<Integer, OperateType> mappings = new HashMap(16);

    @Getter
    private Integer code;

    @Getter
    private String content;

    OperateType(Integer code, String content) {
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
        for (OperateType instance : OperateType.values()) {
            if (instance.code.equals(code)) {
                return instance.getContent();
            }
        }
        return null;
    }

    @Nullable
    public static OperateType resolve(@Nullable Integer code) {
        return code != null ? mappings.get(code) : null;
    }

    public boolean matches(Integer code) {
        return this == resolve(code);
    }

    static {
        OperateType[] all = values();
        int len = all.length;

        for (int i = 0; i < len; ++i) {
            OperateType type = all[i];
            mappings.put(type.getCode(), type);
        }
    }
}
