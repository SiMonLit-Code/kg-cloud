package com.plantdata.kgcloud.domain.graph.attr.service;

import com.plantdata.kgcloud.domain.graph.attr.dto.AttrDefGroupDTO;
import com.plantdata.kgcloud.domain.graph.attr.req.AttrGroupReq;
import com.plantdata.kgcloud.domain.graph.attr.req.AttrGroupSearchReq;
import com.plantdata.kgcloud.domain.graph.attr.rsp.GraphAttrGroupRsp;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/21 16:18
 */
public interface GraphAttrGroupService {

    /**
     * 根据图谱名称查询属性分组
     */
    List<AttrDefGroupDTO> queryAllByKgName(String kgName);

    /**
     * 创建属性分组
     *
     * @param kgName
     * @param attrGroupReq
     * @return
     */
    Long createAttrGroup(String kgName, AttrGroupReq attrGroupReq);

    /**
     * 删除属性分组
     *
     * @param kgName
     * @param id
     */
    void deleteAttrGroup(String kgName, Long id);

    /**
     * 修改属性分组名称
     *
     * @param kgName
     * @param id
     * @param attrGroupReq
     * @return
     */
    Long updateAttrGroup(String kgName, Long id, AttrGroupReq attrGroupReq);

    /**
     * 查询属性分组
     *
     * @param kgName
     * @param attrGroupSearchReq
     * @return
     */
    List<GraphAttrGroupRsp> listAttrGroups(String kgName, AttrGroupSearchReq attrGroupSearchReq);

    /**
     * 向属性分组里面添加属性
     *
     * @param kgName
     * @param id
     * @param attrIds
     * @return 添加的数量
     */
    Integer addAttrToAttrGroup(String kgName, Long id, List<Integer> attrIds);

    /**
     * 从属性分组里面删除属性
     *
     * @param kgName
     * @param id
     * @param attrIds
     */
    void deleteAttrFromAttrGroup(String kgName, Long id, List<Integer> attrIds);
}
