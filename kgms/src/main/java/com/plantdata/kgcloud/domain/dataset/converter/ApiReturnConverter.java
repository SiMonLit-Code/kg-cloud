package com.plantdata.kgcloud.domain.dataset.converter;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.exception.BizException;
import org.springframework.http.HttpStatus;

import java.util.Optional;

/**
 * @Author: LinHo
 * @Date: 2019/12/27 15:55
 * @Description:
 */
public class ApiReturnConverter {

    public static <T> Optional<T> convert(ApiReturn<T> apiReturn){
        if (HttpStatus.OK.value() != apiReturn.getErrCode()){
            throw new BizException(apiReturn.getErrCode(),apiReturn.getMessage());
        }
        return Optional.ofNullable(apiReturn.getData());
    }
}
