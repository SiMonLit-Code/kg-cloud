package com.plantdata.kgcloud.domain.share.service.impl;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.share.entity.LinkShare;
import com.plantdata.kgcloud.domain.share.repository.LinkShareRepository;
import com.plantdata.kgcloud.domain.share.service.LinkShareService;
import com.plantdata.kgcloud.sdk.UserClient;
import com.plantdata.kgcloud.sdk.rsp.UserDetailRsp;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
