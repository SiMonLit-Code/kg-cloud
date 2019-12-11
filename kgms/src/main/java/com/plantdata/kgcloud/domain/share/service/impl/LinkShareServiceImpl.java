package com.plantdata.kgcloud.domain.share.service.impl;

import com.plantdata.kgcloud.domain.share.entity.LinkShare;
import com.plantdata.kgcloud.domain.share.repository.LinkShareRepository;
import com.plantdata.kgcloud.domain.share.rsp.LinkShareRsp;
import com.plantdata.kgcloud.domain.share.rsp.ShareRsp;
import com.plantdata.kgcloud.domain.share.service.LinkShareService;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.KgKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by jdm on 2019/12/7 14:28.
 */
@Service
public class LinkShareServiceImpl implements LinkShareService {


    @Autowired
    private LinkShareRepository linkShareRepository;

    @Autowired
    private KgKeyGenerator kgKeyGenerator;

    @Override
    public LinkShareRsp shareStatus(String userId, String kgName) {
//        Integer i = linkShareRepository.hasShareRole(userId);
        LinkShareRsp linkShareRsp = new LinkShareRsp();
//        if (Objects.equals(i, 0)) {
//            linkShareRsp.setHasRole(0);
//        } else {
//            linkShareRsp.setHasRole(1);
//        }
        List<LinkShare> all = linkShareRepository.findByUserIdAndKgName(userId, kgName);
        List<ShareRsp> collect = all.stream().map(ConvertUtils.convert(ShareRsp.class)).collect(Collectors.toList());
        linkShareRsp.setShareList(collect);
        return linkShareRsp;
    }


    private LinkShare getOne(String kgName, String spaId){
        Optional<LinkShare> bean = linkShareRepository.findByKgNameAndSpaId(kgName, spaId);
        return bean.orElseGet(() -> {
            LinkShare share = new LinkShare();
            share.setId(kgKeyGenerator.getNextId());
            share.setKgName(kgName);
            share.setSpaId(spaId);
            return share;
        });
    }

    @Override
    public ShareRsp shareLink(String userId, String kgName, String spaId) {
        LinkShare linkShare = getOne(kgName, spaId);
        linkShare.setShared(true);
        LinkShare save = linkShareRepository.save(linkShare);
        return ConvertUtils.convert(ShareRsp.class).apply(save);
    }

    @Override
    public ShareRsp shareCancel(String userId, String kgName, String spaId) {
        LinkShare linkShare = getOne(kgName, spaId);
        linkShare.setShared(false);
        LinkShare save = linkShareRepository.save(linkShare);
        return ConvertUtils.convert(ShareRsp.class).apply(save);
    }
}
