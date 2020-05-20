package com.plantdata.kgcloud.domain.repo.service;

import com.plantdata.kgcloud.domain.repo.model.rsp.GroupRsp;

import java.util.List;

/**
 * @author cjw
 * @date 2020/5/19  16:57
 */
public interface RepositoryGroupService {

    List<GroupRsp> listAllGroup();
}
