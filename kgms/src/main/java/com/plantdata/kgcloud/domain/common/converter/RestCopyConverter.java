package com.plantdata.kgcloud.domain.common.converter;

import cn.hiboot.mcn.core.model.result.RestResp;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.util.ConvertUtils;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/6 11:40
 */
public class RestCopyConverter {

    public static <T, E> E copyRestRespResult(RestResp<T> rest, E result) {
        Optional<T> opt = RestRespConverter.convert(rest);
        opt.ifPresent(a -> BeanUtils.copyProperties(a, result));
        return result;
    }

    public static <T, R> List<R> copyToNewList(List<T> reqList, Class<R> r) {
        return reqList.stream().map(ConvertUtils.convert(r)).collect(Collectors.toList());
    }
}
