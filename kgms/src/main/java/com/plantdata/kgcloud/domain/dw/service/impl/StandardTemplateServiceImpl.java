package com.plantdata.kgcloud.domain.dw.service.impl;

import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.dw.entity.DWStandardTemplate;
import com.plantdata.kgcloud.domain.dw.repository.StandardTemplateRepository;
import com.plantdata.kgcloud.domain.dw.rsp.StandardTemplateRsp;
import com.plantdata.kgcloud.domain.dw.service.StandardTemplateService;
import com.plantdata.kgcloud.exception.BizException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StandardTemplateServiceImpl implements StandardTemplateService {

    @Autowired
    private StandardTemplateRepository standardTemplateRepository;

    private final Function<DWStandardTemplate, StandardTemplateRsp> st2rsp = (s) -> {
        StandardTemplateRsp stRsp = new StandardTemplateRsp();
        BeanUtils.copyProperties(s, stRsp);
        return stRsp;
    };

    @Override
    public List<StandardTemplateRsp> findAll(String userId) {

        List<DWStandardTemplate> standardTemplateList = standardTemplateRepository.findAll();

        return standardTemplateList.stream().map(st2rsp).collect(Collectors.toList());
    }

    @Override
    public StandardTemplateRsp findOne(String userId, Long standardTemplateId) {

        Optional<DWStandardTemplate> byId = standardTemplateRepository.findById(standardTemplateId);

        if(!byId.isPresent()){
            throw BizException.of(KgmsErrorCodeEnum.STANDARD_TEMPLATE_NOT_EXIST);
        }

        return st2rsp.apply(byId.get());
    }
}
