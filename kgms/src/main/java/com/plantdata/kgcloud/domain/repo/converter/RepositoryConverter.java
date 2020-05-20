package com.plantdata.kgcloud.domain.repo.converter;

import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.repo.model.RepoCheckConfig;
import com.plantdata.kgcloud.domain.repo.model.Repository;
import com.plantdata.kgcloud.domain.repo.model.RepositoryGroup;
import com.plantdata.kgcloud.domain.repo.model.common.BaseRepositoryVO;
import com.plantdata.kgcloud.domain.repo.model.req.RepoCheckConfigReq;
import com.plantdata.kgcloud.domain.repo.model.req.RepositoryReq;
import com.plantdata.kgcloud.domain.repo.model.req.RepositoryUpdateReq;
import com.plantdata.kgcloud.domain.repo.model.rsp.GroupRsp;
import com.plantdata.kgcloud.domain.repo.model.rsp.RepositoryListRsp;
import com.plantdata.kgcloud.domain.repo.model.rsp.RepositoryRsp;
import lombok.NonNull;

import java.util.List;

/**
 * @author cjw
 * @date 2020/5/18  17:01
 */
public class RepositoryConverter extends BasicConverter {

    public static RepositoryRsp repository2RepositoryRsp(@NonNull Repository repository) {
        RepositoryRsp rsp = new RepositoryRsp();
        rsp.setConfig(repository.getConfig());
        rsp.setGroupId(repository.getGroupId());
        rsp.setId(repository.getId());
        rsp.setType(repository.getType());
        rsp.setRank(repository.getRank());
        rsp.setState(repository.isState());
        rsp.setName(repository.getName());
        rsp.setRemark(repository.getRemark());
        rsp.setMenuIds(repository.getMenuIds());
        return rsp;
    }

    public static <T extends BaseRepositoryVO> Repository repositoryReq2Repository(@NonNull T repository) {
        Repository rsp = new Repository();
        rsp.setConfig(repository.getConfig());
        rsp.setGroupId(repository.getGroupId());
        rsp.setCheckConfigs(listToRsp(repository.getCheckConfigs(), RepositoryConverter::repoCheckConfigReq2RepoCheckConfig));
        rsp.setName(repository.getName());
        rsp.setType(repository.getType());
        rsp.setRank(repository.getRank());
        rsp.setRemark(repository.getRemark());
        rsp.setMenuIds(repository.getMenuIds());
        return rsp;
    }


    public static GroupRsp repositoryGroup2GroupRsp(@NonNull RepositoryGroup group) {
        GroupRsp groupRsp = new GroupRsp();
        groupRsp.setDesc(group.getDesc());
        groupRsp.setGroupId(group.getGroupId());
        groupRsp.setGroupName(group.getGroupName());
        groupRsp.setId(group.getId());
        return groupRsp;
    }

    public static RepositoryListRsp buildRepositoryList(List<RepositoryRsp> list, List<RepositoryGroup> groups) {
        List<GroupRsp> groupRspList = listToRsp(groups, RepositoryConverter::repositoryGroup2GroupRsp);
        return new RepositoryListRsp(groupRspList, list);
    }

    public static Repository repositoryUpdateReq2RepoCheckConfig(RepositoryUpdateReq updateReq) {
        Repository repository = repositoryReq2Repository(updateReq);
        repository.setId(updateReq.getId());
        return repository;
    }

    public static RepoCheckConfig repoCheckConfigReq2RepoCheckConfig(RepoCheckConfigReq checkConfigReq) {
        return new RepoCheckConfig(checkConfigReq.getCheckType(), checkConfigReq.getContent());
    }
}
