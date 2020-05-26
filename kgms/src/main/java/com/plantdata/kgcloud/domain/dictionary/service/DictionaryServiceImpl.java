package com.plantdata.kgcloud.domain.dictionary.service;

import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.dictionary.constant.DictConst;
import com.plantdata.kgcloud.domain.dictionary.entity.Dictionary;
import com.plantdata.kgcloud.domain.dictionary.repository.DictionaryRepository;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.DictionaryReq;
import com.plantdata.kgcloud.sdk.rsp.DictionaryRsp;
import com.plantdata.kgcloud.security.SessionHolder;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.KgKeyGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
public class DictionaryServiceImpl implements DictionaryService {

    @Autowired
    private DictionaryRepository dictionaryRepository;

    @Autowired
    private KgKeyGenerator kgKeyGenerator;

    @Override
    public List<DictionaryRsp> findAll(String userId) {
        Dictionary probe = Dictionary.builder()
                .userId(userId)
                .build();
        List<Dictionary> all = dictionaryRepository.findAll(Example.of(probe));
        List<DictionaryRsp> collect = all.stream()
                .map(ConvertUtils.convert(DictionaryRsp.class))
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public Page<DictionaryRsp> findAll(String userId, BaseReq baseReq) {
        Dictionary probe = Dictionary.builder()
                .userId(userId)
                .build();
        Page<Dictionary> all = dictionaryRepository.findAll(Example.of(probe), PageRequest.of(baseReq.getPage() - 1, baseReq.getSize(), Sort.by(Sort.Direction.DESC,"createAt")));
        Page<DictionaryRsp> map = all.map(ConvertUtils.convert(DictionaryRsp.class));
        return map;
    }

    @Override
    public DictionaryRsp findById(String userId, Long id) {
        Optional<Dictionary> one = dictionaryRepository.findByIdAndUserId(id, userId);
        DictionaryRsp data = one
                .map(ConvertUtils.convert(DictionaryRsp.class))
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DICTIONARY_NOT_EXISTS));
        return data;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String userId,Long id) {
        dictionaryRepository.deleteByIdAndUserId(id, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DictionaryRsp insert(String userId, DictionaryReq req) {
        Dictionary target = new Dictionary();
        BeanUtils.copyProperties(req, target);
        String dbName = SessionHolder.getUserId() + DictConst.DATABASE_SUFFIX;
        target.setDbName(dbName);
        target.setId(kgKeyGenerator.getNextId());
        target.setUserId(userId);
        target = dictionaryRepository.save(target);
        DictionaryRsp apply = ConvertUtils.convert(DictionaryRsp.class).apply(target);
        return apply;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DictionaryRsp update(String userId, Long id, DictionaryReq req) {
        Dictionary target = dictionaryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DICTIONARY_NOT_EXISTS));
        BeanUtils.copyProperties(req, target);
        target = dictionaryRepository.save(target);
        DictionaryRsp apply = ConvertUtils.convert(DictionaryRsp.class).apply(target);
        return apply;
    }
}