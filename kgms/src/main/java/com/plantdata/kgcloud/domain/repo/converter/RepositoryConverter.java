package com.plantdata.kgcloud.domain.repo.converter;

import com.plantdata.kgcloud.domain.repo.model.Repository;
import com.plantdata.kgcloud.domain.repo.model.req.RepositoryReq;
import com.plantdata.kgcloud.domain.repo.model.rsp.RepositoryRsp;
import lombok.NonNull;

/**
 * @author cjw
 * @date 2020/5/18  17:01
 */
public class RepositoryConverter {

    public static RepositoryRsp repository2RepositoryRsp(@NonNull Repository repository) {
        RepositoryRsp rsp = new RepositoryRsp();
        rsp.setConfig(repository.getConfig());
        rsp.setGroup(rsp.getGroup());
        rsp.setId(rsp.getId());
        rsp.setRank(rsp.getRank());
        rsp.setRemark(rsp.getRemark());
        rsp.setMenuId(rsp.getMenuId());
        return rsp;
    }

    public static Repository repository2RepositoryRsp(@NonNull RepositoryReq repository) {
        Repository rsp = new Repository();
        rsp.setConfig(repository.getConfig());
        rsp.setGroup(rsp.getGroup());
        rsp.setId(rsp.getId());
        rsp.setRank(rsp.getRank());
        rsp.setRemark(rsp.getRemark());
        rsp.setMenuId(rsp.getMenuId());
        return rsp;
    }
}
