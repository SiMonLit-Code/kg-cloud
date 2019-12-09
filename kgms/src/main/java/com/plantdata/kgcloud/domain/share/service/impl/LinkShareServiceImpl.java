package com.plantdata.kgcloud.domain.share.service.impl;

import com.hiekn.ierule.util.CommonUtils;
import com.plantdata.kgcloud.domain.app.converter.ApkConverter;
import com.plantdata.kgcloud.domain.share.entity.LinkShare;
import com.plantdata.kgcloud.domain.share.repository.LinkShareRepository;
import com.plantdata.kgcloud.domain.share.rsp.LinkShareRsp;
import com.plantdata.kgcloud.domain.share.service.LinkShareService;
import com.plantdata.kgcloud.sdk.rsp.app.main.ApkRsp;
import com.plantdata.kgcloud.security.SessionHolder;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.KgKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by jdm on 2019/12/7 14:28.
 */
@Service
public class LinkShareServiceImpl implements LinkShareService{


    @Autowired
    private LinkShareRepository linkShareRepository;

    @Override
    public LinkShareRsp status(String userId, String kgName) {
        Integer i = linkShareRepository.hasShareRole(userId);
        LinkShareRsp linkShareRsp = new LinkShareRsp();
        if (Objects.equals(i, 0)) {
            linkShareRsp.setHasRole(0);
        } else {
            linkShareRsp.setHasRole(1);
        }
        List<LinkShare> all = linkShareRepository.findByUserId(userId,kgName);
        linkShareRsp.setShareList(all);
        return ConvertUtils.convert(LinkShareRsp.class).apply(linkShareRsp);
    }

    @Override
    public Boolean shareLink(String userId, String kgName, String spaId) {
        LinkShare bean = linkShareRepository.findByKgNameAndSpaId(kgName, spaId);
        if (bean == null) {
            return linkShareRepository.save(userId, kgName, spaId, 0);
        } else {
            return linkShareRepository.cancelShare(kgName, spaId);
        }

    }

    @Override
    public Boolean cancelShare(String userId, String kgName, String spaId) {
        LinkShare bean = linkShareRepository.findByKgNameAndSpaId(kgName, spaId);

        if (bean == null) {
            return linkShareRepository.save(userId, kgName, spaId, 0);
        } else {
            return linkShareRepository.cancelShare(kgName, spaId);
        }
    }
}
