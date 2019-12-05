package com.plantdata.kgcloud.service.impl;

import com.plantdata.kgcloud.service.LinkShareService;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
@Service
public class LinkShareServiceImpl implements LinkShareService {

//    @Autowired
//    private ShareLinkMapper shareLinkMapper;
//
//    @Autowired
//    private UserApkMapper userApkMapper;
//
//    @Override
//    public ShareRoleVo status(String userId, String kgName) {
//        Integer i = shareLinkMapper.hasShareRole(userId);
//        ShareRoleVo shareRoleVo = new ShareRoleVo();
//        if (Objects.equals(i, 0)) {
//            shareRoleVo.setHasRole(0);
//        } else {
//            shareRoleVo.setHasRole(1);
//        }
//        List<ShareLinkBean> byUserId = shareLinkMapper.findByUserId(userId, kgName);
//
//        shareRoleVo.setShareList(byUserId);
//        return shareRoleVo;
//    }
//
//    @Override
//    public int shareLink(String userId, String kgName, String spaId) {
//        ShareLinkBean bean = shareLinkMapper.findByKgNameAndSpaId(kgName, spaId);
//        if (bean == null) {
//            return save(userId, kgName, spaId, 1);
//        } else {
//            return shareLinkMapper.shareLink(kgName, spaId);
//        }
//
//    }
//
//    @Override
//    public int cancelShare(String userId, String kgName, String spaId) {
//        ShareLinkBean bean = shareLinkMapper.findByKgNameAndSpaId(kgName, spaId);
//
//        if (bean == null) {
//            return save(userId, kgName, spaId, 0);
//        } else {
//            return shareLinkMapper.cancelShare(kgName, spaId);
//        }
//    }
//
//    private int save(String userId, String kgName, String spaId, Integer status) {
//        ShareLinkBean bean = new ShareLinkBean();
//        String apk = userApkMapper.get(userId);
//        String link = CommonUtils.buildShareUrl(kgName, apk, spaId);
//        bean.setLink(link);
//        bean.setUserId(userId);
//        bean.setKgName(kgName);
//        bean.setSpaId(spaId);
//        bean.setStatus(status);
//        bean.setCreateTime(new Date());
//        bean.setUpdateTime(new Date());
//        return shareLinkMapper.insert(bean);
//    }
}
