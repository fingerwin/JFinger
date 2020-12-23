package org.jfinger.cloud.enumerate;

import lombok.Getter;

/**
 * @Description 操作类型
 * @Author finger
 * @Date 2020/12/23 0023
 * @Version 1.0
 */
public enum OperateType {
    JOB(2, "定时任务");

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
}
