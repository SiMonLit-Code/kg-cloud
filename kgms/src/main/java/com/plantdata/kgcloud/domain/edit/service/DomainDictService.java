package com.plantdata.kgcloud.domain.edit.service;

import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.domain.edit.req.dict.DictReq;
import com.plantdata.kgcloud.domain.edit.rsp.DictRsp;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2019/12/4 11:45
 * @Description:
 */
public interface DomainDictService {

    /**
     * 批量添加领域词
     *
     * @param kgName
     * @param dictReqs
     */
    void batchInsert(String kgName, List<DictReq> dictReqs);

    /**
     * 修改领域词
     *
     * @param kgName
     * @param id
     * @param dictReq
     */
    void update(String kgName, String id, DictReq dictReq);

    /**
     * 批量删除
     *
     * @param kgName
     * @param ids
     */
    void batchDelete(String kgName, List<String> ids);

    /**
     * 领域词列表
     *
     * @param kgName
     * @param baseReq
     * @return
     */
    Page<DictRsp> listDict(String kgName, BaseReq baseReq);
}
