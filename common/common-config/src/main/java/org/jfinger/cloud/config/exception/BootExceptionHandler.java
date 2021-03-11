package org.jfinger.cloud.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.jfinger.cloud.entity.Result;
import org.jfinger.cloud.exception.JFingerException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * @Description 统一的异常处理
 * @Author finger
 * @Date 2021/3/9 0009
 * @Version 1.0
 */
@Slf4j
@RestControllerAdvice
public class BootExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Result processFileNotFound(NoHandlerFoundException e) {
        log.error(e.getMessage(), e);
        return Result.fail(org.jfinger.cloud.enumerate.HttpStatus.RSP_NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public Result processMethodNotSupport(NoHandlerFoundException e) {
        log.error(e.getMessage(), e);
        return Result.fail(org.jfinger.cloud.enumerate.HttpStatus.RSP_NOT_ALLOWED);
    }

    /**
     * 自定义处理异常
     *
     * @param e 异常
     * @throws Exception 异常
     */
    @ExceptionHandler(value = JFingerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Result defaultBusinessErrorHandler(JFingerException e, HttpServletRequest request) {
        log.error(e.getMessage(), e);
        return handleException(request, e.getMessage());
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Result defaultParameterErrorHandler(HttpServletRequest request, MissingServletRequestParameterException e) {
        log.error(e.getMessage(), e);
        return handleException(request, "参数错误：类型(" + e.getParameterType() + "),参数名(" + e.getParameterName() + ")");
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Result defaultConstraintViolationException(HttpServletRequest request, ConstraintViolationException e) {
        log.error(e.getMessage(), e);
        String msg = "数据校验失败:\n";
        for (ConstraintViolation violation : e.getConstraintViolations()) {
            msg += violation.getPropertyPath() + ":" + violation.getMessage();
        }
        return handleException(request, msg);
    }

    /**
     * 系统异常处理，比如：500
     *
     * @param e 异常
     * @return JsonResult结构
     * @throws Exception 异常
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Result defaultErrorHandler(HttpServletRequest request, Exception e) throws Exception {
        log.error(e.getMessage(), e);
        return handleException(request, e.getMessage());
    }

    /**
     * 拦截@Valid请求参数验证不通过的异常
     *
     * @param request request
     * @param e       验证不通过的异常
     * @return 执行结果
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    @Order(0)
    public Result handler(HttpServletRequest request, MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String validation_message;
        BindingResult bindingResult = e.getBindingResult();
        if (bindingResult != null && bindingResult.getFieldError() != null) {
            validation_message = bindingResult.getFieldError().getDefaultMessage();
        } else {
            validation_message = e.getMessage();
        }
        log.error("参数错误信息：" + validation_message);
        return handleException(request, validation_message);
    }

    /**
     * 通用处理异常
     *
     * @param request
     * @param message
     * @return
     */
    private Result<?> handleException(HttpServletRequest request, String message) {
        String title = "";
        switch (HttpMethod.resolve(request.getMethod())) {
            case GET:
                title = "查询失败,";
                break;
            case PUT:
            case POST:
            case PATCH:
                title = "操作失败,";
                break;
            case DELETE:
                title = "删除失败,";
                break;
        }
        return Result.fail(title + message);
    }
}
