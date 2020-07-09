package com.plantdata.kgcloud.domain.common.converter;

import ai.plantdata.cloud.web.util.ConvertUtils;
import ai.plantdata.kg.api.edit.resp.BatchResult;
import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.sdk.rsp.OpenBatchResult;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/6 11:40
 */
public class RestCopyConverter {


    public static <T, R> OpenBatchResult<R> copyToBatchResult(BatchResult<T> batchResult, Class<R> clazz) {
        List<R> success = BasicConverter.listConvert(batchResult.getSuccess(), a -> BasicConverter.copy(a, clazz));
        List<R> error = BasicConverter.listConvert(batchResult.getError(), a -> BasicConverter.copy(a, clazz));
        return new OpenBatchResult<>(success, error);
    }

    public static <T, R> List<R> copyToNewList(List<T> reqList, Class<R> r) {
        return reqList.stream().map(ConvertUtils.convert(r)).collect(Collectors.toList());
    }
}
