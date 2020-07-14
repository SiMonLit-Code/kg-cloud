package ai.plantdata.kgcloud.plantdata.req.app;

import ai.plantdata.kgcloud.plantdata.req.explore.common.AttrScreeningBean;
import ai.plantdata.kgcloud.plantdata.req.explore.common.EntityScreeningBean;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-06-13 16:05
 **/
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class LayerKnowledgeParameter {

    private List<Long> replaceClassIds;
    private List<String> replaceClassIdsKey;
    private Boolean isRelationMerge = false;
    private List<Integer> allowAtts;
    private List<String> allowAttsKey;
    private List<Long> allowTypes;
    private List<String> allowTypesKey;
    private List<Long> allowAttrGroups;
    private List<EntityScreeningBean> entityQuery;
    private List<AttrScreeningBean> attAttFilters;
    private List<AttrScreeningBean> reservedAttFilters;
    private Boolean isInherit = false;
}
