package com.plantdata.kgcloud.domain.share.Controller;

import com.plantdata.kgcloud.domain.share.service.LinkShareService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jdm on 2019/12/9 12:06.
 */

@Api(tags = "知识应用分享")
@RestController
@RequestMapping("/share")
public class LinkShareController {

    @Autowired
    private LinkShareService linkShareService;


}
