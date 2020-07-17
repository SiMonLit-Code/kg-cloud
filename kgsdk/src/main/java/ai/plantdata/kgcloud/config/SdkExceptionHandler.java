package ai.plantdata.kgcloud.config;

import ai.plantdata.cloud.constant.CommonErrorCode;
import ai.plantdata.cloud.exception.BizException;
import ai.plantdata.cloud.util.ExceptionUtils;
import cn.hiboot.mcn.core.model.result.RestResp;
import com.fasterxml.jackson.annotation.JsonInclude;
import ai.plantdata.kgcloud.constant.SdkErrorCodeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * @author cjw
 */
@RestControllerAdvice(basePackages = "com.plantdata.kgcloud.plantdata")
@Slf4j
public class SdkExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public Object handleException(Exception e) {
        log.error("全局异常处理", e);

        if (e instanceof MaxUploadSizeExceededException) {
            return new RestResp<>(CommonErrorCode.BAD_REQUEST.getErrorCode(), "上传文件超出限制大小");
        }
        if (e instanceof BindException) {
            BindException bindException = (BindException) e;
            return dealBindingResult(bindException);
        }
        //参数类型不匹配
        if (e instanceof MethodArgumentTypeMismatchException) {
            return new RestResp<>(CommonErrorCode.BAD_REQUEST.getErrorCode(), "请求参数类型不匹配");
        }
        //参数校验无效
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) e;
            BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
            return new RestResp<>(CommonErrorCode.BAD_REQUEST.getErrorCode(), bindingResult.getFieldError().getField() + bindingResult.getFieldError().getDefaultMessage());
        }

        //业务异常
        if (e instanceof BizException) {
            BizException bizException = (BizException) e;
            return new RestResp<>(bizException.getErrCode(), bizException.getMessage());
        }
        //未知异常
        return new RestResp<>(CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorCode(), ExceptionUtils.getStackTrace(e));
    }

    private RestResp dealBindingResult(BindingResult bindingResult) {
        String msg = null;
        ValidationErrorBean data = null;
        for (ObjectError objectError : bindingResult.getAllErrors()) {
            if (objectError instanceof FieldError) {
                FieldError fieldError = (FieldError) objectError;
                msg = fieldError.contains(NumberFormatException.class) ? SdkErrorCodeEnum.NUMBER_FORMAT_ERROR.getMessage() : objectError.getDefaultMessage();
                data = new ValidationErrorBean(msg, fieldError.getField(), fieldError.getRejectedValue() == null ? null : fieldError.getRejectedValue().toString());
            } else {
                msg = "请求参数类型不匹配";
                data = new ValidationErrorBean(objectError.getDefaultMessage(), objectError.getObjectName(), null);
            }
            break;
        }
        RestResp<Object> restResp = new RestResp<>(CommonErrorCode.BAD_REQUEST.getErrorCode(), msg);
        restResp.setData(data);
        return restResp;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Getter
    @Setter
    private static class ValidationErrorBean {
        private String message;
        private String messageTemplate;
        private String path;
        private String invalidValue;

        private ValidationErrorBean(String message, String path, String invalidValue) {
            this.message = message;
            this.path = path;
            this.invalidValue = invalidValue;
        }
    }
}