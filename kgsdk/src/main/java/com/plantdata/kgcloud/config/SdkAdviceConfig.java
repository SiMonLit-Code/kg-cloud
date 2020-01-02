package com.plantdata.kgcloud.config;

import cn.hiboot.mcn.core.model.result.RestResp;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.constant.CommonErrorCode;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.exception.SdkException;
import com.plantdata.kgcloud.util.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
@Slf4j
public class SdkAdviceConfig {


    @ExceptionHandler(value = {Exception.class})
    public Object handleException(Exception e) {
        log.error("全局异常处理", e);
        //文件上传异常
        if (e instanceof MaxUploadSizeExceededException) {
            return ApiReturn.fail(CommonErrorCode.BAD_REQUEST.getErrorCode(), "上传文件超出限制大小");
        }
        //参数类型不匹配
        if (e instanceof MethodArgumentTypeMismatchException) {
            return ApiReturn.fail(CommonErrorCode.BAD_REQUEST.getErrorCode(), "请求参数类型不匹配");
        }
        //参数校验无效
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) e;
            BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
            return ApiReturn.fail(CommonErrorCode.BAD_REQUEST.getErrorCode(), bindingResult.getFieldError().getField() + bindingResult.getFieldError().getDefaultMessage());
        }
        if (e instanceof SdkException) {
            SdkException exception = (SdkException) e;
            RestResp restResp = new RestResp();
            restResp.setActionStatus(RestResp.ActionStatusMethod.FAIL);
            restResp.setErrorCode(exception.getErrCode());
            restResp.setErrorInfo(exception.getMessage());
            return restResp;
        }
        //业务异常
        if (e instanceof BizException) {
            BizException bizException = (BizException) e;
            return ApiReturn.fail(bizException.getErrCode(), bizException.getMessage());
        }
        //未知异常
        return ApiReturn.fail(CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorCode(), ExceptionUtils.getStackTrace(e));
    }
}