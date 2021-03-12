package org.jfinger.cloud.enumerate;

import lombok.Getter;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 短信模式
 * @Author finger
 * @Date 2020/12/23 0023
 * @Version 1.0
 */
public enum SmsMode {

    SMS_CODE(0, "短信验证码"),
    SMS_LOGIN(1, "短信登录"),
    SMS_REGISTER(2, "短信注册");

    private static final Map<Integer, SmsMode> mappings = new HashMap(8);

    @Getter
    private Integer code;

    @Getter
    private String content;

    SmsMode(Integer code, String content) {
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
        for (SmsMode instance : SmsMode.values()) {
            if (instance.code.equals(code)) {
                return instance.getContent();
            }
        }
        return null;
    }

    @Nullable
    public static SmsMode resolve(@Nullable Integer code) {
        return code != null ? mappings.get(code) : null;
    }

    public boolean matches(Integer code) {
        return this == resolve(code);
    }

    static {
        SmsMode[] all = values();
        int len = all.length;

        for (int i = 0; i < len; ++i) {
            SmsMode mode = all[i];
            mappings.put(mode.getCode(), mode);
        }
    }
}
