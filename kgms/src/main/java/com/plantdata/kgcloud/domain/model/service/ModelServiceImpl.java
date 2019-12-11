package com.plantdata.kgcloud.domain.model.service;

import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.model.entity.Model;
import com.plantdata.kgcloud.domain.model.repository.ModelRepository;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.KgmsModelReq;
import com.plantdata.kgcloud.sdk.rsp.ModelRsp;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.KgKeyGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
@Service
public class ModelServiceImpl implements ModelService {

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private KgKeyGenerator kgKeyGenerator;

    @Override
    public List<ModelRsp> findAll(String userId) {
        Model probe = Model.builder()
                .userId(userId)
                .build();
        List<Model> all = modelRepository.findAll(Example.of(probe));
        return all.stream().map(ConvertUtils.convert(ModelRsp.class)).collect(Collectors.toList());
    }

    @Override
    public Page<ModelRsp> findAll(String userId, BaseReq baseReq) {
        Model probe = Model.builder()
                .userId(userId)
                .build();
        Page<Model> all = modelRepository.findAll(Example.of(probe), PageRequest.of(baseReq.getPage() - 1,
                baseReq.getSize()));
        return all.map(ConvertUtils.convert(ModelRsp.class));
    }

    @Override
    public ModelRsp findById(String userId, Long id) {
        Optional<Model> one = modelRepository.findByIdAndUserId(id, userId);
        return one.map(ConvertUtils.convert(ModelRsp.class))
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.MODEL_NOT_EXISTS));
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String userId, Long id) {
        modelRepository.deleteByIdAndUserId(id, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ModelRsp insert(KgmsModelReq req) {
        Model target = new Model();
        BeanUtils.copyProperties(req, target);
        target.setId(kgKeyGenerator.getNextId());
        target = modelRepository.save(target);
        return ConvertUtils.convert(ModelRsp.class).apply(target);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ModelRsp update(String userId, Long id, KgmsModelReq req) {
        Model target = modelRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.MODEL_NOT_EXISTS));
        BeanUtils.copyProperties(req, target);
        target = modelRepository.save(target);
        return ConvertUtils.convert(ModelRsp.class).apply(target);
    }


    @Override
    public Object call(Long id, List<String> input) {
        //TODO
        return null;
    }
}
