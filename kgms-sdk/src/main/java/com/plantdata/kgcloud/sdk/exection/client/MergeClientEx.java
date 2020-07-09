package com.plantdata.kgcloud.sdk.exection.client;

import ai.plantdata.cloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.MergeClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-06-19 19:19
 **/

@Component
public class MergeClientEx implements MergeClient {
    @Override
    public ApiReturn<String> createMergeEntity(String kgName, List<Long> ids) {
        return ApiReturn.fail(500,"请求超时");
    }
}
