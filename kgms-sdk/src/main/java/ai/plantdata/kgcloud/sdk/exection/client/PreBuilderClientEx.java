package ai.plantdata.kgcloud.sdk.exection.client;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.kgcloud.sdk.PreBuilderClient;
import ai.plantdata.kgcloud.sdk.req.StandardSearchReq;
import ai.plantdata.kgcloud.sdk.rsp.StandardTemplateRsp;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-06-19 19:19
 **/
@Component
public class PreBuilderClientEx implements PreBuilderClient {
    @Override
    public ApiReturn standardList(StandardSearchReq req) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<List<StandardTemplateRsp>> findIds(List<Integer> ids) {
        return ApiReturn.fail(500,"请求超时");
    }
}
