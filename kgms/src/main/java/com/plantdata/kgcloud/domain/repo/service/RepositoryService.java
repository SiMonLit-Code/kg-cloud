package com.plantdata.kgcloud.domain.repo.service;

import com.plantdata.kgcloud.domain.repo.entity.RepoItem;
import com.plantdata.kgcloud.domain.repo.model.req.RepositoryReq;
import com.plantdata.kgcloud.domain.repo.model.req.RepositoryUpdateReq;
import com.plantdata.kgcloud.domain.repo.model.rsp.RepoItemRsp;
import com.plantdata.kgcloud.sdk.rsp.RepositoryLogMenuRsp;

import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @date 2020/5/18  14:10
 */
public interface RepositoryService {
    /**
     * 查询所有组件
     * @param userId 用户id
     * @return list
     */
    List<RepoItemRsp> list(String userId);

    /**
     * 新增组件
     * @param repositoryReq ..
     * @return int
     */
    Integer add(RepositoryReq repositoryReq);

    Boolean state(Integer repoId);
    Map<Integer ,Boolean> stateMap(List<RepoItem> all);

    /**
     * 更新
     *
     * @param updateReq 更新参数
     * @return boolean
     */
    boolean modify(RepositoryUpdateReq updateReq);

    /**
     * 组件删除
     * @param id 组件id
     * @return boolean
     */
    boolean delete(int id);
    /**
     * @param id    id
     * @param start true启用
     * @return boolean
     */
    boolean updateStatus(Integer id, boolean start);

    /**
     * 菜单相关
     * @param userId 用户id
     * @return list
     */
    List<RepositoryLogMenuRsp> menuLog(String userId);
}
