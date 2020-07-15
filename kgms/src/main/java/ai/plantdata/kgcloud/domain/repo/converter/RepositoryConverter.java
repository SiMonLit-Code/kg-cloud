package ai.plantdata.kgcloud.domain.repo.converter;

import ai.plantdata.kgcloud.domain.app.converter.BasicConverter;
import ai.plantdata.kgcloud.domain.repo.model.RepoCheckConfig;
import ai.plantdata.kgcloud.domain.repo.entity.RepoItem;
import ai.plantdata.kgcloud.domain.repo.entity.RepoItemGroup;
import ai.plantdata.kgcloud.domain.repo.model.common.BaseRepositoryVO;
import ai.plantdata.kgcloud.domain.repo.model.req.RepoCheckConfigReq;
import ai.plantdata.kgcloud.domain.repo.model.req.RepositoryUpdateReq;
import ai.plantdata.kgcloud.domain.repo.model.rsp.GroupRsp;
import ai.plantdata.kgcloud.domain.repo.model.rsp.RepoItemRsp;
import lombok.NonNull;

/**
 * @author cjw
 * @date 2020/5/18  17:01
 */
public class RepositoryConverter extends BasicConverter {

    public static RepoItemRsp repository2RepositoryRsp(@NonNull RepoItem repository) {
        RepoItemRsp rsp = new RepoItemRsp();
        rsp.setConfig(repository.getConfig());
        rsp.setGroupId(repository.getGroupId());
        rsp.setId(repository.getId());
        rsp.setType(repository.getType());
        rsp.setRank(repository.getRank());
        rsp.setState(repository.isState());
        rsp.setName(repository.getName());
        rsp.setRemark(repository.getRemark());
        return rsp;
    }

    public static <T extends BaseRepositoryVO> RepoItem repositoryReq2Repository(@NonNull T repository) {
        RepoItem rsp = new RepoItem();
        rsp.setConfig(repository.getConfig());
        rsp.setGroupId(repository.getGroupId());
        rsp.setCheckConfigs(listToRsp(repository.getCheckConfigs(), RepositoryConverter::repoCheckConfigReq2RepoCheckConfig));
        rsp.setName(repository.getName());
        rsp.setType(repository.getType());
        rsp.setRank(repository.getRank());
        rsp.setRemark(repository.getRemark());
        return rsp;
    }


    public static GroupRsp repositoryGroup2GroupRsp(@NonNull RepoItemGroup group) {
        GroupRsp groupRsp = new GroupRsp();
        groupRsp.setDesc(group.getDesc());
        groupRsp.setGroupId(group.getGroupId());
        groupRsp.setGroupName(group.getGroupName());
        groupRsp.setRank(group.getRank());
        groupRsp.setId(group.getId());
        return groupRsp;
    }

    public static RepoItem repositoryUpdateReq2RepoCheckConfig(RepositoryUpdateReq updateReq) {
        RepoItem repository = repositoryReq2Repository(updateReq);
        repository.setId(updateReq.getId());
        return repository;
    }

    public static RepoCheckConfig repoCheckConfigReq2RepoCheckConfig(RepoCheckConfigReq checkConfigReq) {
        return new RepoCheckConfig(checkConfigReq.getCheckType(), checkConfigReq.getContent());
    }

}
