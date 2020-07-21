package ai.plantdata.kgcloud.domain.share.service.impl;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.cloud.redis.util.KgKeyGenerator;
import ai.plantdata.cloud.web.util.ConvertUtils;
import ai.plantdata.kgcloud.domain.share.repository.LinkShareRepository;
import ai.plantdata.kgcloud.domain.share.service.LinkShareService;
import ai.plantdata.kgcloud.domain.share.entity.LinkShare;
import ai.plantdata.kgcloud.domain.share.rsp.LinkShareRsp;
import ai.plantdata.kgcloud.domain.share.rsp.ShareRsp;
import ai.plantdata.kgcloud.sdk.UserClient;
import ai.plantdata.kgcloud.sdk.req.SelfSharedRsp;
import ai.plantdata.kgcloud.sdk.rsp.LinkShareSpaRsp;
import ai.plantdata.kgcloud.sdk.rsp.UserDetailRsp;
import ai.plantdata.kgcloud.sdk.rsp.UserLimitRsp;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by jdm on 2019/12/7 14:28.
 */
@Service
public class LinkShareServiceImpl implements LinkShareService {

    private final static String SHARE_APK = "kgcloud:share:apk";
    @Autowired
    private UserClient userClient;
    @Autowired
    private LinkShareRepository linkShareRepository;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private KgKeyGenerator kgKeyGenerator;


    @Override
    public LinkShareSpaRsp shareStatus(String userId, String kgName, String spaId) {
        ApiReturn<UserLimitRsp> detail = userClient.getCurrentUserLimitDetail();
        UserLimitRsp data = detail.getData();
        LinkShareSpaRsp linkShareRsp = new LinkShareSpaRsp();
        linkShareRsp.setKgName(kgName);
        linkShareRsp.setSpaId(spaId);
        if (data.getShareable()) {
            Optional<LinkShare> linkShare = linkShareRepository.findByKgNameAndSpaId(kgName, spaId);
            if (linkShare.isPresent()) {
                LinkShare linkShare1 = linkShare.get();
                Boolean shared = linkShare1.getShared();
                linkShareRsp.setShareable(shared);
            } else {
                linkShareRsp.setShareable(false);
            }
        } else {
            linkShareRsp.setShareable(false);
        }
        return linkShareRsp;
    }

    @Override
    public LinkShareRsp shareStatus(String userId, String kgName) {
        LinkShareRsp linkShareRsp = linkShareRsp();
        List<LinkShare> all = linkShareRepository.findByUserIdAndKgName(userId, kgName);
        List<ShareRsp> collect = all.stream().map(ConvertUtils.convert(ShareRsp.class)).collect(Collectors.toList());
        linkShareRsp.setShareList(collect);
        return linkShareRsp;
    }

    @Override
    public LinkShareRsp liteShareStatus(String userId) {
        LinkShareRsp linkShareRsp = linkShareRsp();
        LinkShare linkShare = new LinkShare();
        linkShare.setUserId(userId);
        linkShare.setSpaId("graph");
        List<LinkShare> all = linkShareRepository.findAll(Example.of(linkShare));
        List<ShareRsp> collect = all.stream().map(ConvertUtils.convert(ShareRsp.class)).collect(Collectors.toList());
        linkShareRsp.setShareList(collect);
        return linkShareRsp;
    }

    private LinkShareRsp linkShareRsp() {
        ApiReturn<UserLimitRsp> detail = userClient.getCurrentUserLimitDetail();
        UserLimitRsp data = detail.getData();
        LinkShareRsp linkShareRsp = new LinkShareRsp();
        if (data != null && data.getShareable() != null && data.getShareable()) {
            linkShareRsp.setHasRole(1);
        } else {
            linkShareRsp.setHasRole(0);
        }
        return linkShareRsp;
    }

    private LinkShare getOne(String kgName, String spaId) {
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
        linkShare.setUserId(userId);
        LinkShare save = linkShareRepository.save(linkShare);
        refresh();
        return ConvertUtils.convert(ShareRsp.class).apply(save);
    }

    @Override
    public ShareRsp shareCancel(String userId, String kgName, String spaId) {
        LinkShare linkShare = getOne(kgName, spaId);
        linkShare.setShared(false);
        linkShare.setUserId(userId);
        LinkShare save = linkShareRepository.save(linkShare);
        refresh();
        return ConvertUtils.convert(ShareRsp.class).apply(save);
    }

    @Override
    public SelfSharedRsp shareSpaStatus(String userId, String kgName, String spaId, String token) {
        SelfSharedRsp selfSharedRsp = new SelfSharedRsp();
        if (!StringUtils.hasText(token)) {
            selfSharedRsp.setLogin(false);
        }
        selfSharedRsp.setSelf(true);
        UserLimitRsp data = userClient.getCurrentUserLimitDetail().getData();
        if (data != null) {
            selfSharedRsp.setSharePermission(data.getShareable());
        } else {
            selfSharedRsp.setSharePermission(false);
        }
        LinkShare linkShare = getOne(kgName, spaId);
        Boolean shared = linkShare.getShared();
        if (shared != null) {
            selfSharedRsp.setShareable(shared);
        } else {
            selfSharedRsp.setShareable(false);
        }
        return selfSharedRsp;
    }

    @Override
    public void refresh() {
        List<LinkShare> all = linkShareRepository.findAll();
        Map<String, List<LinkShare>> map = new HashMap<>();
        for (LinkShare linkShare : all) {
            List<LinkShare> linkShares = map.computeIfAbsent(linkShare.getUserId(), (k) -> new ArrayList<>());
            linkShares.add(linkShare);
        }
        Map<String, Set<String>> setMap = new HashMap<>();
        for (Map.Entry<String, List<LinkShare>> entry : map.entrySet()) {
            ApiReturn<UserDetailRsp> userIdDetail = userClient.getCurrentUserIdDetail(entry.getKey());
            if (userIdDetail != null && userIdDetail.getData() != null) {
                UserDetailRsp data = userIdDetail.getData();
                String apk = data.getApk();
                Boolean shareable = data.getShareable();
                if (shareable != null && shareable) {
                    List<LinkShare> value = entry.getValue();
                    for (LinkShare linkShare : value) {
                        Boolean shared = linkShare.getShared();
                        if (shared != null && shared) {
                            String kgName = linkShare.getKgName();
                            String spaId = linkShare.getSpaId();
                            UriComponents build = UriComponentsBuilder.newInstance().pathSegment("spa", "container", kgName, apk, spaId).build();
                            String s = build.toUriString();
                            setMap.computeIfAbsent(apk, (a) -> new HashSet<>()).add(s);
                        }
                    }
                }
            }
        }
        redissonClient.getKeys().delete(SHARE_APK);
        RSet<Object> set = redissonClient.getSet(SHARE_APK);
        for (Map.Entry<String, Set<String>> entry : setMap.entrySet()) {
            set.addAll(entry.getValue());
        }
    }
}
