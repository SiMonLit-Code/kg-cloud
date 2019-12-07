package com.plantdata.kgcloud.domain.app.converter.graph;

import cn.hiboot.mcn.core.model.result.RestResp;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import org.springframework.beans.BeanUtils;

import java.util.Optional;

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
}
