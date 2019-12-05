package com.plantdata.kgcloud.domain.common.service;

import com.plantdata.kgcloud.bean.BaseReq;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface BaseService<T, R, ID> {
    /**
     * 查询所有接口
     *
     * @return
     */
    List<T> findAll(String userId);

    /**
     * 分页查询接口
     *
     * @return
     */
    Page<T> findAll(String userId, BaseReq baseReq);

    /**
     * 根据id查询接口
     *
     * @param id
     * @return
     */
    T findById(String userId, ID id);

    /**
     * 根据id删除接口
     *
     * @param id
     * @return
     */
    void delete(String userId, ID id);

    /**
     * 插入接口
     *
     * @param r
     * @return
     */
    T insert(R r);

    /**
     * 修改接口
     *
     * @param id
     * @param r
     * @return
     */
    T update(String userId, ID id, R r);

}
