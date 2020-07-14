package ai.plantdata.kgcloud.sdk;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.kgcloud.sdk.exection.client.PreBuilderClientEx;
import ai.plantdata.kgcloud.sdk.req.StandardSearchReq;
import ai.plantdata.kgcloud.sdk.rsp.StandardTemplateRsp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author czj
 * @version 1.0
 * @date 2020/6/8 10:59
 */
@FeignClient(value = "kgms", path = "builder/", contextId = "preBuilderClient",fallback = PreBuilderClientEx.class)
public interface PreBuilderClient {


    /**
     * 行业标准读取
     *
     * @param req
     * @return
     */
    @PostMapping("/standard/list")
    ApiReturn standardList(@RequestBody StandardSearchReq req) ;


    /**
     * 模式获取
     *
     * @param ids
     * @return
     */
    @PostMapping("/find/ids")
    ApiReturn<List<StandardTemplateRsp>> findIds(@RequestBody List<Integer> ids);
}
