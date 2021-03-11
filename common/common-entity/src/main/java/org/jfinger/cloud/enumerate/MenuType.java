package org.jfinger.cloud.enumerate;

import lombok.Getter;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 菜单类型
 * @Author finger
 * @Date 2020/12/23 0023
 * @Version 1.0
 */
public enum MenuType {

    PARENT(0, "一级菜单"),
    CHILDREN(1, "子菜单"),
    BUTTON(2, "按钮权限");

    private static final Map<Integer, MenuType> mappings = new HashMap(16);

    @Getter
    private Integer code;

    @Getter
    private String content;

    MenuType(Integer code, String content) {
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
        for (MenuType instance : MenuType.values()) {
            if (instance.code.equals(code)) {
                return instance.getContent();
            }
        }
        return null;
    }

    @Nullable
    public static MenuType resolve(@Nullable Integer code) {
        return code != null ? mappings.get(code) : null;
    }

    public boolean matches(Integer code) {
        return this == resolve(code);
    }

    static {
        MenuType[] all = values();
        int len = all.length;

        for (int i = 0; i < len; ++i) {
            MenuType type = all[i];
            mappings.put(type.getCode(), type);
        }
    }
}
