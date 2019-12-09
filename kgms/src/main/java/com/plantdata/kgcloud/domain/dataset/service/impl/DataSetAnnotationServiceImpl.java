package com.plantdata.kgcloud.domain.dataset.service.impl;

import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.dataset.entity.DataSetAnnotation;
import com.plantdata.kgcloud.domain.dataset.repository.DataSetAnnotationRepository;
import com.plantdata.kgcloud.domain.dataset.service.DataSetAnnotationService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.AnnotationQueryReq;
import com.plantdata.kgcloud.sdk.req.AnnotationReq;
import com.plantdata.kgcloud.sdk.rsp.AnnotationRsp;
import com.plantdata.kgcloud.util.ConvertUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-09 21:13
 **/
@Service
public class DataSetAnnotationServiceImpl implements DataSetAnnotationService {

    @Autowired
    DataSetAnnotationRepository dataSetAnnotationRepository;

    @Override
    public Page<AnnotationRsp> findAll(String kgName, AnnotationQueryReq baseReq) {
        DataSetAnnotation build = DataSetAnnotation.builder().kgName(kgName).build();
        PageRequest of = PageRequest.of(baseReq.getPage() - 1, baseReq.getSize());
        Page<DataSetAnnotation> all = dataSetAnnotationRepository.findAll(Example.of(build), of);
        return all.map(ConvertUtils.convert(AnnotationRsp.class));
    }

    @Override
    public void delete(String kgName, Long id) {
        dataSetAnnotationRepository.deleteById(id);
    }

    @Override
    public AnnotationRsp findById(String kgName, Long id) {
        Optional<DataSetAnnotation> one = dataSetAnnotationRepository.findById(id);
        return one.map(ConvertUtils.convert(AnnotationRsp.class))
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.ANNOTATION_NOT_EXISTS));
    }

    @Override
    public AnnotationRsp update(String kgName, Long id, AnnotationReq req) {
        DataSetAnnotation dataSetAnnotation = dataSetAnnotationRepository
                .findById(id)
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.ANNOTATION_NOT_EXISTS));
        BeanUtils.copyProperties(req, dataSetAnnotation);
        return ConvertUtils.convert(AnnotationRsp.class).apply(dataSetAnnotation);
    }

    @Override
    public AnnotationRsp add(String kgName, AnnotationReq req) {
        DataSetAnnotation dataSetAnnotation = new DataSetAnnotation();
        BeanUtils.copyProperties(req, dataSetAnnotation);
        dataSetAnnotation.setKgName(kgName);
        DataSetAnnotation save = dataSetAnnotationRepository.save(dataSetAnnotation);
        return ConvertUtils.convert(AnnotationRsp.class).apply(save);
    }
}
