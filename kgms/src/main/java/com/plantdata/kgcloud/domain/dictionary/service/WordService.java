package com.plantdata.kgcloud.domain.dictionary.service;


import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.sdk.req.WordReq;
import com.plantdata.kgcloud.sdk.rsp.WordRsp;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-05 14:59
 **/
public interface WordService {
    /**
     * 词条全查找
     *
     * @param dictId
     * @return
     */
    List<WordRsp> findAll(Long dictId);

    /**
     * 词条分页查找
     *
     * @param dictId
     * @return
     */
    Page<WordRsp> findAll(Long dictId, BaseReq baseReq);

    /**
     * 词条按照id查找
     *
     * @param dictId
     * @param id
     * @return
     */
    WordRsp findById(Long dictId, String id);

    /**
     * 词条删除
     *
     * @param dictId
     * @param id
     * @return
     */
    void delete(Long dictId, String id);

    /**
     * 词条新增
     *
     * @param r
     * @return
     */
    WordRsp insert(Long dictId, WordReq r);

    /**
     * 词条更新
     *
     * @param dictId
     * @param id
     * @param r
     * @return
     */
    WordRsp update(Long dictId, String id, WordReq r);
}
