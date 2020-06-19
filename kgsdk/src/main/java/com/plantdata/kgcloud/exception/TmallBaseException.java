package com.plantdata.kgcloud.exception;


import cn.hiboot.mcn.core.model.result.RestResp;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.plantdata.kgcloud.constant.ErrorCode;
import com.plantdata.kgcloud.constant.SdkErrorCodeEnum;
import feign.FeignException;
import feign.Request;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-06-19 18:23
 **/
@ControllerAdvice
@Slf4j
public class TmallBaseException  {

    /**
     * @Author: lixg
     * @Description: 系统异常捕获处理
     */
    @ResponseBody
    @ExceptionHandler(value = HystrixBadRequestException.class)
    public RestResp errorExceptionHandler(HystrixBadRequestException ex) {
        log.error("捕获到 HystrixBadRequestException 异常", ex);
        return new RestResp(SdkErrorCodeEnum.CONFIG_PARAM_ERROR.getErrorCode(), "系统繁忙,请稍后再试");
    }

}
