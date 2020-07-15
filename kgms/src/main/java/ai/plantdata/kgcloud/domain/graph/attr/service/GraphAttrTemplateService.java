package ai.plantdata.kgcloud.domain.graph.attr.service;

import ai.plantdata.kgcloud.domain.graph.attr.rsp.GraphAttrTemplateRsp;

import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2019/12/4 16:54
 * @Description:
 */
public interface GraphAttrTemplateService {

    /**
     * 所有属性模板
     *
     * @return
     */
    List<GraphAttrTemplateRsp> listAttrTemplate();

    /**
     * 属性模板详情
     *
     * @param kgName
     * @param id
     * @return
     */
    GraphAttrTemplateRsp getDetails(String kgName, Long id);
}
