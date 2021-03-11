package org.jfinger.cloud.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jfinger.cloud.enumerate.HttpStatus;

import java.io.Serializable;

/**
 * @Description 响应结果
 * @Author finger
 * @Date 2020/12/23 0023
 * @Version 1.0
 */
@Data
@NoArgsConstructor
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -8820551038074578025L;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误消息
     */
    private String message = "";
    /**
     * 业务数据
     */
    private T result;

    /**
     * 成功
     *
     * @param <T>
     * @return
     */
    public static <T> Result<T> success() {
        Result<T> response = new Result<T>();
        response.setCode(HttpStatus.RSP_OK.getCode());
        response.setMessage(HttpStatus.RSP_OK.getContent());
        return response;
    }

    /**
     * 成功
     *
     * @param msg message设置的信息
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(String msg) {
        Result<T> response = new Result<T>();
        response.setCode(HttpStatus.RSP_OK.getCode());
        response.setMessage(msg);
        return response;
    }

    /**
     * 成功
     *
     * @param result
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(T result) {
        Result<T> response = new Result<T>();
        response.setCode(HttpStatus.RSP_OK.getCode());
        response.setMessage(HttpStatus.RSP_OK.getContent());
        response.setResult(result);
        return response;
    }

    /**
     * 成功
     *
     * @param msg
     * @param result
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(String msg, T result) {
        Result<T> response = new Result<T>();
        response.setCode(HttpStatus.RSP_OK.getCode());
        response.setMessage(msg);
        response.setResult(result);
        return response;
    }

    /**
     * 失败
     *
     * @param status
     * @param <T>
     * @return
     */
    public static <T> Result<T> fail(HttpStatus status) {
        Result<T> response = new Result<T>();
        response.setCode(status.getCode());
        response.setMessage(status.getContent());
        return response;
    }

    /**
     * 失败
     *
     * @param message
     * @param <T>
     * @return
     */
    public static <T> Result<T> fail(String message) {
        Result<T> response = new Result<T>();
        response.setCode(HttpStatus.RSP_INTERNAL_ERROR.getCode());
        response.setMessage(message);
        return response;
    }

    /**
     * 失败
     *
     * @param code    自定义错误码，请使用大于{@linkplain org.jfinger.cloud.constant.CommonConstant#RSP_BUSINESS_BEGIN RSP_BUSINESS_BEGIN}的值
     * @param message
     * @param <T>
     * @return
     */
    public static <T> Result<T> fail(Integer code, String message) {
        Result<T> response = new Result<T>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }

    /**
     * 是否成功
     *
     * @return
     */
    public boolean isSuccess() {
        return HttpStatus.RSP_OK.getCode().equals(code);
    }
}
