package com.plantdata.kgcloud.sdk;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.req.DataAccessTaskConfigReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@FeignClient(value = "kgms", path = "access", contextId = "access")
public interface AccessTaskClient {

    @PostMapping("/run/task")
    ApiReturn runTask(@Valid @RequestBody List<DataAccessTaskConfigReq> reqs);
}
