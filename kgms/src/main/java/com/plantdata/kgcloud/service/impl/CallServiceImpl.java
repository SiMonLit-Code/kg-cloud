package com.plantdata.kgcloud.service.impl;

import com.plantdata.kgcloud.domain.audit.ApiAuditRepository;
import com.plantdata.kgcloud.service.CallService;
import com.plantdata.kgcloud.bean.ApiReturn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
@Service
@Slf4j
public class CallServiceImpl implements CallService {

    @Autowired
    ApiAuditRepository apiAuditRepository;

    @Override
    public ApiReturn call(String id, String input) {


        return null;
    }

}
