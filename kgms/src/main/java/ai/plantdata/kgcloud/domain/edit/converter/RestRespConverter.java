package ai.plantdata.kgcloud.domain.edit.converter;

import ai.plantdata.cloud.exception.BizException;
import cn.hiboot.mcn.core.model.result.RestResp;

import java.util.Optional;

/**
 * @Author: LinHo
 * @Date: 2019/11/18 10:58
 * @Description:
 */
public class RestRespConverter {

    public static <T> Optional<T> convert(RestResp<T> restResp) {
        if (!RestResp.ActionStatusMethod.OK.equals(restResp.getActionStatus())) {
            throw new BizException(restResp.getErrorCode(), restResp.getErrorInfo());
        }
        return Optional.ofNullable(restResp.getData());
    }

    public static Optional<Integer> convertCount(RestResp restResp) {
        if (!RestResp.ActionStatusMethod.OK.equals(restResp.getActionStatus())) {
            throw new BizException(restResp.getErrorCode(), restResp.getErrorInfo());
        }
        return Optional.ofNullable(restResp.getCount());
    }

    public static void convertVoid(RestResp restResp) {
        if (!RestResp.ActionStatusMethod.OK.equals(restResp.getActionStatus())) {
            throw new BizException(restResp.getErrorCode(), restResp.getErrorInfo());
        }
    }
}
