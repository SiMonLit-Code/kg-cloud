package com.plantdata.kgcloud.domain.share.service.impl;

import com.plantdata.kgcloud.domain.share.service.LinkShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author Bovin
 * @description
 * @since 2020-06-03 17:32
 **/
@Component
public class ShareApplicationRunner implements ApplicationRunner {

    @Autowired
    private LinkShareService linkShareService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        linkShareService.refresh();
    }
}
