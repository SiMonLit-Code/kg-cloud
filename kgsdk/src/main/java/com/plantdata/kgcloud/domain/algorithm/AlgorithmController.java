package com.plantdata.kgcloud.domain.algorithm;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.AppClient;
import com.plantdata.kgcloud.sdk.req.app.algorithm.BusinessGraphRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cjw
 * @date 2020/7/7  15:36
 */
@RestController
@RequestMapping("v3/algorithm")
public class AlgorithmController {

    @Autowired
    private AppClient appClient;

    @PostMapping("run/{kgName}/{id}")
    public ApiReturn<BusinessGraphRsp> executeAlgorithm(@PathVariable("kgName") String kgName,
                                                 @PathVariable("id") long id,
                                                 @RequestBody BusinessGraphRsp graphBean) {
        return appClient.executeAlgorithm(kgName, id, graphBean);
    }
}
