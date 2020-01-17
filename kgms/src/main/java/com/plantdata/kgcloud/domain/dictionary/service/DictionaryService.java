package com.plantdata.kgcloud.domain.dictionary.service;


import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.sdk.req.DictionaryReq;
import com.plantdata.kgcloud.sdk.rsp.DictionaryRsp;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface DictionaryService {


    /**
     * 查询所有接口
     *
     * @return
     */
    List<DictionaryRsp> findAll(String userId);

    /**
     * 分页查询接口
     *
     * @return
     */
    Page<DictionaryRsp> findAll(String userId, BaseReq baseReq);

    /**
     * 根据id查询接口
     *
     * @param id
     * @return
     */
    DictionaryRsp findById(String userId, Long id);

    /**
     * 根据id删除接口
     *
     * @param id
     * @return
     */
    void delete(String userId, Long id);

    /**
     * 插入接口
     *
     * @param r
     * @return
     */
    DictionaryRsp insert(String userId,DictionaryReq r);

    /**
     * 修改接口
     *
     * @param id
     * @param r
     * @return
     */
    DictionaryRsp update(String userId, Long id, DictionaryReq r);
}