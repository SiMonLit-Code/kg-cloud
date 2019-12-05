package com.plantdata.kgcloud.domain.common.converter;

import cn.hiboot.mcn.core.model.result.RestResp;
import com.plantdata.kgcloud.exception.BizException;

import java.util.Optional;

public class RestRespConverter {
    public static <T> Optional<T> convert(RestResp<T> restResp) {
        if (!RestResp.ActionStatusMethod.OK.equals(restResp.getActionStatus())) {
            throw new BizException(restResp.getErrorCode(), restResp.getErrorInfo());
        }
        return Optional.ofNullable(restResp.getData());
    }
}
