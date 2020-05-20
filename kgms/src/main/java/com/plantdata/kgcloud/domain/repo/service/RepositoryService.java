package com.plantdata.kgcloud.domain.repo.service;

import com.plantdata.kgcloud.domain.repo.model.req.RepositoryReq;
import com.plantdata.kgcloud.domain.repo.model.req.RepositoryUpdateReq;
import com.plantdata.kgcloud.domain.repo.model.rsp.RepositoryRsp;

import java.util.List;

/**
 * @author cjw
 * @date 2020/5/18  14:10
 */
public interface RepositoryService {
    /**
     * 查询所有组件
     *
     * @param userId 用户id
     * @return list
     */
    List<RepositoryRsp> list(String userId);

    /**
     * @param repositoryReq
     * @return
     */
    Integer add(RepositoryReq repositoryReq);

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
     * 组件使用记录一下
     *
     * @param id     组件id
     * @param userId 用户id
     */
    void useLog(Integer id, String userId);
}
