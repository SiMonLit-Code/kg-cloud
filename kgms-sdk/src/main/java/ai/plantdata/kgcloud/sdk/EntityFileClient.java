package ai.plantdata.kgcloud.sdk;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.kgcloud.sdk.exection.client.EntityFileClientEx;
import ai.plantdata.kgcloud.sdk.req.EntityFileRelationAddReq;
import ai.plantdata.kgcloud.sdk.rsp.edit.EntityFileRelationRsp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author lp
 * @date 2020/5/21 21:04
 */
@FeignClient(value = "kgms", path = "entity/file", contextId = "entityFileClient",fallback = EntityFileClientEx.class)
public interface EntityFileClient {

    /**
     * 实体文件管理-建立文件标引关系
     *
     * @param kgName
     * @param req
     * @return
     */
    @PostMapping("{kgName}/create/relation")
    ApiReturn<EntityFileRelationRsp> add(@PathVariable("kgName") String kgName, @RequestBody EntityFileRelationAddReq req);
}
