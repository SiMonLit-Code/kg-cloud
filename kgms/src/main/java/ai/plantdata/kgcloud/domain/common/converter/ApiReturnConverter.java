package ai.plantdata.kgcloud.domain.common.converter;


import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.cloud.exception.BizException;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/26 10:15
 */
public class ApiReturnConverter {

    private static final int SUCCESS = 200;

    public static <T> T convert(ApiReturn<T> apiReturn) {
        if (apiReturn.getErrCode() != SUCCESS) {
            throw new BizException(apiReturn.getErrCode(), apiReturn.getMessage());
        }
        return apiReturn.getData();
    }
}
