package com.plantdata.kgcloud.sdk;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.req.EntityFileRelationAddReq;
import com.plantdata.kgcloud.sdk.rsp.edit.EntityFileRelationRsp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author lp
 * @date 2020/5/21 21:04
 */
@FeignClient(value = "kgms", path = "entity/file", contextId = "entityFileClient")
public interface EntityFileClient {

    /**
     * 实体文件管理-建立文件标引关系
     *
     * @param kgName
     * @param req
     * @return
     */
    @PostMapping("{kgName}/create/file/relation")
    ApiReturn<EntityFileRelationRsp> add(@PathVariable("kgName") String kgName, @RequestBody EntityFileRelationAddReq req);
}
