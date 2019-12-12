package com.plantdata.kgcloud.plantdata;

import cn.hiboot.mcn.core.model.result.RestResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/12 11:29
 */
@RestController
@RequestMapping("sdk/app")
public class AppController {

    @GetMapping("schema")
    public RestResp<Object> schema() {

        return new RestResp<>("success");
    }
}
